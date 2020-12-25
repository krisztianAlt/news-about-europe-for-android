package com.example.newsabouteuropeforandroid;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public HttpHandler() {
    }

    public String getArticlesFromAPI(String agencyName, String countryName) {
        // Convert strings with spaces:
        if (agencyName.contains(" ")){
            agencyName = agencyName.replace(" ", "-");
        }
        if (countryName.contains(" ")){
            countryName = countryName.replace(" ", "-");
        }

        // API request:
        String apiKey = MainActivity.sharedPreferences.getString ("apiKey", "api key");
        String urlString = "https://newsapi.org/v2/everything?q=\"" + countryName +
                "\"&sources=" + agencyName +
                "&sortBy=relevancy&apiKey=" + apiKey;
        String response = null;
        try {
            // URL connection:
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(90000);
            conn.setUseCaches(false);
            conn.setRequestProperty("User-Agent", "MyAgent");
            conn.setReadTimeout(90000);
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setDoInput(true);
            conn.connect();

            // Read the response:
            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST){
                InputStream errorStream = new BufferedInputStream(conn.getErrorStream());
                response = convertStreamToString(errorStream);
                errorStream.close();
                conn.disconnect();
            } else {
                try {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);
                    in.close();
                } finally {
                    conn.disconnect();
                }
            }
            Log.e(TAG, response);
        } catch (MalformedURLException ex) {
            Log.e(TAG, "MalformedURLException: " + ex.getMessage());
        } catch (ProtocolException ex) {
            Log.e(TAG, "ProtocolException: " + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG, "IOException: " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "Exception: " + ex.getMessage());
        }
        return response;
    }

    public String checkAPIKey(String newAPIKey) {
        String countryName = "hungary";
        String newsAgencyName = "reuters";
        String urlString = "https://newsapi.org/v2/everything?q=\"" + countryName +
                "\"&sources=" + newsAgencyName +
                "&sortBy=relevancy&apiKey=" + newAPIKey;
        String response = null;
        try {
            // URL connection:
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(90000);
            conn.setUseCaches(false);
            conn.setRequestProperty("User-Agent", "MyAgent");
            conn.setReadTimeout(90000);
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setDoInput(true);
            conn.connect();

            // Read the response:
            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST){
                InputStream errorStream = new BufferedInputStream(conn.getErrorStream());
                response = convertStreamToString(errorStream);
                errorStream.close();
                conn.disconnect();
            } else {
                try {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    response = convertStreamToString(in);
                    in.close();
                } finally {
                    conn.disconnect();
                }
            }
            Log.e(TAG, response);
        } catch (MalformedURLException ex) {
            Log.e(TAG, "MalformedURLException: " + ex.getMessage());
        } catch (ProtocolException ex) {
            Log.e(TAG, "ProtocolException: " + ex.getMessage());
        } catch (IOException ex) {
            Log.e(TAG, "IOException: " + ex.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "Exception: " + ex.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
