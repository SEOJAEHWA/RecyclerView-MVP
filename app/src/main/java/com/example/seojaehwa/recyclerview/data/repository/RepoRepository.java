package com.example.seojaehwa.recyclerview.data.repository;

import com.example.seojaehwa.recyclerview.BaseDataSource;
import com.example.seojaehwa.recyclerview.ListRepository;
import com.example.seojaehwa.recyclerview.SimpleDataSource;
import com.example.seojaehwa.recyclerview.api.GithubService;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

public final class RepoRepository extends ListRepository<Repo> {

    private static final int NETWORK_PAGE_SIZE = 10;
    private static final String IN_QUALIFIER = "in:name,description";

    private static RepoRepository INSTANCE;

    private IRepoDataSource mDataSource;

    private String mCurrentQueryString;

    private List<Pair<Integer, Repo>> mRestoreDataList;

    public static RepoRepository getInstance(IRepoDataSource dataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RepoRepository(dataSource);
        }
        return INSTANCE;
    }

    private RepoRepository(IRepoDataSource dataSource) {
        super();
        this.mDataSource = dataSource;
        mRestoreDataList = new ArrayList<>();
    }

    private GithubService getApi() {
        return GithubService.COMPANION.getApi();
    }

    public void getRepos(String queryString, @NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        mCurrentQueryString = queryString;
        final String apiQuery = queryString + IN_QUALIFIER;
        mDataSource.getRepos(getApi(), apiQuery, NETWORK_PAGE_SIZE,
                new SimpleDataSource.LoadData<List<Repo>>() {
                    @Override
                    public void onDataLoaded(List<Repo> data) {
                        initCachedDataList(data);
                        callback.onDataLoaded(getCachedList());
                    }

                    @Override
                    public void onNetworkState(@Nullable NetworkState state) {
                        callback.onNetworkState(state);
                    }
                });
    }

    public void getMoreRepos(@NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        mDataSource.getMoreRepos(getApi(), mCurrentQueryString, NETWORK_PAGE_SIZE,
                new SimpleDataSource.LoadData<List<Repo>>() {
                    @Override
                    public void onDataLoaded(List<Repo> data) {
                        addListToCachedDataList(data);
                        callback.onDataLoaded(getCachedList());
                    }

                    @Override
                    public void onNetworkState(@Nullable NetworkState state) {
                        callback.onNetworkState(state);
                    }
                });
    }

    public void restoreRepo(@NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        for (Pair<Integer, Repo> item : mRestoreDataList) {
            getCachedDataList().add(Objects.requireNonNull(item.first),
                    Objects.requireNonNull(item.second));
        }
        mRestoreDataList.clear();
        callback.onDataLoaded(getCachedList());
    }

    public void removeRepo(int position, @NonNull BaseDataSource.LoadData<List<Repo>> callback) {
        if (getCachedDataListSize() > position) {
            mRestoreDataList.clear();
            mRestoreDataList.add(new Pair<>(position, getCachedDataList().get(position)));
            getCachedDataList().remove(position);
        }
        callback.onDataLoaded(getCachedList());
    }
}
