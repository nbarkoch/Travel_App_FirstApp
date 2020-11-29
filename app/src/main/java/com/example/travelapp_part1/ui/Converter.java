package com.example.travelapp_part1.ui;

import android.widget.EditText;

import androidx.databinding.InverseMethod;

public class Converter {
    @InverseMethod("stringToInt")
    public static String intToString(EditText view, Integer oldValue, Integer value) {
        return value.toString();// Converts long to String.
    }

    public static Integer stringToInt(EditText view, Integer oldValue,
                                    String value) {
        return Integer.parseInt(value);// Converts String to long.
    }
}
