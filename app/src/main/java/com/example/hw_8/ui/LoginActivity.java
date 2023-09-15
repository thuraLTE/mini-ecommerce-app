package com.example.hw_8.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.LoginCredentials;
import com.example.hw_8.ui.admin.AdminDashboardActivity;
import com.example.hw_8.ui.staff.StaffDashboardActivity;
import com.example.hw_8.util.Constants;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    AppCompatButton btnLogin, btnRegister;
    TextInputLayout txtFieldEmail, txtFieldPassword;
    DatabaseHelper databaseHelper;
    ArrayList<LoginCredentials> loginCredentialsList = new ArrayList<>();
    ArrayList<LoginCredentials> loginCredentialsListTwo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        retrieveLoginCredentials();
        initEvents();
    }

    private void retrieveLoginCredentials() {
        databaseHelper = new DatabaseHelper(this);
        Cursor cursor = databaseHelper.readAllStaff();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                LoginCredentials loginCredentials = new LoginCredentials(
                        cursor.getString(Constants.STAFF_EMAIL_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_PASSWORD_COLUMN_POSITION));
                loginCredentialsList.add(loginCredentials);
                Log.d(TAG, "LoginCredentialsSize: " + loginCredentialsList.size());

                LoginCredentials loginCredentialsTwo = new LoginCredentials(
                        cursor.getInt(Constants.STAFF_ID_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_NAME_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_EMAIL_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_NRC_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_GENDER_COLUMN_POSITION),
                        cursor.getString(Constants.STAFF_PASSWORD_COLUMN_POSITION));
                loginCredentialsListTwo.add(loginCredentialsTwo);
            }
        }
        cursor.close();
    }

    private void initViews() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        txtFieldEmail = findViewById(R.id.txtFieldEmail);
        txtFieldPassword = findViewById(R.id.txtFieldPassword);
    }

    private void initEvents() {
        btnLogin.setOnClickListener(view -> {
            Log.d(TAG, "LoginCredentialsSize: " + loginCredentialsList.size());
            // Check Credentials & Move To Main Dashboard
            String emailEntry = txtFieldEmail.getEditText().getText().toString().trim();
            String passwordEntry = txtFieldPassword.getEditText().getText().toString().trim();

            if (TextUtils.isEmpty(emailEntry)) {
                txtFieldPassword.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(true);
                txtFieldEmail.setError("Empty Email!");

            } else if (TextUtils.isEmpty(passwordEntry)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(true);
                txtFieldPassword.setError("Empty Password!");

            } else if (emailEntry.equals(Constants.ADMIN_EMAIL) && passwordEntry.equals(Constants.ADMIN_PASSWORD)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                Toast.makeText(this, "Login success as admin", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();

            } else if (loginCredentialsList.isEmpty()) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                Toast.makeText(this, "This account does not exist yet! Register first", Toast.LENGTH_SHORT).show();

            } else {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                for (int i = 0; i < loginCredentialsList.size(); i++) {
                    if (emailEntry.equals(loginCredentialsList.get(i).getEmail()) && passwordEntry.equals(loginCredentialsList.get(i).getPassword())) {
                        Toast.makeText(this, "Login success as staff", Toast.LENGTH_SHORT).show();
                        // Retrieve Remaining Credentials
                        LoginCredentials currentLoginCredentials = loginCredentialsListTwo.get(i);

                        Intent intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                        intent.putExtra(Constants.CURRENT_LOGIN_CREDENTIALS_KEY, currentLoginCredentials);
                        startActivity(intent);
                        finish();
                    } else {
                        if (i == loginCredentialsList.size() - 1) {
                            if (!emailEntry.equals(loginCredentialsList.get(i).getEmail()) || !passwordEntry.equals(loginCredentialsList.get(i).getPassword()))
                                Toast.makeText(this, "This account does not exist yet! Register first", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btnRegister.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });
    }
}