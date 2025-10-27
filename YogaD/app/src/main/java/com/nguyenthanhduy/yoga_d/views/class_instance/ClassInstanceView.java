package com.nguyenthanhduy.yoga_d.views.class_instance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nguyenthanhduy.yoga_d.adapter.ClassInstanceAdapter;
import com.nguyenthanhduy.yoga_d.adapter.IAdapterEvent;
import com.nguyenthanhduy.yoga_d.constant.ViewResultCode;
import com.nguyenthanhduy.yoga_d.database.ClassInstanceDB;
import com.nguyenthanhduy.yoga_d.database.Database;
import com.nguyenthanhduy.yoga_d.databinding.ViewClassInstanceBinding;
import com.nguyenthanhduy.yoga_d.model.ClassInstance;
import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.ArrayList;

public class ClassInstanceView extends AppCompatActivity {
    private ViewClassInstanceBinding binding;
    private ClassInstanceAdapter classInstanceAdapter;
    private ClassInstanceDB classInstanceDB;
    private YogaClass yogaClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ViewClassInstanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        classInstanceDB = Database.GetInstance(this).getClassInstanceRepository();
        InitClassInstanceRecyclerView();

        listener();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPress();
            }
        });

        yogaClass = (YogaClass) getIntent().getSerializableExtra(YogaClass.class.getSimpleName());
        binding.txtDayOfWeek.setText(yogaClass.getDayOfWeekString());
        binding.txtYogaName.setText(yogaClass.yogaName);
        classInstanceAdapter.updateDataList(classInstanceDB.getByYogaId(yogaClass.yogaClassID));
    }

    private void onBackPress() {
        finish();
    }

    private void listener() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassInstanceView.this, CreateClassInstanceView.class);
                intent.putExtra(YogaClass.class.getSimpleName(), yogaClass);
                classInstanceStartView.launch(intent);
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPress();
            }
        });
    }

    private void InitClassInstanceRecyclerView() {
        classInstanceAdapter = new ClassInstanceAdapter(new ArrayList<>());
        classInstanceAdapter.setAdapterEvent(new IAdapterEvent<ClassInstance>() {
            @Override
            public void onObjectClicked(ClassInstance model) {

            }

            @Override
            public void onEditClicked(ClassInstance model) {
                Intent intent = new Intent(ClassInstanceView.this, CreateClassInstanceView.class);
                intent.putExtra(YogaClass.class.getSimpleName(), yogaClass);
                intent.putExtra(ClassInstance.class.getSimpleName(), model);
                classInstanceStartView.launch(intent);
            }

            @Override
            public void onRemoveClicked(String id) {
                displayConfirmDeleteClassInstance(id);
            }
        });
        binding.classInstanceRcv.setAdapter(classInstanceAdapter);
        binding.classInstanceRcv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayConfirmDeleteClassInstance(String id) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete")
                .setMessage("Remove this class instance?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        classInstanceDB.remove(id);
                        classInstanceAdapter.remove(id);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private final ActivityResultLauncher<Intent> classInstanceStartView = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data == null) {
                    return;
                }

                ClassInstance classInstance = (ClassInstance) data.getSerializableExtra(ClassInstance.class.getSimpleName());
                if (classInstance == null) {
                    return;
                }
                int resultCode = result.getResultCode();
                if (ViewResultCode.ADD == resultCode) {
                    classInstanceAdapter.add(classInstance);
                    classInstanceDB.add(classInstance);
                } else if (ViewResultCode.EDIT == resultCode) {
                    classInstanceAdapter.update(classInstance);
                    classInstanceDB.update(classInstance);
                }
            }
    );
}