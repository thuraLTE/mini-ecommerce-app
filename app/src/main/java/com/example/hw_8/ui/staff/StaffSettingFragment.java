package com.example.hw_8.ui.staff;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.hw_8.R;
import com.example.hw_8.database.DatabaseHelper;
import com.example.hw_8.model.LoginCredentials;
import com.example.hw_8.ui.LoginActivity;
import com.example.hw_8.util.Constants;
import com.google.android.material.textfield.TextInputLayout;

public class StaffSettingFragment extends Fragment {

    AppCompatButton btnUpdate;
    ImageView ivLogout;
    TextInputLayout txtFieldName, txtFieldEmail, txtFieldNrc, txtFieldPassword, txtFieldConfirmPassword;
    RadioButton radioMale, radioFemale, radioOthers;
    LoginCredentials currentStaffCredentials;
    DatabaseHelper databaseHelper;

    public StaffSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        retrieveCurrentStaffCredentials();
        initEvents();
    }

    private void initViews(View view) {
        btnUpdate = view.findViewById(R.id.btnUpdate);
        ivLogout = view.findViewById(R.id.ivLogout);

        txtFieldName = view.findViewById(R.id.txtFieldName);
        txtFieldEmail = view.findViewById(R.id.txtFieldEmail);
        txtFieldNrc = view.findViewById(R.id.txtFieldNrc);
        txtFieldPassword = view.findViewById(R.id.txtFieldPassword);
        txtFieldConfirmPassword = view.findViewById(R.id.txtFieldConfirmPassword);

        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);
        radioOthers = view.findViewById(R.id.radioOthers);
    }

    private void retrieveCurrentStaffCredentials() {
        if (getArguments() != null) {
            currentStaffCredentials = (LoginCredentials) getArguments().getSerializable(Constants.CURRENT_LOGIN_CREDENTIALS_KEY);

            txtFieldName.getEditText().setText(currentStaffCredentials.getName());
            txtFieldEmail.getEditText().setText(currentStaffCredentials.getEmail());
            txtFieldNrc.getEditText().setText(currentStaffCredentials.getNrc());
            txtFieldPassword.getEditText().setText(currentStaffCredentials.getPassword());
            txtFieldConfirmPassword.getEditText().setText(currentStaffCredentials.getPassword());
        }
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
        btnUpdate.setOnClickListener(view -> {

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

            } else if (name.equals(currentStaffCredentials.getName())) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldName.setError("Your new name must not match your old one!");

            } else if (TextUtils.isEmpty(email)) {
                txtFieldName.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldEmail.setErrorEnabled(true);
                txtFieldEmail.setError("Empty Email!");

            } else if (email.equals(currentStaffCredentials.getEmail())) {
                txtFieldName.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldEmail.setError("Your new email must not match your old one!");

            } else if (TextUtils.isEmpty(nrc)) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldNrc.setErrorEnabled(true);
                txtFieldNrc.setError("Empty Nrc!");

            } else if (nrc.equals(currentStaffCredentials.getNrc())) {
                txtFieldEmail.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldNrc.setError("Your new nrc must not match your old one!");

            } else if (TextUtils.isEmpty(password)) {
                txtFieldNrc.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldPassword.setErrorEnabled(true);
                txtFieldPassword.setError("Empty Password!");

            } else if (password.equals(currentStaffCredentials.getPassword())) {
                txtFieldNrc.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                txtFieldPassword.setError("Your new password must not match your old one!");

            } else if (TextUtils.isEmpty(confirmPassword)) {
                txtFieldPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);

                txtFieldConfirmPassword.setErrorEnabled(true);
                txtFieldConfirmPassword.setError("Empty Password!");

            } else if (!password.equals(confirmPassword)) {
                txtFieldPassword.setErrorEnabled(false);
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);

                txtFieldConfirmPassword.setError("Passwords Unmatched!");

            } else {
                txtFieldName.setErrorEnabled(false);
                txtFieldEmail.setErrorEnabled(false);
                txtFieldNrc.setErrorEnabled(false);
                txtFieldPassword.setErrorEnabled(false);
                txtFieldConfirmPassword.setErrorEnabled(false);

                Toast.makeText(requireContext(), "Update Success", Toast.LENGTH_SHORT).show();

                databaseHelper = new DatabaseHelper(requireContext());
                databaseHelper.updateCurrentStaff(currentStaffCredentials.getId(), name, email, nrc, gender, password);

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        ivLogout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }
}