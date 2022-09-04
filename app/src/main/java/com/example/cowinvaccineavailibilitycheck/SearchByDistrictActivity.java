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
    String district;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_district);
        Bundle extras = getIntent().getExtras();

        availability = findViewById(R.id.lv_availabilityByDistrict);
        district = extras.getString("district");
        dateByUser = extras.getString("date");

        findViewById(R.id.btn_goBack).setOnClickListener(view -> finish());

        locationName.clear();
        url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=" +districtId+ "&date=" +dateByUser;
        Log.i("url",url);
        // Using Executor to run a background thread and request data from the API
        ExecutorService executorService = Executors.newSingleThreadExecutor();

    }
}