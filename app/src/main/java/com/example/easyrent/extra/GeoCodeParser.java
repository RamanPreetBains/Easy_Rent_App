package com.example.easyrent.extra;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GeoCodeParser extends AsyncTask<String, Void, String> {
    private Context context;
    private GeoCodeCallback geoCodeCallback;
    public GeoCodeParser(Context context, GeoCodeCallback geoCodeCallback) {
        this.context=context;
        this.geoCodeCallback = geoCodeCallback;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;

        URL url = null;
        String jsonString = null;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setDoOutput(true);

            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            char[] buffer = new char[1024];

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();

            System.out.println("JSON: " + jsonString);
            urlConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        geoCodeCallback.onSuccess(s);
    }
}
