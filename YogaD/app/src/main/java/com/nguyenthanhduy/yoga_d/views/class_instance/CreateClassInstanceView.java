package com.nguyenthanhduy.yoga_d.views.class_instance;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthanhduy.yoga_d.constant.ViewResultCode;
import com.nguyenthanhduy.yoga_d.databinding.ViewCreateClassInstanceBinding;
import com.nguyenthanhduy.yoga_d.utils.DateUtil;
import com.nguyenthanhduy.yoga_d.utils.DateTimePicker;
import com.nguyenthanhduy.yoga_d.utils.KeyboardUtil;
import com.nguyenthanhduy.yoga_d.model.ClassInstance;
import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.Calendar;

public class CreateClassInstanceView extends AppCompatActivity {
    private DateTimePicker dateTimePicker;
    private ViewCreateClassInstanceBinding bind;
    private Calendar calendar;
    private ClassInstance classInstance;
    private YogaClass yogaClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ViewCreateClassInstanceBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        initDateTimePicker();
        listener();

        yogaClass = (YogaClass) getIntent().getSerializableExtra(YogaClass.class.getSimpleName());
        classInstance = (ClassInstance) getIntent().getSerializableExtra(ClassInstance.class.getSimpleName());
        if (classInstance != null) {
            bind.txtTitle.setText("Edit Class Instance");
            bindUI();
        }
    }

    private void bindUI() {
        bind.txtDate.getEditText().setText(classInstance.date);
        bind.txtTeacher.getEditText().setText(classInstance.teacher);
        bind.txtCmt.getEditText().setText(classInstance.comment);
    }

    private void initDateTimePicker() {
        dateTimePicker = new DateTimePicker(getSupportFragmentManager(), DateTimePicker.D_M_Y);
        dateTimePicker.setOnPicked((calendar, dateTimeFormat) -> {
            bind.txtDate.getEditText().setText(dateTimeFormat);
            this.calendar = calendar;
        });
        bind.txtDate.getEditText().setOnClickListener(v -> {
            dateTimePicker.show();
        });
    }

    private void listener() {
        bind.back.setOnClickListener(v -> {
            finish();
        });
        bind.save.setOnClickListener(v -> {
            saveClassInstance();
        });
    }

    private void saveClassInstance() {
        KeyboardUtil.HideKeyBoard(this);
        if (!CheckClassInstanceData()) {
            return;
        }
        int resultCode = ViewResultCode.EDIT;
        if (classInstance == null) {
            classInstance = new ClassInstance();
            classInstance.yogaClassID = yogaClass.yogaClassID;
            resultCode = ViewResultCode.ADD;
        }

        classInstance.teacher = bind.txtTeacher.getEditText().getText().toString();
        classInstance.date = bind.txtDate.getEditText().getText().toString();
        classInstance.comment = bind.txtCmt.getEditText().getText().toString();
        Intent intent = new Intent();
        intent.putExtra(ClassInstance.class.getSimpleName(), classInstance);
        setResult(resultCode, intent);
        finish();
    }

    private boolean CheckClassInstanceData() {
        boolean is_good_data = true;
        String date = bind.txtDate.getEditText().getText().toString();
        if (date.isEmpty()) {
            bind.txtDate.setError("Invalid date");
            is_good_data = false;
        } else {
            if (calendar != null) {
                boolean valid_day_of_week = DateUtil.isValidDateOfDayOfWeek(calendar, yogaClass.dayOfWeek);
                if (!valid_day_of_week) {
                    bind.txtDate.setError("Invalid date");
                    is_good_data = false;
                }
            }
        }

        String teacher = bind.txtTeacher.getEditText().getText().toString();
        if (teacher.isEmpty()) {
            bind.txtTeacher.setError("Invalid Teacher");
            is_good_data = false;
        }
        return is_good_data;
    }
}