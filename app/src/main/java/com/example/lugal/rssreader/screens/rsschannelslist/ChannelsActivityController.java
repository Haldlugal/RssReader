package com.example.lugal.rssreader.screens.rsschannelslist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.database.ChannelDBWorker;
import com.example.lugal.rssreader.errors.ErrorsReceiver;
import com.example.lugal.rssreader.screens.CommonActivityController;
import com.example.lugal.rssreader.screens.addchannel.AddChannelActivity;
import com.example.lugal.rssreader.screens.articlelist.ArticleListActivity;
import com.example.lugal.rssreader.screens.articlelist.ArticleListActivityController;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.Article;
import com.example.lugal.rssreader.screens.articlewithdescription.ArticleWithDescriptionActivity;
import com.example.lugal.rssreader.utils.AlarmController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public final class ChannelsActivityController implements CommonActivityController{
    private final Activity activity;
    private ChannelReceiver channelReceiver;
    private ArrayList<Channel> channels;
    private ErrorsReceiver errorsReceiver;


    ChannelsActivityController(final Activity activity) {
        this.activity = activity;
    }

    public void onCreate(){
        activity.setContentView(R.layout.channels_screen);
        AlarmController alarm = new AlarmController(activity);
        alarm.restartAlarm();
        final Button goToNewsListButton = activity.findViewById(R.id.showRssListButton);
        final Button addRssChannelButton = activity.findViewById(R.id.goToAddingChannelsButton);
        ButtonClickListener buttonClickListener = new ButtonClickListener();
        goToNewsListButton.setOnClickListener(buttonClickListener);
        addRssChannelButton.setOnClickListener(buttonClickListener);
        goToSavedActivity();
    }

    public void onResume(){
        addChannelReceiver();
        addErrorReceiver();
        startChannelList();
    }

    @Override
    public void onStart() {
        saveCurrentActivity();
    }

    public void onPause(){
        activity.unregisterReceiver(channelReceiver);
        activity.unregisterReceiver(errorsReceiver);
    }

    public Activity getActivity() {
        return activity;
    }

    private void addChannelReceiver(){
        channelReceiver = new ChannelReceiver(this);
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.CHANNEL_DELIVERY_BROADCAST_ACTION);
        intentFilter.addAction(Constants.CHANNEL_CHANGED_BROADCAST_ACTION);
        activity.registerReceiver(channelReceiver, intentFilter);
    }
    private void addErrorReceiver(){
        errorsReceiver = new ErrorsReceiver();
        IntentFilter errorIntentFilter = new IntentFilter(Constants.ERROR_BROADCAST_ACTION);
        activity.registerReceiver(errorsReceiver, errorIntentFilter);
    }
    public void startChannelList(){
        final Intent deliverChannelsIntent = ChannelDBWorker.getDbWorkerIntentForSelectingChannels(activity);
        activity.startService(deliverChannelsIntent);
    }

    private void addChannelButtonClicked(){
        final Intent intent = AddChannelActivity.getAddChannelActivityIntent(activity);
        activity.startActivity(intent);
    }

    private void showAllRssButtonClicked(){
        final Intent intent = ArticleListActivity.getArticleListIntent(activity, channels);
        activity.startActivity(intent);
    }
    private void saveCurrentActivity(){
        SharedPreferences savedActivity = activity.getSharedPreferences(Constants.SAVED_ACTIVITY_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedActivity.edit();
        editor.putString(Constants.SAVED_ACTIVITY_NAME, Constants.CHANNEL_ACTIVITY_NAME);
        editor.apply();
    }

    private final class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(final View view) {
            switch (view.getId()){
                case R.id.goToAddingChannelsButton: {
                    addChannelButtonClicked();
                    break;
                }
                case R.id.showRssListButton:{
                    showAllRssButtonClicked();
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }
    public void showChannelsList(final ArrayList<Channel> channels) {
        this.channels = channels;
        final RecyclerView channelList = activity.findViewById(R.id.channelsList);
        channelList.setLayoutManager(new LinearLayoutManager(activity));
        ChannelsAdapter adapter = new ChannelsAdapter(activity, channels);
        channelList.setAdapter(adapter);
    }

    private void goToSavedActivity(){
        final SharedPreferences savedActivity = activity.getSharedPreferences(Constants.SAVED_ACTIVITY_INFO, Context.MODE_PRIVATE);
        final String savedActivityName = savedActivity.getString(Constants.SAVED_ACTIVITY_NAME, "");
        if (Constants.SAVED_ARTICLE_WITH_DESCRIPTION_ACTIVITY_NAME.equals(savedActivityName)){
            final Article article = new Article();
            article.setArticleTitle(savedActivity.getString(ArticleWithDescriptionActivity.ARTICLE_TITLE_LABEL, ""));
            article.setArticleDescription(savedActivity.getString(ArticleWithDescriptionActivity.ARTICLE_DESCRIPTION_LABEL, ""));
            article.setUrlToFullArticle(savedActivity.getString(ArticleWithDescriptionActivity.ARTICLE_URL_LABEL, ""));
            final Intent intent = ArticleWithDescriptionActivity.getArticleWithDescriptionActivityIntent(activity, article);
            activity.startActivity(intent);
        }
        else if (Constants.ADD_CHANNEL_ACTIVITY_NAME.equals(savedActivityName)){
            if (! savedActivity.getBoolean(Constants.IS_CHANNEL_ADDED, true)) {
                final Intent intent = AddChannelActivity.getAddChannelActivityIntent(activity);
                activity.startActivity(intent);
            }
        }
        else if (Constants.ARTICLES_LIST_NAME.equals(savedActivityName)){
            final HashSet<String> channelUrls = (HashSet<String>) savedActivity.getStringSet(ArticleListActivityController.ARTICLES_CHANNEL_URLS, new HashSet<String>());
            final Iterator<String> iterator = channelUrls.iterator();
            final ArrayList<Channel> channels = new ArrayList<>();
            while (iterator.hasNext()) {
                Channel channel = new Channel();
                channel.setChannelUrl(iterator.next());
                channels.add(channel);
            }
            final Intent intent = ArticleListActivity.getArticleListIntent(activity, channels);
            activity.startActivity(intent);
        }
    }
}
