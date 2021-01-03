package com.example.newsabouteuropeforandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static RadioGroup newsAgencyRadioButtonGroup;
    public static int checkedRadioButtonID;
    public static SharedPreferences sharedPreferences;
    private static final CountryListData[] countryListData = new CountryListData[]{
            new CountryListData("Albania", R.drawable.al),
            new CountryListData("Andorra", R.drawable.ad),
            new CountryListData("Austria", R.drawable.at),
            new CountryListData("Belarus", R.drawable.by),
            new CountryListData("Belgium", R.drawable.be),
            new CountryListData("Bosnia", R.drawable.ba),
            new CountryListData("Bulgaria", R.drawable.bg),
            new CountryListData("Croatia", R.drawable.hr),
            new CountryListData("Cyprus", R.drawable.cy),
            new CountryListData("Czech Republic", R.drawable.cz),
            new CountryListData("Denmark", R.drawable.dk),
            new CountryListData("Estonia", R.drawable.ee),
            new CountryListData("Finland", R.drawable.fi),
            new CountryListData("Faroe", R.drawable.fo),
            new CountryListData("France", R.drawable.fr),
            new CountryListData("Germany", R.drawable.de),
            new CountryListData("Greece", R.drawable.gr),
            new CountryListData("Hungary", R.drawable.hu),
            new CountryListData("Iceland", R.drawable.is),
            new CountryListData("Ireland", R.drawable.ie),
            new CountryListData("Italy", R.drawable.it),
            new CountryListData("Kosovo", R.drawable.xk),
            new CountryListData("Latvia", R.drawable.lv),
            new CountryListData("Lithuania", R.drawable.lt),
            new CountryListData("Luxembourg", R.drawable.lu),
            new CountryListData("Macedonia", R.drawable.mk),
            new CountryListData("Malta", R.drawable.mt),
            new CountryListData("Monaco", R.drawable.mo),
            new CountryListData("Moldova", R.drawable.md),
            new CountryListData("Montenegro", R.drawable.me),
            new CountryListData("Netherlands", R.drawable.nl),
            new CountryListData("Norway", R.drawable.no),
            new CountryListData("Poland", R.drawable.pl),
            new CountryListData("Portugal", R.drawable.pt),
            new CountryListData("Romania", R.drawable.ro),
            new CountryListData("Russia", R.drawable.ru),
            new CountryListData("San Marino", R.drawable.sm),
            new CountryListData("Serbia", R.drawable.rs),
            new CountryListData("Slovakia", R.drawable.sk),
            new CountryListData("Slovenia", R.drawable.si),
            new CountryListData("Spain", R.drawable.es),
            new CountryListData("Sweden", R.drawable.se),
            new CountryListData("Switzerland", R.drawable.ch),
            new CountryListData("Turkey", R.drawable.tr),
            new CountryListData("Ukraine", R.drawable.ua),
            new CountryListData("United Kingdom", R.drawable.gb)
};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RadioButton reutersRadioButton = findViewById(R.id.reutersRadioButton);

        sharedPreferences =  this.getPreferences(MODE_PRIVATE);

        newsAgencyRadioButtonGroup = findViewById(R.id.radioGroup);
        if (checkedRadioButtonID != 0) {
            newsAgencyRadioButtonGroup.check(checkedRadioButtonID);
        } else {
            // Default setting (when app starts):
            reutersRadioButton.setChecked(true);
        }

        Button apiKeyButton = findViewById(R.id.changeAPIKey);
        Button favoritesButton = findViewById(R.id.favoritesButton);
        Button closeAppButton = findViewById(R.id.closeApp);

        RecyclerView countryListView = (RecyclerView) findViewById(R.id.countryListView);
        CountryListAdapter adapter = new CountryListAdapter(countryListData, this);
        try {
            countryListView.setHasFixedSize(true);
            countryListView.setLayoutManager(new LinearLayoutManager(this));
            countryListView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        Activity main = this;

        apiKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleAPIKeyActivity.updateActivity(main);
                Intent intent = new Intent(getApplicationContext(), HandleAPIKeyActivity.class);
                // Checking that api key exists or not:
                if (sharedPreferences.contains("apiKey")){
                    intent.putExtra("purpose", "change");
                } else {
                    intent.putExtra("purpose", "add");
                }

                startActivity(intent);
            }
        });

        favoritesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Checking that api key exists or not:
                if (sharedPreferences.contains("apiKey")){
                    ShowArticlesActivity.updateActivity(main);
                    Intent intent = new Intent(main.getApplicationContext(), ShowArticlesActivity.class);
                    intent.putExtra("mode", "favorites");
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String messageToUse = "API key is needed. Please, touch 'API Key' button";
                            Toast.makeText(getApplicationContext(),
                                    messageToUse,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        TextView newsAPILink = findViewById(R.id.poweredBy);
        newsAPILink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://newsapi.org/"));
                startActivity(i);
            }
        });

        closeAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });
    }
}