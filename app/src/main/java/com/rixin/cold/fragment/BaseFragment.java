package com.rixin.cold.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.cold.DetailsActivity;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.RandomUtils;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Fragment基类
 * Created by 飘渺云轩 on 2017/1/31.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        loadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessPage() {
                // 这里必须写BaseFragment.this. ，否则会栈溢出
                return BaseFragment.this.onCreateSuccessPage();
            }

            @Override
            public ResultState onLoadData() {
                return BaseFragment.this.onLoadData();
            }
        };

        loadData();

        return loadingPage;
    }

    /**
     * 开始加载数据
     */
    public void loadData() {
        if (loadingPage != null) {
            loadingPage.loadData();
        }
    }

    /**
     * 对网络返回的数据进行合法性校验
     *
     * @param object
     * @return
     */
    public LoadingPage.ResultState check(Object object) {
        if (object != null) {
            if (object instanceof ArrayList) {  //判断是否是集合
                ArrayList list = (ArrayList) object;
                if (list.isEmpty()) {
                    return LoadingPage.ResultState.STATE_ERROR;
                } else {
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }
            }
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }

    /**
     * 获取列表数据
     *
     * @param url
     * @return
     */
    public ArrayList<ColdInfo> getServiceData(String url) {
        ArrayList<ColdInfo> data = new ArrayList<>();
        //从一个URL中加载一个Document对象
        try {
            Document document = Jsoup.connect(url).get();
            if (document != null) {
                Elements elements = document.select("article");
                if (elements != null) {
                    for (Element element : elements) {
                        ColdInfo info = new ColdInfo();
                        info.title = element.select("header h2").text();
                        info.picUrl = element.select("img").attr("data-original");
                        info.contentUrl = element.select("header a").attr("href");
                        info.readCount = RandomUtils.getReadRandom();
                        info.starCount = RandomUtils.getStarRandom();
                        data.add(info);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String time = dateFormat.format(date);
        return time;
    }

    /**
     * 获取写入缓存的字符串
     *
     * @param data
     * @return
     */
    public String getCacheString(ArrayList<ColdInfo> data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            ColdInfo info = data.get(i);
            sb.append(info.toString() + "\n");
        }
        return sb.toString();
    }

    /**
     * 跳转到详情页面
     * @param url
     */
    public void toDetailsPage(String url) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(GlobalConstants.DETAILS_URL_KEY, url);
        startActivity(intent);
    }

    /**
     * 加载成功的布局，必须由子类实现
     *
     * @return
     */
    public abstract View onCreateSuccessPage();

    /**
     * 加载网络数据，必须由子类实现
     *
     * @return
     */
    public abstract LoadingPage.ResultState onLoadData();
}
