package com.onlyapps.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.etsy.android.sample.R;
import com.onlyapps.sample.view.AnimatedExpandableListView;

/**
 * Created by hyungsoklee on 2015. 6. 22..
 */
public class AnimatedExpandableListViewActivity extends Activity {
    private int preGroupPosition = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AnimatedExpandableListView listView = new AnimatedExpandableListView(this);
        listView.setGroupIndicator(null);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setAdapter(new ExampleAdapter(this));
        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (preGroupPosition >= 0 && preGroupPosition != groupPosition) {
                    listView.collapseGroupWithAnimation(preGroupPosition);
                }

                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                preGroupPosition = groupPosition;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int a = listView.getFirstVisiblePosition();
                        int b = listView.getLastVisiblePosition();
                        Log.d("AAA", "setSelection(" + groupPosition + ") : " + a + ", " + b);
                        listView.setSelection(groupPosition);

                    }
                }, 500);

                return true;
            }

        });



        setContentView(listView);
    }

    private class ChildHolder {
        TextView title;
        TextView hint;
    }

    private class GroupHolder {
        TextView title;
    }

    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.expandable_child_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.hint = (TextView) convertView.findViewById(R.id.textHint);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }
            holder.title.setText("Title : " + childPosition);
            holder.hint.setText("Hint : " + childPosition);
            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.expandable_group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText("Group " + groupPosition);

            convertView.setBackgroundColor(groupPosition % 2 == 0 ? Color.GRAY : Color.LTGRAY);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return 3;
        }

        @Override
        public int getGroupCount() {
            return 30;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
