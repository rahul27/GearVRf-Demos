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
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;
import org.gearvrf.GVRTexture;
import org.gearvrf.GVRTransform;
import org.joml.Vector3f;

public class SampleMain extends GVRScript {

    private GVRContext mGVRContext;
    GVRSceneObject parent, sceneObject;
    Vector3f objectPosition, direction;

    @Override
    public void onInit(GVRContext gvrContext) {

        // save context for possible use in onStep(), even though that's empty
        // in this sample
        mGVRContext = gvrContext;
        objectPosition = new Vector3f();
        direction = new Vector3f();

        GVRScene scene = gvrContext.getNextMainScene();

        // set background color
        GVRCameraRig mainCameraRig = scene.getMainCameraRig();
        mainCameraRig.getLeftCamera()
                .setBackgroundColor(Color.WHITE);
        mainCameraRig.getRightCamera()
                .setBackgroundColor(Color.WHITE);
        parent = new GVRSceneObject(gvrContext);
        // load texture
        GVRTexture texture = gvrContext.loadTexture(new GVRAndroidResource(
                mGVRContext, R.drawable.gearvr_logo));

        // create a scene object (this constructor creates a rectangular scene
        // object that uses the standard 'unlit' shader)
        sceneObject = new GVRSceneObject(gvrContext, 4.0f, 2.0f,
                texture);

        // set the scene object position
        parent.getTransform().setPosition(0.0f, 0.0f, -3.0f);

        // add the scene object to the scene graph
        parent.addChildObject(sceneObject);
        scene.addSceneObject(parent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        for (int i = 0; i < 20; i = i + 5) {
                            for (int j = 0; j < 20; j = j + 5) {
                                for (int k = 0; k < 20; k = k + 5) {
                                    parent.getTransform().setPosition(i, j, k);
                                    lookAt();

                                    Thread.sleep(1000);
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onStep() {


    }

    /**
     * Lookat implemented using:
     * <p/>
     * http://mmmovania.blogspot.com/2014/03/making-opengl-object-look-at-another.html
     */
    protected void lookAt() {
        GVRTransform transform = parent.getTransform();
        objectPosition.set(transform.getPositionX(), transform.getPositionY(),
                transform.getPositionZ());
        objectPosition.negate(direction);

        Vector3f up;
        direction.normalize();

        if (Math.abs(direction.x) < 0.00001
                && Math.abs(direction.z) < 0.00001) {
            if (direction.y > 0) {
                up = new Vector3f(0.0f, 0.0f, -1.0f); // if direction points in +y
            } else {
                up = new Vector3f(0.0f, 0.0f, 1.0f); // if direction points in -y
            }
        } else {
            up = new Vector3f(0.0f, 1.0f, 0.0f); // y-axis is the general up
        }

        up.normalize();
        Vector3f right = new Vector3f();
        up.cross(direction, right);
        right.normalize();
        direction.cross(right, up);
        up.normalize();

        float[] matrix = new float[]{right.x, right.y, right.z, 0.0f, up.x, up.y,
                up.z, 0.0f, direction.x, direction.y, direction.z, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f};
        sceneObject.getTransform().setModelMatrix(matrix);
    }
}
