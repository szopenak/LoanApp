package com.example.szopen.loancompanyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.view.View.VISIBLE;

public class Calculator extends AppCompatActivity {

    private TextView rateNumberTV;
    private SeekBar rateNumberSB;
    private EditText loanValueET;
    private TextView selectLocationTV;
    private Button goMap;
    private LoanContext context;

    private TextView transportCostTV;
    private TextView percentageTV;
    private TextView creditCostTV;
    private TextView rrsoTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        context = (LoanContext)getApplicationContext();
        init();
    }

    private void init() {
        rateNumberTV = findViewById(R.id.rate_number_value);
        rateNumberSB = findViewById(R.id.rate_number_sb);
        loanValueET = findViewById(R.id.loan_value_et);
        selectLocationTV = findViewById(R.id.select_location_on_calc_tv);
        goMap = findViewById(R.id.go_map_button);

        transportCostTV = findViewById(R.id.transport_cost_value_tv);
        percentageTV = findViewById(R.id.percentage_value_tv);
        creditCostTV = findViewById(R.id.total_credit_value_tv);
        rrsoTV = findViewById(R.id.rrso_value_tv);

        String distance = getIntent().getStringExtra("DISTANCE_KEY");
        String isFree = getIntent().getStringExtra("IS_FREE");
        if (distance != null) {
            if (Boolean.valueOf(isFree)) {
                context.setDistance(Integer.valueOf(0));
            } else {
                context.setDistance(Integer.valueOf(distance));
            }
            percentageTV.setText("5 %");
            selectLocationTV.setText("Distance from our office : " + distance + " m");
            goMap.setVisibility(View.INVISIBLE);
            findViewById(R.id.calculation_layout).setVisibility(VISIBLE);
            updateCalc();
        }

        fillFromContext();

        rateNumberSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                rateNumberTV.setText(i + 1 + " rates");
                context.setRates(i);
                updateCalc();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        loanValueET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()!=0) {
                    context.setCredit(Double.valueOf(charSequence.toString()));
                    updateCalc();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void changeActivityToMap(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void fillFromContext() {
        if (context.getRates() != 0) {
            rateNumberSB.setProgress(context.getRates());
            rateNumberTV.setText(context.getRates() + 1 + " rates");
        }

        if (context.getCredit() != 0) {
            loanValueET.setText(String.valueOf(context.getCredit()));
        }
    }

    private void updateCalc() {
            double percentage = 0.05;
            int transportCost = context.getDistance() / 4;
            transportCostTV.setText(transportCost + " $");

            double credit = context.getCredit();
            int rates = context.getRates() + 1;
            double q = 1 + (percentage/12.0);
            int creditCost = (int)(credit * Math.pow(q, rates)*((q-1)/(Math.pow(q,rates)-1))*rates);
            creditCost += transportCost;
            creditCostTV.setText(creditCost + " $");

            double rrso = Math.round((creditCost - credit)/credit * 100 * 100);
            rrso /= 100;
            rrsoTV.setText(rrso + " %");
    }

    public void resign(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        context.setDistance(0);
        context.setCredit(0);
        context.setRrso(0);
        context.setRates(0);
        startActivity(intent);
    }

    public void takeLoan(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        context.setDistance(0);
        context.setCredit(0);
        context.setRrso(0);
        context.setRates(0);
        startActivity(intent);
    }

}
