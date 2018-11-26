package com.example.lugal.rssreader.screens.articlelist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.Article;

import java.util.ArrayList;

public final class ArticlesReceiver extends BroadcastReceiver {

    private final ArticleListActivityController articleListView;
    public ArticlesReceiver(ArticleListActivityController view){
        this.articleListView = view;
    }
    @Override
    @SuppressWarnings("unchecked")
    /*suppressed for casting generic*/
    public void onReceive(final Context context, final Intent intent) {
        final ArrayList<Article> articles = (ArrayList<Article>) intent.getSerializableExtra(Constants.RSS_LIST);
        articleListView.showArticleList(articles);
    }

    public static Intent getRssNewsReceiverIntent(ArrayList<Article> articlesList){
        Intent deliveryIntent = new Intent(Constants.RSS_DELIVERY_BROADCAST_ACTION);
        deliveryIntent.putExtra(Constants.RSS_LIST, articlesList);
        return deliveryIntent;
    }
}
