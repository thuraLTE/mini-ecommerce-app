package com.example.hw_8.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.Staff;
import com.example.hw_8.util.Constants;
import com.google.android.material.textfield.TextInputLayout;

public class AdminAddStaffActivity extends AppCompatActivity {

    AppCompatButton btnAddNewStaff;
    TextView tvNewOrCurrentStaff, tvAddOrUpdateScreenMessage;
    TextInputLayout txtFieldName, txtFieldEmail, txtFieldNrc, txtFieldPassword, txtFieldConfirmPassword;
    RadioButton radioMale, radioFemale, radioOthers;
    ImageView ivBack;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_staff);

        initViews();
        if (getIntent().getSerializableExtra(Constants.CURRENT_STAFF_KEY) != null)
            initUpdateStaffEvents();
        else
            initAddStaffEvents();
    }

    private void initViews() {
        btnAddNewStaff = findViewById(R.id.btnAddNewStaff);

        tvNewOrCurrentStaff = findViewById(R.id.tvNewOrCurrentStaff);
        tvAddOrUpdateScreenMessage = findViewById(R.id.tvAddOrUpdateScreenMessage);

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

    private void initUpdateStaffEvents() {
        tvNewOrCurrentStaff.setText(R.string.current_staff);
        tvAddOrUpdateScreenMessage.setText(R.string.admin_update_staff_screen_message);
        btnAddNewStaff.setText(R.string.update);
        Staff staff = (Staff) getIntent().getSerializableExtra(Constants.CURRENT_STAFF_KEY);

        txtFieldName.getEditText().setText(staff.getName());
        txtFieldEmail.getEditText().setText(staff.getEmail());
        txtFieldNrc.getEditText().setText(staff.getNrc());
        txtFieldPassword.getEditText().setText(staff.getPassword());
        txtFieldConfirmPassword.getEditText().setText(staff.getPassword());

        btnAddNewStaff.setOnClickListener(view -> {

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
                txtFieldName.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldEmail.setErrorEnabled(true);
                txtFieldEmail.setError("Empty Email!");

            } else if (TextUtils.isEmpty(nrc)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldNrc.setErrorEnabled(true);
                txtFieldNrc.setError("Empty Nrc!");

            } else if (TextUtils.isEmpty(password)) {
                txtFieldNrc.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldPassword.setErrorEnabled(true);
                txtFieldPassword.setError("Empty Password!");

            } else if (TextUtils.isEmpty(confirmPassword)) {
                txtFieldPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);

                txtFieldConfirmPassword.setErrorEnabled(true);
                txtFieldConfirmPassword.setError("Empty Password!");

            } else {
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                Toast.makeText(this, "Staff Updated Successfully", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(this);
                databaseHelper.updateCurrentStaff(staff.getId(), name, email, nrc, gender, password);

                Intent intent = new Intent(this, AdminStaffListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
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

    private void initAddStaffEvents() {
        tvNewOrCurrentStaff.setText(R.string.new_staff);
        tvAddOrUpdateScreenMessage.setText(R.string.admin_add_staff_screen_message);
        btnAddNewStaff.setText(R.string.add_new_staff);

        btnAddNewStaff.setOnClickListener(view -> {

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
                txtFieldName.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldEmail.setErrorEnabled(true);
                txtFieldEmail.setError("Empty Email!");

            } else if (TextUtils.isEmpty(nrc)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldNrc.setErrorEnabled(true);
                txtFieldNrc.setError("Empty Nrc!");

            } else if (TextUtils.isEmpty(password)) {
                txtFieldNrc.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldPassword.setErrorEnabled(true);
                txtFieldPassword.setError("Empty Password!");

            } else if (TextUtils.isEmpty(confirmPassword)) {
                txtFieldPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);

                txtFieldConfirmPassword.setErrorEnabled(true);
                txtFieldConfirmPassword.setError("Empty Password!");

            } else {
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                Toast.makeText(this, "New Staff Added Successfully", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(this);
                databaseHelper.insertNewStaff(name, email, nrc, gender, password);

                Intent intent = new Intent(this, AdminStaffListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}