package com.rixin.cold.fragment.others;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rixin.cold.R;

import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.video.VideoAdListener;
import net.youmi.android.normal.video.VideoAdManager;
import net.youmi.android.normal.video.VideoAdSettings;

/**
 * 赞助我们
 * Created by 飘渺云轩 on 2017/3/24.
 */

public class SponsorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sponsor, null);

        Button btnAd = (Button) view.findViewById(R.id.btn_sponsor_ad);
        btnAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupVideoAd();
            }
        });

        return view;
    }


    /**
     * 设置视频广告
     */
    private void setupVideoAd() {
        // 设置视频广告
        final VideoAdSettings videoAdSettings = new VideoAdSettings();
        videoAdSettings.setInterruptTips("观看完整视频以支持我们 -->" + "\r\n\r\n不再支持?");

        // 只需要调用一次，由于在主页窗口中已经调用了一次，所以此处无需调用
//        VideoAdManager.getInstance(getContext()).requestVideoAd(getContext());

        // 展示视频广告
        VideoAdManager.getInstance(getContext())
                .showVideoAd(getContext(), videoAdSettings, new VideoAdListener() {
                    @Override
                    public void onPlayStarted() {
//                        System.out.println("开始播放视频");
                    }

                    @Override
                    public void onPlayInterrupted() {
//                        System.out.println("播放视频被中断");
                    }

                    @Override
                    public void onPlayFailed(int errorCode) {
//                        System.out.println("视频播放失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
//                                System.out.println("网络异常");
                                break;
                            case ErrorCode.NON_AD:
//                                System.out.println("视频暂无广告");
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
//                                System.out.println("视频资源还没准备好");
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
//                                System.out.println("视频展示间隔限制");
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
//                                System.out.println("视频控件处在不可见状态");
                                break;
                            default:
//                                System.out.println("请稍后再试");
                                break;
                        }
                    }

                    @Override
                    public void onPlayCompleted() {
//                        System.out.println("视频播放成功");
                    }
                });
    }

}
