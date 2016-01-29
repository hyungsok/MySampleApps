package com.onlyapps.sample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.onlyapps.sample.R;
import com.onlyapps.sample.recycleviews.OnScrollStickyViewListener;

/**
 * Created by hyungsoklee on 2015. 6. 18..
 */
public class StickyHeaderHolder extends OnScrollStickyViewListener.StickyViewHolder {
    public int id;
    public View root;
    public ImageView image;
    public TextView text;

    public StickyHeaderHolder(View itemView) {
        super(itemView);
        root = itemView;
        image = (ImageView) itemView.findViewById(R.id.image);
        text = (TextView) itemView.findViewById(R.id.text);
    }

    @Override
    public void onStickyViewHolder(RecyclerView.ViewHolder holder) {
    }
}