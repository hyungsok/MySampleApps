package com.onlyapps.sample;

import android.app.Application;
import android.content.Context;

/**
 * Created by hyungsoklee on 2015. 8. 20..
 */
public class MainApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
