package com.example.seojaehwa.recyclerview;


import com.example.seojaehwa.recyclerview.api.NetworkState;

import androidx.annotation.Nullable;


public class SimpleDataSource implements BaseDataSource {

    public static abstract class LoadData<T> implements BaseDataSource.LoadData<T> {

        @Override
        public void onNetworkState(@Nullable NetworkState state) {

        }
    }
}
