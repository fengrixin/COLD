package com.rixin.cold.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.cold.R;

import java.util.ArrayList;

/**
 * 标签页的Adapter
 * Created by 飘渺云轩 on 2017/2/10.
 */

public class TabsRecyclerViewAdapter extends RecyclerView.Adapter<TabsRecyclerViewAdapter.TabsViewHolder> {

    private ArrayList<String> data;

    public TabsRecyclerViewAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public TabsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent.getTag() == null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_tabs,parent,false);
            return new TabsViewHolder(view);
        } else {
            return new TabsViewHolder((View) parent.getTag());
        }
    }

    @Override
    public void onBindViewHolder(TabsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TabsViewHolder extends RecyclerView.ViewHolder {
        public TabsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
