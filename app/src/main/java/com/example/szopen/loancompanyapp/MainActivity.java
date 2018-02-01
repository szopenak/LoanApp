package com.example.szopen.loancompanyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button goCalcButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        goCalcButton = findViewById(R.id.go_calc_btn);

    }

    public void changeActivityToCalc(View v) {
        Intent intent = new Intent(this, Calculator.class);
        startActivity(intent);
    }
    public void changeActivityToAboutUs(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void changeActivityToLocation(View v) {
        Intent intent = new Intent(this, ShowOfficeLocation.class);
        startActivity(intent);
    }
}
