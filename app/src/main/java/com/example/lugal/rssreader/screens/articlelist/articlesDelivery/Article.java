package com.example.lugal.rssreader.screens.articlelist.articlesDelivery;

import java.io.Serializable;

public final class Article implements Serializable{
    private String articleTitle;
    private String articleDescription;
    private String urlToFullArticle;
    private String publishingDate;

    public void setArticleTitle(final String articleTitle) {
        final String noTitle = "Отсутствует название статьи";
        if (articleTitle == null || articleTitle.isEmpty()) {
            this.articleTitle = noTitle;
        }
        this.articleTitle = articleTitle;
    }

    public void setArticleDescription(final String articleDescription) {
        final String noDescription = "Отсутствует описание статьи";
        if (articleDescription == null || articleDescription.isEmpty()) {
            this.articleDescription = noDescription;
        }
        this.articleDescription = articleDescription;
    }

    public void setUrlToFullArticle(final String urlToFullArticle) {
        final String noUrl = "";
        if (android.util.Patterns.WEB_URL.matcher(urlToFullArticle).matches()) {
            this.urlToFullArticle = urlToFullArticle;
        }
        else this.urlToFullArticle = noUrl;
    }

    public void setPublishingDate(final String publishingDate) {
        final String noDate = "";
        if (publishingDate == null || publishingDate.isEmpty()){
            this.publishingDate = noDate;
        }
        this.publishingDate = publishingDate;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public String getArticleTitle() {
        return articleTitle;
    }
    public String getUrlToFullArticle() {
        return urlToFullArticle;

    }
    public String getPublishingDate() {
        return publishingDate;
    }

}
