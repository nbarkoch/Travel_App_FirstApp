package com.example.travelapp_part1.UI;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp_part1.Data.TravelRepository;
import com.example.travelapp_part1.Entities.Travel;

public class TravelViewModel extends ViewModel {

    private MutableLiveData<Boolean> isSuccess= new MutableLiveData<>();

    private TravelRepository repository;

    public TravelViewModel() {
        repository= TravelRepository.getInstance();
    }

    public void saveTravel(Travel trouble) {
        TravelRepository.saveTravel(trouble);
    }

    public LiveData<Boolean> getIsSuccess() {
        return repository.getIsSuccess();
    }



    /// check if the fields are valid
    //
}
