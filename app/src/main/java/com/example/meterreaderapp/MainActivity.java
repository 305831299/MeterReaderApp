package com.example.meterreaderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String ENERJOY_BASE_URL = "https://enerjoy.be/api/meterstanden";
    private static final String PARAM_QUERY = "q";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText search_1 = findViewById(R.id.adres);
        final EditText search_2 = findViewById(R.id.Klant_Naam);
        final EditText search_3 = findViewById(R.id.Meterwaarde);

        Button sendButton = findViewById(R.id.submitButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enerjoySearchQuery = search_1.getText().toString() + search_2.getText().toString() + search_3.getText().toString();

                URL urlApi = buildUrl(enerjoySearchQuery);


                new setMeterDataApi().execute(urlApi);
            }
        });
    }

    private class setMeterDataApi extends AsyncTask<URL, Integer, String> {
        @Override
        protected String doInBackground(URL... urls) {
            return urls[0].toString();
        }

        @Override
        protected void onPostExecute(String str) {
            TextView done = findViewById(R.id.endText);
            done.setText(str);
        }
    }

    public static URL buildUrl(String enerjoySearchQuery) {
        Uri builtUri = Uri.parse(ENERJOY_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, enerjoySearchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}


