package com.onlyapps.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.sample.R;
import com.onlyapps.sample.utils.ExpandableListViewHelper;

/**
 * Created by hyungsoklee on 2015. 6. 23..
 */
public class AnimatedExpandableSimpleListView extends Activity {
    private ExpandableListViewHelper mExpandableListViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView = new ListView(this);
        mExpandableListViewHelper = new ExpandableListViewHelper(listView, R.id.content);

        // Creating the list adapter and populating the list
        ArrayAdapter<String> listAdapter = new CustomListAdapter(this, R.layout.list_item_expandable);
        for (int i = 0; i < 100; i++) {
            listAdapter.add("Title -------- " + i);
        }
        listView.setVerticalFadingEdgeEnabled(false);

        // Creating an item click listener, to open/close our toolbar for each item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                mExpandableListViewHelper.select(view, position);
            }
        });

//        View header = new View(this);
//        header.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 500));
//        header.setBackgroundColor(Color.BLUE);
//        listView.addHeaderView(header);

        View footer = new View(this);
        footer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 3000));
        footer.setBackgroundColor(Color.GRAY);
        listView.addFooterView(footer);

        listView.setAdapter(listAdapter);

        setContentView(listView);
    }

    /**
     * A simple implementation of list adapter.
     */
    class CustomListAdapter extends ArrayAdapter<String> {
        public CustomListAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_expandable, null);
            }

            TextView title = (TextView) convertView.findViewById(R.id.title);
            if (title != null) {
                title.setText("Title : " + position);
            }
            View content = convertView.findViewById(R.id.content);
            if (mExpandableListViewHelper.getLastPosition() == position) {
                content.setVisibility(View.VISIBLE);
            } else {
                content.setVisibility(View.GONE);
            }

            convertView.setBackgroundColor(position % 2 == 0 ? Color.GRAY : Color.LTGRAY);

            convertView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Button Click : " + position, Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }
    }
}
