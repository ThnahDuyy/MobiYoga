package com.nguyenthanhduy.yoga_d.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public static String D_M_Y = "dd/MM/yyyy";
    public static String Y_M_D = "yyyy/MM/dd/";
    public static String M_D_Y = "MM/dd/yyyy";
    private String dateFormat;
    private DateTimePickerEvent dateTimePickerEventListener;
    private final FragmentManager fragmentManager;

    public DateTimePicker(FragmentManager fragmentManager, String dateFormat) {
        this.dateFormat = dateFormat;
        this.fragmentManager = fragmentManager;
    }

    public void show() {
        if (this.isAdded()) {
            return;
        }
        this.show(fragmentManager, "DateTimePicker");
    }

    public void setFormatDate(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setOnPicked(DateTimePickerEvent dateTimePickerEvent) {
        this.dateTimePickerEventListener = dateTimePickerEvent;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i1, i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.dateFormat, Locale.getDefault());
        String date_time_format = simpleDateFormat.format(calendar.getTime());
        dateTimePickerEventListener.onDatePicked(calendar, date_time_format);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }

    public interface DateTimePickerEvent {
        void onDatePicked(Calendar calendar, String dateTimeFormat);
    }
}
