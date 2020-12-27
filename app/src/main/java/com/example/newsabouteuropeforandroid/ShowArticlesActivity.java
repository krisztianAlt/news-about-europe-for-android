package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private String countryName;
    private String agencyName;
    private static RecyclerView articleListView;

    private static WeakReference<Activity> mActivityRef;

    public static void updateActivity(Activity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_articles);
        Intent intent = getIntent();
        agencyName = intent.getExtras().getString("selectedAgency");
        countryName = intent.getExtras().getString("selectedCountry");
        TextView articleListTitle = findViewById(R.id.articleListTitle);
        articleListTitle.setText("Articles about " + countryName + " from " + agencyName);
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

            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);

            // Sorting by date:
            ArrayList<ArticleListData> listToSort = new ArrayList<>(articleListDatas);
            listToSort.sort((l1, l2) -> l2.getDate().compareTo(l1.getDate()));

            // Delete recurring articles from list:
            Set<String> titles = new HashSet<>();
            ArrayList<ArticleListData> listWithoutDuplicates = new ArrayList<>();
            for (ArticleListData article : listToSort){
                if (!titles.contains(article.getTitle().split(" - ")[0])){
                    titles.add(article.getTitle().split(" - ")[0]);
                    listWithoutDuplicates.add(article);
                }
            }

            // Listing in UI:
            if (listToSort.size() > 0){
                articleListView = (RecyclerView) findViewById(R.id.articleListView);
                ArticleListAdapter adapter = new ArticleListAdapter(listWithoutDuplicates, ShowArticlesActivity.this);
                try {
                    articleListView.setHasFixedSize(true);
                    articleListView.setLayoutManager(new LinearLayoutManager(ShowArticlesActivity.this));
                    articleListView.setAdapter(adapter);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No articles. Please, try another news agency or country!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
