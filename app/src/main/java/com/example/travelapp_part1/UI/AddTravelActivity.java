package com.example.travelapp_part1.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
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

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
// !!!!


public class AddTravelActivity extends AppCompatActivity {

    // google map


    protected TravelViewModel travelViewModel;
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
    List<UserLocation> destLocations;
    Travel travel;
    boolean errorExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        setView();
        travel = new Travel();
        destLocations = new LinkedList<>();
        travelViewModel = new ViewModelProvider(this).get(TravelViewModel.class);
        travelViewModel.getIsSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean bool) {
            Toast.makeText(AddTravelActivity.this, bool.toString(), Toast.LENGTH_LONG).show();
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Alert","Lets See if it Works !!!" + paramThread.getName());
                errorExist = true;
            }
        });
    }


    private void setView(){

        editTextClientName = (EditText) findViewById(R.id.IdClientName);
        editTextClientPhone = (EditText) findViewById(R.id.IdClientPhone);
        editTextClientEmail = (EditText) findViewById(R.id.IdClientEmail);
        editTextNumPassengers = (EditText) findViewById(R.id.IdNumPassengers);
        editTextTravelDate=(EditText) findViewById(R.id.IdEditDate1);
        editTextTravelDate.setInputType(InputType.TYPE_NULL);
        editTextArrivalDate =(EditText) findViewById(R.id.IdEditDate2);
        editTextArrivalDate.setInputType(InputType.TYPE_NULL);
        editTextClientTargetLocX = (EditText) findViewById(R.id.IdClientTargetLocX);
        editTextClientTargetLocY = (EditText) findViewById(R.id.IdClientTargetLocY);
        editTextClientSourceLocX = (EditText) findViewById(R.id.IdClientSourceLocX);
        editTextClientSourceLocY = (EditText) findViewById(R.id.IdClientSourceLocY);
        editTextViewError = (TextView) findViewById(R.id.IdTextViewError);
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
        picker.show();
    }

    public void addLocation_onClick(View view) {
        try {
            double targetLocX = Double.parseDouble(this.editTextClientTargetLocX.getText().toString());
            double targetLocY = Double.parseDouble(this.editTextClientTargetLocY.getText().toString());
            UserLocation travelLocation = new UserLocation(targetLocX, targetLocY);
            destLocations.add(travelLocation);
            this.editTextClientTargetLocX.setText("");
            this.editTextClientTargetLocY.setText("");
            Toast.makeText(AddTravelActivity.this, "The location Added Successfully", Toast.LENGTH_LONG).show();
        }
        catch (Exception exception){
            Toast.makeText(AddTravelActivity.this, "One or more of the fields are missing", Toast.LENGTH_LONG).show();
        }
    }




}