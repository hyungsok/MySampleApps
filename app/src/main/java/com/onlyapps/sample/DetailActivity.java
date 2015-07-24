/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onlyapps.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.etsy.android.sample.R;
import com.onlyapps.sample.utils.Utils;

/**
 * Our secondary Activity which is launched from {@link MainActivity}. Has a simple detail UI
 * which has a large banner image, title and body text.
 */
public class DetailActivity extends Activity {

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_IMAGEURL = "detail:_imageurl";
    public static final String EXTRA_PARAM_POSITION = "detail:_position";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;
    private TextView mText;
    private String mImageUrl;

    public static void startActivity(Activity activity, View view, String imageUrl, int position) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_IMAGEURL, imageUrl);
        intent.putExtra(DetailActivity.EXTRA_PARAM_POSITION, position);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setDrawingCacheEnabled(true);
        Bitmap bitmap = image.getDrawingCache();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, bitmap, 0, 0);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
        image.setDrawingCacheEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);

        mImageUrl = getIntent().getStringExtra(EXTRA_PARAM_IMAGEURL);

        mHeaderImageView = (ImageView) findViewById(R.id.imageview_header);
        mHeaderTitle = (TextView) findViewById(R.id.textview_title);
        mText = (TextView) findViewById(R.id.textview_text);

        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);

        mHeaderTitle.setText("Deatil Page - ￦ " + Utils.getSampleValue(getIntent().getIntExtra(EXTRA_PARAM_POSITION, 1)));
        Glide.with(this).load(mImageUrl).asBitmap().into(new BitmapImageViewTarget(mHeaderImageView) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                applyPaletee(resource);
            }
        });
    }

    private void applyPaletee(Bitmap bitmap) {
        Palette palette = Palette.generate(bitmap);

        mHeaderTitle.setTextColor(palette.getLightMutedColor(Color.WHITE));
        mHeaderTitle.setBackgroundColor(palette.getDarkMutedColor(Color.BLACK));

        mText.setTextColor(palette.getLightVibrantColor(Color.WHITE));
        mText.setBackgroundColor(palette.getDarkVibrantColor(Color.BLACK));

    }
}
