package com.example.lugal.rssreader.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.lugal.rssreader.Constants;

public final class AlarmController {

    private final Context context;

    public AlarmController(@NonNull final Context context) {
        this.context = context;
    }
    private void startAlarm() {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, ArticlesRefresher.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), Constants.REFRESHMENT_TIME_IN_MILLIS * 1000
                    , pendingIntent);
        }
    }


    private void stopAlarm() {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, ArticlesRefresher.class);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    public void restartAlarm() {
        stopAlarm();
        startAlarm();
    }


}
