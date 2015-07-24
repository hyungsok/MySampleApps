package com.onlyapps.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.etsy.android.sample.R;

public class AnimationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("애니메이션 테스트");
        setContentView(R.layout.activity_animation);
    }

    public void scaleupAnimation(View view) {
        // Create a scale-up animation that originates at the button
        // being pressed.
        ActivityOptionsCompat opts = ActivityOptionsCompat.makeScaleUpAnimation(view, 0, 0, view.getWidth(), view.getHeight());
        // Request the activity be started, using the custom animation options.
        ActivityCompat.startActivity(AnimationActivity.this, new Intent(AnimationActivity.this, AnimationDetailActivity.class), opts.toBundle());
    }

    public void thumbNailScaleAnimation(View view) {
        view.setDrawingCacheEnabled(true);
        view.setPressed(false);
        view.refreshDrawableState();
        Bitmap bitmap = view.getDrawingCache();
        ActivityOptionsCompat opts = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, 0, 0);
        // Request the activity be started, using the custom animation options.
        ActivityCompat.startActivity(AnimationActivity.this, new Intent(AnimationActivity.this, AnimationDetailActivity.class), opts.toBundle());
        view.setDrawingCacheEnabled(false);
    }

    public void fadeAnimation(View view) {
        ActivityOptionsCompat opts = ActivityOptionsCompat.makeCustomAnimation(AnimationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);
        // Request the activity be started, using the custom animation options.
        ActivityCompat.startActivity(AnimationActivity.this, new Intent(AnimationActivity.this, AnimationDetailActivity.class), opts.toBundle());
    }
}
