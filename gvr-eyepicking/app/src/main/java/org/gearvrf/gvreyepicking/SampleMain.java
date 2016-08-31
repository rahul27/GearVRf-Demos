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

import android.view.MotionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gearvrf.*;
import org.gearvrf.GVRMaterial.GVRShaderType;
import org.gearvrf.scene_objects.GVRSphereSceneObject;
import org.gearvrf.utility.Log;

public class SampleMain extends GVRScript {
    public class PickHandler implements IPickEvents
    {
        public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo)
        {
            sceneObj.getRenderData().getMaterial().setVec4("u_color", PICKED_COLOR_R, PICKED_COLOR_G, PICKED_COLOR_B, PICKED_COLOR_A);
        }
        public void onExit(GVRSceneObject sceneObj)
        {
            sceneObj.getRenderData().getMaterial().setVec4("u_color", UNPICKED_COLOR_R, UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        }
        public void onNoPick(GVRPicker picker) { }
        public void onPick(GVRPicker picker) { }
        public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }      
    }

    private static final String TAG = "SampleMain";

    private static final float UNPICKED_COLOR_R = 0.7f;
    private static final float UNPICKED_COLOR_G = 0.7f;
    private static final float UNPICKED_COLOR_B = 0.7f;
    private static final float UNPICKED_COLOR_A = 1.0f;
    private static final float PICKED_COLOR_R = 1.0f;
    private static final float PICKED_COLOR_G = 0.0f;
    private static final float PICKED_COLOR_B = 0.0f;
    private static final float PICKED_COLOR_A = 1.0f;

    private GVRContext mGVRContext = null;
    private List<GVRSceneObject> mObjects = new ArrayList<GVRSceneObject>();
    private IPickEvents mPickHandler = new PickHandler();
    private GVRPicker mPicker;

    private GVRActivity mActivity;
    private GVRSphereSceneObject cursor;
    
    SampleMain(GVRActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onInit(GVRContext gvrContext) {
        mGVRContext = gvrContext;

        GVRScene mainScene = mGVRContext.getNextMainScene();

        mainScene.getMainCameraRig().getLeftCamera()
                .setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        mainScene.getMainCameraRig().getRightCamera()
                .setBackgroundColor(1.0f, 1.0f, 1.0f, 1.0f);
        mainScene.getEventReceiver().addListener(mPickHandler);
        //mPicker = new GVRPicker(gvrContext, mainScene);

        /*
         * Adding Boards
         */
        GVRSceneObject object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(0.0f, 3.0f, -5.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(0.0f, -3.0f, -5.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(-3.0f, 0.0f, -5.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(3.0f, 0.0f, -5.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(3.0f, 3.0f, -5.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(3.0f, -3.0f, -5.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(-3.0f, 3.0f, -5.0f);
        attachSphereCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorBoard(1.0f, 1.0f);
        object.getTransform().setPosition(-3.0f, -3.0f, -5.0f);
        attachSphereCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        /*
         * Adding bunnies.
         */

        GVRMesh mesh = null;
        try {
            mesh = mGVRContext.loadMesh(new GVRAndroidResource(mGVRContext,
                    "bunny.obj"));
        } catch (IOException e) {
            e.printStackTrace();
            mesh = null;
        }
        if (mesh == null) {
            mActivity.finish();
            Log.e(TAG, "Mesh was not loaded. Stopping application!");
        }
        // activity was stored in order to stop the application if the mesh is
        // not loaded. Since we don't need anymore, we set it to null to reduce
        // chance of memory leak.
        mActivity = null;

        // These 2 are testing by the whole mesh.
        object = getColorMesh(1.0f, mesh);
        object.getTransform().setPosition(0.0f, 0.0f, -2.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorMesh(1.0f, mesh);
        object.getTransform().setPosition(3.0f, 3.0f, -2.0f);
        attachMeshCollider(object);
        mainScene.addSceneObject(object);
        object.getRenderData().setCullTest(false);
        mObjects.add(object);

        // These 2 are testing by the bounding box of the mesh.
        object = getColorMesh(2.0f, mesh);
        object.getTransform().setPosition(-5.0f, 0.0f, -2.0f);
        attachBoundsCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        object = getColorMesh(1.0f, mesh);
        object.getTransform().setPosition(0.0f, -5.0f, -2.0f);
        attachBoundsCollider(object);
        mainScene.addSceneObject(object);
        mObjects.add(object);

        cursor = new GVRSphereSceneObject(gvrContext, true);
        GVRMaterial material = new GVRMaterial(mGVRContext, GVRShaderType.BeingGenerated.ID);
        material.setVec4("u_color", 0.0f, 1.0f, 0.0f, UNPICKED_COLOR_A);
        cursor.getRenderData().setMaterial(material);
        cursor.getTransform().setScale(0.5f, 0.5f, 0.5f);
        cursor.getRenderData().setShaderTemplate(ColorShader.class);
        cursor.getTransform().setPosition(0.0f, 0.0f, -3.0f);
        mainScene.getMainCameraRig().addChildObject(cursor);

        GVRObjectPicker objectPicker = new GVRObjectPicker(gvrContext, mainScene);
        cursor.attachComponent(objectPicker);
    }

    @Override
    public void onStep() {
    }

    private GVRSceneObject getColorBoard(float width, float height) {
        GVRMaterial material = new GVRMaterial(mGVRContext, GVRShaderType.BeingGenerated.ID);
        material.setVec4("u_color", UNPICKED_COLOR_R,
                UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);
        GVRSceneObject board = new GVRSceneObject(mGVRContext, width, height);
        board.getRenderData().setMaterial(material);
        board.getRenderData().setShaderTemplate(ColorShader.class);
        return board;
    }

    private GVRSceneObject getColorMesh(float scale, GVRMesh mesh) {
        GVRMaterial material = new GVRMaterial(mGVRContext, GVRShaderType.BeingGenerated.ID);
        material.setVec4("u_color", UNPICKED_COLOR_R,
                UNPICKED_COLOR_G, UNPICKED_COLOR_B, UNPICKED_COLOR_A);

        GVRSceneObject meshObject = null;
        meshObject = new GVRSceneObject(mGVRContext, mesh);
        meshObject.getTransform().setScale(scale, scale, scale);
        meshObject.getRenderData().setMaterial(material);
        meshObject.getRenderData().setShaderTemplate(ColorShader.class);
        return meshObject;
    }

    private void attachMeshCollider(GVRSceneObject sceneObject) {
        sceneObject.attachComponent(new GVRMeshCollider(mGVRContext, false));
    }

    private void attachSphereCollider(GVRSceneObject sceneObject) {
        sceneObject.attachComponent(new GVRSphereCollider(mGVRContext));
    }
    
    private void attachBoundsCollider(GVRSceneObject sceneObject) {
        sceneObject.attachComponent(new GVRMeshCollider(mGVRContext, true));
    }

    public boolean onSwipe(MotionEvent e, VRTouchPadGestureDetector.SwipeDirection
            swipeDirection, float velocityX, float velocityY) {
        if (cursor != null) {
            if (swipeDirection == VRTouchPadGestureDetector.SwipeDirection.Forward) {
                cursor.getTransform().translate(0.0f, 0.0f, -1.0f);
            } else if (swipeDirection == VRTouchPadGestureDetector.SwipeDirection.Backward) {
                cursor.getTransform().translate(0.0f, 0.0f, 1.0f);
            }
        }
        return true;
    }
}
