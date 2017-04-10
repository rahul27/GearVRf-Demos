package org.gearvrf.sample.controller;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRCameraRig;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRCursorController;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRMeshCollider;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.gearvrf.IPickEvents;
import org.gearvrf.OvrGearController;
import org.gearvrf.io.CursorControllerListener;
import org.gearvrf.io.GVRControllerType;
import org.gearvrf.io.GVRInputManager;
import org.joml.Vector3f;

import java.io.IOException;

import java.util.concurrent.Future;

public class SampleMain extends GVRMain {

    public static final String TAG = "SampleMain";

    private static final float DEPTH = -3.5f;

    private Context context;
    private GVRContext mGVRContext;
    private GVRScene mScene;

    private GVRPicker mPicker;
    private PickHandler mPickHandler = new PickHandler();
    private GVRSceneObject selectedObject;
    private GVRSceneObject sphere;

    private enum ButtonState {
        UP, DOWN
    }

    public SampleMain(Context context) {
        this.context = context;
    }

    @Override
    public void onInit(GVRContext gvrContext) {
        mGVRContext = gvrContext;
        mScene = gvrContext.getMainScene();
        mPicker = new GVRPicker(gvrContext, mScene);
        mScene.getEventReceiver().addListener(mPickHandler);

        GVRCameraRig mainCameraRig = mScene.getMainCameraRig();

        mScene.addSceneObject(test());

        GVRInputManager inputManager = gvrContext.getInputManager();
        inputManager.addCursorControllerListener(cursorControllerListener);
        for (GVRCursorController cursor : inputManager.getCursorControllers()) {
            cursorControllerListener.onCursorControllerAdded(cursor);
        }
    }

    private CursorControllerListener cursorControllerListener = new CursorControllerListener() {
        @Override
        public void onCursorControllerAdded(GVRCursorController gvrCursorController) {
            if (gvrCursorController.getControllerType() == GVRControllerType.CONTROLLER) {
                android.util.Log.d(TAG, "Got the orientation remote controller");
                try {
                    GVRSceneObject parent = new GVRSceneObject(mGVRContext);
                    Future<GVRTexture> futureTexture = mGVRContext.getAssetLoader()
                            .loadFutureTexture(new GVRAndroidResource(mGVRContext, "texture.png"));

                    sphere = new GVRSceneObject(mGVRContext, mGVRContext.loadFutureMesh(new
                            GVRAndroidResource(mGVRContext, "sphere.obj")), futureTexture);
                    sphere.getTransform().setPosition(0.0f, 0.0f, DEPTH);
                    sphere.getTransform().setScale(0.5f, 0.5f, 0.5f);
                    parent.addChildObject(sphere);

                    GVRSceneObject cube = new GVRSceneObject(mGVRContext, mGVRContext
                            .loadFutureMesh(new GVRAndroidResource(mGVRContext, "cube.obj")),
                            futureTexture);
                    cube.getTransform().setPosition(0.0f, 0.0f, 0.0f);
                    cube.getTransform().setScale(0.05f, 0.05f, 1f);
                    parent.addChildObject(cube);


                    sphere.getRenderData().setDepthTest(false);
                    sphere.getRenderData().setRenderingOrder(GVRRenderData.GVRRenderingOrder
                            .OVERLAY);

                    gvrCursorController.setSceneObject(parent);
                    parent.attachComponent(mPicker);

                    gvrCursorController.addControllerEventListener(controllerEventListener);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                gvrCursorController.setEnable(false);
            }
        }

        @Override
        public void onCursorControllerRemoved(GVRCursorController gvrCursorController) {
            if (gvrCursorController.getControllerType() == GVRControllerType.CONTROLLER) {
                android.util.Log.d(TAG, "Got the orientation remote controller");
            }
        }
    };

    private GVRCursorController.ControllerEventListener controllerEventListener = new
            GVRCursorController.ControllerEventListener() {
                @Override
                public void onEvent(GVRCursorController gvrCursorController) {
                    KeyEvent keyEvent = gvrCursorController.getKeyEvent();
                    OvrGearController controller = (OvrGearController) gvrCursorController;
                    Vector3f position = controller.getPosition();
                    //Log.d(TAG, String.format("Position %f %f %f", position.x, position.y,
                    //        position.z));
                    if (keyEvent != null) {
                        mPickHandler.setClicked(keyEvent.getAction() == KeyEvent.ACTION_DOWN);
                    }
                }
            };

    private class PickHandler implements IPickEvents {

        private boolean clicked = false;
        private boolean prevClicked = false;
        private ButtonState buttonState;

        public void setClicked(boolean clicked) {
            prevClicked = this.clicked;
            this.clicked = clicked;

            if (prevClicked && !clicked) {
                buttonState = ButtonState.UP;
            } else if (!prevClicked && clicked) {
                buttonState = ButtonState.DOWN;
            }

            if (buttonState == ButtonState.UP && selectedObject != null) {
                selectedObject.getCollider().setEnable(true);
            }
        }
        @Override
        public void onPick(GVRPicker picker) {

        }
        @Override
        public void onNoPick(GVRPicker picker) {

        }
        @Override
        public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject collision) {
            Log.d(TAG, "onEnter");

        }
        @Override
        public void onExit(GVRSceneObject sceneObj) {
            Log.d(TAG, "onExit");
        }
        @Override
        public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject collision) {
            Log.d(TAG, "onInside");
            if (buttonState == ButtonState.DOWN && selectedObject == null) {
                sceneObj.getCollider().setEnable(false);
                selectedObject = sceneObj;
            }
        }
    }
    @Override
    public void onStep() {
    }
    GVRSceneObject test(){
        GVRTexture texture = mGVRContext.loadTexture(new GVRAndroidResource(
                mGVRContext, R.drawable.__default_splash_screen__));
        GVRSceneObject sceneObject = new GVRSceneObject(mGVRContext,2.6f,2.2f,
                texture);
        sceneObject.getTransform().setPosition(0f, 0f, DEPTH);
        //sceneObject.getTransform().setScale(0.1f,0.1f,0.1f);
        GVRMeshCollider collider = new GVRMeshCollider(mGVRContext,true);
        sceneObject.attachComponent(collider);
        // sceneObject.attachComponent(new GVRSphereCollider(getGVRContext()));
        return sceneObject;
    }

}