package com.onlyapps.sample.recycleviews;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hyungsoklee on 2016. 1. 27..
 */
public class OnScrollStickyViewListener extends RecyclerView.OnScrollListener {
    private static final String TAG = OnScrollStickyViewListener.class.getSimpleName();

    private LinearLayoutManager mLayoutManager;
    private ViewGroup mStickyView;

    private StickyChangeListener mStickyChangeListener;
    private StickyAddListener mStickyAddListener;

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
    private Map<Integer, Boolean> mStickViewMap = new HashMap<>();

    public interface StickyChangeListener {
        void onChange(int position, boolean isSticky);
    }

    public interface StickyAddListener {
        RecyclerView.ViewHolder addView(RecyclerView recyclerView, int position);
    }

    public void setStickyChangeListener(StickyChangeListener listener) {
        this.mStickyChangeListener = listener;
    }

    public void setStickyAddListener(StickyAddListener listener) {
        this.mStickyAddListener = listener;
    }

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
                    if (isNewStickyView(position)) {
                        if (hasStickyView()) {
                            removeStickView();
                        }
                        addStickyView(recyclerView, position);
                    }
                } else if (mLastedStickyViewIndex > position) {
                    if (hasStickyView()) {
                        removeStickView();

                        int headerPostion = preStickHeaderPosition(position);
                        if (headerPostion > 0) {
                            if (mStickyChangeListener != null) {
                                mStickyChangeListener.onChange(mLastedStickyViewIndex, false);
                            }
                            addStickyView(recyclerView, headerPostion);
                        } else {
                            if (mStickyChangeListener != null) {
                                mStickyChangeListener.onChange(mLastedStickyViewIndex, false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void addStickyView(RecyclerView recyclerView, int position) {
        RecyclerView.ViewHolder holder = null;
        if (mStickyAddListener != null) {
            holder = mStickyAddListener.addView(recyclerView, position);
        } else {
            holder = recyclerView.getAdapter().onCreateViewHolder(recyclerView, position);
            recyclerView.getAdapter().onBindViewHolder(holder, position);
        }

        mStickyView.addView(holder.itemView);
        mStickyView.setVisibility(View.VISIBLE);

        mStickViewMap.put(position, true);
        mLastedStickyViewIndex = position;

        if (mStickyChangeListener != null) {
            mStickyChangeListener.onChange(position, true);
        }
    }

    private boolean hasStickyView() {
        if (mStickyView != null) {
            return mStickyView.getChildCount() > 0;
        }
        return false;
    }

    private boolean isNewStickyView(int position) {
        return mStickViewMap.get(position) == null || !mStickViewMap.get(position).booleanValue();
    }

    private void removeStickView() {
        mStickyView.removeAllViews();
        mStickyView.setVisibility(View.GONE);
        mStickViewMap.put(mLastedStickyViewIndex, false);
    }

    private int preStickHeaderPosition(int position) {
        List<Integer> list = new ArrayList<>(mStickViewMap.keySet());
        Collections.sort(list);

        for (int i = list.size() - 1; i >= 0; i--) {
            int p = list.get(i);
            if (position >= p) {
                return p;
            }
        }
        return 0;
    }

}
