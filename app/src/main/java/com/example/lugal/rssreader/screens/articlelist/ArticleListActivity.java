package com.example.lugal.rssreader.screens.articlelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.screens.CommonActivity;
import com.example.lugal.rssreader.screens.rsschannelslist.Channel;

import java.util.ArrayList;

public final class ArticleListActivity extends CommonActivity {

    private ArticleListActivityController articleListActivityController;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleListActivityController = new ArticleListActivityController(this);
        addObserver(articleListActivityController);
        notifyOnCreate();
    }

    @Override
    protected void onResume() {
        addObserver(articleListActivityController);
        super.onResume();
    }

    @Override
    protected void onPause() {
        notifyOnPause();
        removeObserver(articleListActivityController);
        super.onPause();
    }

    public static Intent getArticleListIntent(Context context, ArrayList<Channel> channels){
        Intent articleListIntent = new Intent(context, ArticleListActivity.class);
        articleListIntent.putExtra(Constants.SHOW_ONE_CHANNEL_MODE, false);
        articleListIntent.putExtra(Constants.CHANNELS_TO_PASS, channels);
        return articleListIntent;
    }
}
