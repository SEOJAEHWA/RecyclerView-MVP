package com.example.seojaehwa.recyclerview;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

public class ListRepository<T> {

    private List<T> mCachedDataList;

    protected ListRepository() {
        this.mCachedDataList = new ArrayList<>();
    }

    protected void initCachedDataList(List<T> list) {
        mCachedDataList.clear();
        addListToCachedDataList(list);
    }

    protected void addListToCachedDataList(List<T> list) {
        mCachedDataList.addAll(list);
    }

    protected List<T> getCachedList() {
        return mCachedDataList == null ? null : new ArrayList<>(mCachedDataList);
    }

    protected List<T> getCachedDataList() {
        return mCachedDataList;
    }

    protected int getCachedDataListSize() {
        return mCachedDataList == null ? -1 : mCachedDataList.size();
    }
}
