package com.onlyapps.sample.test;

import android.test.ActivityInstrumentationTestCase2;

import com.onlyapps.sample.MainActivity;
import com.robotium.solo.Solo;


public class TestMain extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;
  	
  	public TestMain() {
		super(MainActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
  
	public void testRun() {
        //Wait for activity: 'com.onlyapps.sample.MainActivity'
		solo.waitForActivity(com.onlyapps.sample.MainActivity.class, 2000);
        //Scroll to 01. GridLayoutManager
		android.widget.ListView listView0 = (android.widget.ListView) solo.getView(android.widget.ListView.class, 0);
		solo.scrollListToLine(listView0, 0);
        //Click on 01. GridLayoutManager
		solo.clickOnView(solo.getView(android.R.id.text1, 1));
        //Wait for activity: 'com.onlyapps.sample.GridLayoutManagerActivity'
		assertTrue("com.onlyapps.sample.GridLayoutManagerActivity is not found!", solo.waitForActivity(com.onlyapps.sample.GridLayoutManagerActivity.class));
        //Press menu back key
		solo.goBack();
        //Click on 02. Animation
		solo.clickOnView(solo.getView(android.R.id.text1, 2));
        //Wait for activity: 'com.onlyapps.sample.AnimationActivity'
		assertTrue("com.onlyapps.sample.AnimationActivity is not found!", solo.waitForActivity(com.onlyapps.sample.AnimationActivity.class));
        //Press menu back key
		solo.goBack();
        //Click on 03. RevealBackgroundView
		solo.clickOnView(solo.getView(android.R.id.text1, 3));
        //Wait for activity: 'com.onlyapps.sample.RevealBackgroundActivity'
		assertTrue("com.onlyapps.sample.RevealBackgroundActivity is not found!", solo.waitForActivity(com.onlyapps.sample.RevealBackgroundActivity.class));
        //Press menu back key
		solo.goBack();
        //Click on 04. AnimatedExpandableListView
		solo.clickOnView(solo.getView(android.R.id.text1, 4));
        //Wait for activity: 'com.onlyapps.sample.AnimatedExpandableListViewActivity'
		assertTrue("com.onlyapps.sample.AnimatedExpandableListViewActivity is not found!", solo.waitForActivity(com.onlyapps.sample.AnimatedExpandableListViewActivity.class));
        //Press menu back key
		solo.goBack();
	}
}
