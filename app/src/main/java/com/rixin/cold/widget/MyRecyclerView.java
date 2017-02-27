package com.rixin.cold.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rixin.cold.R;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;

/**
 * 封装RecyclerView
 * Created by 飘渺云轩 on 2017/211.
 */

public abstract class MyRecyclerView {

    private XRecyclerView mRecyclerView;
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

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.rv_list);

        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SemiCircleSpin);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(UIUtils.getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyRecyclerView.this.onItemClick(view, position);
            }
        }));

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                ThreadManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        MyRecyclerView.this.onRefresh();
                    }
                });
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                ThreadManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        MyRecyclerView.this.onLoadMore();
                    }
                });
                mRecyclerView.loadMoreComplete();
            }
        });
    }

    /**
     * RecyclerView的item点击回调，由子类实现
     * @param view
     * @param position
     */
    public abstract void onItemClick(View view, int position);

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
