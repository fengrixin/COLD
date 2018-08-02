package com.rixin.cold.utils;

import android.util.Log;

/**
 * Created by rixin on 2018/4/27.
 */

public class Logger {

    private static boolean isOpen;
    private static String tag;

    public static void init(String tag, boolean isOpen) {
        Logger.tag = tag;
        Logger.isOpen = isOpen;
    }

    private static String checkMsg(String msg) {
        return msg == null ? "msg is null" : msg;
    }

    public static void eAll(String msg) {
        if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) {
                    e(msg.substring(i, i + 4000));
                } else {
                    e(msg.substring(i, msg.length()));
                }
            }
        } else {
            e(msg);
        }
    }

    public static void eAll(Class cls, String msg) {
        if (msg.length() > 4000) {
            for (int i = 0; i < msg.length(); i += 4000) {
                if (i + 4000 < msg.length()) {
                    e(cls, msg.substring(i, i + 4000));
                } else {
                    e(cls, msg.substring(i, msg.length()));
                }
            }
        } else {
            e(cls, msg);
        }
    }

    public static void e(String msg) {
        if (isOpen) {
            Log.e(tag, checkMsg(msg));
        }
    }

    public static void e(Class cls, String msg) {
        if (isOpen) {
            Log.e(cls.getSimpleName(), checkMsg(msg));
        }
    }


}
