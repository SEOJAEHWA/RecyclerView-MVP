package com.example.seojaehwa.recyclerview.data;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class RepoSearchResponse {

    @SerializedName("total_count")
    private int total = 0;

    @SerializedName("items")
    private List<Repo> items = Collections.emptyList();

    private Integer nextPage = null;

    public int getTotal() {
        return total;
    }

    public List<Repo> getItems() {
        return items;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }
}
