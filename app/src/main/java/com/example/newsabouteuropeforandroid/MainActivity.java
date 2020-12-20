package com.example.newsabouteuropeforandroid;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private Button changeAPIKeyButton;
    private Button closeAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "We are in MAIN");

        // Check that api key exists or not:
        SharedPreferences sharedPreferences =  this.getPreferences(MODE_PRIVATE);
        if (sharedPreferences.contains("apiKey")){

            changeAPIKeyButton = findViewById(R.id.changeAPIKey);
            closeAppButton = findViewById(R.id.closeApp);
            Log.e(TAG, "API KEY IS OKAY" );

            changeAPIKeyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HandleAPIKeyActivity.class);
                    intent.putExtra("purpose", "change");
                    startActivity(intent);
                    Log.e(TAG, "CHANGE API KEY" );
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
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

        } else {
            HandleAPIKeyActivity.updateActivity(this);
            Intent intent = new Intent(getApplicationContext(), HandleAPIKeyActivity.class);
            intent.putExtra("purpose", "add");
            startActivity(intent);
            Log.e(TAG, "GET API KEY FROM USER" );
        };

    }
}