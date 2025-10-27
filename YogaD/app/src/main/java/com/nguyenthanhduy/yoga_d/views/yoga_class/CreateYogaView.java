package com.nguyenthanhduy.yoga_d.views.yoga_class;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthanhduy.yoga_d.R;
import com.nguyenthanhduy.yoga_d.adapter.UnitArrayAdapter;
import com.nguyenthanhduy.yoga_d.constant.ViewResultCode;
import com.nguyenthanhduy.yoga_d.databinding.ViewCreateYogaBinding;
import com.nguyenthanhduy.yoga_d.utils.KeyboardUtil;
import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.ArrayList;

public class CreateYogaView extends AppCompatActivity {
    private YogaClass yogaClass;
    private AlertDialog dayOfWeekDialog;
    private ArrayAdapter<UnitArrayAdapter<Integer>> dayOfWeekAdapter;
    private TimePickerDialog timePickerDialog;
    private AlertDialog typeOfClassDialog;
    private String[] typeOfClassData;
    private int dayOfWeek;
    private boolean isUpdate = false;
    private ViewCreateYogaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewCreateYogaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initDayOfWeekDialog();
        initTimePickerDialog();
        initTypeOfClassDialog();

        listener();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPress();
            }
        });

        yogaClass = (YogaClass) getIntent().getSerializableExtra(YogaClass.class.getSimpleName());
        if (yogaClass != null) {
            isUpdate = true;
            dayOfWeek = yogaClass.dayOfWeek;
            binding.txtTitle.setText("Update Yoga Class");

            bindUI();
        }
    }

    private void bindUI() {
        binding.txtTypeOfClass.getEditText().setText(yogaClass.typeOfClass);
        binding.txtDes.getEditText().setText(yogaClass.description);
        binding.txtName.getEditText().setText(yogaClass.yogaName);
        binding.txtCapacity.getEditText().setText(String.valueOf(yogaClass.capacity));
        binding.txtPrice.getEditText().setText(String.valueOf(yogaClass.getPrice()));
        binding.txtDayOfWeek.getEditText().setText(yogaClass.getDayOfWeekString());
        binding.txtTimeOfCourse.getEditText().setText(yogaClass.timeOfCourse);
        binding.txtDuration.getEditText().setText(String.valueOf(yogaClass.getDuration()));
    }

    private void listener() {
        binding.txtDayOfWeek.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayOfWeekDialog.show();
            }
        });

        binding.txtTimeOfCourse.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });

        binding.txtTypeOfClass.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfClassDialog.show();
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveYogaClass();
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private void initTimePickerDialog() {
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d:%02d", hourOfDay, minute);
                binding.txtTimeOfCourse.getEditText().setText(time);
            }
        }, 0, 0, true);
    }

    private void initDayOfWeekDialog() {
        String[] dayOfWeekData = getResources().getStringArray(R.array.day_of_week);
        dayOfWeekAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        dayOfWeekAdapter.notifyDataSetChanged();
        for (int j = 0; j < dayOfWeekData.length; j++) {
            dayOfWeekAdapter.add(new UnitArrayAdapter<>(dayOfWeekData[j], j + 1));
        }

        dayOfWeekDialog = new AlertDialog.Builder(this)
                .setTitle("Day of week")
                .setAdapter(dayOfWeekAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UnitArrayAdapter<Integer> elementDayOfWeek = dayOfWeekAdapter.getItem(which);
                        binding.txtDayOfWeek.getEditText().setText(elementDayOfWeek.getLabel());
                        dayOfWeek = elementDayOfWeek.getValue();
                    }
                })
                .create();
    }

    private void initTypeOfClassDialog() {
        typeOfClassData = getResources().getStringArray(R.array.yoga_class_type);
        typeOfClassDialog = new AlertDialog.Builder(this)
                .setTitle("Type Of Class")
                .setItems(typeOfClassData, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.txtTypeOfClass.getEditText().setText(typeOfClassData[which]);
                    }
                }).create();
    }

    private void saveYogaClass() {
        KeyboardUtil.HideKeyBoard(this);
        if (!CheckYogaClassData()) {
            return;
        }
        if (!isUpdate) {
            yogaClass = new YogaClass();
        }

        yogaClass.dayOfWeek = dayOfWeek;
        yogaClass.yogaName = binding.txtName.getEditText().getText().toString();
        yogaClass.setPrice(Double.parseDouble(binding.txtPrice.getEditText().getText().toString()));
        yogaClass.setDuration(Integer.parseInt(binding.txtDuration.getEditText().getText().toString()));
        yogaClass.typeOfClass = binding.txtTypeOfClass.getEditText().getText().toString();
        yogaClass.description = binding.txtDes.getEditText().getText().toString();
        yogaClass.timeOfCourse = binding.txtTimeOfCourse.getEditText().getText().toString();
        yogaClass.capacity = Integer.parseInt(binding.txtCapacity.getEditText().getText().toString());
        displayConfirmSave(yogaClass);
    }

    private void displayConfirmSave(YogaClass yogaClass) {
        new AlertDialog.Builder(this)
                .setTitle("Yoga Class information")
                .setMessage(yogaClass.toString())
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra(YogaClass.class.getSimpleName(), yogaClass);
                        int resultCode = isUpdate ? ViewResultCode.EDIT : ViewResultCode.ADD;
                        setResult(resultCode, intent);
                        finish();
                    }
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    private boolean CheckYogaClassData() {
        boolean isGoodData = true;
        
        if (binding.txtDayOfWeek.getEditText().getText().toString().isEmpty()) {
            binding.txtDayOfWeek.setError("Choose day of week");
            isGoodData = false;
        } else {
            binding.txtDayOfWeek.setError(null);
        }

        if (binding.txtName.getEditText().getText().toString().isEmpty()) {
            binding.txtName.setError("Enter name");
            isGoodData = false;
        } else {
            binding.txtName.setError(null);
        }

        if (binding.txtCapacity.getEditText().getText().toString().isEmpty()) {
            binding.txtCapacity.setError("Enter capacity");
            isGoodData = false;
        } else {
            binding.txtCapacity.setError(null);
        }


        if (binding.txtTimeOfCourse.getEditText().getText().toString().isEmpty()) {
            binding.txtTimeOfCourse.setError("Choose time of course");
            isGoodData = false;
        } else {
            binding.txtTimeOfCourse.setError(null);
        }


        if (binding.txtPrice.getEditText().getText().toString().isEmpty()) {
            binding.txtPrice.setError("Enter price");
            isGoodData = false;
        } else {
            binding.txtPrice.setError(null);
        }

        if (binding.txtTypeOfClass.getEditText().getText().toString().isEmpty()) {
            binding.txtTypeOfClass.setError("Choose type of class");
            isGoodData = false;
        } else {
            binding.txtTypeOfClass.setError(null);
        }

        if (binding.txtDuration.getEditText().getText().toString().isEmpty()) {
            binding.txtDuration.setError("Enter duration");
            isGoodData = false;
        } else {
            binding.txtDuration.setError(null);
        }


        return isGoodData;
    }

    private void onBackPress() {
        finish();
    }
}