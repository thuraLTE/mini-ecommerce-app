package com.example.hw_8.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.LoginCredentials;
import com.example.hw_8.util.Constants;
import com.example.hw_8.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    AppCompatButton btnRegister;
    ImageView ivBack;
    TextInputLayout txtFieldName, txtFieldEmail, txtFieldNrc, txtFieldPassword, txtFieldConfirmPassword;
    RadioButton radioMale, radioFemale, radioOthers;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initEvents();
    }

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);

        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldEmail = findViewById(R.id.txtFieldEmail);
        txtFieldNrc = findViewById(R.id.txtFieldNrc);
        txtFieldPassword = findViewById(R.id.txtFieldPassword);
        txtFieldConfirmPassword = findViewById(R.id.txtFieldConfirmPassword);

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        radioOthers = findViewById(R.id.radioOthers);

        ivBack = findViewById(R.id.ivBack);
    }

    private String getInputFromGenderSelection() {
        String userSelection = null;
        if (radioMale.isChecked())
            userSelection = radioMale.getText().toString();
        else if (radioFemale.isChecked())
            userSelection = radioFemale.getText().toString();
        else if (radioOthers.isChecked())
            userSelection = radioOthers.getText().toString();
        return userSelection;
    }

    private void initEvents() {
        btnRegister.setOnClickListener(view -> {

            String name = txtFieldName.getEditText().getText().toString().trim();
            String email = txtFieldEmail.getEditText().getText().toString().trim();
            String nrc = txtFieldNrc.getEditText().getText().toString().trim();
            String gender = getInputFromGenderSelection();
            String password = txtFieldPassword.getEditText().getText().toString().trim();
            String confirmPassword = txtFieldConfirmPassword.getEditText().getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldName.setErrorEnabled(true);
                txtFieldName.setError("Empty Name!");

            } else if (TextUtils.isEmpty(email)) {
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);

                txtFieldEmail.setErrorEnabled(true);
                txtFieldEmail.setError("Empty Email!");

            } else if (TextUtils.isEmpty(nrc)) {
                txtFieldName.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);

                txtFieldNrc.setErrorEnabled(true);
                txtFieldNrc.setError("Empty Nrc!");

            } else if (TextUtils.isEmpty(password)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);

                txtFieldPassword.setErrorEnabled(true);
                txtFieldPassword.setError("Empty Password!");

            } else if (TextUtils.isEmpty(confirmPassword)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);

                txtFieldConfirmPassword.setErrorEnabled(true);
                txtFieldConfirmPassword.setError("Empty Password!");

            } else if (!password.equals(confirmPassword)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);

                txtFieldConfirmPassword.setErrorEnabled(true);
                txtFieldConfirmPassword.setError("Passwords Unmatched!");

            } else {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                Toast.makeText(this, "Registration Success", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(this);
                databaseHelper.insertNewStaff(name, email, nrc, gender, password);

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}