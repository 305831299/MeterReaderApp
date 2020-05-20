package com.example.meterreaderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button next = findViewById(R.id.nextButton);
        Button submit = findViewById(R.id.submitButton);
        TextView end = findViewById(R.id.secondText);
        final TextView hidden = findViewById(R.id.hidden);
        final TextView hidden_2 = findViewById(R.id.hidden_2);
        final EditText emailK = findViewById(R.id.email);

        next.setVisibility(View.GONE);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT) == true) {
            String token = intent.getStringExtra(Intent.EXTRA_TEXT);
            hidden.setText(token);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // -- API URL BUILDER

                String apiCall_1 = "customers";
                String apiCall_2 = "showOne";
                String apiCall_3 = "";
                String keyArray[] = new String[]{"email"};
                String valueArray[] = new String[]{emailK.getText().toString()};
                String urlString = NetworkUtils.buildUrlString(apiCall_1, apiCall_2, apiCall_3, keyArray, valueArray);


                // -- Temporary url json file
                //String urlString = "https://jsonblob.com/api/jsonBlob/ce772bff-976c-11ea-a2b9-3739443e3526";

                String token = hidden.getText().toString();
                new getCustomerApi().execute(urlString, token);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(SecondActivity.this, ThirdActivity.class);
                sendIntent.putExtra(Intent.EXTRA_TITLE, hidden.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TEXT, hidden_2.getText().toString());
                startActivity(sendIntent);
            }
        });

    }

    public class getCustomerApi extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String token = params[1];

            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                String result = NetworkUtils.getResponseFromHttpUrl(url, token);
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject JObj = null;
            JSONObject JObjData = null;
            JSONObject JObjAddress = null;
            JSONObject JObjCity = null;
            JSONObject JObjCountry = null;

            try {
                JObj = new JSONObject(s);
                TextView end = findViewById(R.id.secondText);
                TextView hidden = findViewById(R.id.hidden_2);
                Button nextButton = findViewById(R.id.nextButton);

                try {
                    JObjData = new JSONObject(JObj.get("data").toString());
                    if (Integer.parseInt(JObjData.get("active").toString()) == 1)
                    {
                        JObjAddress = new JSONObject(JObjData.get("address").toString());
                        JObjCity = new JSONObject(JObjAddress.get("city").toString());
                        JObjCountry = new JSONObject(JObjCity.get("country").toString());

                        end.setText("Name : " + "\n\t" + JObjData.get("first").toString() + " " +JObjData.get("last").toString() + "\n" +
                                "E-mail : " + "\n\t" + JObjData.get("email").toString() + "\n" +
                                "Address : " + "\n\t" + JObjAddress.get("street").toString() + " " + JObjAddress.get("number") + "\n\t" +
                                JObjCity.get("postalcode") + " " + JObjCity.get("name") + "\n\t" +
                                JObjCountry.get("name"));

                        hidden.setText(JObjData.get("email").toString());
                        nextButton.setVisibility(View.VISIBLE);
                    }
                    else {
                        end.setText("No Active Customer");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
