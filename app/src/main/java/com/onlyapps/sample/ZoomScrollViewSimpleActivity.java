package com.onlyapps.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.onlyapps.sample.utils.SampleData;

public class ZoomScrollViewSimpleActivity extends AppCompatActivity {

    private ScrollView mScrollView;

    // step 1: add some instance
    private float mScale = 1f;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_scrollview);

        mScrollView = (ScrollView) findViewById(R.id.scrollViewZoom);

        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        for (int i = 0; i < 10; i++) {
            root.addView(SampleData.getImageView(this, i));
        }

        //step 2: create instance from GestureDetector(this step sholude be place into onCreate())
        mGestureDetector = new GestureDetector(this, new GestureListener());

        // animation for scalling
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale = 1 - detector.getScaleFactor();

                float prevScale = mScale;
                mScale += scale;

                if (mScale < 0.1f) {
                    mScale = 0.1f;
                }
                if (mScale > 1f) {
                    mScale = 1f;
                }

                ScaleAnimation animation = new ScaleAnimation(1f / prevScale, 1f / mScale, 1f / prevScale, 1f / mScale, detector.getFocusX(), detector.getFocusY());
                animation.setDuration(0);
                animation.setFillAfter(true);

                mScrollView.startAnimation(animation);
                return true;
            }
        });
    }

    // step 3: override dispatchTouchEvent()
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    //step 4: add private class GestureListener
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }
    }

}
