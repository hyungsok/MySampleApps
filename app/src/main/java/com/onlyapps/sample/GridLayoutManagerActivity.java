package com.onlyapps.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.etsy.android.sample.R;
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
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private MyHeaderRecycleViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recycle);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        mRecyclerView.addItemDecoration(new MarginDecoration(this));
        mAdapter = new MyHeaderRecycleViewAdapter(this);
        mLayoutManager = new GridLayoutManager(this, 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? mLayoutManager.getSpanCount() : 1;
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
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.list_item_image, parent, false);
            return new ItemHolder(convertView);
        }

        @Override
        protected void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {
            if (headerViewHolder instanceof HeaderHolder) {
                HeaderHolder vh = (HeaderHolder) headerViewHolder;
            }
        }

        @Override
        protected void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {

        }

        @Override
        protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
            if (contentViewHolder instanceof ItemHolder) {
                ItemHolder vh = (ItemHolder) contentViewHolder;
                Glide.with(mContext).load(mDatas.get(position)).into(vh.image);
                vh.text.setText("￦ " + Utils.getSampleValue(position));
                vh.image.setHeightRatio(1.2);
            }
        }
    }
}
