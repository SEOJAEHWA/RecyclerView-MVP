package com.example.seojaehwa.recyclerview.adapter;

import android.view.ViewGroup;

import com.example.seojaehwa.recyclerview.RepoViewHolder;
import com.example.seojaehwa.recyclerview.adapter.Presenter.RepoAdapterContract;
import com.example.seojaehwa.recyclerview.api.NetworkState;
import com.example.seojaehwa.recyclerview.data.Repo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class RepoListAdapter extends ListAdapter<Repo, RecyclerView.ViewHolder>
        implements RepoAdapterContract.View, RepoAdapterContract.Model {

    public RepoListAdapter() {
        super(DIFF);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RepoViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RepoViewHolder) {
            ((RepoViewHolder) holder).bind(getItem(position));
        }
    }

    @Override
    public void setNetworkState(@Nullable NetworkState state) {

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
