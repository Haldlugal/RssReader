package com.example.lugal.rssreader.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public final class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                || Intent.ACTION_PACKAGE_FIRST_LAUNCH.equals(intent.getAction())
                || Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) {
            final AlarmController alarmController = new AlarmController(context);
            alarmController.restartAlarm();
        }
    }
}
