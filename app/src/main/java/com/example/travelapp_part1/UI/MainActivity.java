package com.example.travelapp_part1.UI;
// made by Michael Shachor & Naor Bar-Kochva
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.travelapp_part1.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Travel Application V0.38");
    }

    public void addTravelButton_OnClick(View v){
        Intent intent = new Intent(this, AddTravelActivity.class);
        startActivity(intent);
    }
}