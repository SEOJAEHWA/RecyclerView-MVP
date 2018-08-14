package com.example.seojaehwa.recyclerview.data.repository;

import com.example.seojaehwa.recyclerview.api.GithubService;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;
import com.example.seojaehwa.recyclerview.data.RepoSearchResponse;

import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoDataSource implements IRepoDataSource {

    public static RepoDataSource INSTANCE = getInstance();

    private static RepoDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepoDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getRepos(GithubService api, String queryString, int page, int itemsPerPage,
                               @NonNull final LoadData<List<Repo>> callback) {
        callback.onNetworkState(NetworkState.LOADING);
        api.searchRepos(queryString, page, itemsPerPage).enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepoSearchResponse> call,
                                   @NonNull Response<RepoSearchResponse> response) {
                callback.onNetworkState(NetworkState.LOADED);
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onNetworkState(NetworkState.error("Response Body is null!"));
                        return;
                    }
                    List<Repo> repoList = response.body().getItems();
                    callback.onDataLoaded(repoList);
                } else {
                    callback.onNetworkState(NetworkState.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RepoSearchResponse> call,
                                  @NonNull Throwable t) {
                callback.onNetworkState(NetworkState.error(t.getMessage()));
            }
        });
    }
}
