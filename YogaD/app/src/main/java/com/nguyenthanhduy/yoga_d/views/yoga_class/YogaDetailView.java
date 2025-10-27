package com.nguyenthanhduy.yoga_d.views.yoga_class;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthanhduy.yoga_d.databinding.ViewYogaDetailBinding;
import com.nguyenthanhduy.yoga_d.model.YogaClass;
import com.nguyenthanhduy.yoga_d.views.class_instance.ClassInstanceView;

public class YogaDetailView extends AppCompatActivity {
    private ViewYogaDetailBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ViewYogaDetailBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        bindUI();

        bind.btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindUI() {
        YogaClass yogaClass = (YogaClass) getIntent().getSerializableExtra(YogaClass.class.getSimpleName());
        bind.txtName.setText(yogaClass.yogaName);
        bind.txtMember.setText(yogaClass.capacity + "");
        bind.txtTimeOfCourse.setText(yogaClass.timeOfCourse);
        bind.txtDayOfWeek.setText(yogaClass.getDayOfWeekString());
        bind.txtTypeOfClass.setText(yogaClass.typeOfClass);
        bind.txtDuration.setText(yogaClass.getDurationString());
        bind.txtDes.setText(yogaClass.description);
        bind.txtPrice.setText(yogaClass.priceString());
        bind.btnViewClassInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YogaDetailView.this, ClassInstanceView.class);
                intent.putExtra(YogaClass.class.getSimpleName(), yogaClass);
                startActivity(intent);
            }
        });
    }
}