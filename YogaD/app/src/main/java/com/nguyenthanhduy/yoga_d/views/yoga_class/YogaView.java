package com.nguyenthanhduy.yoga_d.views.yoga_class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nguyenthanhduy.yoga_d.adapter.IAdapterEvent;
import com.nguyenthanhduy.yoga_d.adapter.YogaClassAdapter;
import com.nguyenthanhduy.yoga_d.constant.ViewResultCode;
import com.nguyenthanhduy.yoga_d.database.Database;
import com.nguyenthanhduy.yoga_d.database.YogaClassDB;
import com.nguyenthanhduy.yoga_d.databinding.ViewYogaBinding;
import com.nguyenthanhduy.yoga_d.model.YogaClass;
import com.nguyenthanhduy.yoga_d.views.option.OptionView;

import java.util.ArrayList;

public class YogaView extends AppCompatActivity {
    private ViewYogaBinding bind;
    private YogaClassAdapter yogaAdapter;
    private YogaClassDB yogaClassDB;
    private final Handler searchTeacherNameDebounce = new Handler(Looper.getMainLooper());
    private Runnable searchTeacherNameRunnable;
    private AdvanceSearchAlert advanceSearchAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = ViewYogaBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        yogaClassDB = Database.GetInstance(this).getYogaClassRepository();
        initAdvanceSearchDialog();
        listener();
        initYogaRecyclerView();
        yogaAdapter.updateDataList(yogaClassDB.getAll());
    }

    private void initAdvanceSearchDialog() {
        advanceSearchAlert = new AdvanceSearchAlert(this, this.getSupportFragmentManager());
        advanceSearchAlert.setAdvanceSearchEventListener(new AdvanceSearchAlert.AdvanceSearchEvent() {
            @Override
            public void onSearch(int dayOfWeek, String date, Dialog dialog) {
                yogaAdapter.updateDataList(yogaClassDB.advanceSearch(dayOfWeek, date));
                dialog.dismiss();
            }

            @Override
            public void onClose(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    private void initYogaRecyclerView() {
        yogaAdapter = new YogaClassAdapter(new ArrayList<>());
        yogaAdapter.setAdapterEvent(new IAdapterEvent<YogaClass>() {
            @Override
            public void onObjectClicked(YogaClass model) {
                Intent intent = new Intent(YogaView.this, YogaDetailView.class);
                intent.putExtra(YogaClass.class.getSimpleName(), model);
                yogaStartView.launch(intent);
            }

            @Override
            public void onEditClicked(YogaClass model) {
                Intent intent = new Intent(YogaView.this, CreateYogaView.class);
                intent.putExtra(YogaClass.class.getSimpleName(), model);
                yogaStartView.launch(intent);
            }

            @Override
            public void onRemoveClicked(String id) {
                displayConfirmDeleteYogaClass(id);
            }
        });
        bind.yogaRcv.setAdapter(yogaAdapter);
        bind.yogaRcv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayConfirmDeleteYogaClass(String id) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to remove this yoga class?")
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    yogaAdapter.remove(id);
                    yogaClassDB.remove(id);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }


    private void listener() {
        bind.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yogaStartView.launch(new Intent(YogaView.this, CreateYogaView.class));
            }
        });

        bind.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yogaAdapter.updateDataList(yogaClassDB.getAll());
            }
        });

        bind.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yogaStartView.launch(new Intent(YogaView.this, OptionView.class));
            }
        });

        bind.btnAdvanceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceSearchAlert.show();
            }
        });

        bind.txtSearchTeacher.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTeacherNameDebounce.removeCallbacks(searchTeacherNameRunnable);
                searchTeacherNameRunnable = () -> {
                    if (s.toString().isEmpty()) {
                        yogaAdapter.updateDataList(yogaClassDB.getAll());
                    } else {
                        yogaAdapter.updateDataList(yogaClassDB.search(s.toString()));
                    }
                };
                searchTeacherNameDebounce.postDelayed(searchTeacherNameRunnable, 700);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private final ActivityResultLauncher<Intent> yogaStartView = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            response -> {
                int viewResultCode = response.getResultCode();
                if (ViewResultCode.DELETE_ALL_DATA == viewResultCode) {
                    Database.GetInstance(this).getClassInstanceRepository().clearData();
                    yogaClassDB.clearData();
                    yogaAdapter.updateDataList(new ArrayList<>());
                    return;
                }
                Intent data = response.getData();
                if (data == null) {
                    return;
                }
                YogaClass yogaClass = (YogaClass) data.getSerializableExtra(YogaClass.class.getSimpleName());
                if (yogaClass == null) {
                    return;
                }

                if (ViewResultCode.ADD == viewResultCode) {
                    yogaAdapter.add(yogaClass);
                    yogaClassDB.add(yogaClass);
                } else if (ViewResultCode.EDIT == viewResultCode) {
                    yogaAdapter.update(yogaClass);
                    yogaClassDB.update(yogaClass);
                }
            }
    );
}