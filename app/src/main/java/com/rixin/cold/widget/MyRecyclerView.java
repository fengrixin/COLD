package com.rixin.cold.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rixin.cold.R;
import com.rixin.cold.utils.UIUtils;

/**
 * 封装RecyclerView
 * Created by 飘渺云轩 on 2017/211.
 */

public class MyRecyclerView {

    private XRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private View view;

    public MyRecyclerView(RecyclerView.LayoutManager manager,RecyclerView.Adapter adapter) {
        this.manager = manager;
        this.adapter = adapter;
        initView();
    }

    private void initView(){
        view = UIUtils.inflate(R.layout.fragment_base);

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.rv_list);

        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        mRecyclerView.setAdapter(adapter);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(UIUtils.getContext(), "刷新", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadMore() {
                Toast.makeText(UIUtils.getContext(), "加载", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public View getView(){
        return view;
    }

}
