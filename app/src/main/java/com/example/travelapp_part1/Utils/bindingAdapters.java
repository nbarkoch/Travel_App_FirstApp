package com.example.travelapp_part1.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class bindingAdapters {
    public interface Validation
    {
        public boolean validete(boolean setMsg);
    }
    static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    @BindingAdapter("error")
    public static void setError(EditText editText, Object strOrResId) {
        if (strOrResId instanceof Integer) {
            editText.setError(editText.getContext().getString((Integer) strOrResId));
        } else {
            editText.setError((String) strOrResId);
        }

    }

    @BindingAdapter("ValidationOnFocus")
    public static void setValidation(EditText editText, Validation validation) {
        View.OnFocusChangeListener onFocusChangeListener = (view, hasFocus) -> {
            EditText et = (EditText) view;
            if (et.getText().length() > 0 && !hasFocus) {
                validation.validete(true);
            }
        };
        editText.setOnFocusChangeListener(onFocusChangeListener);
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
            //return;
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
