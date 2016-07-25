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

package org.gearvrf.simplesample;

import android.graphics.Color;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRCameraRig;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;
import org.gearvrf.GVRTexture;
import org.gearvrf.ZipLoader;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVRRepeatMode;
import org.gearvrf.utility.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

public class SampleMain extends GVRScript {

    private static final String TAG = SampleMain.class.getSimpleName();
    private GVRContext mGVRContext;

    @Override
    public void onInit(GVRContext gvrContext) {

        // save context for possible use in onStep(), even though that's empty
        // in this sample
        mGVRContext = gvrContext;

        GVRScene scene = gvrContext.getNextMainScene();

        // set background color
        GVRCameraRig mainCameraRig = scene.getMainCameraRig();
        mainCameraRig.getLeftCamera()
                .setBackgroundColor(Color.BLACK);
        mainCameraRig.getRightCamera()
                .setBackgroundColor(Color.BLACK);

        // load texture
        GVRTexture texture = gvrContext.loadTexture(new GVRAndroidResource(
                mGVRContext, R.drawable.gearvr_logo));

        // create a scene object (this constructor creates a rectangular scene
        // object that uses the standard 'unlit' shader)
        GVRSceneObject sceneObject = new GVRSceneObject(gvrContext, 4.0f, 2.0f,
                texture);

        // set the scene object position
        sceneObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);

        // add the scene object to the scene graph
        //scene.addSceneObject(sceneObject);

        try {
            List<Future<GVRTexture>> loaderTextures = ZipLoader.load(gvrContext,
                    "loading.zip", new ZipLoader.ZipEntryProcessor<Future<GVRTexture>>() {
                        @Override
                        public Future<GVRTexture> getItem(GVRContext context, GVRAndroidResource
                                resource) {
                            return context.loadFutureTexture(resource);
                        }
                    });

            GVRSceneObject loadingObject = new GVRSceneObject(gvrContext, 1.0f, 1.0f);

            GVRRenderData renderData = loadingObject.getRenderData();
            GVRMaterial loadingMaterial = new GVRMaterial(gvrContext);
            renderData.setMaterial(loadingMaterial);
            loadingMaterial.setMainTexture(loaderTextures.get(0));
            GVRAnimation animation = new GVRImageFrameAnimation(loadingMaterial, 1.5f,
                    loaderTextures);
            animation.setRepeatMode(GVRRepeatMode.REPEATED);
            animation.setRepeatCount(-1);
            animation.start(mGVRContext.getAnimationEngine());

            loadingObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
            scene.addSceneObject(loadingObject);
        } catch (IOException e) {
            Log.e(TAG, "Error loading animation", e);
        }
    }

    @Override
    public void onStep() {
    }
}
