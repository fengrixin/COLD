package com.rixin.cold.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rixin.cold.DetailsActivity;
import com.rixin.cold.R;
import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by rixin on 2017/12/19.
 */

public class TanTanAdapter extends RecyclerView.Adapter<TanTanAdapter.TanTanViewHolder> {

    private Context context;
    private List<ColdDetailsInfo> list = new ArrayList<>();

    public TanTanAdapter(Context context, List<ColdDetailsInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TanTanAdapter.TanTanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TanTanViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tantan, parent, false));
    }

    @Override
    public void onBindViewHolder(final TanTanAdapter.TanTanViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getPicUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, UIUtils.dip2px(8), 0))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.not_found)  // 默认图片
                .into(holder.ivAvatar);
        holder.tvTitle.setText(list.get(position).getTitle());
        holder.tvContent.setText(Html.fromHtml(list.get(position).getpContent()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转详情页
                context.startActivity(new Intent(context, DetailsActivity.class)
                        .putExtra(GlobalConstants.DETAILS_URL_KEY, list.get(position).getUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void inflateData(List<ColdDetailsInfo> data) {
        if (list != null) {
            list.addAll(data);
        } else {
            list = new ArrayList<>();
        }
        this.notifyDataSetChanged();
    }

    public class TanTanViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivAvatar;
        public ImageView ivLike;
        public ImageView ivDisLike;
        public TextView tvTitle;
        public TextView tvContent;

        public TanTanViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            ivDisLike = (ImageView) itemView.findViewById(R.id.iv_dislike);
            ivLike = (ImageView) itemView.findViewById(R.id.iv_like);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

}
