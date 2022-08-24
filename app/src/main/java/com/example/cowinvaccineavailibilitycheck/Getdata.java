package com.example.cowinvaccineavailibilitycheck;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/*
* This class extends AsyncTask to
*
*/

public class Getdata extends AsyncTask<String, Void, String> {


    private final MainActivity mainActivity;

    public Getdata(MainActivity mainActivity) {this.mainActivity = mainActivity;}

    @Override
    protected String doInBackground(String... strings) {
//        RequestQueue queue = Volley.newRequestQueue(mainActivity.getApplicationContext());


        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {

            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1) {

                char current = (char) data;
                result += current;
                data = reader.read();

            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {

            JSONObject jsonObject = new JSONObject(s);

            String vacInfo = jsonObject.getString("sessions");

            JSONArray arr = new JSONArray(vacInfo);

            for (int i = 0; i < arr.length(); i++) {

                JSONObject jsonPart = arr.getJSONObject(i);

                mainActivity.locationName.add(i, jsonPart.getString("name") + " has "
                        + jsonPart.getString("available_capacity")
                        + " doses available");
                Log.i("available locations", Integer.toString(arr.length()));
                Log.i("loc", Integer.toString(i) + " " + mainActivity.locationName.get(i));

            }
            if (mainActivity.locationName.isEmpty()) {

                Toast.makeText(mainActivity
                        , "No doses available at this location on this date. Try some other date or location."
                        , Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mainActivity.adapter = new ArrayAdapter<String>(mainActivity.getApplicationContext(), android.R.layout.simple_list_item_1, mainActivity.locationName);
        mainActivity.availability.setAdapter(mainActivity.adapter);
    }
}
