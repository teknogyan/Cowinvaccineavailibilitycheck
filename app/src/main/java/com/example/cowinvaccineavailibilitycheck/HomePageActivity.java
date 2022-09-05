package com.example.cowinvaccineavailibilitycheck;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class HomePageActivity extends AppCompatActivity {
    Button btn_searchByPin;
    Button btn_searchByDistrict;
    EditText et_pinCode;
    AutoCompleteTextView et_state;
    AutoCompleteTextView et_district;
    TextView tv_date;
    String distId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        btn_searchByPin = findViewById(R.id.btn_searchByPin);
        btn_searchByDistrict = findViewById(R.id.btn_searchByDistrict);
        et_pinCode = findViewById(R.id.et_pinCode);
        et_state = findViewById(R.id.et_state);
        et_district = findViewById(R.id.et_district);
        tv_date = findViewById(R.id.et_date);

        btn_searchByPin.setOnClickListener(view -> {

            Intent pinIntent = new Intent();
            pinIntent.putExtra("pinCode", et_pinCode.getText().toString());
            pinIntent.putExtra("date", tv_date.getText().toString());
            pinIntent.setClass(getApplicationContext(),SearchByPinActivity.class);
            startActivity(pinIntent);
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
                        , (datePicker, year1, month1, day1) -> {
                            String datePicked = day1 + "-" + (month1 + 1) + "-" + year1;
                            tv_date.setText(datePicked);
                        }, year, month, day);
                picker.show();
            }
        });

        btn_searchByDistrict.setOnClickListener(view -> {

            if (TextUtils.isEmpty(et_district.getText().toString()) || TextUtils.isEmpty(tv_date.getText().toString())){
                Toast.makeText(getApplicationContext(),"Please Enter Both District Name and Date"
                        ,Toast.LENGTH_SHORT).show();
            } else {
                Intent districtIntent = new Intent();
                districtIntent.putExtra("districtId", distId);
                districtIntent.putExtra("date", tv_date.getText().toString());
                districtIntent.setClass(getApplicationContext(), SearchByDistrictActivity.class);
                startActivity(districtIntent);
            }
        });

        // Initialize autoCompleteTextViewFiller and Load the name of States.
        AutoCompleteTextViewFiller autoCompleteTextViewFiller = new AutoCompleteTextViewFiller(this);
        ArrayList<String> States = autoCompleteTextViewFiller.getStateNames();
        ArrayAdapter<String> adapterStates = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, States);
        et_state.setAdapter(adapterStates);

        // Load districts of the state after the user selects that state.
        et_state.setOnItemClickListener((adapterView, view, i, l) -> {
            Log.i("State Selected", et_state.getText().toString());
            String id = autoCompleteTextViewFiller.getStateId(et_state.getText().toString());
            Log.i("State id: ", id);
            ArrayList<String> districts = autoCompleteTextViewFiller.getDistrictNames(id);
            ArrayAdapter<String> adapterDistricts = new ArrayAdapter<>(getApplicationContext()
                    , R.layout.support_simple_spinner_dropdown_item,districts);
            adapterDistricts.clear();
            et_district.setAdapter(adapterDistricts);

        });

       // update distId after the user selects a district.
        et_district.setOnItemClickListener((parent, view, position, id) -> {
            Log.i("District selected: ", et_district.getText().toString());
            distId = autoCompleteTextViewFiller.getDistrictId(et_district.getText().toString());
            Log.i("District id:", distId);
        });


    }
}