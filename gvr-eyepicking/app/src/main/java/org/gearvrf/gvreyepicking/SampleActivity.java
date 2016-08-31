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
import android.view.MotionEvent;

import org.gearvrf.GVRActivity;

public class SampleActivity extends GVRActivity implements VRTouchPadGestureDetector
        .OnTouchPadGestureListener {
    private VRTouchPadGestureDetector mDetector = null;
    private SampleMain main;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        main = new SampleMain(this);
        setScript(main, "gvr.xml");
        mDetector = new VRTouchPadGestureDetector(this);
    }

    @Override
    public boolean onSingleTap(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onSwipe(MotionEvent e, VRTouchPadGestureDetector.SwipeDirection
            swipeDirection, float velocityX, float velocityY) {
        return main.onSwipe(e, swipeDirection, velocityX, velocityY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
