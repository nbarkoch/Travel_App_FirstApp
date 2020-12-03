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

public class TravelViewModel extends ViewModel {

    public FormTravel form = new FormTravel();
    static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


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

    public View.OnFocusChangeListener getFocusDateBeginChangeListener() {
        return focusDateBeginChangeListener;
    }

    public View.OnFocusChangeListener getFocusDateEndChangeListener() {
        return focusDateEndChangeListener;
    }

    private View.OnFocusChangeListener focusEmailChangeListener;
    private View.OnFocusChangeListener focusPhoneChangeListener;
    private View.OnFocusChangeListener focusNPassengersChangeListener;
    private View.OnFocusChangeListener focusDateBeginChangeListener;
    private View.OnFocusChangeListener focusDateEndChangeListener;

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
        focusDateBeginChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText et = (EditText) view;
                if (hasFocus && (et.getError() == null))
                    et.callOnClick();
                if (et.getText().length() > 0 && !hasFocus) {
                    form.isDatesValid(true);
                }
            }
        };
        focusDateEndChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                EditText et = (EditText) view;
                if (hasFocus && (et.getError() == null))
                    et.callOnClick();
                if (et.getText().length() > 0 && !hasFocus) {
                    form.isDatesValid(true);
                }
            }
        };
    }

    public void saveTravelOnClick(View view){
        saveTravel(form.getTravel());
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


    //TODO if relevant
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
