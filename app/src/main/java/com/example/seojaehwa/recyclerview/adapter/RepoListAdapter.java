package com.example.seojaehwa.recyclerview.adapter;

import android.view.ViewGroup;

import com.example.seojaehwa.recyclerview.NetworkStateViewHolder;
import com.example.seojaehwa.recyclerview.RepoViewHolder;
import com.example.seojaehwa.recyclerview.ViewType;
import com.example.seojaehwa.recyclerview.adapter.Presenter.RepoAdapterContract;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class RepoListAdapter extends ListAdapter<Repo, RecyclerView.ViewHolder>
        implements RepoAdapterContract.View, RepoAdapterContract.Model {

    private NetworkState mNetworkState;

    public RepoListAdapter() {
        super(DIFF);
    }

    private RecyclerView mCurrentRecyclerView;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mCurrentRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mCurrentRecyclerView = null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ViewType.ITEM_DEFAULT:
                return RepoViewHolder.create(parent);
            case ViewType.PROGRESS:
                return NetworkStateViewHolder.create(parent);
            default:
                throw new IllegalArgumentException("unknown view type viewType");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ViewType.ITEM_DEFAULT:
                if (holder instanceof RepoViewHolder) {
                    ((RepoViewHolder) holder).bind(getItem(position));
                }
                break;
            case ViewType.PROGRESS:
                if (holder instanceof NetworkStateViewHolder) {
                    ((NetworkStateViewHolder) holder).bind(mNetworkState);
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasProgressRow() && position == getItemCount() - 1) {
            return ViewType.PROGRESS;
        } else {
            return ViewType.ITEM_DEFAULT;
        }
    }

    private boolean hasProgressRow() {
        return mNetworkState != null && (mNetworkState != NetworkState.LOADED);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasProgressRow() ? 1 : 0);
    }

    @Override
    public void setNetworkState(@Nullable NetworkState state) {
        Logger.w("[Adapter::setNetworkState] " + state);
        mCurrentRecyclerView.post(() -> {
            NetworkState previousState = mNetworkState;
            boolean hadProgressRow = hasProgressRow();
            mNetworkState = state;
            boolean hasProgressRow = hasProgressRow();
            if (hadProgressRow != hasProgressRow) {
                if (hadProgressRow) {
                    notifyItemRemoved(RepoListAdapter.super.getItemCount());
                } else {
                    notifyItemInserted(RepoListAdapter.super.getItemCount());
                }
            } else if (hasProgressRow && (previousState != state)) {
                notifyItemChanged(getItemCount() - 1);
            }
        });
    }

    @Override
    public void submitRepoList(List<Repo> repoList) {
        super.submitList(repoList);
    }

    private static DiffUtil.ItemCallback<Repo> DIFF = new DiffUtil.ItemCallback<Repo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem.getFullName().equals(newItem.getFullName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
            return oldItem == newItem;
        }
    };
}
