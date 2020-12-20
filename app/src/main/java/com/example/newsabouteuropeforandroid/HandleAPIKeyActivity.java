package com.example.newsabouteuropeforandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class HandleAPIKeyActivity extends AppCompatActivity {
    private String TAG = HandleAPIKeyActivity.class.getSimpleName();
    private String apiKeyFromUser;
    private String purpose;
    private boolean savingSucceeded;

    private static WeakReference<Activity> mActivityRef;

    public static void updateActivity(Activity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_api_key);

        Intent intent = getIntent();
        purpose = intent.getExtras().getString("purpose");
        apiKeyFromUser = intent.getExtras().getString("apiKeyFromUser");

        if (purpose.equals("change")) {
            TextView currentAPIKey = findViewById(R.id.currentAPIKey);
            currentAPIKey.setVisibility(View.VISIBLE);
            SharedPreferences sharedPreferences =  this.getPreferences(MODE_PRIVATE);
            currentAPIKey.setText(sharedPreferences.getString ("apiKey", "api key"));
        }

        if (apiKeyFromUser != null) {
            EditText newAPIKeyField = findViewById(R.id.newAPIKey);
            newAPIKeyField.setText(apiKeyFromUser);
        }

        savingSucceeded = false;

        Button submitButton = findViewById(R.id.saveNewAPIKey);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText apiKeyField = findViewById(R.id.newAPIKey);
                if (apiKeyField.getText() == null) {
                    apiKeyFromUser = "";
                } else {
                    apiKeyFromUser = apiKeyField.getText().toString();
                }

                new AddNewAPIKey().execute();
            }
        });

    }

    private class AddNewAPIKey extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.checkAPIKey(apiKeyFromUser);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    final String status = jsonObj.get("status").toString();
                    Log.e(TAG, "ANSWER FROM REST API: " + status);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    status,
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                    if (status.equals("ok")) {
                        Log.e(TAG, "HERE COMES PUT API KEY TO SHARED PREFERENCES");
                        savingSucceeded = true;
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            super.onPostExecute(result);
            if (savingSucceeded) {
                mActivityRef.get().recreate();
                finish();
            } else {
                Intent intent = getIntent();
                finish();
                intent.putExtra("apiKeyFromUser", apiKeyFromUser);
                startActivity(intent);
            }
        }
    }
}