package com.onlyapps.sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.onlyapps.sample.utils.ScreenUtils;

public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final ImageView imageview = (ImageView) findViewById(R.id.image);

        Bitmap bitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.sample2_bg);
        int leftMargin = ScreenUtils.getPixelFromDip(this, 15f);
        int screenWidth = ScreenUtils.getScreenWidth(this) + leftMargin;
        int screenHeight = ScreenUtils.getScreenHeight(this);

        if (screenWidth > 0 && screenHeight > 0 && bitmapBg != null) {
            int bitmapWidth = bitmapBg.getWidth();
            int bitmapHeight = bitmapBg.getHeight();
            if(screenWidth > bitmapWidth || screenHeight > bitmapHeight) {
                bitmapBg = resizeBitmap(bitmapBg, screenWidth, screenHeight);
            }
            if (bitmapWidth > 0 && bitmapHeight > 0) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageview.getLayoutParams();
                params.leftMargin = ScreenUtils.getScreenWidth(this) - bitmapBg.getWidth();
                params.width = screenWidth;
                params.height = screenHeight;
                imageview.setImageBitmap(bitmapBg);
                imageview.setVisibility(View.VISIBLE);
            }
        }

        TranslateAnimation translate = new TranslateAnimation(0, leftMargin, 0, 0);
        translate.setDuration(2000);
        translate.setFillAfter(true);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageview.startAnimation(translate);
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int screenWidth, int screenHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int newWidth = bitmapWidth;
        int newHeight = bitmapHeight;

        int wmargin = 0;
        int hmargin = 0;
        if (screenWidth > newWidth) {
            wmargin = screenWidth - bitmapWidth;
            newWidth += wmargin;
            hmargin = wmargin / bitmapWidth * bitmapHeight;
            newHeight += hmargin;
        }

        if (screenHeight > newHeight) {
            hmargin = screenHeight - newHeight;
            newHeight += hmargin;
            wmargin = hmargin / newHeight * newWidth;
            newWidth += wmargin;
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }
}
