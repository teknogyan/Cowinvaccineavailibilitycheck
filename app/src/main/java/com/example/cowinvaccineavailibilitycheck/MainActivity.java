package com.example.cowinvaccineavailibilitycheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText pinCode;
    EditText date;
    ListView availability;
    ArrayList<String> locationName = new ArrayList<String>();
    DatePickerDialog picker;
    ArrayAdapter<String> adapter;
    String url;
    public void checkAvail (View view) {
        locationName.clear();
        // hide the keyboard when pressed the button.
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        }


        String pinCodeByUser = pinCode.getText().toString();
        String dateByUser = date.getText().toString();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinCode = findViewById(R.id.et_PinCode);
        date = findViewById(R.id.et_ViewDate);
        availability = findViewById(R.id.lv_availability);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current date
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                picker = new DatePickerDialog(MainActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String datePicked = day + "-" + (month + 1) + "-" + year;
                        date.setText(datePicked);

                    }
                },year, month, day);
                picker.show();
            }
        });


    }
}