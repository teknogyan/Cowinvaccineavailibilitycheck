package com.example.cowinvaccineavailibilitycheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchByDistrictActivity extends AppCompatActivity {
    ListView availability;
    ArrayList<String> locationName = new ArrayList<>();
    String districtId;
    String dateByUser;
    ArrayAdapter<String> adapter;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_district);
        Bundle extras = getIntent().getExtras();

        availability = findViewById(R.id.lv_availabilityByDistrict);
        districtId = extras.getString("districtId");
        dateByUser = extras.getString("date");

        findViewById(R.id.btn_goBack).setOnClickListener(view -> finish());

        locationName.clear();
        url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" +districtId+ "&date=" +dateByUser;
        // Using Executor to run a background thread and request data from the API
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Data for districts: ", response.toString());
                    try {
                        JSONArray  array = response.getJSONArray("centers");
                        for (int i = 0; i < array.length(); i++) {
                            locationName.add(array.getJSONObject(i).getString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("Centre Names:", locationName.toString());
                    adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, locationName);
                    availability.setAdapter(adapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(request);
        });

    }

}