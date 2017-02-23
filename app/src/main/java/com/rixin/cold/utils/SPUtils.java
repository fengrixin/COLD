package com.rixin.cold.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharePreferences的封装类
 * 先封装常用的int、String、boolean类型，其他以后需要再进行添加
 * <p>
 * Created by 飘渺云轩 on 2017/2/15.
 */
public class SPUtils {

    public static final String SP_CONFIG_NAME = "config";

    /**
     * 获取SharePreferences中boolean类型的key对应的值
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 保存一个或多个Boolean类型的值进SharePreferences中
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();  //异步提交
    }

    /**
     * 获取SharePreferences中String类型的key对应的值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * 保存一个或多个String类型的值进SharePreferences中
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();  //异步提交
    }

    /**
     * 获取SharePreferences中int类型的key对应的值
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    /**
     * 保存一个或多个int类型的值进SharePreferences中
     *
     * @param context
     * @param keys
     * @param values
     */
    public static void setInt(Context context, String[] keys, int[] values) {
        SharedPreferences sp = context.getSharedPreferences(SP_CONFIG_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        for (int i = 0; i < keys.length; i++) {
            editor.putInt(keys[i], values[i]);
        }
        editor.apply();  //异步提交
    }

}
