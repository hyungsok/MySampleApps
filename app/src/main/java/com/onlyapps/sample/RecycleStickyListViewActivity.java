package com.onlyapps.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.onlyapps.sample.recycleviews.AnimalsAdapter;
import com.onlyapps.sample.recycleviews.DividerDecoration;
import com.onlyapps.sample.recycleviews.sticky.StickyRecyclerHeadersAdapter;
import com.onlyapps.sample.recycleviews.sticky.StickyRecyclerHeadersDecoration;
import com.onlyapps.sample.recycleviews.sticky.StickyRecyclerHeadersTouchListener;

import java.security.SecureRandom;

/**
 * Created by hyungsoklee on 2016. 1. 25..
 */
public class RecycleStickyListViewActivity extends Activity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = new RecyclerView(this);
        // Set adapter populated with example dummy data
        final AnimalsHeadersAdapter adapter = new AnimalsHeadersAdapter();
        adapter.add("Animals below!");
        adapter.addAll(getDummyDataSet());
        recyclerView.setAdapter(adapter);

        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
        recyclerView.addItemDecoration(new DividerDecoration(this));

        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener = new StickyRecyclerHeadersTouchListener(recyclerView, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId) {
                        Toast.makeText(getApplicationContext(), "Header position: " + position + ", id: " + headerId, Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.addOnItemTouchListener(touchListener);

        setContentView(recyclerView);
    }

    private String[] getDummyDataSet() {
        return getResources().getStringArray(R.array.animals);
    }

    /**
     *
     */
    private class AnimalsHeadersAdapter extends AnimalsAdapter<RecyclerView.ViewHolder>
            implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(getItem(position));
        }

        @Override
        public long getHeaderId(int position) {
            if (position < 5) {
                return RecyclerView.NO_POSITION;
            } else {
                return getItem(position).charAt(0);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false);
//            return new RecyclerView.ViewHolder(view) {
//            };
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header_right_button, parent, false);
            return new AnimalsHeadersHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder.itemView instanceof TextView) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(String.valueOf(getItem(position).charAt(0)));
            } else {
                AnimalsHeadersHolder h = (AnimalsHeadersHolder) holder;
                h.text.setText(String.valueOf(getItem(position).charAt(0)));
            }
            holder.itemView.setBackgroundColor(getRandomColor());
        }

        private int getRandomColor() {
            SecureRandom rgen = new SecureRandom();
            return Color.HSVToColor(150, new float[]{
                    rgen.nextInt(359), 1, 1
            });
        }

        private class AnimalsHeadersHolder extends RecyclerView.ViewHolder {
            public TextView text;
            public Button button;

            public AnimalsHeadersHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.text);
                button = (Button) itemView.findViewById(R.id.button);
                button.setTag("Button");
            }

        }

    }
}
