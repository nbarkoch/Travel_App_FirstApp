package com.example.travelapp_part1.ui;


import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.travelapp_part1.Data.TravelRepository;
import com.example.travelapp_part1.entities.Travel;

public class TravelViewModel extends ViewModel {
    public FormTravel form = new FormTravel();
    private TravelRepository repository;
    public TravelViewModel() {
        repository = TravelRepository.getInstance();
    }

    public void saveTravelOnClick(View view){
        form.getTravel().setRequestType(Travel.RequestType.sent);
        TravelRepository.saveTravel(form.getTravel());
    }

    public LiveData<Boolean> getIsSuccess() {
        return repository.getIsSuccess();
    }

}
