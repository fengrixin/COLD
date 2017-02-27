package com.rixin.cold.fragment;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.rixin.cold.adapter.VideoRecyclerViewAdapter;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.MyRecyclerView;

import java.util.ArrayList;

/**
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class VideoFragment extends BaseFragment {

    @Override
    public View onCreateSuccessPage() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        VideoRecyclerViewAdapter adapter = new VideoRecyclerViewAdapter(getTestList());
        MyRecyclerView myRecyclerView = new MyRecyclerView(manager, adapter){

            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        };
        return myRecyclerView.getView();
    }

    private ArrayList<String> getTestList(){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            list.add("第"+i+"条数据");
        }
        return list;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

}
