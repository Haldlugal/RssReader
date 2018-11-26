package com.example.lugal.rssreader.database;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.errors.ErrorsReceiver;
import com.example.lugal.rssreader.screens.rsschannelslist.Channel;
import com.example.lugal.rssreader.screens.rsschannelslist.ChannelReceiver;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.example.lugal.rssreader.R;


public final class ChannelDBWorker extends Service{

    private static final int DEFAULT_THREAD_POOL_SIZE = 4;
    private final ExecutorService pool;
    private DBHelper dbHelper;

    public ChannelDBWorker() {
        pool = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final String addActionName = "addChannel";
        final String deleteActionName = "deleteChannel";
        final String selectActionName = "selectChannels";

        dbHelper = new DBHelper(this);
        if (addActionName.equals(intent.getStringExtra("action"))){
            addChannelToDb(intent);
        }
        else if (selectActionName.equals(intent.getStringExtra("action"))){
            selectAllChannels();
        }
        else if (deleteActionName.equals(intent.getStringExtra("action"))) {
            deleteChannel(intent);
        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        pool.shutdown();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {
        return null;
    }

    private void addChannelToDb(final Intent intent){
        pool.execute(new Runnable() {
            public void run() {
                ContentValues channelInfo = new ContentValues();
                channelInfo.put(Constants.CHANNEL_NAME_ROW, intent.getBundleExtra("channelInfo").getString(Constants.CHANNEL_NAME_ROW));
                channelInfo.put(Constants.CHANNEL_URL_ROW, intent.getBundleExtra("channelInfo").getString(Constants.CHANNEL_URL_ROW));
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.insert(Constants.CHANNEL_TABLE_NAME, null, channelInfo);
                    Intent channelsChangedBroadcast = new Intent(Constants.CHANNEL_CHANGED_BROADCAST_ACTION);
                    sendBroadcast(channelsChangedBroadcast);
                }
                catch(final SQLiteException e){
                    final String message = getString(R.string.dbAddingChannelError);
                    sendError(message);
                }
                finally {
                    dbHelper.close();
                }
            }
        });
    }

    private void selectAllChannels(){
        pool.execute(new Runnable() {
            public void run() {
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
                    Intent deliveryIntent = ChannelReceiver.getChannelReceiverIntentForSelecting(channels);
                    sendBroadcast(deliveryIntent);
                }
                catch (SQLiteException e){
                    final String message = getString(R.string.dbSelectingChannelError);
                    sendError(message);
                }
                finally{
                    dbHelper.close();
                }
            }
        });
    }

    private void deleteChannel(final Intent intent){
        pool.execute(new Runnable() {
            public void run() {
                try {
                    final SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String channelId = intent.getStringExtra(Constants.CHANNEL_ID_ROW);
                    db.delete(Constants.ARTICLE_TABLE_NAME, Constants.ARTICLE_CHANNEL_ID_ROW + " = " + channelId, null);
                    db.delete(Constants.CHANNEL_TABLE_NAME, Constants.CHANNEL_ID_ROW + " = " + channelId, null);
                    Intent channelsChangedBroadcast = ChannelReceiver.getChannelReceiverIntentForChanging();
                    sendBroadcast(channelsChangedBroadcast);
                }
                catch (SQLiteException e){
                    final String message = getString(R.string.dbDeletingChannelError);
                    sendError(message);
                }
                finally{
                    dbHelper.close();
                }
            }
        });
    }

    public static Intent getDbWorkerIntentForSelectingChannels(final Context context){
        final Intent intent = new Intent(context, ChannelDBWorker.class);
        intent.putExtra("action", "selectChannels");
        return intent;
    }
    public static Intent getDbWorkerIntentForAddingChannel(final Context context, final Bundle channelInfo){
        final Intent intent = new Intent(context, ChannelDBWorker.class);
        intent.putExtra("action", "addChannel");
        intent.putExtra("channelInfo", channelInfo);
        return intent;
    }
    public static Intent getDbWorkerIntentForDeletingChannel(final Context context, final String id){
        final Intent intent = new Intent(context, ChannelDBWorker.class);
        intent.putExtra("action", "deleteChannel");
        intent.putExtra(Constants.CHANNEL_ID_ROW, id);
        return intent;
    }
    private void sendError(final String message){
        Intent errorBroadcast = ErrorsReceiver.getErrorReceiver(message);
        sendBroadcast(errorBroadcast);
    }
}
