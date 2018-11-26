package com.example.lugal.rssreader.screens.articlelist.articlesDelivery;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.database.DBHelper;
import com.example.lugal.rssreader.errors.ErrorsReceiver;
import java.util.ArrayList;
import com.example.lugal.rssreader.R;

public final class ArticlesDeliveryDbWorker {

    private final DBHelper dbHelper;
    private final Context context;

    public ArticlesDeliveryDbWorker(final Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void addArticles(final String channelId, final ArrayList<Article> articlesToAdd){
        try {
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            final Cursor cursor = db.query(Constants.ARTICLE_TABLE_NAME, null, Constants.ARTICLE_CHANNEL_ID_ROW + " = ?", new String[]{channelId}, null, null, null);
            if (cursor != null){
                db.delete(Constants.ARTICLE_TABLE_NAME, Constants.ARTICLE_CHANNEL_ID_ROW + " = " + channelId, null);
                cursor.close();
            }
            for (int i = 0; i < articlesToAdd.size(); i ++){
                Article article = articlesToAdd.get(i);
                ContentValues articleInfo = new ContentValues();
                articleInfo.put(Constants.ARTICLE_NAME_ROW, article.getArticleTitle());
                articleInfo.put(Constants.ARTICLE_URL_ROW, article.getUrlToFullArticle());
                articleInfo.put(Constants.ARTICLE_DESCRIPTION_ROW, article.getArticleDescription());
                articleInfo.put(Constants.ARTICLE_DATE_ROW, article.getPublishingDate());
                articleInfo.put(Constants.ARTICLE_CHANNEL_ID_ROW, channelId);
                db.insert(Constants.ARTICLE_TABLE_NAME, null, articleInfo);
            }

        }
        catch (SQLiteException e){
            final String errorMessage = context.getString(R.string.errorAddingArticle);
            sendError(errorMessage);
        }
        finally{
            dbHelper.close();
        }
    }
    public ArrayList<Article> selectArticlesByChannel(final String channelId){
        ArrayList<Article> articles = new ArrayList<>();
        try {
            final SQLiteDatabase db = dbHelper.getWritableDatabase();
            final Cursor cursor = db.query(Constants.ARTICLE_TABLE_NAME, null, Constants.ARTICLE_CHANNEL_ID_ROW + " = ?", new String[]{channelId}, null, null, null);
            if (cursor.moveToFirst()) {
                int nameColIndex = cursor.getColumnIndex(Constants.ARTICLE_NAME_ROW);
                int urlColIndex = cursor.getColumnIndex(Constants.ARTICLE_URL_ROW);
                int descriptionColIndex = cursor.getColumnIndex(Constants.ARTICLE_DESCRIPTION_ROW);
                int dateColIndex = cursor.getColumnIndex(Constants.ARTICLE_DATE_ROW);
                do {

                    Article article = new Article();
                    article.setArticleTitle(cursor.getString(nameColIndex));
                    article.setUrlToFullArticle(cursor.getString(urlColIndex));
                    article.setArticleDescription(cursor.getString(descriptionColIndex));
                    article.setPublishingDate(cursor.getString(dateColIndex));
                    articles.add(article);
                }
                while (cursor.moveToNext());
                cursor.close();
            }
        }
        catch (SQLiteException e){
            final String errorMessage = "Error while selecting article from database";
            sendError(errorMessage);
        }
        finally {
            dbHelper.close();
        }
        return articles;
    }

    private void sendError(final String message){
        Intent errorBroadcast = ErrorsReceiver.getErrorReceiver(message);
        context.sendBroadcast(errorBroadcast);
    }

}
