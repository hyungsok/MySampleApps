package com.onlyapps.sample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AlarmManagerTestActivity extends Activity {

    private BroadcastReceiver mBroadcastReceiver;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmmanager);
        registerAlarmBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterAlarmBroadcast();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void scheduleJob(View v) {
        Log.d("AAA", "scheduleJob()");
        Toast.makeText(this, "scheduleJob()", Toast.LENGTH_SHORT).show();

        long triggerTime = 60 * 75 * 1000;
        // 1초마다 계속 호출
//        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, mPendingIntent);
        AlarmManager.AlarmClockInfo info = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + triggerTime, mPendingIntent);
        mAlarmManager.setAlarmClock(info, info.getShowIntent());


    }

    public void cancelAllJobs(View v) {
        Log.d("AAA", "cancelAllJobs()");
        Toast.makeText(this, "cancelAllJobs()", Toast.LENGTH_SHORT).show();
        mAlarmManager.cancel(mPendingIntent);
    }

    private void registerAlarmBroadcast() {
        mBroadcastReceiver = new BroadcastReceiver() {
            private int index = 0;

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("AAA", "BroadcastReceiver onReceive() : " + intent);
                Toast.makeText(context, "Alarm time has been reached", Toast.LENGTH_LONG).show();
                showNotification("Test : " + index++);
                scheduleJob(null);
            }
        };

        registerReceiver(mBroadcastReceiver, new IntentFilter("sample"));
        mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("sample"), 0);
        mAlarmManager = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
    }

    private void unregisterAlarmBroadcast() {
        mAlarmManager.cancel(mPendingIntent);
        getBaseContext().unregisterReceiver(mBroadcastReceiver);
    }

    private void showNotification(String text) {
        //We get a reference to the NotificationManager
        String MyText = "Reminder";
        String MyNotificationTitle = "Alarm!";
        String MyNotificationText = text;

        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification noti = new Notification.Builder(this)
                .setTicker(MyText)
                .setContentTitle(MyNotificationTitle)
                .setContentText(MyNotificationText)
                .setSmallIcon(R.drawable.icon_search_magnifier)
                .setContentIntent(intent)
                .build();

        int NOTIFICATION_ID = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, noti);
    }
}
