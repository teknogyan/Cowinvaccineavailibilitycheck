package com.example.cowinvaccineavailibilitycheck;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText pinCode;
    EditText date;
    ListView availability;
    ArrayList<String> locationName = new ArrayList<String>();
    DatePickerDialog picker;
    ArrayAdapter<String> adapter;

    public class Getdata extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

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

                for (int i=0; i< arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    locationName.add(i, jsonPart.getString("name") + " has " 
                            + jsonPart.getString("available_capacity") 
                            + " doses available" );
                    Log.i("available locations", Integer.toString(arr.length()));
                    Log.i("loc", Integer.toString(i) + " " + locationName.get(i));

                }
                if (locationName.isEmpty()) {

                    Toast.makeText(MainActivity.this
                            , "No doses available at this location on this date. Try some other date or location."
                            , Toast.LENGTH_SHORT).show();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,locationName);
            availability.setAdapter(adapter);
        }
    }

    public void checkAvail (View view) {

        locationName.clear();
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
        }


        String pinCodeByUser = pinCode.getText().toString();
        String dateByUser = date.getText().toString();

        Log.i("url","https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="
                + pinCodeByUser + "&date=" + dateByUser);

        Getdata getdata = new Getdata();
        getdata.execute("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="
                + pinCodeByUser + "&date=" + dateByUser);

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
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                picker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
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