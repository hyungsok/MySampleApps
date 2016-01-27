package com.onlyapps.sample.recycleviews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.onlyapps.sample.GridLayoutManagerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyungsoklee on 2016. 1. 27..
 */
public class OnScrollStickyViewListener extends RecyclerView.OnScrollListener {
    private static final String TAG = OnScrollStickyViewListener.class.getSimpleName();

    private LinearLayoutManager mLayoutManager;
    private ViewGroup mStickyView;

    public OnScrollStickyViewListener(LinearLayoutManager manager, ViewGroup stickyView) {
        this.mLayoutManager = manager;
        this.mStickyView = stickyView;
    }

    public static class StickyHolder extends RecyclerView.ViewHolder {
        public StickyHolder(View itemView) {
            super(itemView);
        }
    }

    private int mLastedStickyViewIndex = -1;
    private List<Integer> mStickViewList = new ArrayList<Integer>();


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (mLayoutManager != null) {
            int position = mLayoutManager.findFirstVisibleItemPosition();

            if (position > 0) {
                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                Log.d(TAG, "onScrolled() : " + position + ", " + holder);

                if (holder instanceof StickyHolder) {
                    if (hasStickyView()) {
                        removeStickView();
                    }
                    addStickyView(recyclerView, position);
                } else if (mLastedStickyViewIndex > position) {
                    if (hasStickyView()) {
                        removeStickView();

                        int headerPostion = preStickHeaderPosition(position);
                        if (headerPostion > 0) {
                            addStickyView(recyclerView, headerPostion);
                        }
                    }
                }
            }
        }
    }

    private void addStickyView(RecyclerView recyclerView, int position) {
//        RecyclerView.ViewHolder holder = recyclerView.getAdapter().onCreateViewHolder(recyclerView, position);
//        recyclerView.getAdapter().onBindViewHolder(holder, position);

        GridLayoutManagerActivity.MyHeaderRecycleViewAdapter adapter = (GridLayoutManagerActivity.MyHeaderRecycleViewAdapter)recyclerView.getAdapter();
        RecyclerView.ViewHolder holder = adapter.onCreateContentItemViewHolder(recyclerView, 1);
        adapter.onBindContentItemViewHolder(holder, position - adapter.getHeaderItemCount());

        mStickyView.addView(holder.itemView);
        mStickyView.setVisibility(View.VISIBLE);

        if (!mStickViewList.contains(position)) {
            mStickViewList.add(position);
        }
        mLastedStickyViewIndex = position;
    }

    private boolean hasStickyView() {
        if (mStickyView != null) {
            return mStickyView.getChildCount() > 0;
        }
        return false;
    }

    private void removeStickView() {
        mStickyView.removeAllViews();
        mStickyView.setVisibility(View.GONE);
    }

    private int preStickHeaderPosition(int position) {
        for (int i = mStickViewList.size() - 1; i >= 0; i--) {
            int p = mStickViewList.get(i);
            if (position >= p) {
                return p;
            }
        }
        return 0;
    }

}
