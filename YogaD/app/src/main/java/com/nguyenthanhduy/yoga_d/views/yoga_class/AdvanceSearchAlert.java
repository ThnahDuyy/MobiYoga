package com.nguyenthanhduy.yoga_d.views.yoga_class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.nguyenthanhduy.yoga_d.R;
import com.nguyenthanhduy.yoga_d.adapter.UnitArrayAdapter;
import com.nguyenthanhduy.yoga_d.databinding.AlertAdvanceSearchBinding;
import com.nguyenthanhduy.yoga_d.utils.DateTimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class AdvanceSearchAlert extends Dialog {
    private final AlertAdvanceSearchBinding bind;
    private AdvanceSearchEvent listener;
    private final FragmentManager fragmentManager;
    private DateTimePicker dateTimePicker;
    private AlertDialog dayOfWeekDialog;
    private final Context context;
    private String[] dayOfWeekArray;
    private ArrayAdapter<UnitArrayAdapter<Integer>> dayOfWeekAdapter;

    public AdvanceSearchAlert(@NonNull Context context, FragmentManager fragmentManager) {
        super(context);
        bind = AlertAdvanceSearchBinding.inflate(LayoutInflater.from(context));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(bind.getRoot());
        this.fragmentManager = fragmentManager;
        this.context = context;
        initView();
    }

    private void initView() {
        InitDateTimePicker();
        InitDayOfWeekDialog();
        bind.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dayOfWeek = (int) bind.txtDayOfWeek.getEditText().getTag();
                String date = bind.txtDate.getEditText().getText().toString();
                listener.onSearch(dayOfWeek, date, AdvanceSearchAlert.this);
            }
        });

        bind.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind.txtDate.getEditText().setText("");
                bind.txtDayOfWeek.getEditText().setText("");
                bind.txtDayOfWeek.getEditText().setTag(-1);
                listener.onClose(AdvanceSearchAlert.this);
            }
        });
    }

    public void setAdvanceSearchEventListener(AdvanceSearchEvent listener) {
        this.listener = listener;
    }

    private void InitDateTimePicker() {
        dateTimePicker = new DateTimePicker(fragmentManager, DateTimePicker.D_M_Y);
        dateTimePicker.setOnPicked(new DateTimePicker.DateTimePickerEvent() {
            @Override
            public void onDatePicked(Calendar calendar, String dateTimeFormat) {
                bind.txtDate.getEditText().setText(dateTimeFormat);
            }
        });
        bind.txtDate.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker.show();
            }
        });
    }

    private void InitDayOfWeekDialog() {
        String[] dayOfWeekArr = this.getContext().getResources().getStringArray(R.array.day_of_week);
        dayOfWeekAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        dayOfWeekAdapter.notifyDataSetChanged();
        for (int j = 0; j < dayOfWeekArr.length; j++) {
            dayOfWeekAdapter.add(new UnitArrayAdapter<>(dayOfWeekArr[j], j + 1));
        }

        dayOfWeekDialog = new AlertDialog.Builder(this.getContext())
                .setTitle("Day of week")
                .setAdapter(dayOfWeekAdapter, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UnitArrayAdapter<Integer> item_day_of_week = dayOfWeekAdapter.getItem(which);
                        bind.txtDayOfWeek.getEditText().setText(item_day_of_week.getLabel());
                        bind.txtDayOfWeek.getEditText().setTag(item_day_of_week.getValue());
                    }
                })
                .create();
        bind.txtDayOfWeek.getEditText().setTag(-1);
        bind.txtDayOfWeek.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeekDialog.show();
            }
        });
    }


    public interface AdvanceSearchEvent {
        public void onSearch(int dayOfWeek, String date, Dialog dialog);

        public void onClose(Dialog dialog);
    }
}
