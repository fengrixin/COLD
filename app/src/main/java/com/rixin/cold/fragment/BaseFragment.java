package com.rixin.cold.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.cold.DetailsActivity;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.Logger;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;
import com.umeng.analytics.MobclickAgent;

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

    /**
     * 友盟统计
     */
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getActivity().getLocalClassName());
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
                Elements elements = document.select(".post .zi");
                if (elements != null) {
                    for (Element element : elements) {
                        ColdInfo info = new ColdInfo();
                        String contentUrl = element.select("a").attr("href");
                        String[] strs = contentUrl.split("post/");
                        info.setId(strs[1]);
                        info.setTitle(element.select(".zi h2").text());
                        info.setPicUrl(element.select(".zi a img").attr("src"));
                        info.setContentUrl(contentUrl);
                        // 选择阅读数所在节点
                        String str = element.select(".zi em").text();
                        String[] splits = str.split("\\|");
                        String[] reads = splits[splits.length - 1].split("个");
                        info.setReadCount(Integer.parseInt(reads[0].trim()) + 100);
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
     *
     * @param url
     */
    public void toDetailsPage(String url, int read) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(GlobalConstants.DETAILS_URL_KEY, url);
        intent.putExtra(GlobalConstants.READCOUNT_KEY, read);
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
