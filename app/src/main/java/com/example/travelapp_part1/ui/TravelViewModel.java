package com.example.travelapp_part1.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp_part1.Data.TravelRepository;
import com.example.travelapp_part1.Entities.Travel;
import com.example.travelapp_part1.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TravelViewModel extends ViewModel {
    public FormTravel form = new FormTravel();
    private TravelRepository repository;
    public TravelViewModel() {
        repository = TravelRepository.getInstance();
    }

    public void saveTravelOnClick(View view){
        saveTravel(form.getTravel());
    }
    public void saveTravel(Travel travel) {
        travel.setRequestType(Travel.RequestType.sent);
        TravelRepository.saveTravel(travel);
    }

    public LiveData<Boolean> getIsSuccess() {
        return repository.getIsSuccess();
    }




}
