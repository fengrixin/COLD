package com.rixin.cold.widget;

import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rixin on 2017/12/20.
 */

public class CardLayoutManager extends RecyclerView.LayoutManager {

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    public CardLayoutManager(@NonNull RecyclerView recyclerView, @NonNull ItemTouchHelper itemTouchHelper) {
        this.recyclerView = checkIsNull(recyclerView);
        this.itemTouchHelper = checkIsNull(itemTouchHelper);
    }

    private <T> T checkIsNull(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);  // detach 轻量回收所有 View
        int itemCount = getItemCount();
        if (itemCount > CardConfig.DEFAULT_SHOW_ITEM) {
            // 当数据源个数大于最大显示数时
            for (int position = CardConfig.DEFAULT_SHOW_ITEM; position >= 0; position--) {
                View viewForPosition = recycler.getViewForPosition(position);
                addView(viewForPosition);
                measureChildWithMargins(viewForPosition, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(viewForPosition);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(viewForPosition);
                // RecyclerView 布局
                layoutDecoratedWithMargins(viewForPosition, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(viewForPosition),
                        heightSpace / 2 + getDecoratedMeasuredHeight(viewForPosition));

                if (position == CardConfig.DEFAULT_SHOW_ITEM) {
                    viewForPosition.setScaleX(1 - (position - 1) * CardConfig.DEFAULT_SCALE);
                    viewForPosition.setScaleY(1 - (position - 1) * CardConfig.DEFAULT_SCALE);
                    viewForPosition.setTranslationY((position - 1) * viewForPosition.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else if (position > 0) {
                    viewForPosition.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    viewForPosition.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    viewForPosition.setTranslationY(position * viewForPosition.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    viewForPosition.setOnTouchListener(listener);
                }
            }
        } else {
            // 当数据源个数小于或等于最大显示数时
            for (int position = itemCount - 1; position >= 0; position--) {
                View viewForPosition = recycler.getViewForPosition(position);
                addView(viewForPosition);
                measureChildWithMargins(viewForPosition, 0, 0);
                int widthSpace = getWidth() - getDecoratedMeasuredWidth(viewForPosition);
                int heightSpace = getHeight() - getDecoratedMeasuredHeight(viewForPosition);
                // RecyclerView 布局
                layoutDecoratedWithMargins(viewForPosition, widthSpace / 2, heightSpace / 2,
                        widthSpace / 2 + getDecoratedMeasuredWidth(viewForPosition),
                        heightSpace / 2 + getDecoratedMeasuredHeight(viewForPosition));

                if (position > 0) {
                    viewForPosition.setScaleX(1 - position * CardConfig.DEFAULT_SCALE);
                    viewForPosition.setScaleY(1 - position * CardConfig.DEFAULT_SCALE);
                    viewForPosition.setTranslationY(position * viewForPosition.getMeasuredHeight() / CardConfig.DEFAULT_TRANSLATE_Y);
                } else {
                    viewForPosition.setOnTouchListener(listener);
                }
            }
        }
    }

    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(v);
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startSwipe(childViewHolder);
            }
            return false;
        }
    };

}
