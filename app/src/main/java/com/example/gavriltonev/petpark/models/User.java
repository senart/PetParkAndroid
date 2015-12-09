package com.example.gavriltonev.petpark.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by gavriltonev on 12/9/15.
 */
public class User {
    private String Email;
    private String Password;
    private String ConfirmPassword;

    public User(String email, String password, String confirmPassword) {
        this.Email = email;
        this.Password = password;
        this.ConfirmPassword = confirmPassword;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
