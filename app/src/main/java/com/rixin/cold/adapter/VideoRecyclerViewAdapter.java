package com.rixin.cold.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rixin.cold.R;

import java.util.ArrayList;

/**
 * 视频精选Adapter
 * Created by 飘渺云轩 on 2017/2/11.
 */

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> data;

    public VideoRecyclerViewAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent.getTag() == null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item_video,parent,false);
            return new ViewHolder(view);
        } else {
            return new ViewHolder((View) parent.getTag());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
