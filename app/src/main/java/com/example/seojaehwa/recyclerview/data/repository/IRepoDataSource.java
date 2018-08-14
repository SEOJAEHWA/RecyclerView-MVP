package com.example.seojaehwa.recyclerview.data.repository;

import com.example.seojaehwa.recyclerview.api.GithubService;
import com.example.seojaehwa.recyclerview.data.Repo;

import java.util.List;

import androidx.annotation.NonNull;

public interface IRepoDataSource extends BaseDataSource {

    void getRepos(GithubService api, String queryString, int page, int itemsPerPage,
                        @NonNull LoadData<List<Repo>> callback);
}
