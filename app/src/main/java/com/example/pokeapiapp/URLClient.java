package com.example.pokeapiapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by RTanweer on 11/2/2019.
 */
public class URLClient extends AsyncTask<String, Void, Object> {
    private static String TAG = "URLClient";
    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    static String getResponseString (String stringUrl) throws ExecutionException, InterruptedException {
        URLClient client = new URLClient();
        String response = null;
        response = (String)client.execute("text", stringUrl).get();
        return response;
    }

    static Bitmap getResponseBitmap (String stringUrl) throws ExecutionException, InterruptedException {
        URLClient client = new URLClient();
        Bitmap response;
        response = (Bitmap)client.execute("img", stringUrl).get();
        return response;
    }

    @Override
    protected Object doInBackground(String... strings) {
        URL url;
        HttpURLConnection con;
        try{
            String type = strings[0];
            url = new URL(strings[1]);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(REQUEST_METHOD);
            con.setReadTimeout(READ_TIMEOUT);
            con.setConnectTimeout(CONNECTION_TIMEOUT);
            con.setUseCaches(false);
            con.setRequestProperty("Content-length", "0");
            con.connect();
            int responseCode = con.getResponseCode();
            switch(responseCode) {
                case HTTP_OK:
                case HTTP_CREATED:
                case HTTP_ACCEPTED:
                    if(type.equals("text")) {
                        InputStreamReader reader = new InputStreamReader(con.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str = bufferedReader.readLine();
                        while (str != null) {
                            stringBuilder.append(str);
                            str = bufferedReader.readLine();
                        }
                        reader.close();
                        bufferedReader.close();
                        return stringBuilder.toString();
                    } else if(type.equals("img")) {
                        InputStream istream = con.getInputStream();
                        Bitmap bmp = BitmapFactory.decodeStream(con.getInputStream());
                        istream.close();
                        return bmp;
                    } else {
                        // not supported
                    }
                default:
                    // Error
                    break;
            }
        }catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
    // Can utilize post execute as well
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}
