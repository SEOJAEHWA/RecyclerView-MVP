package com.example.seojaehwa.recyclerview.presenter;

import android.widget.AdapterView;

import com.example.seojaehwa.recyclerview.adapter.Presenter.RepoAdapterContract;

public interface RepoListContract {

    interface View {

        void setPresenter(RepoListPresenter presenter);
    }

    interface Presenter {

        void setAdapterView(RepoAdapterContract.View view);

        void setAdapterModel(RepoAdapterContract.Model model);

        void searchRepo(String queryString);

        void searchRepoMore(int visibleItemCount, int lastVisibleItemPosition, int totalItemCount);
    }
}
