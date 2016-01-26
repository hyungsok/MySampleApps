package com.onlyapps.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onlyapps.sample.adapter.HeaderFooterRecyclerViewAdapter;
import com.onlyapps.sample.adapter.HeaderHolder;
import com.onlyapps.sample.adapter.ItemHolder;
import com.onlyapps.sample.utils.MarginDecoration;
import com.onlyapps.sample.utils.RecyclerItemClickListener;
import com.onlyapps.sample.utils.SampleData;
import com.onlyapps.sample.utils.Utils;

import java.util.ArrayList;

/**
 * Created by hyungsoklee on 2015. 6. 18..
 */
public class GridLayoutManagerActivity extends Activity implements RecyclerItemClickListener.OnItemClickListener {
    private static final String TAG = GridLayoutManagerActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private LinearLayout mStickeyView;
    private GridLayoutManager mLayoutManager;
    private MyHeaderRecycleViewAdapter mAdapter;

    private static final int STICKY_VIEW_INDEX = 6;

    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_STICKEY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recycle);

        mStickeyView = (LinearLayout) findViewById(R.id.sticky);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mLastedStickyViewIndex = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = mLayoutManager.findFirstVisibleItemPosition();
                if (position > 0) {
                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                    if (holder instanceof HeaderHolder) {
                        if (mStickeyView.getChildCount() == 0) {
                            recyclerView.removeView(holder.itemView);
                            mStickeyView.addView(holder.itemView);
                            mStickeyView.setVisibility(View.VISIBLE);
                            mLastedStickyViewIndex = position;
                        }
                    } else if (mLastedStickyViewIndex > position) {
                        if (mStickeyView.getChildCount() > 0) {
                            mStickeyView.removeAllViews();
                            mStickeyView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });


        mRecyclerView.addItemDecoration(new MarginDecoration(this));
        mAdapter = new MyHeaderRecycleViewAdapter(this);
        mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0 || position == STICKY_VIEW_INDEX + 1) ? mLayoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.iv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int spanCount = mLayoutManager.getSpanCount();
                switch (spanCount) {
                    case 1:
                        mLayoutManager.setSpanCount(2);
                        break;
                    case 2:
                        mLayoutManager.setSpanCount(3);
                        break;
                    case 3:
                        mLayoutManager.setSpanCount(4);
                        break;
                    case 4:
                    default:
                        mLayoutManager.setSpanCount(1);
                        break;
                }
                mLayoutManager.requestLayout();
            }
        });
    }

    @Override
    public void onItemClick(View childView, int position) {
        if (position == 0 || position == STICKY_VIEW_INDEX + 1) {
            Toast.makeText(getApplicationContext(), "onItemClick() : " + position, Toast.LENGTH_SHORT).show();
            return;
        }
        int[] startingLocation = new int[2];
        childView.getLocationOnScreen(startingLocation);
        startingLocation[0] += childView.getWidth() / 2;
        DetailRevealBackgroundViewActivity.startActivity(this,
                mAdapter.getData(position - mAdapter.getHeaderItemCount()), position - mAdapter.getHeaderItemCount(),
                startingLocation);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Toast.makeText(this, "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    private class MyHeaderRecycleViewAdapter extends HeaderFooterRecyclerViewAdapter {

        private Context mContext;

        private ArrayList<String> mDatas = new ArrayList<String>();

        public void add(String data) {
            mDatas.add(data);
        }

        public String getData(int position) {
            return mDatas.get(position);
        }

        public MyHeaderRecycleViewAdapter(Context context) {
            this.mContext = context;
            Utils.initRatio();
            for (String data : SampleData.getData()) {
                add(data);
            }
        }

        @Override
        public int getHeaderItemCount() {
            return 1;
        }

        @Override
        protected int getFooterItemCount() {
            return 0;
        }

        @Override
        protected int getContentItemCount() {
            return mDatas.size();
        }

        @Override
        protected int getContentItemViewType(int position) {
            if(position == STICKY_VIEW_INDEX) {
                return TYPE_STICKEY;
            }
            return TYPE_CONTENT;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.list_item_header_footer, parent, false);
            return new HeaderHolder(convertView);
        }

        @Override
        protected RecyclerView.ViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
            return null;
        }

        @Override
        protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
            if (contentViewType == 0) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.list_item_image, parent, false);
                return new ItemHolder(convertView);
            } else {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View convertView = inflater.inflate(R.layout.list_item_header_footer, parent, false);
                return new HeaderHolder(convertView);
            }
        }

        @Override
        protected void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {
//            if (headerViewHolder instanceof HeaderHolder) {
//                HeaderHolder vh = (HeaderHolder) headerViewHolder;
//            }
        }

        @Override
        protected void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {

        }

        @Override
        protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, final int position) {
            if (contentViewHolder instanceof ItemHolder) {
                ItemHolder vh = (ItemHolder) contentViewHolder;
                Glide.with(mContext).load(mDatas.get(position)).into(vh.image);
                vh.text.setText("￦ " + Utils.getSampleValue(position));
                vh.image.setHeightRatio(1);
            } else if (contentViewHolder instanceof HeaderHolder) {
                HeaderHolder vh = (HeaderHolder) contentViewHolder;
                vh.root.setBackgroundColor(Utils.getRandomColor(position));
                vh.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "Header : " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                vh.text.setText("Stickey View : " + position);
                vh.text.setTextColor(Color.WHITE);
            }
        }
    }
}
