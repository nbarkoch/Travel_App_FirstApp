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
import com.example.travelapp_part1.R;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormTravel extends BaseObservable {
    private String email;
    private String phoneNumber;
    private Integer numPassengers;
    private Date dateBegin;
    private Date dateEnd;
    static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


    public ObservableField<Integer> phoneError = new ObservableField<>();
    public ObservableField<Integer> emailError = new ObservableField<>();
    public ObservableField<Integer> numPassengersError = new ObservableField<>();
    public ObservableField<Integer> dateError = new ObservableField<>();


//    @InverseBindingAdapter(attribute = "android:text")
//    public static int getText(TextView view) {
//        return Integer.parseInt(view.getText().toString());
//    }
//
//    @BindingAdapter("android:text")
//    public static void setText(TextView view, int value) {
//        if (view.getText() != null
//                && ( !view.getText().toString().isEmpty() )
//                && Integer.parseInt(view.getText().toString()) != value) {
//            view.setText(Integer.toString(value));
//        }
//    }

    @Bindable
    public boolean isValid() {
        boolean validPhoneNumber = isPhoneValid(false);
        boolean validEmail = isEmailValid(false);
        boolean validNumPassengers = isNumPassengersValid(false);
        return  isDatesValid(false) && validPhoneNumber && validEmail && validNumPassengers;
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
        notifyPropertyChanged(BR.valid);
    }

    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
        notifyPropertyChanged(BR.valid);
    }

    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
        notifyPropertyChanged(BR.valid);
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

    @Bindable
    public Date getDateBegin() {
        return dateBegin;
    }

    @Bindable
    public Date getDateEnd() {
        return dateEnd;
    }


    public boolean RegexValidation(String value,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public boolean isPhoneValid(boolean setMsg) {
        if(phoneNumber == null)
            return false;
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
        if(email == null)
            return false;
        String regex = "^(.+)@(.+)$";
        if (!RegexValidation(email,regex)) {
            if(setMsg)
                emailError.set(R.string.email_not_valid);
            return false;
        }
        emailError.set(null);
        return true;
    }

    public boolean isNumPassengersValid(boolean setMsg){
        if(numPassengers == null)
            return false;
        if (numPassengers <= 0){
            if(setMsg)
                numPassengersError.set(R.string.numPassengers_not_valid);
            return false;
        }
        numPassengersError.set(null);
        return true;
    }

    public boolean isDatesValid(boolean setMsg){
        if(dateBegin == null || dateEnd == null)
            return false;
        if(dateBegin.after(dateEnd)) {
            if (setMsg)
                dateError.set(R.string.date_not_valid);
            return false;
        }
        dateError.set(null);
        return true;
    }



    @BindingAdapter("android:text")
    public static void setIntegerText(TextView view, Integer value) {
        boolean setValue = view.getText().length() == 0;
        try {
            if (!setValue) {
                setValue = Integer.parseInt(view.getText().toString()) != value;
            }
        } catch (NumberFormatException e) {
            return;
        }
//        if (setValue) {
//            view.setText(String.valueOf(value));
//        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Integer getIntegerText(TextView view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }




    @BindingAdapter("android:text")
    public static void setDateText(TextView view, Date value) {
        boolean setValue = view.getText().length() == 0;
        try {
            if (!setValue) {
                setValue = format.parse(view.getText().toString()) != value;
            }
        } catch (ParseException e) {
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Date getDateText(TextView view) {
        try {
            return format.parse(view.getText().toString());
        } catch (ParseException e) {
            return null;
        }
    }


///// ?????
    @BindingAdapter(value = {"onTextChange", "textAttrChanged"}, requireAll = false)
    public static void setTextListener(TextView view,
                                       final TextWatcher listener,
                                       final InverseBindingListener textChange) {
        if (textChange == null) {
            view.addTextChangedListener(listener);
        } else {
            view.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (listener != null){
                        listener.onTextChanged(s, start, before, count);
                    }
                    else {
                        textChange.onChange();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }


}
