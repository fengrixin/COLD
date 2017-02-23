package com.rixin.cold.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.rixin.cold.R;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;

/**
 * 根据当前状态来显示不同页面的自定义控件
 * <p>
 * -未加载  -加载中  -加载失败  -数据为空  -加载成功
 * Created by 飘渺云轩 on 2017/2/5.
 */

public abstract class LoadingPage extends FrameLayout {

    private static final int STATE_LOAD_UNDO = 1;  //未加载
    private static final int STATE_LOAD_LOADING = 2;  //加载中
    private static final int STATE_LOAD_FAILURE = 3;  //加载失败
    private static final int STATE_LOAD_EMPTY = 4;  //数据为空
    private static final int STATE_LOAD_SUCCESS = 5;  //加载成功

    private int currentState = STATE_LOAD_UNDO;  //当前状态

    private View loadingPage;  //加载中页面
    private View errorPage;  //加载失败页面
    private View emptyPage;  //数据为空页面
    private View successPage;  //加载成功页面

    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        //////////////// 初始化加载中的布局 ////////////////////
        if (loadingPage == null) {
            loadingPage = UIUtils.inflate(R.layout.page_loading);
            addView(loadingPage);  //将加载中的布局添加到当前的帧布局
        }
        //////////////// 初始化加载失败的布局 ///////////////////
        if (errorPage == null) {
            errorPage = UIUtils.inflate(R.layout.page_error);
            errorPage.findViewById(R.id.btn_load_again).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData();
                    //显示加载中页面
                    currentState = STATE_LOAD_LOADING;
                    showRightPage();
                }
            });
            addView(errorPage);
        }
        //////////////// 初始化数据为空的布局 ///////////////////
        if (emptyPage == null) {
            emptyPage = UIUtils.inflate(R.layout.page_empty);
            emptyPage.findViewById(R.id.iv_load_again).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadData();
                    //显示加载中页面
                    currentState = STATE_LOAD_LOADING;
                    showRightPage();
                }
            });
            addView(emptyPage);
        }

        showRightPage();
    }

    private void showRightPage() {
        loadingPage.setVisibility((currentState == STATE_LOAD_UNDO || currentState == STATE_LOAD_LOADING) ? View.VISIBLE : View.GONE);
        errorPage.setVisibility(currentState == STATE_LOAD_FAILURE ? View.VISIBLE : View.GONE);
        emptyPage.setVisibility(currentState == STATE_LOAD_EMPTY ? View.VISIBLE : View.GONE);

        // 当前布局为空并且当前状态为加载成功才初始化成功界面
        if (successPage == null && currentState == STATE_LOAD_SUCCESS) {
            successPage = onCreateSuccessPage();
            if (successPage != null) {
                addView(successPage);
            }
        }
        if (successPage != null) {
            successPage.setVisibility(currentState == STATE_LOAD_SUCCESS ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 开始加载数据
     */
    public void loadData() {
        //如果没有加载，就开始加载数据
        if (currentState != STATE_LOAD_LOADING) {
            currentState = STATE_LOAD_LOADING;
            //线程池
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    final ResultState resultState = onLoadData();

                    //更新界面必须在主线程
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resultState != null) {
                                //网络加载结束后，更新网络状态
                                currentState = resultState.getResultState();

                                //根据最新状态来更新界面
                                showRightPage();
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 加载成功后的布局，必须由调用者来实现
     *
     * @return
     */
    public abstract View onCreateSuccessPage();

    /**
     * 加载网络数据，必须由调用者来实现
     *
     * @return 请求网络结束后的状态
     */
    public abstract ResultState onLoadData();

    /**
     * 枚举，网络请求后的状态
     */
    public enum ResultState {
        STATE_SUCCESS(STATE_LOAD_SUCCESS),
        STATE_EMPTY(STATE_LOAD_EMPTY),
        STATE_ERROR(STATE_LOAD_FAILURE);

        private int state;

        private ResultState(int state) {
            this.state = state;
        }

        public int getResultState() {
            return state;
        }
    }
}
