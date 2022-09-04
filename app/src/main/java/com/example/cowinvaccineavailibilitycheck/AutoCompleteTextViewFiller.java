package com.example.cowinvaccineavailibilitycheck;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoCompleteTextViewFiller{
    private final ArrayList<String> stateNames = new ArrayList<>();
    private final ArrayList<String> districtNames = new ArrayList<>();
    final private Context context;
    JSONArray array = null;
    JSONArray distArray;
    public AutoCompleteTextViewFiller(Context context){
        this.context = context;
    }

    public ArrayList<String> getStateNames() {
         loadStates();
         return stateNames;
    }
    public ArrayList<String> getDistrictNames(String id) {
        loadDistricts(id);
        return districtNames;
    }
    private void loadStates() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    array = response.getJSONArray("states");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i =0; i < array.length();i++){
                    try {
                        stateNames.add(array.getJSONObject(i).getString("state_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.i("district_names: ", stateNames.toString());

            }, error -> Log.i("Error", error.toString())
            );
            requestQueue.add(request);
        });
    }

    public String getStateId(String state){
        String id = "35";
        Log.i("Array:", array.toString());
        for (int i =0; i < array.length(); i++){
            try {
                JSONObject object = array.getJSONObject(i);
                if (object.getString("state_name").equals(state)) id = object.getString("state_id");
            } catch (JSONException e) {
                Log.e("Error", "Couldn't check the state id");
            }
        }
        return id;
    }
    private void loadDistricts(String id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String url = "https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + id;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    distArray = response.getJSONArray("districts");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i =0; i < distArray.length();i++){
                    try {
                        districtNames.add(distArray.getJSONObject(i).getString("district_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.i("district_names: ", districtNames.toString());

            }, error -> Log.i("Error", error.toString())
            );
            requestQueue.add(request);
        });
    }
}
