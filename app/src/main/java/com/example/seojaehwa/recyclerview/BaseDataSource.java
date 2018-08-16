package com.example.seojaehwa.recyclerview;


import com.example.seojaehwa.recyclerview.api.NetworkState;

import androidx.annotation.Nullable;


public interface BaseDataSource {

    interface LoadData<T> {

        void onDataLoaded(T data);

        void onNetworkState(@Nullable NetworkState state);
    }
}
