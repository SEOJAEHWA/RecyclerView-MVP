package com.example.seojaehwa.recyclerview.data;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;

public class Repo {

    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    @Nullable
    @SerializedName("description")
    private String description;

    @SerializedName("html_url")
    private String url;

    @SerializedName("stargazers_count")
    private int stars;

    @SerializedName("forks_count")
    private int forks;

    @SerializedName("language")
    private String language;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Integer getStars() {
        return stars;
    }

    public Integer getForks() {
        return forks;
    }

    public String getLanguage() {
        return language;
    }
}
