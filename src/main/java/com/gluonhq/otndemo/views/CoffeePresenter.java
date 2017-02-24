/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.otndemo.views;

import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.ProgressBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.otndemo.OTNView;
import com.gluonhq.otndemo.OtnDemo;
import com.gluonhq.otndemo.model.OTNCoffee;
import com.gluonhq.otndemo.model.Service;
import eu.hansolo.fx.qualitygauge.QualityGauge;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.Iterator;

public class CoffeePresenter extends GluonPresenter<OtnDemo> {

    private static final String QR_MESSAGE = "Please scan the QR code at a coffee machine";
    private static final String COFFEE_INSTRUCTION = "Choose your favorite coffee and enjoy\\nthe hot beverage at a machine near you";
    private static final String COFFEE_STRENGTH = "Strength of Coffee";
    private static final String COFFEE_TYPE = "Choose a Coffee Type";

    @FXML
    private View coffeeView;

    @FXML
    private VBox instructionsContainer;

    @FXML
    private HBox submitContainer;

    @FXML
    private VBox coffeeBox;

    @FXML
    private Label instructions;

    @FXML
    private Label typeLabel;

    @FXML
    private Label strengthLabel;

    @FXML
    private VBox coffeeTypes;

    @FXML
    private Button placeOrder;

    @FXML
    private QualityGauge strengthGauge;

    @Inject
    private Service service;
    
    @FXML
    private ProgressBar indicator;

    private final BooleanProperty processing = new SimpleBooleanProperty();
    private OrderHandler ordering;
    
    public void initialize() {
        System.out.println("show coffee");
        coffeeView.setOnShowing(event -> {
            AppBar appBar =  getApp().getAppBar();
            appBar.setNavIcon(getApp().getNavBackButton());
            appBar.setTitleText(OTNView.COFFEE.getTitle());
            loadView();

        });
        
        coffeeView.setOnHiding(event -> {
            if (ordering != null) {
                ordering.cancel();
            }
        });

        instructions.setText(COFFEE_INSTRUCTION);
        typeLabel.setText(COFFEE_TYPE);
        strengthLabel.setText(COFFEE_STRENGTH);
        
        strengthGauge.disableProperty().bind(Bindings.isEmpty(coffeeTypes.getChildren()));

        ToggleGroup coffeeType = new ToggleGroup();
        createCoffeeTypes(coffeeType);
        configureOrderPlacement(coffeeType);
        
        indicator.visibleProperty().bind(processing);
        indicator.progressProperty().bind(Bindings.when(processing).then(-1).otherwise(0));
    }

    private void configureOrderPlacement(ToggleGroup group) {
        placeOrder.setText("Place Order");
        ordering = new OrderHandler(() -> service.orderOTNCoffee((OTNCoffee) group.getSelectedToggle().getUserData(), strengthGauge.getValue()),
                                            QR_MESSAGE);
        processing.bind(ordering.processingProperty());
        
        placeOrder.setOnAction(ordering);
        placeOrder.disableProperty().bind(group.selectedToggleProperty().isNull().or(processing));
        coffeeTypes.disableProperty().bind(processing);
        strengthGauge.disableProperty().bind(processing);
    }

    private void createCoffeeTypes(ToggleGroup toggleGroup) {
        service.retrieveOTNCoffees().addListener((ListChangeListener.Change<? extends OTNCoffee> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (OTNCoffee coffee : c.getAddedSubList()) {
                        RadioButton button = new RadioButton(coffee.getName());
                        button.setUserData(coffee);
                        button.setToggleGroup(toggleGroup);
                        coffeeTypes.getChildren().add(button);
                    }
                } 
                if (c.wasRemoved()) {
                    for (OTNCoffee coffee : c.getRemoved()) {
                        Iterator i = coffeeTypes.getChildren().iterator();
                        while (i.hasNext()) {
                            RadioButton button = (RadioButton) i.next();
                            if (button.getUserData().equals(coffee)) {
                                toggleGroup.getToggles().remove(button);
                                coffeeTypes.getChildren().remove(button);
                            }
                        }
                    }
                }
            }
            if (!toggleGroup.getToggles().isEmpty()) {
                toggleGroup.getToggles().get(0).setSelected(true);
            }
        });
    }

    private void loadView() {
        coffeeView.setTop(instructionsContainer);
        coffeeView.setCenter(coffeeBox);
        coffeeView.setBottom(submitContainer);
    }

}
