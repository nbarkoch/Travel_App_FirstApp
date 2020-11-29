package com.example.travelapp_part1.ui;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.travelapp_part1.BR;
import com.example.travelapp_part1.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormTravel extends BaseObservable {
    private String email;
    private String phoneNumber;
    private Integer numPassengers;

    public ObservableField<Integer> phoneError = new ObservableField<>();
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> numPassengersError = new ObservableField<>();
    public ObservableField<Integer> dateError = new ObservableField<>();


    @Bindable
    public boolean isValid() {
        boolean valid = isPhoneValid(false);
        return  isEmailValid(false) && valid;// && isNumPassengersValid();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.valid);
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.valid);
    }

    public void setNumPassengers(Integer numPassengers) {
        this.numPassengers = numPassengers;
       // notifyPropertyChanged(BR.valid);
    }


    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    @Bindable
    public Integer getNumPassengers() {
        return numPassengers;
    }


    public boolean RegexValidation(String value,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean isPhoneValid(boolean setMsg) {
        if(email == null){
            if(setMsg)
                phoneError.set(R.string.empty_field_error);
            return false;
        }
        String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        if (!RegexValidation(phoneNumber,regex)) {
            if(setMsg)
                phoneError.set(R.string.phone_not_valid);
            return false;
        }
        phoneError.set(null);
        return true;
    }

    public boolean isEmailValid(boolean setMsg) {
        if(email == null){
            if(setMsg)
                emailError.set(R.string.empty_field_error);
            return false;
        }
        String regex = "^(.+)@(.+)$";
        if (!RegexValidation(email,regex)) {
            if(setMsg)
                emailError.set(R.string.email_not_valid);
            return false;
        }
        emailError.set(null);
        return true;
    }

    public boolean isNumPassengersValid(){
        if(numPassengers == null){
            emailError.set(R.string.empty_field_error);
            return false;
        }
        if (numPassengers <= 0){
            numPassengersError.set(R.string.numPassengers_not_valid);
            return false;
        }
        numPassengersError.set(null);
        return true;
    }

}
