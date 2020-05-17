package com.example.meterreaderapp;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    final static String API_BASE_URL = "http://10.0.2.2:8000/api/";


    public static String buildUrlString(String apiCall_1, String apiCall_2, String apiCall_3, String[] paramArray, String[] valueArray) {
        Uri.Builder buildingUri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(apiCall_1).appendPath(apiCall_2);
        if (apiCall_3 != "")
        {
            buildingUri.appendPath(apiCall_3);
        }
        for (int i = 0; i < paramArray.length; i++)
        {
            buildingUri.appendQueryParameter(paramArray[i], valueArray[i]);
        }

        Uri buildUri = buildingUri.build();
        String urlString = buildUri.toString();

        return urlString;
    }


    public static String getResponseFromHttpUrl(URL url, String token) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if (token != "" && token != null)
        {
            urlConnection.addRequestProperty("AUTHORIZATION", token);
        }
        try {
            String data = null;
            InputStream in = urlConnection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in));

            String line = "";
            while(line != null)
            {
                line = buffer.readLine();
                data += line;
            }
            data = data.replace("null", "");
            return data;

        } finally {
            urlConnection.disconnect();
        }
    }
}
