package com.example.travelapp_part1.ui;

import android.view.View;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.travelapp_part1.Data.TravelRepository;
import com.example.travelapp_part1.Entities.Travel;

public class TravelViewModel extends ViewModel {

    public FormTravel form = new FormTravel();

    private TravelRepository repository;

    public View.OnFocusChangeListener getFocusEmailChangeListener() {
        return focusEmailChangeListener;
    }

    public View.OnFocusChangeListener getFocusPhoneChangeListener() {
        return focusPhoneChangeListener;
    }

    public View.OnFocusChangeListener getFocusNPassengersChangeListener() {
        return focusNPassengersChangeListener;
    }

    private View.OnFocusChangeListener focusEmailChangeListener;
    private View.OnFocusChangeListener focusPhoneChangeListener;
    private View.OnFocusChangeListener focusNPassengersChangeListener;

    public TravelViewModel() {
        repository = TravelRepository.getInstance();
        focusPhoneChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !hasFocus) {
                    form.isPhoneValid(true);
                }
            }
        };
        focusEmailChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !hasFocus) {
                    form.isEmailValid(true);
                }
            }
        };
        focusNPassengersChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText et = (EditText) view;
                if (et.getText().length() > 0 && !hasFocus) {
                    form.isNumPassengersValid(true);
                }
            }
        };
    }

    public void saveTravel(Travel trouble) {
        TravelRepository.saveTravel(trouble);
    }

    public LiveData<Boolean> getIsSuccess() {
        return repository.getIsSuccess();
    }


    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }

    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener) {
        if (editText.getOnFocusChangeListener() == null) {
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }



    /// check if the fields are valid
    //
}
