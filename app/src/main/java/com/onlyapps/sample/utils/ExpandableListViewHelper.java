package com.onlyapps.sample.utils;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Created by hyungsoklee on 2015. 6. 29..
 */
public class ExpandableListViewHelper {
    private final static String TAG = ExpandableListViewHelper.class.getSimpleName();

    private final static int ACTION_SMOOTH_SCROLL = 0;
    private final static int ACTION_LIST_COLLAPSE = 1;
    private final static int ACTION_LIST_EXPAND = 2;

    public final static int ANIMATION_SCROLL_DURATION = 200;
    public final static int ANIMATION_EXPAND_DURATION = 200;
    public final static int ANIMATION_COLLAPSE_DURATION = 200;

    private ListView mListView;
    private int mContentViewId = View.NO_ID;
    private int mLastPosition = -1;

    public ExpandableListViewHelper(ListView listView, int contentViewId) {
        this.mListView = listView;
        this.mContentViewId = contentViewId;
    }

    public void select(View view, int position) {
        Log.d(TAG, "select() position : " + position);

        int delayedTime = 0;
        if (mLastPosition >= 0) {
            int realLastPosition = mLastPosition + getHeaderViewsCount();
            if (collapse(realLastPosition)) {
                delayedTime = ANIMATION_COLLAPSE_DURATION;
            }
            Log.d(TAG, "> collapse() LastPosition : " + realLastPosition + ", delayedTime : " + delayedTime);
        }

        final View content = view.findViewById(mContentViewId);
        if (content == null) {
            Log.e(TAG, "> View(Content) is NULL!");
            return;
        }

        Message message = new Message();
        if (content.isShown()) {
            message.what = ACTION_LIST_COLLAPSE;
            delayedTime = 0;
        } else {
            message.what = ACTION_SMOOTH_SCROLL;
            message.arg1 = ACTION_LIST_EXPAND;
        }
        message.arg2 = position;
        mListAnimationControllHandler.sendMessageDelayed(message, delayedTime);
    }

    public ListView getListView() {
        return this.mListView;
    }

    public int getLastPosition() {
        return mLastPosition;
    }

    /**
     * 애니메이션의 시간차 처리를 위해서 핸들러를 써서 해당 작업의 딜레이를 주겠음 하였음
     */
    private Handler mListAnimationControllHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            final int command = msg.what;
            final int nextCommand = msg.arg1;
            final int position = msg.arg2;

            switch (command) {
                case ACTION_SMOOTH_SCROLL:
                    smoothScrollToPositionFromTop(position);

                    Message message = new Message();
                    message.what = nextCommand;
                    message.arg2 = position;
                    sendMessageDelayed(message, ANIMATION_SCROLL_DURATION);
                    break;

                case ACTION_LIST_COLLAPSE:
                    collapse(position);
                    break;

                case ACTION_LIST_EXPAND:
                    expand(position);
                    break;
            }
        }
    };

    private void smoothScrollToPositionFromTop(int position) {
        try {
            Log.d(TAG, "\tsmoothScrollToPositionFromTop() position : " + position);
            ListView listView = getListView();
            if (listView == null) {
                Log.e(TAG, "\tsmoothScrollToPositionFromTop() ListView == null");
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                listView.smoothScrollToPositionFromTop(position, 0, ANIMATION_SCROLL_DURATION);
            } else {
                listView.setSelectionFromTop(position, 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "\tsmoothScrollToPositionFromTop() Exception : " + e);
        }
    }

    private boolean collapse(int position) {
        ListView listView = getListView();
        if (listView == null) {
            Log.e(TAG, "\tcollapse() ListView == null");
            return false;
        }
        View v = listView.getChildAt(position - listView.getFirstVisiblePosition());

        if (v == null) {
            Log.e(TAG, "\tcollapse() View == null");
            return false;
        }

        mLastPosition = -1;
        Log.d(TAG, "\tcollapse() position : " + position);

        View content = v.findViewById(mContentViewId);
        if (content == null) {
            Log.e(TAG, "> View(Content) is NULL!");
            return false;
        }
        if (content.isShown()) {
            AnimUtils.collapse(content, ANIMATION_COLLAPSE_DURATION);
            return true;
        } else {
            content.setVisibility(View.GONE);
        }
        return false;
    }

    private void expand(int position) {
        ListView listView = getListView();
        if (listView == null) {
            Log.e(TAG, "\texpand() ListView == null");
            return;
        }
        View v = listView.getChildAt(position - listView.getFirstVisiblePosition());

        if (v == null) {
            Log.e(TAG, "\texpand() View == null");
            return;
        }

        Log.d(TAG, "\texpand() position : " + position);

        View content = v.findViewById(mContentViewId);
        if (content == null) {
            Log.e(TAG, "> View(Content) is NULL!");
            return;
        }
        AnimUtils.expand(content, ANIMATION_EXPAND_DURATION);
        mLastPosition = position - getHeaderViewsCount();
    }

    private int getHeaderViewsCount() {
        ListView listView = getListView();
        if (listView == null) {
            Log.e(TAG, "\tgetHeaderViewsCount() ListView == null");
            return 0;
        }
        return listView.getHeaderViewsCount();
    }
}
