package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShowArticlesActivity extends AppCompatActivity {
    private String TAG = ShowArticlesActivity.class.getSimpleName();
    private static Button backToCountryListButton;
    private String mode;
    private String countryName;
    private String agencyName;
    private static RecyclerView articleListView;
    private boolean sortingByDateDesc;
    private boolean sortingByTitleDesc;
    private boolean sortingByAuthorDesc;

    private static WeakReference<Activity> mActivityRef;

    public static void updateActivity(Activity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_articles);
        Intent intent = getIntent();
        mode = intent.getExtras().getString("mode");
        TextView articleListTitle = findViewById(R.id.articleListTitle);
        TextView articleListIntro = findViewById(R.id.articleListIntro);
        if (mode.equals("selection")){
            agencyName = intent.getExtras().getString("selectedAgency");
            countryName = intent.getExtras().getString("selectedCountry");
            articleListTitle.setText("Articles about " + countryName + " from " + agencyName);
            articleListIntro.setText(R.string.articleListIntroText);
        } else {
            articleListTitle.setText("Favorite articles");
            articleListIntro.setText(R.string.favoriteListIntroText);
        }
        new GetArticles().execute();

        backToCountryListButton = findViewById(R.id.backToCountryList);
        backToCountryListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivityRef.get().recreate();
                finish();
            }
        });
    }

    private class GetArticles extends AsyncTask<Void, Void, Void> {

        private ArrayList<ArticleListData> articleListDatas = new ArrayList<>();

        @Override
        protected Void doInBackground(Void... arg0) {
            if (mode.equals("selection")){
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.getArticlesFromAPI(agencyName, countryName);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        final String status = jsonObj.get("status").toString();
                        String messageToUser;
                        if (status.equals("ok")) {
                            messageToUser = "Data request succeeded.";

                            JSONArray articles = jsonObj.getJSONArray("articles");

                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject n = articles.getJSONObject(i);
                                String title = n.getString("title");
                                String author;
                                if (!n.getString("author").equals("null")) {
                                    author = n.getString("author");
                                } else {
                                    author = "not signed";
                                }
                                String url = n.getString("url");
                                String date = n.getString("publishedAt").substring(0, 10);
                                ArticleListData newArticle = new ArticleListData(title, author, url, date);
                                articleListDatas.add(newArticle);
                            }
                        } else {
                            messageToUser = jsonObj.get("message").toString();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShowArticlesActivity.this,
                                        messageToUser,
                                        Toast.LENGTH_LONG).show();
                            }
                        });


                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get data from News API. Please, try later!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                FavoriteArticles favoriteArticlesHandler = new FavoriteArticles();
                ArrayList<ArticleListData> favoriteArticles = favoriteArticlesHandler.loadFavoriteArticles();
                if (favoriteArticles.size() > 0) {
                    articleListDatas.addAll(favoriteArticles);
                }
            }

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            ArrayList<ArticleListData> listWithoutDuplicates = new ArrayList<>();

            // Delete recurring articles from list:
            if (mode.equals("selection")){
                Set<String> titles = new HashSet<>();
                for (ArticleListData article : articleListDatas){
                    if (!titles.contains(article.getTitle().split(" - ")[0])){
                        titles.add(article.getTitle().split(" - ")[0]);
                        listWithoutDuplicates.add(article);
                    }
                }
            } else {
                listWithoutDuplicates.addAll(articleListDatas);
            }

            if (listWithoutDuplicates.size() > 0){
                sortingByTitleDesc = false;
                sortingByAuthorDesc = false;
                sortingByDateDesc = true;
                showArticles("date", listWithoutDuplicates);
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message;
                        if (mode.equals("selection")){
                            message = "No articles. Please, try another news agency or country!";
                        } else {
                            message = "There aren't favorite articles yet.";
                        }
                        Toast.makeText(getApplicationContext(),
                                message,
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void showArticles(String sortBy, ArrayList<ArticleListData> articleList){

            // Sorting:
            ArrayList<ArticleListData> listToSort = new ArrayList<>(articleList);
            switch (sortBy){
                case "date":
                    if (sortingByDateDesc){
                        listToSort.sort((l1, l2) -> l2.getDate().compareTo(l1.getDate()));
                        sortingByDateDesc = false;
                    } else {
                        listToSort.sort((l1, l2) -> l1.getDate().compareTo(l2.getDate()));
                        sortingByDateDesc = true;
                    }
                    break;
                case "title":
                    if (sortingByTitleDesc){
                        listToSort.sort((l1, l2) -> l2.getTitle().compareTo(l1.getTitle()));
                        sortingByTitleDesc = false;
                    } else {
                        listToSort.sort((l1, l2) -> l1.getTitle().compareTo(l2.getTitle()));
                        sortingByTitleDesc = true;
                    }
                    break;
                case "author":
                    if (sortingByAuthorDesc){
                        listToSort.sort((l1, l2) -> l2.getAuthor().compareTo(l1.getAuthor()));
                        sortingByAuthorDesc = false;
                    } else {
                        listToSort.sort((l1, l2) -> l1.getAuthor().compareTo(l2.getAuthor()));
                        sortingByAuthorDesc = true;
                    }
                    break;
                default:
                    listToSort.sort((l1, l2) -> l2.getDate().compareTo(l1.getDate()));
            }

            FavoriteArticles favoriteArticlesHandler = new FavoriteArticles();

            if (mode.equals("favorites")){
                favoriteArticlesHandler.saveFavoriteArticles(listToSort);
            }

            // Listing in UI:
            articleListView = (RecyclerView) findViewById(R.id.articleListView);
            ArticleListAdapter adapter = new ArticleListAdapter(listToSort, ShowArticlesActivity.this, mode);
            try {
                articleListView.setHasFixedSize(true);
                articleListView.setLayoutManager(new LinearLayoutManager(ShowArticlesActivity.this));
                articleListView.setAdapter(adapter);

                TextView titleButton = findViewById(R.id.articleListHeaderTitle);
                TextView authorButton = findViewById(R.id.articleListHeaderAuthor);
                TextView dateButton = findViewById(R.id.articleListHeaderDate);

                titleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mode.equals("favorites")){
                            ArrayList<ArticleListData> freshList;
                            freshList = favoriteArticlesHandler.loadFavoriteArticles();
                            showArticles("title", freshList);
                        } else {
                            showArticles("title", articleList);
                        }

                    }
                });
                authorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mode.equals("favorites")){
                            ArrayList<ArticleListData> freshList;
                            freshList = favoriteArticlesHandler.loadFavoriteArticles();
                            showArticles("author", freshList);
                        } else {
                            showArticles("author", articleList);
                        }
                    }
                });
                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mode.equals("favorites")){
                            ArrayList<ArticleListData> freshList;
                            freshList = favoriteArticlesHandler.loadFavoriteArticles();
                            showArticles("date", freshList);
                        } else {
                            showArticles("date", articleList);
                        }
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
