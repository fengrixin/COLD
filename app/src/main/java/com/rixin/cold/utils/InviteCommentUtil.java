package com.rixin.cold.utils;

import android.content.Context;

/**
 *  邀请评论工具类
 * Created by 飘渺云轩 on 2017/3/31.
 */

public class InviteCommentUtil {
    private String mDataFormat = "yyyy-mm-dd";
    private String mInviteCommentTime;

    /**
     *  选择在哪天弹出邀请评论框
     * @param context
     */
    public void startCommet(Context context){
        mInviteCommentTime = SPUtils.getString(context, "", "");
    }

}
