package com.onlyapps.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.onlyapps.aspectj.DebugTrace;

import hugo.weaving.DebugLog;

/**
 * 어노테이선 처리
 */
public class AnnotationTestActivity extends Activity {

    @DebugTrace
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation_test);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @DebugLog
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, button1!", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @DebugLog
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hello, button2!", Toast.LENGTH_SHORT).show();
            }
        });

        testAnnotatedMethod(1000);
    }

    @DebugTrace
    private void testAnnotatedMethod(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}

