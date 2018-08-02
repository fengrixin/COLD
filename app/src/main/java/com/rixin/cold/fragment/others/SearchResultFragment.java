package com.rixin.cold.fragment.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.rixin.cold.R;
import com.rixin.cold.adapter.TabsRecyclerViewAdapter;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.NetworkUtils;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.MyRecyclerView;

import java.util.ArrayList;

/**
 * 动物
 * Created by 飘渺云轩 on 2017/2/9.
 */

public class SearchResultFragment extends BaseFragment {

    private ArrayList<ColdInfo> mData;
    private TabsRecyclerViewAdapter mAdapter;
    private int page = 1;
    private int currentSize = 0;
    private String searchText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 接收 Activity 传过来的值
        Bundle bundle = getArguments();
        searchText = bundle.getString("search_text");
    }

    @Override
    public View onCreateSuccessPage() {
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        mAdapter = new TabsRecyclerViewAdapter(UIUtils.getContext(), mData, R.layout.recycler_list_item_linear);
        MyRecyclerView myRecyclerView = new MyRecyclerView(manager, mAdapter) {

            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
            }
        };
        return myRecyclerView.getView();
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        page = 1;
        mData = getServiceData(GlobalConstants.SEARCH_SERVICE_URL + GlobalConstants.SOCKET +  searchText);
        return check(mData);
    }
}
