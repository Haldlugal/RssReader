package com.example.lugal.rssreader.screens.articlelist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lugal.rssreader.R;
import com.example.lugal.rssreader.screens.articlelist.articlesDelivery.Article;
import com.example.lugal.rssreader.screens.articlewithdescription.ArticleWithDescriptionActivity;

import java.util.ArrayList;

public final class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleHolder>{

    private final ArrayList<Article> articleList;
    private final Context activity;


    ArticlesAdapter(final Context activity, final ArrayList<Article> articleList) {
        this.articleList = articleList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent,false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleHolder articleHolder, final int position) {
        articleHolder.postUrl.setText(articleList.get(position).getUrlToFullArticle());
        articleHolder.postTitle.setText(articleList.get(position).getArticleTitle());
        articleHolder.postDate.setText(articleList.get(position).getPublishingDate());

        articleHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ArticleWithDescriptionActivity.getArticleWithDescriptionActivityIntent(activity, articleList.get(articleHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    final class ArticleHolder extends RecyclerView.ViewHolder{
        private final TextView postUrl;
        private final TextView postTitle;
        private final TextView postDate;

        ArticleHolder(@NonNull final View itemView) {
            super(itemView);
            postUrl = itemView.findViewById(R.id.urlToFullArticle);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDate = itemView.findViewById(R.id.postDate);
        }
    }
}
