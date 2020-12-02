package com.example.travelapp_part1.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp_part1.Entities.Travel;
import com.example.travelapp_part1.Entities.UserLocation;
import com.example.travelapp_part1.R;
import com.example.travelapp_part1.databinding.ActivityTravelBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
// !!!!


public class AddTravelActivity extends AppCompatActivity {

    // google map


    private TravelViewModel travelViewModel;
    private EditText editTextClientName;
    private EditText editTextClientPhone;
    private EditText editTextClientEmail;
    private EditText editTextNumPassengers;
    private EditText editTextClientTargetLocX;
    private EditText editTextClientTargetLocY;
    private EditText editTextClientSourceLocX;
    private EditText editTextClientSourceLocY;
    private TextView editTextViewError;
    EditText editTextTravelDate;
    EditText editTextArrivalDate;
    UserLocation sourceLocation;
    List<UserLocation> destLocations;
    Travel travel;

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<String> userLocationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTravelBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_travel);
        //setContentView(R.layout.activity_travel);
        travel = new Travel();
        destLocations = new LinkedList<>();
        travelViewModel = new ViewModelProvider(this).get(TravelViewModel.class);
        travelViewModel.getIsSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
            Toast.makeText(AddTravelActivity.this, bool.toString(), Toast.LENGTH_LONG).show();
            }
        });
        binding.setViewModel(travelViewModel);
        // set up the RecyclerView
        userLocationsView = new ArrayList<String>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyRecyclerViewAdapter(this, userLocationsView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void sendRequest_onClick(View view) throws ParseException, IllegalAccessException {
        try{

            String clientName;
            if(this.editTextClientName.getText().toString().equals(""))
                clientName = null;
            else
                clientName = this.editTextClientName.getText().toString();

            String clientPhone;
            if(this.editTextClientPhone.getText().toString().equals(""))
                clientPhone = null;
            else
                clientPhone = this.editTextClientPhone.getText().toString();

            String clientEmail;
            if(this.editTextClientEmail.getText().toString().equals(""))
                clientEmail = null;
            else
                clientEmail = this.editTextClientEmail.getText().toString();

            Integer numPassengers;
            if(this.editTextNumPassengers.getText().toString().equals(""))
                numPassengers = null;
            else
                numPassengers = Integer.valueOf(this.editTextNumPassengers.getText().toString());

            Date travelDate;
            if(this.editTextTravelDate.getText().toString().equals(""))
                travelDate = null;
            else
                travelDate = new Travel.DateConverter().fromTimestamp(editTextTravelDate.getText().toString());

            Date arrivalDate;
            if(this.editTextArrivalDate.getText().toString().equals(""))
                arrivalDate = null;
            else
                arrivalDate = new Travel.DateConverter().fromTimestamp(editTextArrivalDate.getText().toString());

            double travelLocationX, travelLocationY;
            UserLocation travelLocation;
            if(this.editTextClientSourceLocX.getText().toString().equals("") ||
                    this.editTextClientSourceLocX.getText().toString().equals(""))
                travelLocation = null;
            else{
                travelLocationX = Double.parseDouble(this.editTextClientSourceLocX.getText().toString());
                travelLocationY = Double.parseDouble(this.editTextClientSourceLocY.getText().toString());
                travelLocation = new UserLocation(travelLocationX,travelLocationY);
            }

//        Travel trouble = new Travel( clientName,clientPhone, clientEmail, numPassengers,
//                travelLocation, destLocations, travelDate, arrivalDate);
            travel.setClientName(clientName);
            travel.setClientPhone(clientPhone);
            travel.setClientEmail(clientEmail);
            travel.setNumPassengers(numPassengers);
            travel.setTravelDate(travelDate);
            travel.setArrivalDate(arrivalDate);
            travel.setTravelLocation(travelLocation);
            travel.setDestLocations(destLocations);

            travelViewModel.saveTravel(travel);
            editTextViewError.setText("");
        }
        catch(Exception exception){
            editTextViewError.setText(exception.getMessage());
        }

    }

    public void addRequest_onClick(View view){
        Toast.makeText(AddTravelActivity.this, "Michael ha Gever", Toast.LENGTH_LONG).show();
    }

    public void onClickCalender(View view) {
        EditText editDate = (EditText)view;
        DatePickerDialog picker;
        Calendar calenderDate = Calendar.getInstance();
        int day = calenderDate.get(Calendar.DAY_OF_MONTH);
        int month = calenderDate.get(Calendar.MONTH);
        int year = calenderDate.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(AddTravelActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        editDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();
    }

    public void addLocation_onClick(View view) {
        Intent intent = new Intent(this, AddLocationActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Bundle LocationsData = data.getExtras();
            sourceLocation = (UserLocation) LocationsData.getParcelable("SRC_LOC");
            destLocations =  data.getParcelableArrayListExtra("LIST_DST");
            userLocationsView.clear();
            userLocationsView.addAll(data.getStringArrayListExtra("LIST_VIEW"));
            adapter.notifyDataSetChanged();
        }
    }




}