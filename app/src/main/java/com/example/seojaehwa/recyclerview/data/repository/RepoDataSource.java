package com.example.seojaehwa.recyclerview.data.repository;

import com.example.seojaehwa.recyclerview.api.GithubService;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;
import com.example.seojaehwa.recyclerview.data.RepoSearchResponse;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Objects;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoDataSource implements IRepoDataSource {

    public static RepoDataSource INSTANCE = getInstance();

    private static RepoDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RepoDataSource();
        }
        return INSTANCE;
    }

    private final Object mKeyLock = new Object();

    @Nullable
    @GuardedBy("mKeyLock")
    private Integer mNextKey = null;

    private void setNextKey(@Nullable Integer nextKey) {
        synchronized (mKeyLock) {
            mNextKey = nextKey;
        }
    }

    private @Nullable
    Integer getNextKey() {
        synchronized (mKeyLock) {
            return mNextKey;
        }
    }

    private boolean isRequestInProgress = false;

    @Override
    public void getRepos(GithubService api, String queryString, int itemsPerPage,
                         @NonNull final LoadData<List<Repo>> callback) {
        Logger.i("[getRepos] queryString: " + queryString);
        isRequestInProgress = true;
        callback.onNetworkState(NetworkState.LOADING);
        setNextKey(1);
        Call<RepoSearchResponse> call = api.searchRepos(queryString,
                Objects.requireNonNull(getNextKey()), itemsPerPage);
        call.enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepoSearchResponse> call,
                                   @NonNull Response<RepoSearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onNetworkState(NetworkState.error("Response Body is null!"));
                        isRequestInProgress = false;
                        return;
                    }

                    // FIXME: nextKey 를 가져오는 방법은 케이스마다 다를것이며 더이상 받을 데이터가 없다면 null 로 세팅
                    Integer nextKey = getNextKey() == null ? null : (getNextKey() + 1);
                    setNextKey(nextKey);

                    List<Repo> repoList = response.body().getItems();
                    callback.onNetworkState(NetworkState.LOADED);
                    callback.onDataLoaded(repoList);
                } else {
                    callback.onNetworkState(NetworkState.error(response.message()));
                }
                isRequestInProgress = false;
            }

            @Override
            public void onFailure(@NonNull Call<RepoSearchResponse> call,
                                  @NonNull Throwable t) {
                callback.onNetworkState(NetworkState.error(t.getMessage()));
                isRequestInProgress = false;
            }
        });
    }

    @Override
    public void getMoreRepos(GithubService api, String queryString, int itemsPerPage,
                             @NonNull LoadData<List<Repo>> callback) {
        if (getNextKey() == null) {
            Logger.d("No more data to load ------------------------------------");
            return;
        }
        if (isRequestInProgress) {
            Logger.w("[WARNING] Still in progress ---------------------------");
            return;
        }
        Logger.i("[loadMoreBoards] nextKey: " + getNextKey());
        isRequestInProgress = true;
        callback.onNetworkState(NetworkState.LOADING);
        Call<RepoSearchResponse> call = api.searchRepos(queryString,
                Objects.requireNonNull(getNextKey()), itemsPerPage);
        call.enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepoSearchResponse> call,
                                   @NonNull Response<RepoSearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        callback.onNetworkState(NetworkState.error("Response Body is null!"));
                        isRequestInProgress = false;
                        return;
                    }

                    Integer nextKey = getNextKey() == null ? null : (getNextKey() + 1);
                    // 10 페이지 호출하면 stop to call!
                    if (nextKey != null && nextKey == 10) {
                        Logger.e("Stop to load data!! " + nextKey);
                        nextKey = null;
                    }
                    setNextKey(nextKey);

                    List<Repo> repoList = response.body().getItems();
                    callback.onDataLoaded(repoList);
                    callback.onNetworkState(NetworkState.LOADED);
                } else {
                    callback.onNetworkState(NetworkState.error(response.message()));
                }
                isRequestInProgress = false;
            }

            @Override
            public void onFailure(@NonNull Call<RepoSearchResponse> call,
                                  @NonNull Throwable t) {
                callback.onNetworkState(NetworkState.error(t.getMessage()));
                isRequestInProgress = false;
            }
        });
    }
}
