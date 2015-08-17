package com.onlyapps.sample.adapter;


import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onlyapps.sample.R;
import com.onlyapps.sample.utils.Utils;
import com.onlyapps.sample.view.DynamicHeightImageView;

import java.util.ArrayList;
import java.util.Random;

/***
 * ADAPTER
 */

public class SampleImageAdapter extends BaseAdapter {

    private static final String TAG = "TTT";
    private Context context;

    private ArrayList<String> mDatas = new ArrayList<String>();
    public void add(String data) {
        mDatas.add(data);
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SampleImageAdapter(final Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        this.context = context;
    }

    static class ViewHolder {
        DynamicHeightImageView image;
        TextView title;
        ImageView btnGo;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_image, parent, false);
            vh = new ViewHolder();
            vh.image = (DynamicHeightImageView) convertView.findViewById(R.id.image);
            vh.btnGo = (ImageView) convertView.findViewById(R.id.btn_go);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        vh.title.setText("￦ " + Utils.getSampleValue(position));
        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);
        vh.image.setHeightRatio(positionHeight);
        Glide.with(context).load(getItem(position)).into(vh.image);

        vh.btnGo.setImageResource(R.drawable.onebit_off);
        vh.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(context, "Button Clicked Position " +
                        position, Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 1.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}