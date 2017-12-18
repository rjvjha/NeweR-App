package com.example.rajeev.newer;

import android.graphics.Bitmap;

/**
 * Custom class containing all the attributes and setter & getter methods for {@link Article} object
 */

public class Article {
    private String sourceId;
    private String sourceName;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private Bitmap articleImage;

    // Constructor without articleImage
    public Article(String sourceId, String sourceName, String author, String title, String description, String url, String urlToImage, String publishedAt) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }
    // Constructor with articlemage
    public Article(String sourceId, String sourceName, String author, String title, String description, String url, String urlToImage, String publishedAt, Bitmap articleImage) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.articleImage = articleImage;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public Bitmap getArticleImage() {
        return articleImage;
    }
}
