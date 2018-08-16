package com.example.seojaehwa.recyclerview.presenter;

import com.example.seojaehwa.recyclerview.adapter.Presenter.RepoAdapterContract;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;
import com.example.seojaehwa.recyclerview.data.repository.RepoRepository;
import com.example.seojaehwa.recyclerview.SimpleDataSource;
import com.orhanobut.logger.Logger;

import java.util.List;

import androidx.annotation.Nullable;

public class RepoListPresenter implements RepoListContract.Presenter {

    private static final int VISIBLE_THRESHOLD = 5;

    private RepoListContract.View mView;
    private RepoAdapterContract.View mAdapterView;
    private RepoAdapterContract.Model mAdapterModel;
    private RepoRepository mRepository;

    public RepoListPresenter(RepoListContract.View view, RepoRepository repository) {
        this.mView = view;
        this.mRepository = repository;
        this.mView.setPresenter(this);
    }

    @Override
    public void searchRepo(String queryString) {
//        mAdapterModel.submitRepoList(null);
        mRepository.getRepos(queryString, new SimpleDataSource.LoadData<List<Repo>>() {
            @Override
            public void onDataLoaded(List<Repo> data) {
                mAdapterModel.submitRepoList(data);
            }
        });
    }

    @Override
    public void searchRepoMore(int visibleItemCount, int lastVisibleItemPosition, int totalItemCount) {
        if (totalItemCount > 0
                && visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            Logger.i("searchRepoMore: " + visibleItemCount
                    + " + " + lastVisibleItemPosition
                    + " + " + VISIBLE_THRESHOLD
                    + " : " + totalItemCount);

            mRepository.getMoreRepos(new SimpleDataSource.LoadData<List<Repo>>() {
                @Override
                public void onDataLoaded(List<Repo> data) {
                    mAdapterModel.submitRepoList(data);
                }

                @Override
                public void onNetworkState(@Nullable NetworkState state) {
//                    mView.setNetworkState(state);
                    mAdapterView.setNetworkState(state);
                }
            });
        }
    }

    @Override
    public void refreshRepos() {
//        mAdapterModel.submitRepoList(null);
        mRepository.refreshRepos(new SimpleDataSource.LoadData<List<Repo>>() {
            @Override
            public void onDataLoaded(List<Repo> data) {
                mAdapterModel.submitRepoList(data);
            }

            @Override
            public void onNetworkState(@Nullable NetworkState state) {
                mView.setRefreshState(state);
            }
        });
    }

    @Override
    public void removeRepo(int position) {
        mRepository.removeRepo(position, new SimpleDataSource.LoadData<List<Repo>>() {
            @Override
            public void onDataLoaded(List<Repo> data) {
                mAdapterModel.submitRepoList(data);
            }
        });
    }

    @Override
    public void restoreRepo() {
        mRepository.restoreRepo(new SimpleDataSource.LoadData<List<Repo>>() {
            @Override
            public void onDataLoaded(List<Repo> data) {
                mAdapterModel.submitRepoList(data);
            }
        });
    }

    @Override
    public void setAdapterView(RepoAdapterContract.View view) {
        this.mAdapterView = view;
    }

    @Override
    public void setAdapterModel(RepoAdapterContract.Model model) {
        this.mAdapterModel = model;
    }
}
