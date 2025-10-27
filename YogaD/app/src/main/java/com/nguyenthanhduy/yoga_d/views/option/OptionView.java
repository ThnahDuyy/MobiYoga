package com.nguyenthanhduy.yoga_d.views.option;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenthanhduy.yoga_d.MainView;
import com.nguyenthanhduy.yoga_d.databinding.ViewOptionBinding;
import com.nguyenthanhduy.yoga_d.services.FirebaseService;
import com.nguyenthanhduy.yoga_d.constant.ViewResultCode;
import com.nguyenthanhduy.yoga_d.database.ClassInstanceDB;
import com.nguyenthanhduy.yoga_d.database.Database;
import com.nguyenthanhduy.yoga_d.database.YogaClassDB;
import com.nguyenthanhduy.yoga_d.utils.NetworkUtil;
import com.nguyenthanhduy.yoga_d.model.ClassInstance;
import com.nguyenthanhduy.yoga_d.model.YogaClass;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OptionView extends AppCompatActivity {
    private ViewOptionBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bind = ViewOptionBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        listener();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void listener() {
        bind.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bind.btnSyncCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkUtil.isNetworkConnected(OptionView.this)) {
                    Toast.makeText(OptionView.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
                displayConfirmPushDataToCloud();
            }
        });
        bind.btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainView.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        bind.btnResetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayConfirmResetData();
            }
        });
    }

    private void displayConfirmPushDataToCloud() {
        new AlertDialog.Builder(this)
                .setTitle("confirm")
                .setMessage("Sync data to cloud?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        syncToCloud();
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

    private void syncToCloud() {
        bind.progressBar.setVisibility(View.VISIBLE);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            YogaClassDB yogaClassRepository = Database.GetInstance(this).getYogaClassRepository();
            ClassInstanceDB classInstanceRepository = Database.GetInstance(this).getClassInstanceRepository();

            List<YogaClass> yogaClassList = yogaClassRepository.getAll();
            List<ClassInstance> classInstanceList = classInstanceRepository.getAll();

            FirebaseService firebaseCloud = new FirebaseService(yogaClassList, classInstanceList,
                    new FirebaseService.FirebaseServiceEvent() {
                        @Override
                        public void success() {
                            runOnUiThread(() -> {
                                new Handler().postDelayed(() -> {
                                    bind.progressBar.setVisibility(View.GONE);
                                    Toast.makeText(OptionView.this, "success!", Toast.LENGTH_SHORT).show();
                                }, 500);
                            });
                        }

                        @Override
                        public void fail(String error) {
                            bind.progressBar.setVisibility(View.GONE);
                            Toast.makeText(OptionView.this, "fail!", Toast.LENGTH_SHORT).show();
                        }
                    });

            firebaseCloud.startSync();
        });
        executorService.shutdown();
    }

    private void displayConfirmResetData() {
        new AlertDialog.Builder(this)
                .setTitle("confirm")
                .setMessage("Reset data?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        YogaClassDB yogaClassRepository = Database.GetInstance(OptionView.this).getYogaClassRepository();
                        ClassInstanceDB classInstanceRepository = Database.GetInstance(OptionView.this).getClassInstanceRepository();
                        yogaClassRepository.clearData();
                        classInstanceRepository.clearData();
                        Toast.makeText(OptionView.this, "Reset data success!", Toast.LENGTH_LONG).show();
                        setResult(ViewResultCode.DELETE_ALL_DATA);
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
}