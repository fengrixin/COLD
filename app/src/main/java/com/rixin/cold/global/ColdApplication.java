package com.rixin.cold.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

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
