package com.example.gavriltonev.petpark.models;

/**
 * Created by gavriltonev on 12/8/15.
 */
public class Token {
    private String access_token;
    private String profilePicURL;
    private String userName;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

