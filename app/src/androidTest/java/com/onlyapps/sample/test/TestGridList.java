package com.onlyapps.sample.test;

import android.test.ActivityInstrumentationTestCase2;

import com.onlyapps.sample.MainActivity;
import com.robotium.solo.Solo;


public class TestGridList extends ActivityInstrumentationTestCase2<MainActivity> {
  	private Solo solo;
  	
  	public TestGridList() {
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
        //Click on 01. GridLayoutManager
		solo.clickOnView(solo.getView(android.R.id.text1, 1));
        //Wait for activity: 'com.onlyapps.sample.GridLayoutManagerActivity'
		assertTrue("com.onlyapps.sample.GridLayoutManagerActivity is not found!", solo.waitForActivity(com.onlyapps.sample.GridLayoutManagerActivity.class));
        //Click on Sample
		solo.clickOnView(solo.getView(com.onlyapps.sample.R.id.iv_title));
        //Assert that: 'ImageView' is shown
		assertTrue("'ImageView' is not shown!", solo.waitForView(solo.getView(com.onlyapps.sample.R.id.iv_icon)));
        //Assert that: 'ImageView' is shown
		assertTrue("'ImageView' is not shown!", solo.waitForView(solo.getView(com.onlyapps.sample.R.id.iv_icon)));
        //Click on Sample
		solo.clickOnView(solo.getView(com.onlyapps.sample.R.id.iv_title));
        //Press menu back key
		solo.goBack();
	}
}
