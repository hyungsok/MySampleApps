package com.onlyapps.sample.recycleviews;

/**
 * Created by hyungsoklee on 2016. 1. 22..
 */

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewConfiguration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A simple {@code RecyclerView} scrolling tricks.
 * <p/>
 * NOTE: the gathered scrollY may not the same as the user scroll distance.
 *
 * @author longkai
 */
public abstract class QuickReturnRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static final int LINEAR = 0;
    public static final int GRID = 1;
    public static final int STAGGERED_GRID = 2;

    // more customized layout manager goes here

    private static final int TOUCH_SLOP_FACTOR = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINEAR, GRID, STAGGERED_GRID})
    public @interface LayoutManager {
    }

    @LayoutManager
    private final int mLayoutManager;

    private final int mTouchSlop;
    private final int mTopOffset;

    private int mScrollY;
    private boolean mVisible = true;

    private int[] mPositions; // currently only for the staggered grid layout manager

    public QuickReturnRecyclerOnScrollListener(Context context, int offset, @LayoutManager int layoutManager) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * TOUCH_SLOP_FACTOR;
        mTopOffset = offset;
        mLayoutManager = layoutManager;
    }

    public QuickReturnRecyclerOnScrollListener(Context context, @LayoutManager int layoutManager) {
        this(context, -1, layoutManager);
    }

    public QuickReturnRecyclerOnScrollListener(Context context) {
        this(context, -1, LINEAR);
    }

    /**
     * Notify that the caller needs to handle hide animation.
     *
     * @param dy currently scrollY since last animation if any
     */
    protected abstract void onHide(int dy);

    /**
     * Notify that the caller needs to handle show animation.
     *
     * @param dy currently scrollY since last animation if any
     */
    protected abstract void onShow(int dy);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        // if the user stop scroll, reset the current scrollY
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            mScrollY = 0;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int firstPosition = findFirstPosition(recyclerView);
        if (mTopOffset > 0 && firstPosition < mTopOffset) {
            handleOffsetScrolling(recyclerView);
        } else {
            // handle scroll
            quickReturn();
        }

        // finally, gather dy
        if ((mVisible && dy > 0) || (!mVisible && dy < 0)) {
            mScrollY += dy;
        }
    }

    private void quickReturn() {
        if (mScrollY > mTouchSlop && mVisible) {
            // up
            onHide(mScrollY);
            mVisible = false;
            mScrollY = 0;
        } else if (mScrollY < -mTouchSlop && !mVisible) {
            // down
            onShow(mScrollY);
            mVisible = true;
            mScrollY = 0;
        }
    }

    private void handleOffsetScrolling(RecyclerView recyclerView) {
        if (!mVisible) {
            onShow(mScrollY);
            mVisible = true;
            mScrollY = 0;
        }
    }

    private int findFirstPosition(RecyclerView recyclerView) {
        int firstPosition = -1;
        switch (mLayoutManager) {
            case LINEAR:
                firstPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                break;
            case GRID:
                firstPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager lm = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                if (mPositions == null) {
                    mPositions = new int[lm.getSpanCount()];
                }
                lm.findFirstVisibleItemPositions(mPositions);
                final int count = mPositions.length;
                for (int i = 0; i < count; i++) {
                    firstPosition = Math.min(firstPosition, mPositions[i]);
                }
                break;
        }
        return firstPosition;
    }

    public static
    @QuickReturnRecyclerOnScrollListener.LayoutManager
    int resolveLayoutManager(RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            return LINEAR;
        } else if (lm instanceof GridLayoutManager) {
            return GRID;
        } else if (lm instanceof StaggeredGridLayoutManager) {
            return STAGGERED_GRID;
        }

        // default
        return LINEAR;
    }
}
