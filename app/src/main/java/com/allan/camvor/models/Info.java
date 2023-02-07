package com.allan.camvor.models;

public class Info {
    double Km;
    double Min;

    public Info() {
    }

    public Info(double km, double min) {
        this.Km = km;
        this.Min = min;
    }

    public double getKm() {
        return Km;
    }

    public void setKm(double km) {
        this.Km = km;
    }

    public double getMin() {
        return Min;
    }

    public void setMin(double min) {
        this.Min = min;
    }
}
