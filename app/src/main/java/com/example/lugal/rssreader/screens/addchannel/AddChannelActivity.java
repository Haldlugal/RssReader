package com.example.lugal.rssreader.screens.addchannel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.lugal.rssreader.screens.CommonActivity;


public final class AddChannelActivity extends CommonActivity {

    private AddChannelActivityController addChannelActivityController;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addChannelActivityController = new AddChannelActivityController(this);
        addObserver(addChannelActivityController);
        notifyOnCreate();
    }

    @Override
    protected void onResume() {
        addObserver(addChannelActivityController);
        super.onResume();
    }

    @Override
    protected void onPause() {
        notifyOnPause();
        removeObserver(addChannelActivityController);
        super.onPause();
    }

    public static Intent getAddChannelActivityIntent(final Context context) {
        return new Intent(context, AddChannelActivity.class);
    }


}
