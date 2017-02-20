package com.rixin.cold.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.cold.R;

import java.util.ArrayList;

/**
 * Created by 飘渺云轩 on 2017/2/11.
 */

public class EverydayRecyclerViewAdapter extends RecyclerView.Adapter<EverydayRecyclerViewAdapter.EverydayViewHolder> {

    private ArrayList<String> data;

    public EverydayRecyclerViewAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public EverydayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent.getTag() == null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_everyday,parent,false);
            return new EverydayViewHolder(view);
        } else {
            return new EverydayViewHolder((View) parent.getTag());
        }
    }

    @Override
    public void onBindViewHolder(EverydayViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class EverydayViewHolder extends RecyclerView.ViewHolder {
        public EverydayViewHolder(View itemView) {
            super(itemView);
        }
    }
}
