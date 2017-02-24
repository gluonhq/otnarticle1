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
package com.gluonhq.otndemo;

import com.gluonhq.charm.glisten.afterburner.AppView;
import com.gluonhq.charm.glisten.afterburner.AppViewRegistry;
import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.otndemo.views.AboutPresenter;
import com.gluonhq.otndemo.views.BadgePresenter;
import com.gluonhq.otndemo.views.CoffeePresenter;
import com.gluonhq.otndemo.views.ExperiencesPresenter;

import static com.gluonhq.charm.glisten.afterburner.AppView.Flag.HOME_VIEW;
import static com.gluonhq.charm.glisten.afterburner.AppView.Flag.SHOW_IN_DRAWER;

public class OTNView  {

    public static final AppViewRegistry registry = new AppViewRegistry();
    public static final AppView EXPERIENCES    = view( ExperiencesPresenter.class, "OTN CodeLounge",    MaterialDesignIcon.VIEW_AGENDA,        SHOW_IN_DRAWER, HOME_VIEW);
    public static final AppView BADGE          = view( BadgePresenter.class,       "Carve a Badge",  MaterialDesignIcon.ALL_INCLUSIVE);
    public static final AppView COFFEE         = view( CoffeePresenter.class,      "Order a Coffee", MaterialDesignIcon.LOCAL_DRINK);
    public static final AppView ABOUT          = view( AboutPresenter.class,       "About",          MaterialDesignIcon.AC_UNIT,            SHOW_IN_DRAWER);

    static AppView view(Class<? extends GluonPresenter<?>> presenterClass, String title, MaterialDesignIcon menuIcon, AppView.Flag... flags ) {
        return registry.createView( name(presenterClass),
                                    title,
                                    presenterClass,
                                    menuIcon,
                                    flags);
    }

    private static String name(Class<? extends GluonPresenter<?>> presenterClass) {
        return presenterClass.getSimpleName().toUpperCase().replace("PRESENTER", "");
    }

}
