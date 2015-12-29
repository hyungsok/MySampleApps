package com.onlyapps.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import hugo.weaving.DebugLog;

public class AnnotationTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_test);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @DebugLog
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, button1!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @DebugLog
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, button2!", Toast.LENGTH_SHORT).show();
            }
        });

        testAnnotatedMethod(1000);
    }

    @DebugLog
    private void testAnnotatedMethod(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
