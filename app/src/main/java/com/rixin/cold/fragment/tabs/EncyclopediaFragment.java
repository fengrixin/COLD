package com.rixin.cold.fragment.tabs;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.rixin.cold.adapter.TabsRecyclerViewAdapter;
import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.MyRecyclerView;

import java.util.ArrayList;

/**
 * 百科
 * Created by 飘渺云轩 on 2017/2/9.
 */

public class EncyclopediaFragment extends BaseFragment {

    TextView tvTest;

    @Override
    public View onCreateSuccessPage() {
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        TabsRecyclerViewAdapter adapter = new TabsRecyclerViewAdapter(getTestList());
        MyRecyclerView myRecyclerView = new MyRecyclerView(manager, adapter);
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
