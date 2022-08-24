package com.example.cowinvaccineavailibilitycheck;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/*
* This class extends AsyncTask to send a request using Volley Library
*
*/

public class Getdata extends AsyncTask<String, Void, String> {


    private final MainActivity mainActivity;

    public Getdata(MainActivity mainActivity) {this.mainActivity = mainActivity;}

    @Override
    protected String doInBackground(String... strings) {

        RequestQueue queue = Volley.newRequestQueue(mainActivity.getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, mainActivity.url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray arr = response.getJSONArray("sessions");

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
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mainActivity, "Error Occured", Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(jsonObjectRequest);
    return null;
    }
}