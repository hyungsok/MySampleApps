package com.onlyapps.sample;

import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by hyungsoklee on 2015. 8. 20..
 */
public class MainApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

//        GoogleAnalyticsManager.getInstance().init(this);
//        Tracker tracker = GoogleAnalyticsManager.getInstance().getTracker();
//        tracker.enableExceptionReporting(true);
//        Thread.UncaughtExceptionHandler myHandler = new ExceptionReporter(
//                tracker,
//                Thread.getDefaultUncaughtExceptionHandler(),
//                this);
//        Thread.setDefaultUncaughtExceptionHandler(myHandler);

        Fabric.with(this, new Crashlytics());

        // Thread.UncaughtExceptionHandler 사용팁 - 맨마지막에 등록된것만 호출된다.
        // Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    /**
     *
     */
    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, final Throwable ex) {
            // 여기서 에러를 처리
            Log.e("MainApplication", "uncaughtException() :" + ex);
            Crashlytics.logException(ex);

            new Thread() {
                @Override
                public void run() {
                    // UI쓰레드에서 토스트 뿌림
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "uncaughtException() :" + ex, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }.start();

            // 쓰레드 잠깐 쉼
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

}
