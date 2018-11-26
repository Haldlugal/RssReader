package com.example.lugal.rssreader.screens.articlelist.articlesDelivery;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public final class ArticleParser {
    private final String rssDescriptionTag = "description";
    private final String atomDescriptionTag = "summary";
    private final String rssTitleTag = "title";
    private final String atomTitleTag = "title";
    private final String rssUrlTag = "link";
    private final String atomUrlTag = "link";
    private final String rssPublishingDateTag = "pubDate";
    private final String atomPublishingDateTag = "published";


    private final ArrayList<String> actualTags = new ArrayList<>();
    private final ArrayList<Article> articles = new ArrayList<>();
    private XmlPullParser xmlPullParser = null;
    private StringReader rssText = new StringReader("");


    public ArticleParser(){
        actualTags.add(rssDescriptionTag);
        actualTags.add(atomDescriptionTag);
        actualTags.add(rssTitleTag);
        actualTags.add(atomTitleTag);
        actualTags.add(rssUrlTag);
        actualTags.add(atomUrlTag);
        actualTags.add(rssPublishingDateTag);
        actualTags.add(atomPublishingDateTag);
        try {
            final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            this.xmlPullParser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void setRssText(StringReader rssText){
        this.rssText = rssText;
    }

    public ArrayList<Article> generateArticleList() throws IOException, XmlPullParserException {

        final String ignoringTag = "ignore";
        final String rssItemTag = "item";
        final String atomItemTag = "entry";

        xmlPullParser.setInput(rssText);
        Article article = new Article();
        String currentTag = null;
        xmlPullParser.next();

        while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xmlPullParser.getEventType()) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    //There can be a case, in which href to full article is written inside start tag (in atom case)
                    if (atomUrlTag.equals(xmlPullParser.getName())){
                        currentTag = atomUrlTag;
                        if (xmlPullParser.getAttributeValue(null, "href") != null) {
                            article.setUrlToFullArticle(xmlPullParser.getAttributeValue(null, "href").trim());
                        }
                    }
                    for (String actualTag : actualTags){
                        if (actualTag.equals(xmlPullParser.getName())){
                            currentTag = actualTag;
                            break;
                        }
                        else currentTag = ignoringTag;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (rssItemTag.equals(xmlPullParser.getName()) || atomItemTag.equals(xmlPullParser.getName())){
                        articles.add(article);
                        article = new Article();
                    }
                    break;
                case XmlPullParser.TEXT:
                    if (rssTitleTag.equals(currentTag) || atomTitleTag.equals(currentTag)) {
                        String articleTitle = xmlPullParser.getText();
                        articleTitle = articleTitle.trim();
                        if (articleTitle.length() > 0) {
                            article.setArticleTitle(articleTitle);
                        }
                    }
                    else if (rssDescriptionTag.equals(currentTag) || atomDescriptionTag.equals(currentTag)){
                        String articleDescription = xmlPullParser.getText();
                        articleDescription = articleDescription.trim();
                        if (articleDescription.length() > 0) {
                            article.setArticleDescription(articleDescription);
                        }
                    }
                    else if (rssUrlTag.equals(currentTag)){
                        String articleUrl = xmlPullParser.getText();
                        articleUrl = articleUrl.trim();
                        if (articleUrl.length() > 0){
                            article.setUrlToFullArticle(articleUrl);
                        }

                    }
                    else if (rssPublishingDateTag.equals(currentTag)){
                        String articlePublishingDate = xmlPullParser.getText();
                        articlePublishingDate = articlePublishingDate.trim();
                        if (articlePublishingDate.length() > 0) {
                            article.setPublishingDate(articlePublishingDate);
                        }
                    }
                    else if (atomPublishingDateTag.equals(currentTag)){
                        String articlePublishingDate = xmlPullParser.getText();
                        articlePublishingDate = articlePublishingDate.trim();
                        if (articlePublishingDate.length() > 0) {
                            article.setPublishingDate(articlePublishingDate);
                        }
                    }
                    break;
                default:
                    break;
            }
            xmlPullParser.next();
        }
        return articles;
    }
}
