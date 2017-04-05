package com.rixin.cold;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.rixin.cold.fragment.others.SearchResultFragment;
import com.umeng.analytics.MobclickAgent;

import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText etSearch;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 设置轮播插屏广告
        setupSlideableSpotAd();

        /** 设置Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        setSupportActionBar(toolbar);   //设置后无法响应NavigationIcon的点击事件

        initView();
    }

    public void initView() {
        etSearch = (EditText) this.findViewById(R.id.et_search);
        btnSearch = (Button) this.findViewById(R.id.btn_search);

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etSearch.setCursorVisible(true);
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etSearch.requestFocus() && !etSearch.getText().toString().isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                try {
                    text = URLEncoder.encode(etSearch.getText().toString(), "UTF-8");
                    text = etSearch.getText().toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!text.isEmpty()) {
                    FragmentManager manager = getSupportFragmentManager();
                    // 获取事务
                    FragmentTransaction transaction = manager.beginTransaction();
                    SearchResultFragment fragment = new SearchResultFragment();
                    // 使用 Bundle 进行传值
                    Bundle bundle = new Bundle();
                    bundle.putString("search_text", text);
                    // 将 Bundle 对象添加到 Fragment 中
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.content_main, fragment).commit();  // 提交事务
                } else {
                    Snackbar.make(view, "请输入内容后再进行搜索", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
                etSearch.setText("");
                etSearch.setCursorVisible(false);
            }
        });

    }

    /**
     * 设置轮播插屏广告
     */
    private void setupSlideableSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(this)
                .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

        // 展示轮播插屏广告
        SpotManager.getInstance(this)
                .showSlideableSpot(this, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
//                        System.out.println("轮播插屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
//                        System.out.println("轮播插屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
//                                System.out.println("网络异常");
                                break;
                            case ErrorCode.NON_AD:
//                                System.out.println("暂无轮播插屏广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
//                                System.out.println("轮播插屏资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
//                                System.out.println("请勿频繁展示");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
//                                System.out.println("请设置插屏为可见状态");
                                break;
                            default:
//                                System.out.println("请稍后再试");
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
//                        System.out.println("轮播插屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
//                        System.out.println("轮播插屏被点击");
//                        System.out.println("是否是网页广告？" + "----" + isWebPage);
//                                logInfo("是否是网页广告？%s", isWebPage ? "是" : "不是");
                    }
                });
    }


    @Override
    public void onBackPressed() {
        // 点击后退关闭轮播插屏广告
        if (SpotManager.getInstance(this).isSlideableSpotShowing()) {
            SpotManager.getInstance(this).hideSlideableSpot();
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPause(this);
        // 轮播插屏广告
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 轮播插屏广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 轮播插屏广告
        SpotManager.getInstance(this).onDestroy();
    }
}
