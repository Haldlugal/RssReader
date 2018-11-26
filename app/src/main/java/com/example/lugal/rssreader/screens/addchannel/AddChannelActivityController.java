package com.example.lugal.rssreader.screens.addchannel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.database.ChannelDBWorker;
import com.example.lugal.rssreader.screens.CommonActivityController;
import com.example.lugal.rssreader.screens.rsschannelslist.ChannelsActivity;


final class AddChannelActivityController implements CommonActivityController{
    private final Activity activity;
    private SharedPreferences channelPreferences;
    private final String ADD_CHANNEL_PREFERENCES = "addChannelPreferences";
    private final String ADD_CHANNEL_NAME_SAVED = "addChannelUrlSaved";
    private final String ADD_CHANNEL_URL_SAVED = "addChannelNameSaved";
    private boolean channelAdded = false;


    AddChannelActivityController(final Activity activity) {
        this.activity = activity;
    }

    public void onCreate(){
        activity.setContentView(R.layout.add_channel);
        final Button addChannelButton = activity.findViewById(R.id.addChannelButton);
        final ButtonClickListener buttonClickListener = new ButtonClickListener();
        addChannelButton.setOnClickListener(buttonClickListener);
    }



    @Override
    public void onPause() {
        saveFields();
    }

    @Override
    public void onResume() {

    }


    public Activity getActivity() {
        return activity;
    }

    public void onStart(){
        channelPreferences = activity.getSharedPreferences(ADD_CHANNEL_PREFERENCES, Context.MODE_PRIVATE);
        if(channelPreferences.contains(ADD_CHANNEL_NAME_SAVED)) {
            ((EditText)activity.findViewById(R.id.channelName)).setText(channelPreferences.getString(ADD_CHANNEL_NAME_SAVED,""));
        }
        if(channelPreferences.contains(ADD_CHANNEL_URL_SAVED)) {
            ((EditText)activity.findViewById(R.id.channelUrl)).setText(channelPreferences.getString(ADD_CHANNEL_URL_SAVED,""));
        }
        saveCurrentActivity();
    }

    private void saveFields(){
        if (channelAdded) {
            saveChannelPreferences("", "");
        }
        else {
            saveChannelPreferences(((EditText) activity.findViewById(R.id.channelName)).getText().toString(), ((EditText) activity.findViewById(R.id.channelUrl)).getText().toString());
        }

    }
    @SuppressWarnings("unchecked")
    /*Suppressed for casting generic*/
    private void addChannelButtonClicked(){


        final String channelName = ((EditText)activity.findViewById(R.id.channelName)).getText().toString();
        final String channelUrl = ((EditText)activity.findViewById(R.id.channelUrl)).getText().toString();


        if ("".equals(channelName)){
            Toast.makeText(activity, activity.getString(R.string.noChannelName) , Toast.LENGTH_LONG).show();
        }
        else if ("".equals(channelUrl)){
            Toast.makeText(activity, activity.getString(R.string.noChannelUrl), Toast.LENGTH_LONG).show();
        }
        else if ( !(Patterns.WEB_URL.matcher(channelUrl).matches()) ){
            Toast.makeText(activity, activity.getString(R.string.incorrectUrl), Toast.LENGTH_LONG).show();
        }
        else {
            channelAdded = true;
            Bundle channelInfo = new Bundle();
            channelInfo.putString(Constants.CHANNEL_NAME_ROW, channelName);
            channelInfo.putString(Constants.CHANNEL_URL_ROW, channelUrl);
            Intent dbManagerIntent = ChannelDBWorker.getDbWorkerIntentForAddingChannel(activity, channelInfo);
            activity.startService(dbManagerIntent);
            SharedPreferences savedActivity = activity.getSharedPreferences(Constants.SAVED_ACTIVITY_INFO, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = savedActivity.edit();
            editor.putBoolean(Constants.IS_CHANNEL_ADDED, true);
            editor.apply();
            Intent goToChannelsActivityIntent = ChannelsActivity.getChannelsActivityIntent(activity);
            activity.startActivity(goToChannelsActivityIntent);
        }
    }

    private void saveChannelPreferences(String channelName, String channelUrl){
        channelPreferences = activity.getSharedPreferences(ADD_CHANNEL_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = channelPreferences.edit();
        editor.putString(ADD_CHANNEL_NAME_SAVED, channelName);
        editor.putString(ADD_CHANNEL_URL_SAVED, channelUrl);
        editor.apply();
    }

    private void saveCurrentActivity(){
        SharedPreferences savedActivity = activity.getSharedPreferences(Constants.SAVED_ACTIVITY_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedActivity.edit();
        editor.putString(Constants.SAVED_ACTIVITY_NAME, Constants.ADD_CHANNEL_ACTIVITY_NAME);
        editor.putBoolean(Constants.IS_CHANNEL_ADDED, false);
        editor.apply();
    }

    private final class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View view) {
            switch (view.getId()){
                case R.id.addChannelButton: {
                    addChannelButtonClicked();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }
}
