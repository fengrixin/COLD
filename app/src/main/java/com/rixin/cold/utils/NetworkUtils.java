package com.rixin.cold.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 *  判断网络状态
 * Created by 飘渺云轩 on 2017/2/25.
 */

public class NetworkUtils {

    /**
     * 判断当前网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context){
        if(context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    /**
     * 判断当前是否是移动网络
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context){
        if(context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
            if(networkInfo != null){
                return networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    /**
     * 判断当前是否是WIFI
     * @param context
     * @return
     */
    public static boolean isWiFiConnected(Context context){
        if(context != null){
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if(networkInfo != null){
                return networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

}
