package com.onlyapps.sample.utils;

import android.annotation.TargetApi;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

/**
 * Created by hyungsoklee on 2015. 6. 24..
 */
public class ListViewScrollHelper {
    /**
     * Returns a child at a specific position from within an {@link AdapterView}.
     *
     * @param view     the {@link AdapterView} which contains the child
     * @param position the position of the child
     * @return child at position as a {@link View}
     */
    public static View getChildAtPosition(final AdapterView<?> view, final int position) {
        if (view != null) {
            final int index = position - view.getFirstVisiblePosition();
            if ((index >= 0) && (index < view.getChildCount())) {
                return view.getChildAt(index);
            }
        }
        return null;
    }

    /**
     * Scrolls smooth to a specific position in the {@link AbsListView}.
     *
     * @param view     the {@link AbsListView} which should smooth scroll to a specific position
     * @param position the position the listview should scroll to
     */
    @TargetApi(14)
    public static void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        if (view == null) {
            return;
        }
        View child = getChildAtPosition(view, position);
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }
        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(final AbsListView listview, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
                // nothing to do here
            }

            @Override
            public void onScrollStateChanged(final AbsListView listview, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    listview.setOnScrollListener(null);
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPosition(position, 0);
            }
        });
    }
}