package com.example.lugal.rssreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lugal.rssreader.Constants;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(final Context context) {
        super(context, "rssReaderDB", null, 1);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + Constants.CHANNEL_TABLE_NAME + " ("
                + Constants.CHANNEL_ID_ROW + " integer primary key autoincrement, "
                + Constants.CHANNEL_NAME_ROW + " text, "
                + Constants.CHANNEL_URL_ROW + " text" + ");");

        sqLiteDatabase.execSQL("create table " + Constants.ARTICLE_TABLE_NAME + " ("
                + Constants.ARTICLE_ID_ROW + " integer primary key autoincrement, "
                + Constants.ARTICLE_CHANNEL_ID_ROW + " integer, "
                + Constants.ARTICLE_NAME_ROW + " text, "
                + Constants.ARTICLE_URL_ROW + " text, "
                + Constants.ARTICLE_DATE_ROW + " text, "
                + Constants.ARTICLE_DESCRIPTION_ROW + " text, "
                + " FOREIGN KEY(" + Constants.ARTICLE_CHANNEL_ID_ROW + ") REFERENCES " + Constants.CHANNEL_TABLE_NAME + "(" + Constants.CHANNEL_ID_ROW + ")"
                + ");");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
