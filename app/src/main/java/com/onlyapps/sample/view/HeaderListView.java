package com.onlyapps.sample.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.onlyapps.sample.R;

/**
 * Created by hyungsoklee on 2016. 2. 22..
 *
 * https://github.com/applidium/HeaderListView
 */
public class HeaderListView extends RelativeLayout {

    // TODO: Handle listViews with fast scroll
    // TODO: See if there are methods to dispatch to mListView

    private static final int FADE_DELAY    = 1000;
    private static final int FADE_DURATION = 2000;

    private InternalListView mListView;
    private SectionAdapter   mAdapter;
    private RelativeLayout   mHeader;
    private View mHeaderConvertView;
    private FrameLayout mScrollView;
    private AbsListView.OnScrollListener mExternalOnScrollListener;
    private StickyScrollListener mStickyScrollListener;
    private boolean          mDisableScrollbar;


    public HeaderListView(Context context) {
        super(context);
        init(context, null);
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void disableScrollBar(){
        mDisableScrollbar = true;
    }

    private void init(Context context, AttributeSet attrs) {
        mListView = new InternalListView(getContext(), attrs);
        LayoutParams listParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listParams.addRule(ALIGN_PARENT_TOP);
        mListView.setLayoutParams(listParams);
        mListView.setDivider(null);
        // ListView selector 사용 안하기
        mListView.setSelector(android.R.drawable.screen_background_light_transparent);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setOnScrollListener(new HeaderListViewOnScrollListener());
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter != null) {
                    //regako 헤더가 붙으면 포지션이 증가하므로 position에서 헤더갯수 제거부분 추가
                    mAdapter.onItemClick(parent, view, position-mListView.getHeaderViewsCount(), id);
                }
            }
        });
        addView(mListView);

        mHeader = new RelativeLayout(getContext());
        LayoutParams headerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        headerParams.addRule(ALIGN_PARENT_TOP);
        mHeader.setLayoutParams(headerParams);
        mHeader.setGravity(Gravity.BOTTOM);
        addView(mHeader);

        // The list view's scroll bar can be hidden by the header, so we display our own scroll bar instead
        Drawable scrollBarDrawable = getResources().getDrawable(R.drawable.scrollbar_handle_holo_light);
        mScrollView = new FrameLayout(getContext());
        LayoutParams scrollParams = new LayoutParams(scrollBarDrawable.getIntrinsicWidth(), LayoutParams.MATCH_PARENT);
        scrollParams.addRule(ALIGN_PARENT_RIGHT);
        scrollParams.rightMargin = (int) dpToPx(2);
        mScrollView.setLayoutParams(scrollParams);

        ImageView scrollIndicator = new ImageView(context);
        scrollIndicator.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        scrollIndicator.setImageDrawable(scrollBarDrawable);
        scrollIndicator.setScaleType(ImageView.ScaleType.FIT_XY);
        mScrollView.addView(scrollIndicator);
        mScrollView.setVisibility(INVISIBLE);

        addView(mScrollView);
    }

    public void setAdapter(SectionAdapter adapter) {
        mAdapter = adapter;
        mListView.setAdapter(adapter);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        mExternalOnScrollListener = l;
    }

    private class HeaderListViewOnScrollListener implements AbsListView.OnScrollListener {

        private int            previousFirstVisibleItem = -1;
        private int            direction                = 0;
        private int            actualSection            = 0;
        private boolean        scrollingStart           = false;
        private boolean        doneMeasuring            = false;
        private int            lastResetSection         = -1;
        private int            nextH;
        private int            prevH;
        private View           previous;
        private View           next;
        private AlphaAnimation fadeOut                  = new AlphaAnimation(1f, 0f);
        private boolean        noHeaderUpToHeader       = false;
        private boolean        didScroll = false;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mExternalOnScrollListener != null) {
                mExternalOnScrollListener.onScrollStateChanged(view, scrollState);
            }
            didScroll = true;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mExternalOnScrollListener != null) {
                mExternalOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            if (!didScroll) {
                return;
            }

            firstVisibleItem -= mListView.getHeaderViewsCount();
            if (firstVisibleItem < 0) {
                mHeader.removeAllViews();
                //regako 추가 -> 스틱키된 헤더 배경이 반투명이라 아래로 원래리스트 헤더가 비춘다.
                for (int i = 0; i < mListView.getChildCount(); i++) {
                    View v = mListView.getChildAt(i);
                    if (v.getTag() != null && v.getTag().equals("tab")) {
                        v.setVisibility(View.VISIBLE);
                        if (mStickyScrollListener != null) {
                            mStickyScrollListener.onSticky(v, false);
                        }
                    }
                }
                return;
            }

            updateScrollBar();
            if (visibleItemCount > 0 && firstVisibleItem == 0 && mHeader.getChildAt(0) == null) {
                addSectionHeader(0);
                lastResetSection = 0;
            }

            int realFirstVisibleItem = getRealFirstVisibleItem(firstVisibleItem, visibleItemCount);
            if (totalItemCount > 0 && previousFirstVisibleItem != realFirstVisibleItem) {
                direction = realFirstVisibleItem - previousFirstVisibleItem;

                actualSection = mAdapter.getSection(realFirstVisibleItem);

                boolean currIsHeader = mAdapter.isSectionHeader(realFirstVisibleItem);
                boolean prevHasHeader = mAdapter.hasSectionHeaderView(actualSection - 1);
                boolean nextHasHeader = mAdapter.hasSectionHeaderView(actualSection + 1);
                boolean currHasHeader = mAdapter.hasSectionHeaderView(actualSection);
                boolean currIsLast = mAdapter.getRowInSection(realFirstVisibleItem) == mAdapter.numberOfRows(actualSection) - 1;
                boolean prevHasRows = mAdapter.numberOfRows(actualSection - 1) > 0;
                boolean currIsFirst = mAdapter.getRowInSection(realFirstVisibleItem) == 0;

                boolean needScrolling = currIsFirst && !currHasHeader && prevHasHeader && realFirstVisibleItem != firstVisibleItem;
                boolean needNoHeaderUpToHeader = currIsLast && currHasHeader && !nextHasHeader && realFirstVisibleItem == firstVisibleItem && Math.abs(mListView.getChildAt(0).getTop()) >= mListView.getChildAt(0).getHeight() / 2;

                noHeaderUpToHeader = false;
                if (currIsHeader && !prevHasHeader && firstVisibleItem >= 0) {
                    resetHeader(direction < 0 ? actualSection - 1 : actualSection);
                } else if ((currIsHeader && firstVisibleItem > 0) || needScrolling) {
                    if (!prevHasRows) {
                        resetHeader(actualSection-1);
                    }
                    startScrolling();
                } else if (needNoHeaderUpToHeader) {
                    noHeaderUpToHeader = true;
                } else if (lastResetSection != actualSection) {
                    resetHeader(actualSection);
                }

                previousFirstVisibleItem = realFirstVisibleItem;
            }

            if (scrollingStart) {
                int scrolled = realFirstVisibleItem >= firstVisibleItem ? mListView.getChildAt(realFirstVisibleItem - firstVisibleItem).getTop() : 0;

                if (!doneMeasuring) {
                    setMeasurements(realFirstVisibleItem, firstVisibleItem);
                }

                int headerH = doneMeasuring ? (prevH - nextH) * direction * Math.abs(scrolled) / (direction < 0 ? nextH : prevH) + (direction > 0 ? nextH : prevH) : 0;

                mHeader.scrollTo(0, -Math.min(0, scrolled - headerH));
                if (doneMeasuring && headerH != mHeader.getLayoutParams().height) {
                    LayoutParams p = (LayoutParams) (direction < 0 ? next.getLayoutParams() : previous.getLayoutParams());
                    p.topMargin = headerH - p.height;
                    mHeader.getLayoutParams().height = headerH;
                    mHeader.requestLayout();
                }
            }

            if (noHeaderUpToHeader) {
                if (lastResetSection != actualSection) {
                    addSectionHeader(actualSection);
                    lastResetSection = actualSection + 1;
                }
                mHeader.scrollTo(0, mHeader.getLayoutParams().height - (mListView.getChildAt(0).getHeight() + mListView.getChildAt(0).getTop()));
            }

            if (mStickyScrollListener != null) {
                mStickyScrollListener.onSticky(mHeaderConvertView, true);
            }
        }

        private void startScrolling() {
            scrollingStart = true;
            doneMeasuring = false;
            lastResetSection = -1;
        }

        private void resetHeader(int section) {
            scrollingStart = false;
            addSectionHeader(section);
            mHeader.requestLayout();
            lastResetSection = section;
        }

        private void setMeasurements(int realFirstVisibleItem, int firstVisibleItem) {

            if (direction > 0) {
                nextH = realFirstVisibleItem >= firstVisibleItem ? mListView.getChildAt(realFirstVisibleItem - firstVisibleItem).getMeasuredHeight() : 0;
            }

            previous = mHeader.getChildAt(0);
            prevH = previous != null ? previous.getMeasuredHeight() : mHeader.getHeight();

            if (direction < 0) {
                if (lastResetSection != actualSection - 1) {
                    addSectionHeader(Math.max(0, actualSection - 1));
                    next = mHeader.getChildAt(0);
                }
                nextH = mHeader.getChildCount() > 0 ? mHeader.getChildAt(0).getMeasuredHeight() : 0;
                mHeader.scrollTo(0, prevH);
            }
            doneMeasuring = previous != null && prevH > 0 && nextH > 0;
        }

        private void updateScrollBar() {
            if (!mDisableScrollbar && mHeader != null && mListView != null && mScrollView != null) {
                int offset = mListView.computeVerticalScrollOffset();
                int range = mListView.computeVerticalScrollRange();
                int extent = mListView.computeVerticalScrollExtent();
                mScrollView.setVisibility(extent >= range ? View.INVISIBLE : View.VISIBLE);
                if (extent >= range) {
                    return;
                }
                int top = range == 0 ? mListView.getHeight() : mListView.getHeight() * offset / range;
                int bottom = range == 0 ? 0 : mListView.getHeight() - mListView.getHeight() * (offset + extent) / range;
                mScrollView.setPadding(0, top, 0, bottom);
                fadeOut.reset();
                fadeOut.setFillBefore(true);
                fadeOut.setFillAfter(true);
                fadeOut.setStartOffset(FADE_DELAY);
                fadeOut.setDuration(FADE_DURATION);
                mScrollView.clearAnimation();
                mScrollView.startAnimation(fadeOut);
            }
        }

        private void addSectionHeader(int actualSection) {
            View previousHeader = mHeader.getChildAt(0);
            if (previousHeader != null) {
                mHeader.removeViewAt(0);
            }

            if (mAdapter.hasSectionHeaderView(actualSection)) {
                mHeaderConvertView = mAdapter.getSectionHeaderView(actualSection, mHeaderConvertView, mHeader);
                mHeaderConvertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                mHeaderConvertView.measure(MeasureSpec.makeMeasureSpec(mHeader.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                mHeader.getLayoutParams().height = mHeaderConvertView.getMeasuredHeight();
                mHeaderConvertView.scrollTo(0, 0);
                mHeader.scrollTo(0, 0);
                mHeader.addView(mHeaderConvertView, 0);

                //regako 추가 -> 스틱키된 헤더 배경이 반투명이라 아래로 원래리스트 헤더가 비춘다.
                for (int i = 0; i < mListView.getChildCount(); i++) {
                    View v = mListView.getChildAt(i);
                    if (v.getTag() != null && v.getTag().equals("tab")) {
                        v.setVisibility(View.INVISIBLE);
                        if (mStickyScrollListener != null) {
                            mStickyScrollListener.onSticky(v, true);
                        }
                    }
                }
            } else {
                mHeader.getLayoutParams().height = 0;
                mHeader.scrollTo(0, 0);
            }

            mScrollView.bringToFront();
        }

        private int getRealFirstVisibleItem(int firstVisibleItem, int visibleItemCount) {
            if (visibleItemCount == 0) {
                return -1;
            }
            int relativeIndex = 0, totalHeight = mListView.getChildAt(0).getTop();
            for (relativeIndex = 0; relativeIndex < visibleItemCount && totalHeight < mHeader.getHeight(); relativeIndex++) {
                totalHeight += mListView.getChildAt(relativeIndex).getHeight();
            }
            int realFVI = Math.max(firstVisibleItem, firstVisibleItem + relativeIndex - 1);
            return realFVI;
        }
    }

    public ListView getListView() {
        return mListView;
    }

    public void addHeaderView(View v) {
        mListView.addHeaderView(v);
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        mListView.addHeaderView(v, data, isSelectable);
    }

    public void addFooterView(View v) {
        mListView.addFooterView(v);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public void setStickyScrollListener(StickyScrollListener listener) {
        this.mStickyScrollListener = listener;
    }

    protected class InternalListView extends ListView {

        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected int computeVerticalScrollExtent() {
            return super.computeVerticalScrollExtent();
        }

        @Override
        protected int computeVerticalScrollOffset() {
            return super.computeVerticalScrollOffset();
        }

        @Override
        protected int computeVerticalScrollRange() {
            return super.computeVerticalScrollRange();
        }
    }

    public interface StickyScrollListener {
        void onSticky(View view, boolean isSticky);
    }

    public abstract static class SectionAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private int mCount = -1;

        public abstract int numberOfSections();

        public abstract int numberOfRows(int section);

        public abstract View getRowView(int section, int row, View convertView, ViewGroup parent);

        public abstract Object getRowItem(int section, int row);

        public boolean hasSectionHeaderView(int section) {
            return false;
        }

        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            return null;
        }

        public Object getSectionHeaderItem(int section) {
            return null;
        }

        public int getRowViewTypeCount() {
            return 1;
        }

        public int getSectionHeaderViewTypeCount() {
            return 1;
        }

        /**
         * Must return a value between 0 and getRowViewTypeCount() (excluded)
         */
        public int getRowItemViewType(int section, int row) {
            return 0;
        }

        /**
         * Must return a value between 0 and getSectionHeaderViewTypeCount() (excluded, if > 0)
         */
        public int getSectionHeaderItemViewType(int section) {
            return 0;
        }

        @Override
        /**
         * Dispatched to call onRowItemClick
         */
        public final void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onRowItemClick(parent, view, getSection(position), getRowInSection(position), id);
        }

        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {

        }

        @Override
        /**
         * Counts the amount of cells = headers + rows
         */
        public final int getCount() {
            if (mCount < 0) {
                mCount = numberOfCellsBeforeSection(numberOfSections());
            }
            return mCount;
        }

        @Override
        public boolean isEmpty() {
            return getCount() == 0;
        }

        @Override
        /**
         * Dispatched to call getRowItem or getSectionHeaderItem
         */
        public final Object getItem(int position) {
            int section = getSection(position);
            if (isSectionHeader(position)) {
                if (hasSectionHeaderView(section)) {
                    return getSectionHeaderItem(section);
                }
                return null;
            }
            return getRowItem(section, getRowInSection(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        /**
         * Dispatched to call getRowView or getSectionHeaderView
         */
        public final View getView(int position, View convertView, ViewGroup parent) {
            int section = getSection(position);
            if (isSectionHeader(position)) {
                if (hasSectionHeaderView(section)) {
                    return getSectionHeaderView(section, convertView, parent);
                }
                return null;
            }
            return getRowView(section, getRowInSection(position), convertView, parent);
        }

        /**
         * Returns the section number of the indicated cell
         */
        protected int getSection(int position) {
            int section = 0;
            int cellCounter = 0;
            while (cellCounter <= position && section <= numberOfSections()) {
                cellCounter += numberOfCellsInSection(section);
                section++;
            }
            return section - 1;
        }

        /**
         * Returns the row index of the indicated cell Should not be call with
         * positions directing to section headers
         */
        protected int getRowInSection(int position) {
            int section = getSection(position);
            int row = position - numberOfCellsBeforeSection(section);
            if (hasSectionHeaderView(section)) {
                return row - 1;
            } else {
                return row;
            }
        }

        /**
         * Returns true if the cell at this index is a section header
         */
        protected boolean isSectionHeader(int position) {
            int section = getSection(position);
            return hasSectionHeaderView(section) && numberOfCellsBeforeSection(section) == position;
        }

        /**
         * Returns the number of cells (= headers + rows) before the indicated
         * section
         */
        protected int numberOfCellsBeforeSection(int section) {
            int count = 0;
            for (int i = 0; i < Math.min(numberOfSections(), section); i++) {
                count += numberOfCellsInSection(i);
            }
            return count;
        }

        private int numberOfCellsInSection(int section) {
            return numberOfRows(section) + (hasSectionHeaderView(section) ? 1 : 0);
        }

        @Override
        public void notifyDataSetChanged() {
            mCount = numberOfCellsBeforeSection(numberOfSections());
            super.notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetInvalidated() {
            mCount = numberOfCellsBeforeSection(numberOfSections());
            super.notifyDataSetInvalidated();
        }

        @Override
        /**
         * Dispatched to call getRowItemViewType or getSectionHeaderItemViewType
         */
        public final int getItemViewType(int position) {
            int section = getSection(position);
            if (isSectionHeader(position)) {
                return getRowViewTypeCount() + getSectionHeaderItemViewType(section);
            } else {
                return getRowItemViewType(section, getRowInSection(position));
            }
        }

        @Override
        /**
         * Dispatched to call getRowViewTypeCount and getSectionHeaderViewTypeCount
         */
        public final int getViewTypeCount() {
            return getRowViewTypeCount() + getSectionHeaderViewTypeCount();
        }

        @Override
        /**
         * By default, disables section headers
         */
        public boolean isEnabled(int position) {
            return (disableHeaders() || !isSectionHeader(position)) && isRowEnabled(getSection(position), getRowInSection(position));
        }

        public boolean disableHeaders() {
            return false;
        }

        public boolean isRowEnabled(int section, int row) {
            return true;
        }
    }
}
