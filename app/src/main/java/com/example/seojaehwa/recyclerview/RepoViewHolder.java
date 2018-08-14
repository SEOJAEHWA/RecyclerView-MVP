package com.example.seojaehwa.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.seojaehwa.recyclerview.data.Repo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RepoViewHolder extends RecyclerView.ViewHolder {

    private Repo mRepo;

    private TextView name;
    private TextView description;
    private TextView stars;
    private TextView language;
    private TextView forks;

    public static RepoViewHolder create(ViewGroup parent) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_view_item, parent, false);
        return new RepoViewHolder(view);
    }

    public void bind(@Nullable Repo repo) {
        if (repo != null) {
            this.mRepo = repo;
            name.setText(mRepo.getFullName());
            if (mRepo.getDescription() != null) {
                description.setText(mRepo.getDescription());
                description.setVisibility(View.VISIBLE);
            } else {
                description.setVisibility(View.GONE);
            }
            stars.setText(mRepo.getStars() == null ? "0": mRepo.getStars().toString());
            forks.setText(mRepo.getForks() == null ? "0": mRepo.getForks().toString());
            if (mRepo.getLanguage()!= null) {
                language.setText(itemView.getResources().getString(R.string.language,
                        mRepo.getLanguage()));
                language.setVisibility(View.VISIBLE);
            } else {
                language.setVisibility(View.GONE);
            }
        }
    }

    private RepoViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.repo_name);
        description = itemView.findViewById(R.id.repo_description);
        stars = itemView.findViewById(R.id.repo_stars);
        language = itemView.findViewById(R.id.repo_language);
        forks = itemView.findViewById(R.id.repo_forks);
    }
}
