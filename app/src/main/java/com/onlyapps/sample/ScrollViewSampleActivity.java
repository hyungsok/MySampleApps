package com.onlyapps.sample;

import android.app.Activity;
import android.os.Bundle;

import com.onlyapps.sample.view.SynchronizedScrollView;


public class ScrollViewSampleActivity extends Activity {

    private SynchronizedScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_sample);

        mScrollView = (SynchronizedScrollView) findViewById(R.id.scroll);

        mScrollView.setAnchorView(findViewById(R.id.anchor));
        mScrollView.setSynchronizedView(findViewById(R.id.header));
    }

}
