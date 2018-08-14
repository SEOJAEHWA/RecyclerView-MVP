package com.example.seojaehwa.recyclerview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.seojaehwa.recyclerview.api.NetworkState;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class NetworkStateViewHolder extends RecyclerView.ViewHolder {

    private final ProgressBar mProgressBar;
    private final Button mRetryButton;
    private final TextView mErrorMessageTextView;

    private NetworkStateViewHolder(@NonNull View itemView) {
        super(itemView);
        mProgressBar = itemView.findViewById(R.id.progress_bar);
        mRetryButton = itemView.findViewById(R.id.retry_button);
        mErrorMessageTextView = itemView.findViewById(R.id.error_msg);
    }

    public static NetworkStateViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.network_state_view_item, parent, false);
        return new NetworkStateViewHolder(view);
    }

    public void bind(@Nullable NetworkState networkState) {
        if (networkState == null) {
            return;
        }
        toVisibility(mProgressBar, networkState.getStatus() == NetworkState.Status.RUNNING);
        toVisibility(mRetryButton, networkState.getStatus() == NetworkState.Status.FAILED);
        toVisibility(mErrorMessageTextView, TextUtils.isEmpty(networkState.getMsg()));
    }

    private void toVisibility(@NonNull View view, boolean constraint) {
        view.setVisibility(constraint ? View.VISIBLE : View.GONE);
    }
}
