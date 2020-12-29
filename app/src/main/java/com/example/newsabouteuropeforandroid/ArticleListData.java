package com.example.newsabouteuropeforandroid;

import androidx.annotation.Nullable;

public class ArticleListData {

    private String title;
    private String author;
    private String url;
    private String date;

    public ArticleListData(String title, String author, String url, String date) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if(obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ArticleListData article = (ArticleListData) obj;
        return (this.title.equals(article.title) && this.author.equals(article.author) &&
                this.date.equals(article.date));
    }

    @Override
    public int hashCode() {
        return title.length();
    }

    @Override
    public String toString() {
        return "Article{" +
                "title=" + title +
                ", author='" + author +
                ", date=" + date + "}";
    }
}
