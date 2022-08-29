package com.example.cowinvaccineavailibilitycheck;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchByPinActivity extends AppCompatActivity {

    ListView availability;
    ArrayList<String> locationName = new ArrayList<>();
    String pinCodeByUser;
    String dateByUser;
    ArrayAdapter<String> adapter;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_pin);
        Bundle extras = getIntent().getExtras();

        availability = findViewById(R.id.lv_availability);
        pinCodeByUser = extras.getString("pinCode");
        dateByUser = extras.getString("date");

        findViewById(R.id.btn_goBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        locationName.clear();
        url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="
                + pinCodeByUser + "&date=" + dateByUser;

        Log.i("url",url);
        // Using Executor to run a background thread and request data from the API
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    JSONArray arr = response.getJSONArray("sessions");

                                    for (int i = 0; i < arr.length(); i++) {

                                        JSONObject jsonPart = arr.getJSONObject(i);

                                        locationName.add(i, jsonPart.getString("name") + " has "
                                                + jsonPart.getString("available_capacity")
                                                + " doses available");
                                        Log.i("available locations", Integer.toString(arr.length()));
                                        Log.i("loc", Integer.toString(i) + " " + locationName.get(i));

                                    }
                                    if (locationName.isEmpty()) {

                                        Toast.makeText(getApplicationContext()
                                                , "No doses available at this location on this date. Try some other date or location."
                                                , Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, locationName);
                                availability.setAdapter(adapter);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();

                            }
                        });

                queue.add(jsonObjectRequest);

            }
        });


    }
}