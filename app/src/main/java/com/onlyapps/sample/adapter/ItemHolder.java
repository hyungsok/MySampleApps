package com.onlyapps.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.etsy.android.sample.R;
import com.onlyapps.sample.view.DynamicHeightImageView;

/**
 * Created by hyungsoklee on 2015. 6. 18..
 */
public class ItemHolder extends RecyclerView.ViewHolder {
    public DynamicHeightImageView image;
    public TextView text;
    public FrameLayout root;

    public ItemHolder(View itemView) {
        super(itemView);
        root = (FrameLayout) itemView.findViewById(R.id.root);
        image = (DynamicHeightImageView) itemView.findViewById(R.id.image);
        text = (TextView) itemView.findViewById(R.id.title);
    }
}
