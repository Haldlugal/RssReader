package com.example.lugal.rssreader.screens.rsschannelslist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lugal.rssreader.Constants;

import java.util.ArrayList;

public final class ChannelReceiver extends BroadcastReceiver {

    private static final String actionLabel = "action";

    private final ChannelsActivityController channelsActivityController;
    public ChannelReceiver(ChannelsActivityController activityController) {
           channelsActivityController = activityController;
    }

    @SuppressWarnings("unchecked")
    /*suppress for casting generic*/
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(actionLabel);
        if (Constants.CHANNEL_DELIVERY_BROADCAST_ACTION.equals(action)){
            final ArrayList<Channel> channels = (ArrayList<Channel>) intent.getSerializableExtra(Constants.CHANNELS_LIST);
            channelsActivityController.showChannelsList(channels);
        }
        else if (Constants.CHANNEL_CHANGED_BROADCAST_ACTION.equals(action)){
            channelsActivityController.startChannelList();
        }
    }

    public static Intent getChannelReceiverIntentForChanging(){
        Intent intent = new Intent(Constants.CHANNEL_CHANGED_BROADCAST_ACTION);
        intent.putExtra(actionLabel, Constants.CHANNEL_CHANGED_BROADCAST_ACTION);
        return intent;
    }
    public static Intent getChannelReceiverIntentForSelecting(ArrayList<Channel> channels){
        Intent intent = new Intent(Constants.CHANNEL_DELIVERY_BROADCAST_ACTION);
        intent.putExtra(actionLabel, Constants.CHANNEL_DELIVERY_BROADCAST_ACTION);
        intent.putExtra(Constants.CHANNELS_LIST, channels);
        return intent;
    }
}
