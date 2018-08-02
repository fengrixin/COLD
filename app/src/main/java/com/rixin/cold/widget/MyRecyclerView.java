package com.rixin.cold.widget;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.nukc.LoadMoreWrapper.LoadMoreAdapter;
import com.github.nukc.LoadMoreWrapper.LoadMoreWrapper;
import com.rixin.cold.R;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;

/**
 * 封装RecyclerView
 * Created by 飘渺云轩 on 2017/211.
 */

public abstract class MyRecyclerView {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout srfRefresh;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private View view;

    public MyRecyclerView(RecyclerView.LayoutManager manager, RecyclerView.Adapter adapter) {
        this.manager = manager;
        this.adapter = adapter;
        initView();
    }

    private void initView() {
        view = UIUtils.inflate(R.layout.fragment_base);

        srfRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srf_refresh);
        srfRefresh.setColorSchemeColors(UIUtils.getColor(R.color.colorSplash));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);

        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setAdapter(adapter);

        srfRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ThreadManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        MyRecyclerView.this.onRefresh();
                        UIUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                srfRefresh.setRefreshing(false);
                            }
                        });
                    }
                });
            }
        });

        LoadMoreWrapper.with(adapter)
                .setShowNoMoreEnabled(true)

                .setListener(new LoadMoreAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(LoadMoreAdapter.Enabled enabled) {
                        ThreadManager.getThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                MyRecyclerView.this.onLoadMore();
                            }
                        });
                    }
                })
                .into(mRecyclerView);
    }

    /**
     * 刷新页面，由子类实现
     */
    public abstract void onRefresh();

    /**
     * 加载更多，由子类实现
     */
    public abstract void onLoadMore();

    public View getView() {
        return view;
    }

}
