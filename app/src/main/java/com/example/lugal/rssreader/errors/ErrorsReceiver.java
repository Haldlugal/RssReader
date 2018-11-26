package com.example.lugal.rssreader.errors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.lugal.rssreader.Constants;

public final class ErrorsReceiver extends BroadcastReceiver {

    private static final String actionLabel = "action";
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String message = intent.getStringExtra(Constants.ERROR_TEXT);
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static Intent getErrorReceiver(final String errorText) {
        Intent intent = new Intent(Constants.ERROR_BROADCAST_ACTION);
        intent.putExtra(actionLabel, Constants.ERROR_BROADCAST_ACTION);
        intent.putExtra(Constants.ERROR_TEXT, errorText);
        return intent;
    }
}
