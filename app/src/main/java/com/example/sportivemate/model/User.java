package com.example.sportivemate.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    @NonNull
    private String id;
    @NonNull
    private String email;
    private String imageUrl;
    private String fullName;
    private long lastUpdated;

    public User(@NonNull String id, @NonNull String email) {
        this.id = id;
        this.email = email;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
