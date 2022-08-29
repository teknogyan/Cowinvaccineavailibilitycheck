package com.example.cowinvaccineavailibilitycheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.RunnableScheduledFuture;

public class HomePageActivity extends AppCompatActivity {
    Button searchByPin;
    Button searchByDistrict;
    EditText et_pinCode;
    EditText et_state;
    EditText et_district;
    TextView tv_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        searchByPin = findViewById(R.id.btn_searchByPin);
        searchByDistrict = findViewById(R.id.btn_searchByDistrict);
        et_pinCode = findViewById(R.id.et_pinCode);
        et_state = findViewById(R.id.et_state);
        et_district = findViewById(R.id.et_district);
        tv_date = findViewById(R.id.et_date);
        View view = this.getCurrentFocus();

        searchByPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pinIntent = new Intent();
                pinIntent.putExtra("pinCode", et_pinCode.getText().toString());
                pinIntent.putExtra("date", tv_date.getText().toString());
                pinIntent.setClass(getApplicationContext(),SearchByPinActivity.class);
                startActivity(pinIntent);
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current date
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog picker = new DatePickerDialog(HomePageActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String datePicked = day + "-" + (month + 1) + "-" + year;
                        tv_date.setText(datePicked);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }, year, month, day);
                picker.show();
            }
        });



    }
}