package com.rixin.cold.fragment.tabs;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.rixin.cold.adapter.TabsRecyclerViewAdapter;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.MyRecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 百科
 * Created by 飘渺云轩 on 2017/2/9.
 */

public class EncyclopediaFragment extends BaseFragment {

    private ArrayList<ColdInfo> data;

    @Override
    public View onCreateSuccessPage() {
        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
        TabsRecyclerViewAdapter adapter = new TabsRecyclerViewAdapter(data);
        MyRecyclerView myRecyclerView = new MyRecyclerView(manager, adapter);
        return myRecyclerView.getView();
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        getServiceData();
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    private void getServiceData(){
        data = new ArrayList<>();
        //从一个URL中加载一个Document对象
        try {
            Document document = Jsoup.connect(GlobalConstants.SERVICE_URL + "baike").get();
            Elements elements = document.select("article");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
