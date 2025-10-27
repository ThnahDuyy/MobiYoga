package com.nguyenthanhduy.yoga_d.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.nguyenthanhduy.yoga_d.utils.DateUtil;

import java.io.Serializable;
import java.util.Calendar;

public class YogaClass implements Serializable {
    public String yogaClassID;
    private double price;
    public int dayOfWeek;
    public String timeOfCourse;
    public String typeOfClass;
    public String description;
    public String yogaName;
    public int capacity;
    private int duration;

    public YogaClass() {
        yogaClassID = String.valueOf(Calendar.getInstance().getTime().getTime());
    }

    public YogaClass(String id) {
        this.yogaClassID = id;
    }


    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    @SuppressLint("DefaultLocale")
    public String priceString() {
        return String.format("%.2f %s", price, " £");
    }

    @SuppressLint("DefaultLocale")
    public String getDurationString() {
        return String.format("%d %s", this.duration, "mins");
    }

    public String getDayOfWeekString() {
        try {
            return DateUtil.dayOfWeekToString(dayOfWeek);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @NonNull
    public String toString() {
        String toString = "Name: " + yogaName + "\n" +
                "Capacity: " + capacity + "\n" +
                "Type of class: " + typeOfClass + "\n" +
                "Day of week: " + getDayOfWeekString() + "\n" +
                "Time of course: " + timeOfCourse + "\n" +
                "Price: " + priceString() + "\n" +
                "Duration: " + getDurationString() + "\n" +
                "Description: " + description;
        return toString;
    }
}
