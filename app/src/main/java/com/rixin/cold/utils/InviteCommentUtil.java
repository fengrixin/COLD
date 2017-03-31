package com.rixin.cold.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.rixin.cold.R;
import com.rixin.cold.global.GlobalConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 邀请评论工具类
 * Created by 飘渺云轩 on 2017/3/31.
 */

public class InviteCommentUtil {
    private static String mDataFormat = "yyyy-mm-dd";
    private static String mInviteCommentTime;

    /**
     * 选择在哪天弹出邀请评论框
     *
     * @param activity
     */
    public static void startCommet(final Activity activity, boolean flag) {
        mInviteCommentTime = SPUtils.getString(activity, GlobalConstants.INVITE_BEFORE_TIME_KEY, getInviteCommentTime());
        if (flag || mInviteCommentTime.equals(getCurrentDate())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("赏个好评")
                    .setMessage("感谢您使用本应用阅读冷知识，如果您喜欢这个应用，希望您能在应用市场给予五星好评,么么哒~")
                    .setPositiveButton("好评鼓励", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 保存，7 天后再提醒
                            SPUtils.setString(activity, GlobalConstants.INVITE_BEFORE_TIME_KEY, getInviteCommentTime());
                            try {
                                Intent intent = new Intent("android.intent.action.VIEW");
                                intent.setData(Uri.parse("market://details?id=com.rixin.cold"));
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // 保存，7 天后再提醒
                            SPUtils.setString(activity, GlobalConstants.INVITE_BEFORE_TIME_KEY, getInviteCommentTime());
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    }

    /**
     * 获取 7 天后的日期
     *
     * @return
     */
    public static String getInviteCommentTime() {
        SimpleDateFormat format = new SimpleDateFormat(mDataFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DAY_OF_YEAR, 7);
        String time = format.format(calendar.getTime());
        return time;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(mDataFormat);
        Date date = new Date(System.currentTimeMillis());
        String time = dateFormat.format(date);
        return time;
    }

}
