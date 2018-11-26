package com.example.lugal.rssreader.screens.rsschannelslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.lugal.rssreader.screens.CommonActivity;

public final class ChannelsActivity extends CommonActivity {

    private ChannelsActivityController channelsActivityController;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelsActivityController = new ChannelsActivityController(this);
        addObserver(channelsActivityController);
        notifyOnCreate();
    }

    @Override
    protected void onResume() {
        addObserver(channelsActivityController);
        super.onResume();
    }

    @Override
    protected void onPause() {
        notifyOnPause();
        removeObserver(channelsActivityController);
        super.onPause();
    }

    public static Intent getChannelsActivityIntent(final Context context) {
        return new Intent(context, ChannelsActivity.class);
    }
}
