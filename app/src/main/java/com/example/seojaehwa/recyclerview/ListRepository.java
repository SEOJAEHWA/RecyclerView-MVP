package com.example.seojaehwa.recyclerview;

import java.util.ArrayList;
import java.util.List;

public class ListRepository<T> implements BaseDataSource {

    private List<T> mCachedDataList;

    protected ListRepository() {
        this.mCachedDataList = new ArrayList<>();
    }

    protected List<T> getInitializedCachedDataList(List<T> list) {
        mCachedDataList.clear();
        return getAddedCachedDataList(list);
    }

    protected List<T> getAddedCachedDataList(List<T> list) {
        mCachedDataList.addAll(list);
        return getCachedList();
    }

    protected List<T> getCachedList() {
        return mCachedDataList == null ? null : new ArrayList<>(mCachedDataList);
    }

    // FIXME: 2018-08-16 getCachedList method 와 혼선이 없도록 수정작업이 필요
    protected List<T> getCachedDataList() {
        return mCachedDataList;
    }

    protected int getCachedDataListSize() {
        return mCachedDataList == null ? -1 : mCachedDataList.size();
    }
}
