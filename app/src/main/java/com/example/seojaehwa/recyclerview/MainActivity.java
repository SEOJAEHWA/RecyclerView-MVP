package com.example.seojaehwa.recyclerview;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.seojaehwa.recyclerview.adapter.RepoListAdapter;
import com.example.seojaehwa.recyclerview.data.repository.RepoDataSource;
import com.example.seojaehwa.recyclerview.data.repository.RepoRepository;
import com.example.seojaehwa.recyclerview.presenter.RepoListContract;
import com.example.seojaehwa.recyclerview.presenter.RepoListPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements RepoListContract.View {

    private RepoListContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RepoListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize logger(https://github.com/orhanobut/logger)
        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder()
                .tag("SEOJAEHWA")
                .build()) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
//        mRecyclerView.setItemAnimator(new SlideInUpAnimator(new AccelerateDecelerateInterpolator()));
//        if (mRecyclerView.getItemAnimator() != null) {
//            mRecyclerView.getItemAnimator().setAddDuration(500);
//            mRecyclerView.getItemAnimator().setRemoveDuration(500);
//        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        RepoRepository repository = RepoRepository.getInstance(RepoDataSource.INSTANCE);
        mPresenter = new RepoListPresenter(this, repository);

        mAdapter = new RepoListAdapter();
        mPresenter.setAdapterView(mAdapter);
        mPresenter.setAdapterModel(mAdapter);

        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    mPresenter.searchRepoMore(visibleItemCount, lastVisibleItemPosition,
                            totalItemCount);
                }
            });
        }

        // Request Repo data for initializing!
        mPresenter.searchRepo("Android");
    }

    @Override
    public void setPresenter(RepoListPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
