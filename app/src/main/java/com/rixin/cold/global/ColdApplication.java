package com.rixin.cold.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.rixin.cold.utils.Logger;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;

import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * 自定义Application， 初始化全局
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class ColdApplication extends Application {

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;  //主线程ID

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

        UMConfigure.init(this, "58d8c5d82ae85b642400166f", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "825e358b2eb36f17dd55796f717f8ef3");

        // 分享配置
        PlatformConfig.setWeixin("wx57ec1d12b3bb8585", "a2fc2a8721766f8a79b872279e4adfd3");
        PlatformConfig.setQQZone("1106083912", "ydv0Ij2tHKMTSnb1");

        // 推送配置
        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        MiPushRegistar.register(this, "2882303761517564831", "5261756433831");

        UMConfigure.setLogEnabled(true);

        Logger.init(getPackageName(), true);
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
