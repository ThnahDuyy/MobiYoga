package com.nguyenthanhduy.yoga_d.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.nguyenthanhduy.yoga_d.databinding.ViewLoginBinding;
import com.nguyenthanhduy.yoga_d.utils.KeyboardUtil;
import com.nguyenthanhduy.yoga_d.utils.NetworkUtil;
import com.nguyenthanhduy.yoga_d.views.yoga_class.YogaView;

public class LoginView extends AppCompatActivity {
    private ViewLoginBinding bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        bind = ViewLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        listener();
    }

    private void listener() {
        bind.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

        bind.txtUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    bind.txtUsername.setError(null);
                }
            }
        });

        bind.txtPass.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    bind.txtPass.setError(null);
                }
            }
        });
    }

    private void checkLogin() {
        KeyboardUtil.HideKeyBoard(this);

        if (!NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "internet unavailable!", Toast.LENGTH_LONG).show();
            return;
        }

        if (!checkLoginData()) {
            return;
        }

        bind.progressBar.setVisibility(View.VISIBLE);
        String username = bind.txtUsername.getEditText().getText().toString();
        String password = bind.txtPass.getEditText().getText().toString();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("user")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    new Handler().postDelayed(() -> {
                        bind.progressBar.setVisibility(View.GONE);
                        if (task.getResult().isEmpty()) {
                            bind.txtPass.setError("Username or password is incorrect");
                        } else {
                            startActivity(new Intent(this, YogaView.class));
                            finish();
                        }
                    }, 500);
                });
    }

    private boolean checkLoginData() {
        boolean isValid = true;
        if (bind.txtUsername.getEditText().getText().toString().isEmpty()) {
            bind.txtUsername.setError("Username is required");
            isValid = false;
        }
        if (bind.txtPass.getEditText().getText().toString().isEmpty()) {
            bind.txtPass.setError("Password is required");
            isValid = false;
        }

        return isValid;
    }
}