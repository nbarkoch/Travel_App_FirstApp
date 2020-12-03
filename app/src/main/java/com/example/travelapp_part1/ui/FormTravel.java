package com.example.travelapp_part1.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseMethod;
import androidx.databinding.ObservableField;

import com.example.travelapp_part1.BR;
import com.example.travelapp_part1.Entities.Travel;
import com.example.travelapp_part1.Entities.UserLocation;
import com.example.travelapp_part1.R;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormTravel extends BaseObservable {
    private  Travel travel = new Travel();

    public ObservableField<Integer> phoneError = new ObservableField<>();
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> numPassengersError = new ObservableField<>();
    public ObservableField<Integer> dateError = new ObservableField<>();

    @Bindable
    public boolean isValid() {
        boolean validPhoneNumber = isPhoneValid(false);
        boolean validEmail = isEmailValid(false);
        boolean validNumPassengers = isNumPassengersValid(false);
        boolean notEmpty = ((getClientName() != null && !getClientName().equals("")) &&
                 getDestLocations() != null &&  getTravelLocation()!=null);
        return  isDatesValid(false) &&
                validPhoneNumber && validEmail && validNumPassengers && notEmpty;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setClientName(String clientName)
    {
        travel.setClientName(clientName);
        notifyPropertyChanged(BR.valid);
    }


    public void setPhoneNumber(String phoneNumber) {
        travel.setClientPhone(phoneNumber);
        notifyPropertyChanged(BR.valid);
    }

    public void setEmail(String email) {
        travel.setClientEmail(email);
        notifyPropertyChanged(BR.valid);
    }

    public void setNumPassengers(Integer numPassengers) {
        travel.setNumPassengers(numPassengers);
        notifyPropertyChanged(BR.valid);
    }

    public void setDateBegin(Date dateBegin){
        travel.setTravelDate(dateBegin);
        notifyPropertyChanged(BR.valid);
    }

    public void setDateEnd(Date dateEnd){
        travel.setArrivalDate(dateEnd);
        notifyPropertyChanged(BR.valid);
    }

    public void setTravelLocation(UserLocation travelLocation)  {
       travel.setTravelLocation(travelLocation) ;
       notifyPropertyChanged(BR.valid);
    }

    public void setDestLocations(List<UserLocation> destLocations) {
        travel.setDestLocations(destLocations);
        notifyPropertyChanged(BR.valid);
    }

    @Bindable
    public String getClientName() { return travel.getClientName(); }
    @Bindable
    public String getPhoneNumber() {
        return travel.getClientPhone();
    }

    @Bindable
    public String getEmail() {
        return travel.getClientEmail();
    }

    @Bindable
    public Integer getNumPassengers() {
        return travel.getNumPassengers();
    }

    @Bindable
    public Date getDateBegin() {
        return travel.getTravelDate();
    }

    @Bindable
    public Date getDateEnd() {
        return travel.getArrivalDate();
    }

    @Bindable
    public UserLocation getTravelLocation() {return travel.getTravelLocation();}
    @Bindable
    public List<UserLocation> getDestLocations() { return travel.getDestLocations(); }




    public boolean RegexValidation(String value,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean isPhoneValid(boolean setMsg) {
        if(getPhoneNumber() == null)
            return false;
        String regex = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$";
        if (!RegexValidation(getPhoneNumber(),regex)) {
            if(setMsg)
                phoneError.set(R.string.phone_not_valid);
            return false;
        }
        phoneError.set(null);
        return true;
    }

    public boolean isEmailValid(boolean setMsg) {
        if(getEmail() == null)
            return false;
        String regex = "^(.+)@(.+)$";
        if (!RegexValidation(getEmail(),regex)) {
            if(setMsg)
                emailError.set(R.string.email_not_valid);
            return false;
        }
        emailError.set(null);
        return true;
    }

    public boolean isNumPassengersValid(boolean setMsg){
        if(getNumPassengers() == null)
            return false;
        if (getNumPassengers() <= 0){
            if(setMsg)
                numPassengersError.set(R.string.numPassengers_not_valid);
            return false;
        }
        numPassengersError.set(null);
        return true;
    }

    public boolean isDatesValid(boolean setMsg){
        if(getDateBegin() == null || getDateEnd() == null)
            return false;
        if(getDateBegin().after(getDateEnd())) {
            if (setMsg)
                dateError.set(R.string.date_not_valid);
            return false;
        }
        dateError.set(null);
        return true;
    }

}
