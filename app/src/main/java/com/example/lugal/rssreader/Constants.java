package com.example.lugal.rssreader;

public class Constants {
    public static final String RSS_LIST = "com.example.lugal.rssreader rss list";
    public static final String CHANNELS_LIST = "com.example.lugal.rssreader channels list";
    public static final String RSS_DELIVERY_BROADCAST_ACTION = "com.example.lugal.rssreader Rss delivery";
    public static final String CHANNEL_DELIVERY_BROADCAST_ACTION = "com.example.lugal.rssreader Channel delivery";
    public static final String CHANNEL_CHANGED_BROADCAST_ACTION ="com.example.lugal.rssreader Channel added";
    public static final String ERROR_BROADCAST_ACTION = "com.example.lugal.rssreader error happened";
    public static final String SHOW_ONE_CHANNEL_MODE = "com.example.lugal.rssreader one channel";
    public static final String CHANNELS_TO_PASS = "com.example.lugal.rssreader channels to pass";
    public static final String ERROR_TEXT = "com.example.lugal.rssreader error text";
    public static final int SETTINGS_ACTIVITY_CODE = 42;
    public static final int SETTINGS_ACTIVITY_UPDATED = 27;
    public static final int REFRESHMENT_TIME_IN_MILLIS = 3600;

    /*Saving activities*/
    public static final String SAVED_ACTIVITY_INFO = "com.example.lugal.rssreader saved activity info";
    public static final String SAVED_ACTIVITY_NAME = "com.example.lugal.rssreader saved activity name";
    public static final String SAVED_ARTICLE_WITH_DESCRIPTION_ACTIVITY_NAME = "com.example.lugal.rssreader article with description activity";
    public static final String ADD_CHANNEL_ACTIVITY_NAME = "com.example.lugal.rssreader add channel activity";
    public static final String CHANNEL_ACTIVITY_NAME = "com.example.lugal.rssreader channels activity";
    public static final String ARTICLES_LIST_NAME = "com.example.lugal.rssreader articles list";
    public static final String IS_CHANNEL_ADDED = "com.example.lugal.rssreader is channel added";

    /*Database names*/
    public static final String CHANNEL_TABLE_NAME = "channels";
    public static final String ARTICLE_TABLE_NAME = "articles";
    /*Channels table*/
    public static final String CHANNEL_ID_ROW = "channelId";
    public static final String CHANNEL_NAME_ROW = "channelName";
    public static final String CHANNEL_URL_ROW = "channelUrl";
    /*Articles table*/
    public static final String ARTICLE_ID_ROW = "articleId";
    public static final String ARTICLE_NAME_ROW = "articleName";
    public static final String ARTICLE_URL_ROW = "articleUrl";
    public static final String ARTICLE_DESCRIPTION_ROW = "articleDescription";
    public static final String ARTICLE_DATE_ROW = "articleDate";
    public static final String ARTICLE_CHANNEL_ID_ROW = "articleChannelId";



}

