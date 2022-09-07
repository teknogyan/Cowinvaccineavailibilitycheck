package com.example.cowinvaccineavailibilitycheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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
    RecyclerView rv_availability;
    ArrayList<DistrictData> districtData = new ArrayList<>();
    String districtId;
    String dateByUser;
    RecyclerAdapter adapter;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_district);
        Bundle extras = getIntent().getExtras();

        rv_availability = findViewById(R.id.rv_availabilityByDistrict);
        districtId = extras.getString("districtId");
        dateByUser = extras.getString("date");

        findViewById(R.id.btn_goBack).setOnClickListener(view -> finish());

        districtData.clear();
        url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" +districtId+ "&date=" +dateByUser;
        // Using Executor to run a background thread and request data from the API
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray  array = response.getJSONArray("centers");
                        for (int i = 0; i < array.length(); i++) {
                            String name, dose1, dose2, vaccineName;

                            name = array.getJSONObject(i).getString("name");
                            dose1 = array.getJSONObject(i).getJSONArray("sessions")
                                    .getJSONObject(0) // next indexes are showing data for   the subsequent days
                                    .getString("available_capacity_dose1");
                            vaccineName = array.getJSONObject(i).getJSONArray("sessions")
                                    .getJSONObject(0) // next indexes are showing data for   the subsequent days
                                    .getString("vaccine");
                            dose2 = array.getJSONObject(i).getJSONArray("sessions")
                                    .getJSONObject(0)
                                    .getString("available_capacity_dose2");

                            districtData.add(new DistrictData(name, dose1, dose2, vaccineName));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("District data", districtData.toString());
                    if (districtData.isEmpty())
                        Toast.makeText(SearchByDistrictActivity.this, "No vaccines available", Toast.LENGTH_SHORT).show();
                    adapter = new RecyclerAdapter(districtData);
                    rv_availability.setAdapter(adapter);
                    rv_availability.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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