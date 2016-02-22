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
    // [final/static_property]====================[START]===================[final/static_property]
    private static final String TAG = OnScrollStickyViewListener.class.getSimpleName();

    // [final/static_property]=====================[END]====================[final/static_property]
    private ViewGroup mStickyView;

    private int mLastedStickyViewIndex = -1;
    private Map<Integer, Boolean> mStickyViewMap = new HashMap<Integer, Boolean>();

    private StickyChangeListener mStickyChangeListener;

    // [private/protected/public_property]========[START]=======[private/protected/public_property]
    // [private/protected/public_property]=========[END]========[private/protected/public_property]
    // [interface/enum/inner_class]===============[START]==============[interface/enum/inner_class]

    /**
     * 스티키 뷰 처리하려면 StickyViewHolder 상속받아 처리하여야함
     */
    public static abstract class StickyViewHolder extends RecyclerView.ViewHolder {
        public StickyViewHolder(View itemView) {
            super(itemView);
        }
        public abstract void onStickyViewHolder(RecyclerView.ViewHolder holder);
    }

    public interface StickyChangeListener {
        void onChange(int position, boolean isSticky);
    }

    // [interface/enum/inner_class]================[END]===============[interface/enum/inner_class]
    // [inherited/listener_method]================[START]===============[inherited/listener_method]

    /**
     * 스티키 변경 리스너
     *
     * @param listener
     */
    public void setStickyChangeListener(StickyChangeListener listener) {
        this.mStickyChangeListener = listener;
    }

    // [inherited/listener_method]=================[END]================[inherited/listener_method]

    public OnScrollStickyViewListener(ViewGroup stickyView) {
        this.mStickyView = stickyView;
    }
    // [life_cycle_method]========================[START]=======================[life_cycle_method]

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (recyclerView != null) {
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) manager);

                int position = linearLayoutManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                    Log.d(TAG, "onScrolled() : " + position + ", " + holder);

                    if (holder instanceof StickyViewHolder) {
                        if (isNewStickyView(position)) {
                            if (hasStickyView()) {
                                removeStickView(recyclerView, false, position);
                            }
                            addStickyView(recyclerView, position);
                        }
                    } else if (mLastedStickyViewIndex > position) {
                        if (hasStickyView()) {
                            removeStickView(recyclerView, true, position);
                        }
                    }
                }
            }
        }
    }
    // [life_cycle_method]=========================[END]========================[life_cycle_method]
    // [private_method]===========================[START]==========================[private_method]

    /**
     *
     * @param recyclerView
     * @param position
     */
    private void addStickyView(RecyclerView recyclerView, int position) {
        if (recyclerView.getAdapter() != null) {
            final int itemViewType = recyclerView.getAdapter().getItemViewType(position);
            RecyclerView.ViewHolder holder = recyclerView.getAdapter().onCreateViewHolder(recyclerView, itemViewType);
            if (holder instanceof StickyViewHolder) {
                recyclerView.getAdapter().onBindViewHolder(holder, position);
                ((StickyViewHolder) holder).onStickyViewHolder(holder);

                Log.d(TAG, "addStickyView(" + itemViewType + ") : " + position + ", " + holder);

                mStickyView.addView(holder.itemView);
                mStickyView.setVisibility(View.VISIBLE);

                mStickyViewMap.put(position, true);
                mLastedStickyViewIndex = position;

                if (mStickyChangeListener != null) {
                    mStickyChangeListener.onChange(position, true);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    private boolean hasStickyView() {
        if (mStickyView != null) {
            return mStickyView.getChildCount() > 0;
        }
        return false;
    }

    /**
     *
     * @param position
     * @return
     */
    private boolean isNewStickyView(int position) {
        return mStickyViewMap.get(position) == null || !mStickyViewMap.get(position).booleanValue();
    }

    /**
     *
     * @param recyclerView
     * @param checkAddSticky
     * @param position
     */
    private void removeStickView(RecyclerView recyclerView, boolean checkAddSticky, int position) {
        Log.d(TAG, "removeStickView() : " + checkAddSticky + ", " + position);

        mStickyView.removeAllViews();
        mStickyView.setVisibility(View.GONE);
        mStickyViewMap.put(mLastedStickyViewIndex, false);

        if (checkAddSticky) {
            int prePosition = preStickHeaderPosition(position);
            if (prePosition > 0) {
                if (mStickyChangeListener != null) {
                    mStickyChangeListener.onChange(mLastedStickyViewIndex, false);
                }
                addStickyView(recyclerView, prePosition);
            } else {
                if (mStickyChangeListener != null) {
                    mStickyChangeListener.onChange(mLastedStickyViewIndex, false);
                }
            }
        }
    }

    /**
     * 이전 스티키 포지션
     *
     * @param position
     * @return
     */
    private int preStickHeaderPosition(int position) {
        List<Integer> list = new ArrayList<Integer>(mStickyViewMap.keySet());
        Collections.sort(list);

        for (int i = list.size() - 1; i >= 0; i--) {
            int p = list.get(i);
            if (position >= p) {
                return p;
            }
        }
        return 0;
    }

    // [private_method]============================[END]===========================[private_method]
    // [public_method]============================[START]===========================[public_method]
    // [public_method]=============================[END]============================[public_method]
    // [get/set]==================================[START]=================================[get/set]
    // [get/set]===================================[END]==================================[get/set]
}
