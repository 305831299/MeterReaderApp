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

/*TODO
-- Update Commentary
-- Second API Call ThirdActivity.java
-- Maybe Post Execute on ThirdActivity.java
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText val_1 = findViewById(R.id.emailEdit);
        final EditText val_2 = findViewById(R.id.passwordEdit);
        final String apiCall_1 = "employees";
        final String apiCall_2 = "login";
        final String apiCall_3 = "";

        Button sendButton = findViewById(R.id.loginButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView done = findViewById(R.id.endText);
                done.setText("loading");


                // -- API URL BUILDER

                String keyArray[] = new String[]{"email", "password"};
                String valueArray[] = new String[]{val_1.getText().toString(), val_2.getText().toString()};
                String urlString = NetworkUtils.buildUrlString(apiCall_1, apiCall_2, apiCall_3, keyArray, valueArray);


                // -- Temporary url json file
                //String urlString = "https://jsonblob.com/api/jsonBlob/320771f0-975e-11ea-a2b9-5fea71c1f242";

                new loginApi().execute(urlString, "");
            }
        });
    }

    public class loginApi extends AsyncTask<String, Integer, String>
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
        protected void onPostExecute(String data) {
            JSONObject JObj = null;
            JSONObject JObj2 = null;
            JSONArray JArray = null;

            try {
                JObj = new JSONObject(data);

                TextView end = findViewById(R.id.endText);
                end.setText(JObj.toString());

                try {
                    if (JObj.get("success").toString() == "true")
                    {
                        JArray = (JSONArray) JObj.get("message");
                        JObj2 = JArray.getJSONObject(0);

                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, JObj2.get("token").toString());
                        startActivity(intent);
                    }
                    else {
                        end.setText("Failed to Login");
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


