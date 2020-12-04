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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelapp_part1.entities.UserLocation;
import com.example.travelapp_part1.R;
import com.example.travelapp_part1.databinding.ActivityTravelBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AddTravelActivity extends AppCompatActivity {


    private TravelViewModel travelViewModel;
    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<String> userLocationsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTravelBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_travel);
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

    public void onClickCalender(View view) {
        EditText editDate = (EditText) view;
        DatePickerDialog picker;
        Calendar calenderDate = Calendar.getInstance();
        int day = calenderDate.get(Calendar.DAY_OF_MONTH);
        int month = calenderDate.get(Calendar.MONTH);
        int year = calenderDate.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(AddTravelActivity.this,
                (view1, year1, monthOfYear, dayOfMonth) ->
                        editDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();
    }

    public void addLocation_onClick(View view) {
        Intent intent = new Intent(this, AddLocationActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle LocationsData = data.getExtras();
            travelViewModel.form.setDestLocations(data.getParcelableArrayListExtra("LIST_DST"));
            //TODO
            travelViewModel.form.setTravelLocation((UserLocation) LocationsData.getParcelable("SRC_LOC"));
            //TODO
            userLocationsView.clear();
            userLocationsView.addAll(data.getStringArrayListExtra("LIST_VIEW"));
            adapter.notifyDataSetChanged();
        }
    }


}