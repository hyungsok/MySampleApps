package com.onlyapps.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.onlyapps.sample.adapter.ItemHolder;
import com.onlyapps.sample.utils.GoogleAnalyticsManager;
import com.onlyapps.sample.utils.MarginDecoration;
import com.onlyapps.sample.utils.RecyclerItemClickListener;
import com.onlyapps.sample.utils.SampleData;
import com.onlyapps.sample.utils.Utils;

import java.util.ArrayList;

/**
 * Created by hyungsoklee on 2015. 6. 18..
 */
public class StaggeredGridLayoutManagerActivity extends Activity implements RecyclerItemClickListener.OnItemClickListener {
    private static final String TAG = StaggeredGridLayoutManagerActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recycle);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
        mRecyclerView.addItemDecoration(new MarginDecoration(this));


        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);

        mAdapter = new MyRecyclerViewAdapter(this);
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
            }
        });
    }

    @Override
    public void onItemClick(View childView, int position) {
        GoogleAnalyticsManager.getInstance().sendEvent(TAG, "onItemClick", "아이템클릭", Long.valueOf(position));

        int[] startingLocation = new int[2];
        childView.getLocationOnScreen(startingLocation);
        startingLocation[0] += childView.getWidth() / 2;
        DetailRevealBackgroundViewActivity.startActivity(this, mAdapter.getData(position), position, startingLocation);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Toast.makeText(this, "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
    }

    static class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context mContext;

        public MyRecyclerViewAdapter(Context context) {
            this.mContext = context;
            Utils.initRatio();
            for (String data : SampleData.getData()) {
                add(data);
            }
        }

        private ArrayList<String> mDatas = new ArrayList<String>();

        public void add(String data) {
            mDatas.add(data);
        }

        public String getData(int position) {
            return mDatas.get(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = inflater.inflate(R.layout.list_item_image, parent, false);
            return new ItemHolder(convertView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ItemHolder) {
                ItemHolder vh = (ItemHolder) holder;
                Glide.with(mContext).load(mDatas.get(position)).into(vh.image);
                vh.text.setText("￦ " + Utils.getSampleValue(position));
                double positionHeight = Utils.getPositionRatio(position);
                vh.image.setHeightRatio(positionHeight);
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }
}
