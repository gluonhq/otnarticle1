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
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.otndemo.OTNView;
import com.gluonhq.otndemo.OtnDemo;
import com.gluonhq.otndemo.model.Experience;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class ExperiencesPresenter extends GluonPresenter<OtnDemo> {

    @FXML
    private View experiences;
    private GridPane experiencesGridPane;

    int maxCol;

    @FXML
    private Label label;

    public void initialize() {
        experiences.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e
                        -> MobileApplication.getInstance().showLayer(OtnDemo.MENU_LAYER)));
                appBar.setTitleText("Experiences");
            }
        });
        layout();
    }

    private void layout() {
        experiencesGridPane = new GridPane();
        experiencesGridPane.getStyleClass().add("experiences-grid-pane");
        experiencesGridPane.setAlignment(Pos.TOP_CENTER);
        ScrollPane sp = new ScrollPane(experiencesGridPane);
            sp.setFitToWidth(true);
            experiences.setCenter(sp);

        experiencesGridPane.getChildren().clear();
        experiencesGridPane.add(new ExperienceHolder(Experience.COFFEE), 0, 0);
        experiencesGridPane.add(new ExperienceHolder(Experience.BADGE), 1, 0);
    }

    private class ExperienceHolder extends PlaceholderBase {

        public ExperienceHolder(Experience experience) {
            getStyleClass().add("experience-holder");
            OTNView.registry.getView(experience.name() + "_VIEW")
                    .ifPresent(v -> getChildren().add(getNodeFromIcon(v.getMenuIcon())));
            this.message.setText(experience.toString());
            getChildren().add(message);
            setOnMouseClicked(e -> experience.switchView());
        }

    }
}
