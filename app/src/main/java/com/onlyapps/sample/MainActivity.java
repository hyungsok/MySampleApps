package com.onlyapps.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.onlyapps.sample.listviewremovalanimation.ListViewRemovalAnimation;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Hyungsok's Sample");
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("00.StaggeredGridLayoutManager");
        adapter.add("01.GridLayoutManager");
        adapter.add("02.Animation");
        adapter.add("03.RevealBackgroundView");
        adapter.add("04.AnimatedExpandableListView");
        adapter.add("05.AnimatedExpandableSimpleListView");
        adapter.add("06.AnimatedListViewRemoved");
        adapter.add("07.CustomViewTest");
        adapter.add("08.DesignLibrarySample");
        adapter.add("09.ScrollViewSample");
        adapter.add("10.ReaimListView");
        adapter.add("11.AlarmManagerTestActivity");
        adapter.add("12.UnigueIdActivity");
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mListView.setSelection(adapter.getCount() - 1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position + mListView.getHeaderViewsCount()) {
            case 0:
                startActivity(new Intent(this, StaggeredGridLayoutManagerActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, GridLayoutManagerActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, AnimationActivity.class));
                break;
            case 3:
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                RevealBackgroundActivity.startActivity(this, startingLocation);
                overridePendingTransition(0, 0);
                break;
            case 4:
                startActivity(new Intent(this, AnimatedExpandableListViewActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, AnimatedExpandableSimpleListView.class));
                break;
            case 6:
                startActivity(new Intent(this, ListViewRemovalAnimation.class));
                break;
            case 7:
                startActivity(new Intent(this, CustomViewTestActivity.class));
                break;
            case 8:
                startActivity(new Intent(this, DesignLibrarySample.class));
                break;
            case 9:
                startActivity(new Intent(this, ScrollViewSampleActivity.class));
                break;
            case 10:
                startActivity(new Intent(this, ReaimListViewActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, AlarmManagerTestActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, UnigueIdActivity.class));
                break;
        }
    }
}
