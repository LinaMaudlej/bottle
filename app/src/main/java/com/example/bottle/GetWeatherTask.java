package com.example.bottle;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetWeatherTask extends AsyncTask<String, Void, String> {
        private TextView textView;

        public GetWeatherTask(TextView textView) {
            this.textView = textView;
        }
        @Override
        protected String doInBackground(String... strings) {
        String weather = "UNDEFINED";
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();

            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                weather += inputString;
                builder.append(inputString);
            }

            JSONObject topLevel = new JSONObject(builder.toString());
            topLevel.put("temprature",weather);
            //JSONObject main = topLevel.getJSONObject("main");
           // weather = String.valueOf(main.getDouble("temprature"));

            urlConnection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }

        @Override
        protected void onPostExecute(String temp) {
            textView.setText("Current Weather: " + temp);
        }
    }

