package com.rixin.cold.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rixin.cold.DetailsActivity;
import com.rixin.cold.R;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.SPUtils;
import com.rixin.cold.utils.UIUtils;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 标签页的Adapter
 * Created by 飘渺云轩 on 2017/2/10.
 */

public class TabsRecyclerViewAdapter extends RecyclerView.Adapter<TabsRecyclerViewAdapter.TabsViewHolder> {

    private Context context;
    private ArrayList<ColdInfo> data;
    private int resource;

    public TabsRecyclerViewAdapter(Context context, ArrayList<ColdInfo> data, int resource) {
        this.context = context;
        this.data = data;
        this.resource = resource;
    }

    public void setDataChangeListener(ArrayList<ColdInfo> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public TabsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent.getTag() == null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
            return new TabsViewHolder(view);
        } else {
            return new TabsViewHolder((View) parent.getTag());
        }
    }

    @Override
    public void onBindViewHolder(final TabsViewHolder holder, final int position) {
        Glide.with(UIUtils.getContext()).load(data.get(position).getPicUrl())
                .bitmapTransform(new RoundedCornersTransformation(UIUtils.getContext(), UIUtils.dip2px(8), 0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.not_found)  // 默认图片
                .into(holder.pic);
        holder.title.setText(data.get(position).getTitle() + "?");
        String readIds = SPUtils.getString(UIUtils.getContext(), "readIds", "");
        if (readIds.contains(data.get(position).getId() + "")) {
            holder.title.setTextColor(Color.rgb(189, 189, 189));
        } else {
            holder.title.setTextColor(Color.BLACK);
        }
        holder.readCount.setText("阅读(" + data.get(position).getReadCount() + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String readIds = SPUtils.getString(UIUtils.getContext(), "readIds", "");
                // 没有 id 才追加，避免重复
                if (!readIds.contains(data.get(position).getId() + "")) {
                    readIds = readIds + data.get(position).getId() + ",";
                    SPUtils.setString(UIUtils.getContext(), "readIds", readIds);
                }
                // 局部刷新
                holder.title.setTextColor(Color.rgb(189, 189, 189));

                // 跳转详情页
                context.startActivity(new Intent(context, DetailsActivity.class)
                        .putExtra(GlobalConstants.DETAILS_URL_KEY, data.get(position).getContentUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TabsViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView title;
        private TextView readCount;

        public TabsViewHolder(View itemView) {
            super(itemView);
            pic = (ImageView) itemView.findViewById(R.id.iv_tabs_list_pic);
            title = (TextView) itemView.findViewById(R.id.tv_tabs_list_title);
            readCount = (TextView) itemView.findViewById(R.id.tv_tabs_list_reader);
        }
    }
}
