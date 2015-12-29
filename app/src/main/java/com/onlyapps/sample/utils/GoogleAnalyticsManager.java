/* ______________________________________________________________________Copyright 2014 wemakeprice
* Description : GoogleAnalytics(V4) 기능 정리
* Date : 20150413
* Author : 20150413
* History : [20150413] 최초 소스 작성(박경민)
_________________________________________________________________________________________________*/
   
package com.onlyapps.sample.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.onlyapps.sample.R;

import java.util.Date;
import java.util.HashMap;

public class GoogleAnalyticsManager {

    // [final/static_property]====================[START]===================[final/static_property]
    private static final String TAG = "GoogleAnalyticsManager";
    private static final String PROPERTY_ID = "UA-24322531-8"; // 상용 : UA-18774526-4, 테스트 : UA-24322531-8
    // [final/static_property]=====================[END]====================[final/static_property]

    // [private/protected/public_property]========[START]=======[private/protected/public_property]
    private static GoogleAnalyticsManager mInstance;
    private Context mContext;
    private HashMap<TrackerName, Tracker> mTrackers;
    private Tracker mTracker;
    private SparseArray<Long> mTimings;
    private boolean mDebug;
    // [private/protected/public_property]=========[END]========[private/protected/public_property]

    // [interface/enum/inner_class]===============[START]==============[interface/enum/inner_class]
    /**
     * Tracker 종류
     */
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER // Tracker used by all ecommerce transactions from a company.
    }
    // [interface/enum/inner_class]================[END]===============[interface/enum/inner_class]

    // [inherited/listener_method]================[START]===============[inherited/listener_method]
    // [inherited/listener_method]=================[END]================[inherited/listener_method]

    // [private_method]===========================[START]==========================[private_method]
    /**
     * Tracker 반환
     *
     * @param trackerId Tracker Id
     * @return Tracker
     */
    private synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(mContext);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID) : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.app_tracker) :
                    analytics.newTracker(R.xml.app_tracker);
            mTrackers.put(trackerId, t);
        }

        return mTrackers.get(trackerId);
    }
    // [private_method]============================[END]===========================[private_method]

    // [life_cycle_method]========================[START]=======================[life_cycle_method]
    /**
     * Singleton 생성자
     *
     * @return GoogleAnalyticsManager
     */
    public static GoogleAnalyticsManager getInstance() {
        if (null == mInstance) {
            synchronized (GoogleAnalyticsManager.class) {
                if (null == mInstance) {
                    mInstance = new GoogleAnalyticsManager();
                }
            }
        }

        return mInstance;
    }
    // [life_cycle_method]=========================[END]========================[life_cycle_method]

    // [public_method]============================[START]===========================[public_method]
    /**
     * 초기화
     *
     * @param context Context
     */
    public void init(Context context) {
        mContext = context;
        mTrackers = new HashMap<TrackerName, Tracker>();
        mTracker = getTracker(TrackerName.APP_TRACKER);
        mTimings = new SparseArray<Long>();
        mDebug = false;
    }

    /**
     * 디버깅 모드 설정
     */
    public void enableDebugMode() {
        mDebug = true;
        GoogleAnalytics.getInstance(mContext).getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
/* 커스텀 Logger
        GoogleAnalytics.getInstance(mContext).setLogger(new Logger() {
            @Override
            public void warn(String message) {
                Log.w(TAG, "[V4] Logger - warn : " + message);
            }

            @Override
            public void verbose(String message) {
                Log.v(TAG, "[V4] Logger - verbose : " + message);
            }

            @Override
            public void setLogLevel(int message) {
            }

            @Override
            public void info(String message) {
                Log.i(TAG, "[V4] Logger - info : " + message);
            }

            @Override
            public int getLogLevel() {
                return 0;
            }

            @Override
            public void error(Exception exception) {
                Log.e(TAG, "[V4] Logger - error(exception) : " + exception.getMessage());
            }

            @Override
            public void error(String message) {
                Log.e(TAG, "[V4] Logger - error : " + message);
            }
        });
*/
    }

    /**
     * Dry Run 설정
     */
    public void enableDryRun() {
        GoogleAnalytics.getInstance(mContext).setDryRun(true);
    }

    /**
     * Activity Start
     * @param context Context
     */
    public void activityStart(Context context) {
        if (mTracker != null) {
            if (mDebug) {
                Log.d(TAG, "[V4] activityStart");
            }

            GoogleAnalytics.getInstance(context).reportActivityStart((Activity) context);
        }
    }

    /**
     * Activity Stop
     * @param context Context
     */
    public void activityStop(Context context) {
        if (mTracker != null) {
            if (mDebug) {
                Log.d(TAG, "[V4] activityStop");
            }

            GoogleAnalytics.getInstance(context).reportActivityStop((Activity) context);
        }
    }

    /**
     * Send View
     *
     * @param screenName Screen Name
     */
    public void sendView(String screenName) {
        if (mTracker != null) {
            if (mDebug) {
                Log.i(TAG, "[V4] sendView - screenName : " + screenName);
            }

            mTracker.setScreenName(screenName);
            mTracker.send(new HitBuilders.AppViewBuilder().build());
        }
    }

    /**
     * Send Event
     *
     * @param category Screen Name
     * @param action Category Id
     * @param label Action Id
     * @param value Label Id
     */
    public void sendEvent(String category, String action, String label, Long value) {
        if (mTracker != null) {
            if (mDebug) {
                Log.i(TAG, "[V4] sendEvent - category : " + category + ", action : " + action + ", label : " + label + ", value : " + value);
            }

            mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).setValue(value).build());
        }
    }

    /**
     * Send Social
     *
     * @param network Network
     * @param action Action
     * @param target Target
     */
    public void sendSocial(String network, String action, String target) {
        if (mTracker != null) {
            if (mDebug) {
                Log.i(TAG, "[V4] sendSocial - network : " + network + ", action : " + action + ", target : " + target);
            }

            mTracker.send(new HitBuilders.SocialBuilder().setNetwork(network).setAction(action).setTarget(target).build());
        }
    }

    /**
     * Timing 설정
     * @param key Timing key값
     */
    public void setTiming(int key) {
        if (mTracker != null && mTimings != null) {
            if (mDebug) {
                Log.i(TAG, "[V4] setTiming() - key : " + key + ", timing : " + new Date().getTime());
            }

            mTimings.put(key, new Date().getTime());
        }
    }

    /**
     * Send Timing
     * @param key Timing key값
     * @param category Category
     */
    public void sendTiming(int key, String category) {
        if (mTracker != null && mTimings.get(key) != null) {
            Long intervalInMilliseconds = mTimings.get(key);
            intervalInMilliseconds = new Date().getTime() - intervalInMilliseconds;

            if (mDebug) {
                Log.i(TAG, "[V4] sendTiming() - category : " + category + ", category : " + intervalInMilliseconds);
            }

            mTracker.send(new HitBuilders.TimingBuilder().setCategory(category).setValue(intervalInMilliseconds).setVariable(category).setLabel(null).build());

            mTimings.delete(key);
        }
    }

    public Tracker getTracker() {
        return mTracker;
    }
    // [public_method]=============================[END]============================[public_method]

    // [get/set]==================================[START]=================================[get/set]
    // [get/set]===================================[END]==================================[get/set]

}
