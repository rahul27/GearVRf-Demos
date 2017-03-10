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

package org.gearvrf.gvr360Photo;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRSphereSceneObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Minimal360PhotoMain extends GVRMain {
    private GVRSphereSceneObject sphereObject;
    private static List<String> files = new ArrayList<>();
    private GVRContext gvrContext;
    private int index = 0;
    static {
        files.add("Nature_1.jpg");
        files.add("Nature_2.jpg");
        files.add("Nature_3.jpg");
        files.add("Nature_4.jpg");
    }

    @Override
    public void onInit(GVRContext gvrContext) {
        this.gvrContext = gvrContext;
        // get a handle to the scene
        GVRScene scene = gvrContext.getMainScene();
        // load texture
        Future<GVRTexture> texture = null;
        try {
            texture = gvrContext.getAssetLoader().loadFutureTexture(new GVRAndroidResource(gvrContext, files.get(index)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a sphere scene object with the specified texture and triangles facing inward (the 'false' argument)
        sphereObject = new GVRSphereSceneObject(gvrContext, 72, 144, false, texture);
        // add the scene object to the scene graph
        scene.addSceneObject(sphereObject);
    }

    public void onTap() {
        index = (index + 1) % files.size();
        try {
            Future<GVRTexture> texture = gvrContext.loadFutureTexture(new
                    GVRAndroidResource(gvrContext, files.get(index)));
            sphereObject.getRenderData().getMaterial().setMainTexture(texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
