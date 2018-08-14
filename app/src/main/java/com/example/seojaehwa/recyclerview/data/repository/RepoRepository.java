package com.example.seojaehwa.recyclerview.data.repository;

import com.example.seojaehwa.recyclerview.api.GithubService;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
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

    private String mCurrentQueryString;

    private List<Repo> mCachedDataList;

    public static RepoRepository getInstance(IRepoDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepoRepository(dataSource);
        }
        return INSTANCE;
    }

    private RepoRepository(IRepoDataSource dataSource) {
        this.mDataSource = dataSource;
        this.mCachedDataList = new ArrayList<>();
    }

    private GithubService getApi() {
        return GithubService.COMPANION.getApi();
    }

    public void getRepos(String queryString,
                         @NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        mCurrentQueryString = queryString;
        mLastRequestedPage = 1;
        requestRepoData(mCurrentQueryString, new BaseDataSource.LoadData<List<Repo>>() {
            @Override
            public void onDataLoaded(List<Repo> data) {
                mCachedDataList.clear();
                mCachedDataList.addAll(data);
                callback.onDataLoaded(mCachedDataList);
            }

            @Override
            public void onNetworkState(@Nullable NetworkState state) {
                callback.onNetworkState(state);
            }
        });
    }

    public void getMoreRepos(@NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        requestRepoData(mCurrentQueryString, new BaseDataSource.LoadData<List<Repo>>() {
            @Override
            public void onDataLoaded(List<Repo> data) {
                mCachedDataList.addAll(data);
                callback.onDataLoaded(mCachedDataList);
            }

            @Override
            public void onNetworkState(@Nullable NetworkState state) {
                callback.onNetworkState(state);
            }
        });
    }

    private void requestRepoData(String queryString,
                                 @NonNull final BaseDataSource.LoadData<List<Repo>> callback) {
        Logger.d("isRequestInProgress: " + isRequestInProgress);
        if (isRequestInProgress) {
            Logger.e("Still in progress -------------------");
            return;
        }
        isRequestInProgress = true;
        final String apiQuery = queryString + IN_QUALIFIER;
        mDataSource.getRepos(getApi(), apiQuery, mLastRequestedPage, NETWORK_PAGE_SIZE,
                new BaseDataSource.LoadData<List<Repo>>() {
                    @Override
                    public void onDataLoaded(List<Repo> data) {
                        isRequestInProgress = false;
                        mLastRequestedPage++;
                        callback.onDataLoaded(data);
                    }

                    @Override
                    public void onNetworkState(@Nullable NetworkState state) {
                        if (state == NetworkState.LOADING) {
                            return;
                        }
                        isRequestInProgress = false;
                        callback.onNetworkState(state);
                    }
                });
    }
}
