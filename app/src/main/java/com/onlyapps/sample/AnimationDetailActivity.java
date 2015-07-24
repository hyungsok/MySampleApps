package com.onlyapps.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.etsy.android.sample.R;

public class AnimationDetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_animation);
    }
}