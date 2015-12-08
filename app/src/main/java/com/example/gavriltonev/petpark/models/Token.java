package com.example.gavriltonev.petpark.models;

/**
 * Created by gavriltonev on 12/8/15.
 */
public class Token {
    private String AccessToken;
    private String ProfilePicURL;
    private String UserName;

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getProfilePicURL() {
        return ProfilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        ProfilePicURL = profilePicURL;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
