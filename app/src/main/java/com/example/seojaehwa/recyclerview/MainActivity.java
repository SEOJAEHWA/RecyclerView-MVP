package com.example.seojaehwa.recyclerview;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.seojaehwa.recyclerview.adapter.RepoListAdapter;
import com.example.seojaehwa.recyclerview.api.NetworkState;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class MainActivity extends AppCompatActivity implements RepoListContract.View {

    private RepoListContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;
    private RepoListAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Logger.d("Do swiping!!!");
            // FIXME: 2018-08-16 뷰를 일단 지워냄?
//            clearBoardListView();
            mPresenter.refreshRepos();
        });
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
//        mRecyclerView.setItemAnimator(new SlideInUpAnimator(new AccelerateDecelerateInterpolator()));
//        if (mRecyclerView.getItemAnimator() != null) {
//            mRecyclerView.getItemAnimator().setAddDuration(200);
//            mRecyclerView.getItemAnimator().setRemoveDuration(200);
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
    public void setNetworkState(@Nullable NetworkState state) {

    }

    @Override
    public void setRefreshState(@Nullable NetworkState state) {
        if (state != NetworkState.LOADING) {
//            scrollListToTop();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
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
        switch (id) {
            case R.id.action_restore_item:
                mPresenter.restoreRepo();
                return true;
            case R.id.action_remove_item:
                mPresenter.removeRepo(1);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
