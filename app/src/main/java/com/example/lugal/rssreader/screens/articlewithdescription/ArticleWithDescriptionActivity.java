package com.example.lugal.rssreader.screens.articlewithdescription;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.lugal.rssreader.Constants;
import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.screens.OptionsActivity;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.Article;
import com.example.lugal.rssreader.screens.settings.SettingsActivity;

public final class ArticleWithDescriptionActivity extends OptionsActivity {
    private static final String articleLabelForIntent = "article";
    public static final String ARTICLE_TITLE_LABEL = "com.example.lugal.rssreader article title label";
    public static final String ARTICLE_DESCRIPTION_LABEL = "com.example.lugal.rssreader article description label";
    public static final String ARTICLE_URL_LABEL = "com.example.lugal.rssreader article url label";



    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_full);
        final Intent intent = getIntent();
        final Article article = (Article) intent.getSerializableExtra(articleLabelForIntent);

        final TextView articleTitle = findViewById(R.id.articleTitle);
        articleTitle.setText(article.getArticleTitle());

        final WebView articleDescription = findViewById(R.id.articleDescription);
        final String articleWebDescription = getDescriptionWithUrl(article.getArticleDescription(), article.getUrlToFullArticle());

        articleDescription.loadDataWithBaseURL(null, articleWebDescription, "text/html", "utf-8", null);
    }

    @Override
    protected void onStart() {
        final Intent intent = getIntent();
        final Article article = (Article) intent.getSerializableExtra(articleLabelForIntent);
        saveCurrentActivity(article);
        super.onStart();
    }

    private void saveCurrentActivity(Article article){
        SharedPreferences savedActivity = getSharedPreferences(Constants.SAVED_ACTIVITY_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedActivity.edit();
        editor.putString(Constants.SAVED_ACTIVITY_NAME, Constants.SAVED_ARTICLE_WITH_DESCRIPTION_ACTIVITY_NAME);
        editor.putString(ARTICLE_TITLE_LABEL, article.getArticleTitle());
        editor.putString(ARTICLE_URL_LABEL, article.getUrlToFullArticle());
        editor.putString(ARTICLE_DESCRIPTION_LABEL, article.getArticleDescription());
        editor.apply();
    }

    private String getDescriptionWithUrl(String description, final String url){
        if (!("".equals(url))) {
            description = "<div style='padding:8px; background-color: #ced8f2; color: #363636;'>" + description + "<hr><a href='" + url + "'>Перейти к статье</a></div>";
        }
        return description;
    }

    public static Intent getArticleWithDescriptionActivityIntent(final Context context, final Article article){
        Intent intent = new Intent(context, ArticleWithDescriptionActivity.class);
        intent.putExtra(articleLabelForIntent, article);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsMenuItem:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
