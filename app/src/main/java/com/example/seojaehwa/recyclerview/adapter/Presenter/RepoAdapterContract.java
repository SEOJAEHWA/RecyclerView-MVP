package com.example.seojaehwa.recyclerview.adapter.Presenter;

import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;

import java.util.List;

import androidx.annotation.Nullable;

public interface RepoAdapterContract {

    interface View {

        void setNetworkState(@Nullable NetworkState state);
    }

    interface Model {

        void submitRepoList(List<Repo> repoList);

    }
}
