package com.example.seojaehwa.recyclerview.presenter;

import android.widget.AdapterView;

import com.example.seojaehwa.recyclerview.adapter.Presenter.RepoAdapterContract;

public interface RepoListContract {

    interface View {

        void setPresenter(RepoListPresenter presenter);
    }

    interface Presenter {

        void searchRepo(String queryString);

        void setAdapterView(RepoAdapterContract.View view);

        void setAdapterModel(RepoAdapterContract.Model model);
    }
}