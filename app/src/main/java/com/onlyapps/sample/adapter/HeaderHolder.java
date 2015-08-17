package com.onlyapps.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.onlyapps.sample.R;

/**
 * Created by hyungsoklee on 2015. 6. 18..
 */
public class HeaderHolder extends RecyclerView.ViewHolder {
    public View root;
    public ImageView image;

    public HeaderHolder(View itemView) {
        super(itemView);
        root = itemView;
        image = (ImageView) itemView.findViewById(R.id.image);
    }
}