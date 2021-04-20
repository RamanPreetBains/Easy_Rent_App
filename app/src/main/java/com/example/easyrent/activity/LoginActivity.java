package com.example.easyrent.activity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyrent.R;
import com.example.easyrent.extra.SessionData;
import com.google.android.libraries.places.api.Places;

public class LoginActivity extends BaseActivity {
    private TextView tvSignUp;
    private Button btnLogin;
    private EditText etPassword, etFirstName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        SessionData.I.init(getApplicationContext());

        tvSignUp = findViewById(R.id.tv_sign_up);
        btnLogin = findViewById(R.id.btn_login);
        etPassword = findViewById(R.id.et_password);
        etFirstName = findViewById(R.id.et_first_name);
        if (SessionData.I.isLogin()) {
            SessionData.I.goTo(this, HomeActivity.class);
            finish();
        }
    }

    @Override
    protected void initViewsWithData() {
        tvSignUp.setOnClickListener(v -> {
            SessionData.I.goTo(this, SignUpActivity.class);
        });
        btnLogin.setOnClickListener(v -> {
            if (isValidate()) {
                if (SessionData.I.localData.userList != null && !SessionData.I.localData.userList.isEmpty()) {
                    for (int i = 0; i < SessionData.I.localData.userList.size(); i++) {
                        if (SessionData.I.localData.userList.get(i).getFirstName().equals(etFirstName.getText().toString().trim())) {
                            if (SessionData.I.localData.userList.get(i).getPassword().equals(etPassword.getText().toString().trim())) {
                                SessionData.I.localData.currentUserData = SessionData.I.localData.userList.get(i);
                                SessionData.I.setLogin(true);
                                SessionData.I.saveLocalData();
                                SessionData.I.goTo(this, HomeActivity.class);
                                finish();
                            } else {
                                Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "User Name is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "There is no account register", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private boolean isValidate() {
        boolean isValid = true;
        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError("Field should not be empty");
            isValid = false;
        }
        if (etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setError("Field should not be empty");
            isValid = false;
        }
        return isValid;
    }
}