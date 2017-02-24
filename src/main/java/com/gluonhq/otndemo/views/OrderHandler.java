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

import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.otndemo.model.Processable;
import javafx.animation.PauseTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.function.Supplier;

public class OrderHandler implements EventHandler<ActionEvent> {

    private final static int PROCESSING_TIME_OUT = 15; // seconds
    private static final String ERROR_TITLE = "Error processing your order";

    private final Supplier<GluonObservableObject<? extends Processable>> order;
    private final BooleanProperty processing = new SimpleBooleanProperty();
    private final PauseTransition timeout;
    private GluonObservableObject<? extends Processable> processOrder;
    private String message;

    private final ChangeListener<ConnectState> listener = (obs, ov, nv) -> {
        if (nv != null) {
            if (ConnectState.SUCCEEDED.equals(nv)) {
                stopProcessing(false, null);
                QRDialog.show(processOrder.get().getId(), message);
            } else if (ConnectState.FAILED.equals(nv)) {
                stopProcessing(true, processOrder.getException() != null ? processOrder.getException().getMessage() : "Failed to process order.");
            }
        }
    };

    public OrderHandler(Supplier<GluonObservableObject<? extends Processable>> order, String message) {
        this.order = order;
        this.message = message;
        
        timeout = new PauseTransition(Duration.seconds(PROCESSING_TIME_OUT));
        timeout.setOnFinished(e -> 
            stopProcessing(true, "Timeout"));
    }
    
    @Override
    public void handle(ActionEvent event) {
        processing.set(true);
        timeout.play();
        // only process order once:
        processOrder = order.get();
        if (processOrder != null) {
            processOrder.stateProperty().addListener(listener);
        } else {
            stopProcessing(true, "Unknown");
        }
    }
    
    private void stopProcessing(boolean showErrorDialog, String message) {
        processing.set(false);
        timeout.stop();
        if (processOrder != null) {
            processOrder.stateProperty().removeListener(listener);
        }
        if (showErrorDialog) {
            Alert error = new Alert(javafx.scene.control.Alert.AlertType.ERROR);
            error.setTitleText(ERROR_TITLE);
            error.setContentText("An error happened while processing your order." +
                                 "\\nError: {0}. " + message +
                                 "\\nPlease try again later.");
            javafx.application.Platform.runLater(error::showAndWait);
        }
    }
    
    public BooleanProperty processingProperty() {
        return processing;
    }
    
    public void cancel() {
        stopProcessing(false, null);
    }
}
