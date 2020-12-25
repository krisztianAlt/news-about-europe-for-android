package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

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
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ShowArticlesActivity.this,"Data is " +
                    "downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.getArticlesFromAPI(agencyName, countryName);

            Log.e(TAG, "Response from url: " + jsonStr);
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
                            String author = n.getString("author");
                            String url = n.getString("url");
                            String date = n.getString("publishedAt").substring(0, 10);
                            ArticleListData newArticle = new ArticleListData(title, author, url, date);
                            Log.e(TAG, newArticle.getTitle());
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

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);

            articleListView = (RecyclerView) findViewById(R.id.articleListView);
            ArticleListAdapter adapter = new ArticleListAdapter(articleListDatas, ShowArticlesActivity.this);
            try {
                articleListView.setHasFixedSize(true);
                articleListView.setLayoutManager(new LinearLayoutManager(ShowArticlesActivity.this));
                articleListView.setAdapter(adapter);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            // sort news by date
            // Collections.sort(newsList, new MapComparator("date"));

            /*final ListAdapter adapter = new SimpleAdapter(ShowNewsActivity.this, newsList,
                    R.layout.news_list_item, new String[]{ "title","author", "date", "newsUrl"},
                    new int[]{R.id.newsTitle, R.id.author, R.id.date, R.id.newsUrl});
            newsListView.setAdapter(adapter);

            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String, String> selectedCountry = (HashMap<String, String>) adapter.getItem(position);
                    String url = selectedCountry.get("newsUrl");
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(i);
                }
            });*/
        }

    }


}
