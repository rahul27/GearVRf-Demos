/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.gearvrf.gvreyepicking;

import android.os.Bundle;

import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.controller.Controller;
import com.google.vr.sdk.controller.ControllerManager;

import org.gearvrf.GVRActivity;

public class SampleActivity extends GVRActivity {

    // These two objects are the primary APIs for interacting with the Daydream controller.
    private ControllerManager controllerManager;
    private Controller controller;
    private SampleMain main;
	
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        main = new SampleMain(this);
        setScript(main, "gvr.xml");

        // Start the ControllerManager and acquire a Controller object which represents a single
        // physical controller. Bind our listener to the ControllerManager and Controller.
        EventListener listener = new EventListener();
        controllerManager = new ControllerManager(this, listener);

        controller = controllerManager.getController();
        controller.setEventListener(listener);

        // This configuration won't be required for normal GVR apps. However, since this sample
        // doesn't
        // use GvrView, it needs pretend to be a VR app in order to receive controller events. The
        // Activity.setVrModeEnabled is only enabled on in N, so this is an GVR-internal utility
        // method
        // to configure the app via reflection.
        //
        // If this sample is compiled with the N SDK, Activity.setVrModeEnabled can be called
        // directly.
        AndroidCompat.setVrModeEnabled(this, true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        controllerManager.start();
    }

    @Override
    protected void onStop() {
        controllerManager.stop();

        super.onStop();
    }

    private class EventListener extends Controller.EventListener
            implements ControllerManager.EventListener {

        @Override
        public void onApiStatusChanged(int i) {
            //Log.d("rahul", "onApiStatusChanged " + i);
            //controller.update();
        }

        @Override
        public void onRecentered() {
            //Log.d("rahul", "onRecentered");
        }

        @Override
        public void onUpdate() {
            //super.onUpdate();
            //Log.d("rahul", "onUpdate");

            controller.update();
            main.updatePivot(controller.orientation.w, controller.orientation.x, controller
                    .orientation.y, controller.orientation.z);
        }
    }
}
