package com.rixin.cold.widget;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

/**
 * Created by rixin on 2017/12/20.
 */

public class CardItemTouchHelperCallback<T> extends ItemTouchHelper.Callback {

    private RecyclerView.Adapter adapter;
    private List<T> list;
    private OnSwipeListener<T> listener;

    public CardItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> list) {
        this.adapter = checkIsNull(adapter);
        this.list = checkIsNull(list);
    }

    public CardItemTouchHelperCallback(@NonNull RecyclerView.Adapter adapter, @NonNull List<T> list, OnSwipeListener<T> listener) {
        this.adapter = checkIsNull(adapter);
        this.list = checkIsNull(list);
        this.listener = listener;
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    public void setOnSwipedListener(OnSwipeListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 移动的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof CardLayoutManager) {
            swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * item 移出屏幕后回调
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 移除 onTouchListener, 否则触摸滑动会乱套的
        viewHolder.itemView.setOnTouchListener(null);

        int layoutPosition = viewHolder.getLayoutPosition();
        // 删除相对应的数据
        T remove = list.remove(layoutPosition);
        adapter.notifyDataSetChanged();
        if (listener != null) {
            listener.onSwiped(viewHolder, remove,
                    direction == ItemTouchHelper.LEFT ? CardConfig.SWIPED_LEFT : CardConfig.SWIPED_RIGHT);
        }
        // 当没有数据时回调 listener
        if (adapter.getItemCount() == 0) {
            if (listener != null) {
                listener.onSwipedClear();
            }
        }
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 防止底层的 item 能滑动
        return false;
    }

    /**
     * 实时刷新
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float ratio = dX / getThreshold(recyclerView, viewHolder);
            // ratio 最大为 1 或最小为 -1
            if (ratio > 1) {
                ratio = 1;
            } else if (ratio < -1) {
                ratio = -1;
            }
            itemView.setRotation(ratio * CardConfig.DEFAULT_ROTATE_DEGREE);
            int childCount = recyclerView.getChildCount();
            if (childCount > CardConfig.DEFAULT_SHOW_ITEM) {
                // 当数据源个数大于最大显示数时
                for (int position = 1; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    view.setScaleX(1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY((index - Math.abs(ratio)) * itemView.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                }
            } else {
                // 当数据源个数小于或者等于最大显示数时
                for (int position = 0; position < childCount - 1; position++) {
                    int index = childCount - position - 1;
                    View view = recyclerView.getChildAt(position);
                    view.setScaleX(1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE);
                    view.setScaleY(1 - index * CardConfig.DEFAULT_SCALE + Math.abs(ratio) * CardConfig.DEFAULT_SCALE);
                    view.setTranslationY((index - Math.abs(ratio)) * itemView.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                }
            }
            if (listener != null) {
                if (ratio != 0) {
                    listener.onSwiping(viewHolder, ratio, ratio < 0 ? CardConfig.SWIPING_LEFT : CardConfig.SWIPING_RIGHT);
                } else {
                    listener.onSwiping(viewHolder, ratio, CardConfig.SWIPING_NONE);
                }
            }
        }
    }

    /**
     * 松手后回调
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setRotation(0f);
    }

    private float getThreshold(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return recyclerView.getWidth() * getSwipeThreshold(viewHolder);
    }

}
