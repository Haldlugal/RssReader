package com.example.lugal.rssreader.utils;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.database.DBHelper;
import com.example.lugal.rssreader.errors.ErrorsReceiver;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.Article;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.ArticleParser;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.ArticlesDeliveryDbWorker;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.RssTextDownloader;
import com.example.lugal.rssreader.screens.rsschannelslist.Channel;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.lugal.rssreader.R;

public final class ArticlesRefresher  extends Service {

    private final DBHelper dbHelper;
    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private final ExecutorService pool;
    private final ArticlesDeliveryDbWorker articlesDeliveryDbWorker;

    public ArticlesRefresher() {
        pool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        articlesDeliveryDbWorker = new ArticlesDeliveryDbWorker(this);
        dbHelper = new DBHelper(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        refreshChannels();
        return super.onStartCommand(intent, flags, startId);
    }

    private void refreshChannels(){
        pool.execute(new Runnable() {
            public void run() {
                final ArrayList<Channel> channelsToRefresh = getChannels();
                for (int i = 0; i < channelsToRefresh.size(); i++) {
                    ArrayList<Article> articlesList = receiveArticlesListFromUrl(channelsToRefresh.get(i).getChannelUrl());
                    if (articlesList.size() != 0) {
                        articlesDeliveryDbWorker.addArticles(channelsToRefresh.get(i).getChannelId(), articlesList);
                    }
                }
                stopSelf();
            }
        });
    }

    private ArrayList<Channel> getChannels(){
        ArrayList<Channel> channels = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Constants.CHANNEL_TABLE_NAME;
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                int idColIndex = cursor.getColumnIndex(Constants.CHANNEL_ID_ROW);
                int nameColIndex = cursor.getColumnIndex(Constants.CHANNEL_NAME_ROW);
                int urlColIndex = cursor.getColumnIndex(Constants.CHANNEL_URL_ROW);
                do {
                    Channel channel = new Channel();
                    channel.setChannelId(cursor.getString(idColIndex));
                    channel.setChannelName(cursor.getString(nameColIndex));
                    channel.setChannelUrl(cursor.getString(urlColIndex));
                    channels.add(channel);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        finally{
            dbHelper.close();
        }
        return channels;
    }

    private ArrayList<Article> receiveArticlesListFromUrl(@NonNull final String channelUrl){
        ArrayList<Article> articlesList = new ArrayList<>();
        if ((Patterns.WEB_URL.matcher(channelUrl).matches())) {
            RssTextDownloader rssTextDownloader = new RssTextDownloader(this);
            try{
                ArticleParser articleParser = new ArticleParser();
                articleParser.setRssText(rssTextDownloader.generateParserInput(channelUrl));
                articlesList = articleParser.generateArticleList();
            }catch (XmlPullParserException e) {
                final String errorMessage = getString(R.string.parsingArticlesError) + channelUrl;
                sendError(errorMessage);
            }catch (MalformedURLException e) {
                final String errorMessage = getString(R.string.malformedUrlError) + channelUrl;
                sendError(errorMessage);
            } catch (IOException e) {
                final String errorMessage = getString(R.string.errorWhileReceivingRss) + channelUrl;
                sendError(errorMessage);
            }
        }
        return articlesList;
    }

    private void sendError(final String message){
        Intent errorBroadcast = ErrorsReceiver.getErrorReceiver(message);
        sendBroadcast(errorBroadcast);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        pool.shutdown();
        super.onDestroy();
    }
}
