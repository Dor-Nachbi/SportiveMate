package com.example.sportivemate.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Sports")
public class Sport implements Serializable {
    @PrimaryKey
    @NonNull
    private String name;
    private String imageUrl;
    private String description;
    long lastUpdated;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Sport() {
    }

    public Sport(@NonNull String name,@NonNull String description,@NonNull String imageUrl) {
        this.name = name;
        this.description=description;
        this.imageUrl = imageUrl;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
