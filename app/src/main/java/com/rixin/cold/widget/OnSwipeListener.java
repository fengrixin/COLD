package com.rixin.cold.widget;

import android.support.v7.widget.RecyclerView;

/**
 * Created by rixin on 2017/12/20.
 */

public interface OnSwipeListener<T> {

    /**
     * 卡片还在滑动时回调
     *
     * @param viewHolder
     * @param ratio      滑动进度的比例
     * @param direction  卡片滑动的方向
     */
    void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction);

    /**
     * 卡片完全滑出时回调
     *
     * @param viewHolder
     * @param t          该滑出卡片的数据
     * @param direction  卡片滑出的方向
     */
    void onSwiped(RecyclerView.ViewHolder viewHolder, T t, int direction);

    /**
     * 所有卡片全部滑出时回调
     */
    void onSwipedClear();

}
