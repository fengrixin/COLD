package com.rixin.cold.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rixin.cold.R;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.utils.UIUtils;

import java.util.ArrayList;

/**
 * 标签页的Adapter
 * Created by 飘渺云轩 on 2017/2/10.
 */

public class TabsRecyclerViewAdapter extends RecyclerView.Adapter<TabsRecyclerViewAdapter.TabsViewHolder> {

    private ArrayList<ColdInfo> data;

    public TabsRecyclerViewAdapter(ArrayList<ColdInfo> data) {
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
        Glide.with(UIUtils.getContext()).load(data.get(position).picUrl).asBitmap().into(holder.pic);
        holder.title.setText(data.get(position).title);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TabsViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView time;
        private TextView title;
        public TabsViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.iv_tabs_list_pic);
            time = (TextView) itemView.findViewById(R.id.tv_tabs_list_time);
            title = (TextView) itemView.findViewById(R.id.tv_tabs_list_title);
        }
    }
}
