package com.onlyapps.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by hyungsoklee on 2015. 7. 2..
 */
public class CustomViewTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_view_test);
    }
}
