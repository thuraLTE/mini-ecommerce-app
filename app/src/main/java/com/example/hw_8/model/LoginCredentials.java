package com.example.hw_8.model;

import java.io.Serializable;

public class LoginCredentials implements Serializable {
    int id;
    String name;
    String email;
    String nrc;
    String gender;
    String password;

    public LoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginCredentials(int id, String name, String email, String nrc, String gender, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nrc = nrc;
        this.gender = gender;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getNrc() {
        return nrc;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }
}
