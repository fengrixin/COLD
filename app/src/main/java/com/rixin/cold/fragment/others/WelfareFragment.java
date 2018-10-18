package com.rixin.cold.fragment.others;

import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rixin.cold.R;
import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;

public class WelfareFragment extends BaseFragment {

    private WebView wvWelfare;
    private ProgressBar progressBar;

    // 当前要显示的页面
    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private int currentState = SUCCESS;

    @Override
    public View onCreateSuccessPage() {
        View view = UIUtils.inflate(R.layout.fragment_welfare);
        wvWelfare = (WebView) view.findViewById(R.id.wv_welfare);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_load);
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {

        if (currentState == SUCCESS) {
            return LoadingPage.ResultState.STATE_SUCCESS;
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
