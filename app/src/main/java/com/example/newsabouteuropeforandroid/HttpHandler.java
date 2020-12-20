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

    /*public String makeServiceCall(String reqUrl,
                                  String httpMethod) {
        String response = null;
        try {
            String plainCredentials= MainActivity.clientEmail + ":" + MainActivity.clientPassword;
            String base64Credentials = new String(android.util.Base64.encode(plainCredentials.getBytes(), android.util.Base64.NO_WRAP));

            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(httpMethod);
            conn.setConnectTimeout(90000);
            conn.setUseCaches(false);
            conn.setRequestProperty("User-Agent", "MyAgent");
            conn.setReadTimeout(90000);
            conn.setRequestProperty("Authorization", "Basic " + base64Credentials);
            conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.connect();

            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST){
                InputStream errorStream = new BufferedInputStream(conn.getErrorStream());
                String errorString = convertStreamToString(errorStream);
                JSONObject error = new JSONObject(errorString);
                errorStream.close();
                return error.toString();
            }

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            in.close();
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

    public String makeServiceCall(String reqUrl,
                                  String newUserName,
                                  String newUserEmail) {
        String response = null;

        try {
            Map<String, String> data = new HashMap<>();
            data.put("name", newUserName);
            data.put("email", newUserEmail);

            JSONObject jsonObject = new JSONObject(data);

            String plainCredentials= MainActivity.clientEmail + ":" + MainActivity.clientPassword;
            String base64Credentials = new String(android.util.Base64.encode(plainCredentials.getBytes(), android.util.Base64.NO_WRAP));

            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(90000);
            conn.setRequestProperty("Authorization", "Basic " + base64Credentials);
            conn.addRequestProperty("Content-Type", "application/json");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObject.toString());
            wr.flush();

            if (conn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST ){
                InputStream errorStream = new BufferedInputStream(conn.getErrorStream());
                String errorString = convertStreamToString(errorStream);
                JSONObject error = new JSONObject(errorString);
                String status = (String) error.get("status");
                Log.e(TAG, status);
                errorStream.close();
                return error.toString();
            }

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
            in.close();
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
    }*/

    public String checkAPIKey(String newAPIKey) {
        String testAPIKey = "3ed83aa5f9564c91b68b08d31a3e7f29";
        String countryName = "hungary";
        String newsAgencyName = "reuters";
        String urlString = "https://newsapi.org/v2/everything?q=\"" + countryName +
                "\"&sources=" + newsAgencyName +
                "&sortBy=relevancy&apiKey=" + testAPIKey;
        String response = null;
        try {
            Log.e(TAG, "API calling starts... URL string: " + urlString);
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

            Log.e(TAG, "Connected. Input stream starts...");
            try {
                // Read the response:
                InputStream in = new BufferedInputStream(conn.getInputStream());
                Log.e(TAG, "Input stream buffered. Conversion starts...");
                response = convertStreamToString(in);
                in.close();
            } finally {
                conn.disconnect();
            }
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
