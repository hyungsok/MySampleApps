package com.onlyapps.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.etsy.android.sample.R;
import com.onlyapps.sample.view.RevealBackgroundView;


public class RevealBackgroundActivity extends Activity {

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private RevealBackgroundView vRevealBackground;

    public static void startActivity(Activity activity, int[] startingLocation) {
        Intent intent = new Intent(activity, RevealBackgroundActivity.class);
        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_background);
        vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);
        setupRevealBackground(savedInstanceState);
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setFillPaintColor(0xFF993322);
        vRevealBackground.setOnStateChangeListener(new RevealBackgroundView.OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                if (RevealBackgroundView.STATE_FINISHED == state) {
                } else {
                }
            }
        });
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_reveal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
