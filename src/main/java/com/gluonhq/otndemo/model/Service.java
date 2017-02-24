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
package com.gluonhq.otndemo.model;

import com.gluonhq.connect.ConnectState;
import com.gluonhq.connect.GluonObservableObject;
import java.util.UUID;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Singleton;

/**
 * A service class that provides the data from a back end system. Some methods
 * require an authenticated user to be available.
 */
@Singleton
public class Service {

    /**
     * Retrieves a list of available OTN coffees for the OTN Get-a-coffee
     * experience.
     *
     * @return list of OTNCoffees
     */
    public ReadOnlyListProperty<OTNCoffee> retrieveOTNCoffees() {
        ObservableList<OTNCoffee> list = FXCollections.observableArrayList();
        Platform.runLater(() -> {
            list.add(new OTNCoffee("type1", "Moka"));
            list.add(new OTNCoffee("type2", "Arabica"));
        });
        return new SimpleListProperty(list);
    }

    /**
     * Orders the coffee and returns the ID of the order.
     *
     * @param coffee the type of coffee ordered
     * @param strength int from 0 to 10
     * @return the ID of the order
     */
    public GluonObservableObject<OTNCoffeeOrder> orderOTNCoffee(OTNCoffee coffee, int strength) {
        OTNCoffeeOrder order = new OTNCoffeeOrder(coffee.getType(), strength);
        order.setLink(UUID.randomUUID().toString());
        GluonObservableObject<OTNCoffeeOrder> answer = new GluonObservableObject<>();
        answer.set(order);
        Platform.runLater(() -> answer.setState(ConnectState.SUCCEEDED));
        return answer;
    }

    /**
     * Orders the shape and returns the ID of the order.
     *
     * @param shape TODO: the format of the shape must still be defined.
     * @return the ID of the order
     */
    public GluonObservableObject<OTNCarvedBadgeOrder> orderOTNCarveABadge(String shape) {
        OTNCarvedBadgeOrder order = new OTNCarvedBadgeOrder(shape);
        order.setLink(UUID.randomUUID().toString());
        GluonObservableObject<OTNCarvedBadgeOrder> answer = new GluonObservableObject<>();
        answer.set(order);
        Platform.runLater(() -> answer.setState(ConnectState.SUCCEEDED));
        return answer;
    }
    
}
