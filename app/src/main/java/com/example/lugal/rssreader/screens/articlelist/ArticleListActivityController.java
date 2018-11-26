package com.example.lugal.rssreader.screens.articlelist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.errors.ErrorsReceiver;
import com.example.lugal.rssreader.screens.CommonActivityController;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.Article;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.ArticlesDeliveryService;
import com.example.lugal.rssreader.screens.rsschannelslist.Channel;

import java.util.ArrayList;
import java.util.HashSet;


public final class ArticleListActivityController implements CommonActivityController{
    public static final String ARTICLES_CHANNEL_URLS = "com.example.lugal.rssreader articles channel urls";
    private final Activity activity;
    private ArticlesReceiver articlesReceiver;
    private ErrorsReceiver errorReceiver;
    private ProgressDialog progressDialog;

    ArticleListActivityController(final Activity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        activity.setContentView(R.layout.rss_news_list);
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading articles");
        progressDialog.show();
        startRssNewsDeliveryService();
    }

    public void onResume() {
        articlesReceiver = new ArticlesReceiver(this);
        errorReceiver = new ErrorsReceiver();
        IntentFilter intentFilter = new IntentFilter(Constants.RSS_DELIVERY_BROADCAST_ACTION);
        activity.registerReceiver(articlesReceiver, intentFilter);
        IntentFilter errorIntentFilter = new IntentFilter(Constants.ERROR_BROADCAST_ACTION);
        activity.registerReceiver(errorReceiver, errorIntentFilter);
    }

    @Override
    public void onStart() {
        saveCurrentActivity();
    }



    private void hideProgressDialog(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public Activity getActivity() {
        return activity;
    }

    public void onPause() {
        activity.unregisterReceiver(articlesReceiver);
        activity.unregisterReceiver(errorReceiver);
    }

    @SuppressWarnings("unchecked")
    /*suppress for casting generic*/
    private void saveCurrentActivity(){
        final ArrayList<Channel> channelList;
        channelList = (ArrayList<Channel>) activity.getIntent().getSerializableExtra(Constants.CHANNELS_TO_PASS);
        SharedPreferences savedActivity = activity.getSharedPreferences(Constants.SAVED_ACTIVITY_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedActivity.edit();
        editor.putString(Constants.SAVED_ACTIVITY_NAME, Constants.ARTICLES_LIST_NAME);
        HashSet<String> channelUrls = new HashSet<>();
        for(int i=0; i < channelList.size(); i++){
            channelUrls.add(channelList.get(i).getChannelUrl());
        }
        editor.putStringSet(ARTICLES_CHANNEL_URLS, channelUrls);
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    /*suppress for casting generic*/
    private void startRssNewsDeliveryService(){
        final ArrayList<Channel> channelList;
        channelList = (ArrayList<Channel>) activity.getIntent().getSerializableExtra(Constants.CHANNELS_TO_PASS);
        final Intent articlesDeliveryIntent = ArticlesDeliveryService.getArticlesDeliveryServiceIntent(activity, channelList);
        activity.startService(articlesDeliveryIntent);
    }

    public void showArticleList(final ArrayList<Article> articles) {
        final RecyclerView articleList = activity.findViewById(R.id.articlesList);
        articleList.setLayoutManager(new LinearLayoutManager(activity));
        ArticlesAdapter adapter = new ArticlesAdapter(activity, articles);
        hideProgressDialog();
        articleList.setAdapter(adapter);

    }

}
