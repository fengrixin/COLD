package com.rixin.cold.fragment;

import android.view.View;

import com.rixin.cold.widget.LoadingPage;

/**
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class ImageTxtFragment extends BaseFragment {

    @Override
    public View onCreateSuccessPage() {


        return null;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

}
