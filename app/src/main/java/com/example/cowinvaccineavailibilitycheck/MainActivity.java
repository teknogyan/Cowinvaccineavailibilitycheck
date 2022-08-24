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

import java.util.ArrayList;
import java.util.Calendar;

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

        Getdata getdata = new Getdata(this);
        getdata.execute(url);

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