package com.example.seojaehwa.recyclerview.data.repository;

import com.example.seojaehwa.recyclerview.api.GithubService;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RepoRepository {

    private static final int NETWORK_PAGE_SIZE = 20;
    private static final String IN_QUALIFIER = "in:name,description";

    private static RepoRepository INSTANCE;

    private IRepoDataSource mDataSource;

    private boolean isRequestInProgress = false;
    private int mLastRequestedPage = 1;

    public static RepoRepository getInstance(IRepoDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepoRepository(dataSource);
        }
        return INSTANCE;
    }

    private RepoRepository(IRepoDataSource dataSource) {
        this.mDataSource = dataSource;
    }

    private GithubService getApi() {
        return GithubService.COMPANION.getApi();
    }

    public void getRepos(String queryString,
                         @NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        mLastRequestedPage = 1;
        requestRepoData(queryString, callback);
    }

    public void getMoreRepos(String queryString,
                             @NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        requestRepoData(queryString, callback);
    }

    private void requestRepoData(String queryString,
                                 @NonNull final BaseDataSource.LoadData<List<Repo>> callback) {
        if (isRequestInProgress) {
            return;
        }
        isRequestInProgress = true;
        final String apiQuery = queryString + IN_QUALIFIER;
        mDataSource.getRepos(getApi(), apiQuery, mLastRequestedPage, NETWORK_PAGE_SIZE,
                new BaseDataSource.LoadData<List<Repo>>() {
                    @Override
                    public void onDataLoaded(List<Repo> data) {
                        mLastRequestedPage++;
                        callback.onDataLoaded(data);
                        isRequestInProgress = false;
                    }

                    @Override
                    public void onNetworkState(@Nullable NetworkState state) {
                        callback.onNetworkState(state);
                        isRequestInProgress = false;
                    }
                });
    }
}
