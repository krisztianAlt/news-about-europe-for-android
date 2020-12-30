package com.example.newsabouteuropeforandroid;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteArticles {
    private static final String TAG = FavoriteArticles.class.getSimpleName();

    public FavoriteArticles() {
    }

    public ArrayList<ArticleListData> loadFavoriteArticles(){
        ArrayList<ArticleListData> articles;
        Gson gson = new Gson();
        String json = MainActivity.sharedPreferences.getString("favoriteArticles", null);
        Type type = new TypeToken<ArrayList<ArticleListData>>() {}.getType();
        articles = gson.fromJson(json, type);
        if (articles == null){
            articles = new ArrayList<>();
        }
        return articles;
    }

    public String saveFavoriteArticles(ArrayList<ArticleListData> articles){
        String status;
        try {
            SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(articles);
            editor.putString("favoriteArticles", json);
            editor.commit();
            status = "ok";
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
            status = "error";
        }
        return status;
    }

    public boolean isArticleInFavorites(ArticleListData article){
        ArrayList<ArticleListData> articles = loadFavoriteArticles();
        if (articles.contains(article)){
            return true;
        }
        return false;
    }

    public String deleteArticle(ArticleListData article) {
        String status;
        ArrayList<ArticleListData> articles = loadFavoriteArticles();
        articles.remove(article);
        status = saveFavoriteArticles(articles);
        return status;
    }

    public String addArticle(ArticleListData article) {
        String status;
        ArrayList<ArticleListData> articles = loadFavoriteArticles();
        articles.add(article);
        status = saveFavoriteArticles(articles);
        return status;
    }
}
