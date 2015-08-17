package com.onlyapps.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.onlyapps.sample.utils.SampleData;

public class ZoomScrollViewCustomActivity extends AppCompatActivity {

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_scroll_view_custom);

        mScrollView = (ScrollView) findViewById(R.id.scrollViewZoom);

        LinearLayout root = (LinearLayout) findViewById(R.id.root);
        for (int i = 0; i < 10; i++) {
            root.addView(SampleData.getImageView(this, i));
        }
    }
}
