package com.example.szopen.loancompanyapp;

import android.app.Application;

/**
 * Created by Szopen on 29.01.2018.
 */

public class LoanContext extends Application {
    double credit;
    int rates;
    int distance;
    int rrso;

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public int getRates() {
        return rates;
    }

    public void setRates(int rates) {
        this.rates = rates;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getRrso() {
        return rrso;
    }

    public void setRrso(int rrso) {
        this.rrso = rrso;
    }
}
