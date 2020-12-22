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

    private static String TAG = MainActivity.class.getSimpleName();
    private static Button changeAPIKeyButton;
    private static Button closeAppButton;
    private static TextView newsAPILink;

    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that api key exists or not:
        sharedPreferences =  this.getPreferences(MODE_PRIVATE);
        if (sharedPreferences.contains("apiKey")){
            changeAPIKeyButton = findViewById(R.id.changeAPIKey);
            closeAppButton = findViewById(R.id.closeApp);

            changeAPIKeyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), HandleAPIKeyActivity.class);
                    intent.putExtra("purpose", "change");
                    startActivity(intent);
                }
            });

            newsAPILink = findViewById(R.id.poweredBy);
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
                    /* moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1); */
                    MainActivity.this.finish();
                    System.exit(0);
                }
            });

        } else {
            HandleAPIKeyActivity.updateActivity(this);
            Intent intent = new Intent(getApplicationContext(), HandleAPIKeyActivity.class);
            intent.putExtra("purpose", "add");
            startActivity(intent);
        };

    }
}