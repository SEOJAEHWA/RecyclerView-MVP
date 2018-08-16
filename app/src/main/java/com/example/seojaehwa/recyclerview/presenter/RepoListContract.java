package com.example.seojaehwa.recyclerview.presenter;

import com.example.seojaehwa.recyclerview.adapter.Presenter.RepoAdapterContract;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;

import androidx.annotation.Nullable;

public interface RepoListContract {

    interface View {

        void setPresenter(RepoListPresenter presenter);

        void setNetworkState(@Nullable NetworkState state);
    }

    interface Presenter {

        void setAdapterView(RepoAdapterContract.View view);

        void setAdapterModel(RepoAdapterContract.Model model);

        void searchRepo(String queryString);

        void searchRepoMore(int visibleItemCount, int lastVisibleItemPosition, int totalItemCount);

        void removeRepo(int position);

        void restoreRepo();
    }
}
