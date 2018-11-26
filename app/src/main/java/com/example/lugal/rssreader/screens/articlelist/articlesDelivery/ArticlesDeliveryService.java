package com.example.lugal.rssreader.screens.articlelist.articlesDelivery;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.errors.ErrorsReceiver;
import com.example.lugal.rssreader.screens.articlelist.ArticlesReceiver;
import com.example.lugal.rssreader.screens.rsschannelslist.Channel;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ArticlesDeliveryService extends Service {

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private final ExecutorService pool;
    private final ArticlesDeliveryDbWorker articlesDeliveryDbWorker;

    public ArticlesDeliveryService() {
        pool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        articlesDeliveryDbWorker = new ArticlesDeliveryDbWorker(this);
    }

    @SuppressWarnings("unchecked")
    /*suppressed for casting generic*/
    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final ArrayList<Channel> channelList = (ArrayList<Channel>) intent.getSerializableExtra(Constants.CHANNELS_TO_PASS);
        receiveArticlesListThread(channelList);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        pool.shutdown();
        super.onDestroy();
    }

    private void receiveArticlesListThread(final ArrayList<Channel> channelList){
        pool.execute(new Runnable() {
            public void run() {
                ArrayList<Article> articlesListToShow = new ArrayList<>();
                for (int i = 0; i < channelList.size(); i++) {
                    ArrayList<Article> articlesList = receiveArticlesListFromUrl(channelList.get(i).getChannelUrl());
                    if (articlesList.size() != 0) {
                        articlesDeliveryDbWorker.addArticles(channelList.get(i).getChannelId(), articlesList);
                    }
                    articlesListToShow.addAll(articlesDeliveryDbWorker.selectArticlesByChannel(channelList.get(i).getChannelId()));
                }
                sendRssDeliveryBroadcast(articlesListToShow);
                stopSelf();
            }
        });
    }

    private ArrayList<Article> receiveArticlesListFromUrl(@NonNull final String channelUrl){
        ArrayList<Article> articlesList = new ArrayList<>();
        if ((Patterns.WEB_URL.matcher(channelUrl).matches())) {
            RssTextDownloader rssTextDownloader = new RssTextDownloader(this);
            try{
                ArticleParser articleParser = new ArticleParser();
                articleParser.setRssText(rssTextDownloader.generateParserInput(channelUrl));
                articlesList = articleParser.generateArticleList();
            }
            catch (XmlPullParserException e) {
                final String errorMessage = "Error while parsing articles from url " + channelUrl;
                sendError(errorMessage);
            }catch (MalformedURLException e) {
                final String errorMessage = "Malformed url " + channelUrl;
                sendError(errorMessage);
            } catch (IOException e) {
                final String errorMessage = "Error while getting article list from url " + channelUrl;
                sendError(errorMessage);
            }
        }
        return articlesList;
    }

    private void sendRssDeliveryBroadcast(final ArrayList<Article> articlesList){
        Intent deliveryIntent = ArticlesReceiver.getRssNewsReceiverIntent(articlesList);
        sendBroadcast(deliveryIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    public static Intent getArticlesDeliveryServiceIntent(final Context context, final ArrayList<Channel> channelsList){
        final Intent intent = new Intent(context, ArticlesDeliveryService.class);
        intent.putExtra(Constants.CHANNELS_TO_PASS, channelsList);
        return intent;
    }
    private void sendError(final String message){
        Intent errorBroadcast = ErrorsReceiver.getErrorReceiver(message);
        sendBroadcast(errorBroadcast);
    }
}
