package com.rixin.cold.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;

import java.util.ArrayList;

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
     * 加载成功的布局，必须由子类实现
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
