package com.rixin.cold.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rixin.cold.R;
import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.CacheUtils;
import com.rixin.cold.utils.Logger;
import com.rixin.cold.utils.SPUtils;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 开屏即冷
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class EverydayFragment extends BaseFragment {

    private ColdDetailsInfo mDetailsInfo;
    private Document document;
    private ImageView mPic;
    private ImageView mArrowPic;
    private TextView mTextArrow;
    private TextView mTitle;
    private TextView mContent;
    private RelativeLayout mShow;
    private TextView mTime;
    private TextView mRead;

    // 当前要显示的页面
    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private int currentState = SUCCESS;
    private boolean isShow = false;  // 标记展示内容的开关状态

    @Override
    public View onCreateSuccessPage() {
        View view = UIUtils.inflate(R.layout.fragment_everyday);
        mPic = (ImageView) view.findViewById(R.id.iv_everyday_pic);
        mArrowPic = (ImageView) view.findViewById(R.id.iv_everyday_arrow);
        mTextArrow = (TextView) view.findViewById(R.id.tv_everyday_arrow);
        mShow = (RelativeLayout) view.findViewById(R.id.rl_show);
        mTitle = (TextView) view.findViewById(R.id.tv_everyday_title);
        mContent = (TextView) view.findViewById(R.id.tv_everyday_content);
        mTime = (TextView) view.findViewById(R.id.tv_everyday_time);
        mRead = (TextView) view.findViewById(R.id.tv_everyday_read);

        if (mDetailsInfo != null) {
            Glide.with(UIUtils.getContext()).load(mDetailsInfo.getPicUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_thumb_bg).into(mPic);
            mTitle.setText(mDetailsInfo.getTitle() + "?");
            mContent.setText(Html.fromHtml(mDetailsInfo.getpContent() == null ? "" : mDetailsInfo.getpContent()));
            mTextArrow.setText("展开");
            mTime.setText("发布时间：" + getCurrentDate());
            mRead.setText("阅读(" + mDetailsInfo.getRead() + ")");
            mShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggle();
                }
            });
        }

        return view;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {

        // 加载下一篇图文的服务器数据
        mDetailsInfo = getServiceData();

        // 加载对应的页面
        if (currentState == SUCCESS) {
            SPUtils.setString(UIUtils.getContext(), GlobalConstants.BEFORE_TIME_KEY, getCurrentDate());
            return LoadingPage.ResultState.STATE_SUCCESS;
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }

    /**
     * 属性动画，展示/收起详细内容
     */
    private void toggle() {
        if (isShow) {
            isShow = false;
            mContent.setMaxLines(8);
            mArrowPic.setImageResource(R.drawable.ic_arrow_down);
            mTextArrow.setText("展开");
        } else {
            isShow = true;
            mContent.setMaxLines(1000);
            mArrowPic.setImageResource(R.drawable.ic_arrow_up);
            mTextArrow.setText("收起");
        }
    }

    /**
     * 访问网页获取数据
     *
     * @return
     */
    private ColdDetailsInfo getServiceData() {
        ColdDetailsInfo detailsInfo = new ColdDetailsInfo();
        try {
            // 从一个URL加载一个Document对象
            document = nextImageText(SPUtils.getString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_URL_KEY, GlobalConstants.EVERYDAY_SERVICE_URL));
            if (document != null) {
                currentState = SUCCESS;
                // 选择标题所在节点
                Element element = document.select("#title").first();
                detailsInfo.setTitle(element.select("h1").text());
                // 选择图片所在节点
                element = document.select("#neir img").first();
                detailsInfo.setPicUrl(element.attr("src"));
                // 选择内容所在节点
                Elements elements = document.select("#neir p");
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < elements.size(); i++) {
                    if (elements.get(i).html().contains("&nbsp;")) {
                        continue;
                    }
                    sb.append("\u3000\u3000" + elements.get(i).html() + "<br/><br/>");
                }
                detailsInfo.setpContent(sb.toString());
                // 选择阅读数所在节点
                String str = document.select("#title em").text();
                String[] splits = str.split("\\|");
                String[] reads = splits[splits.length - 1].split("个");
                detailsInfo.setRead(Integer.parseInt(reads[0].trim()) + 100);
                // 选择上一篇所在的节点
                element = document.select("#sx span a").first();
                if (element != null) {
                    detailsInfo.setPrevUrl(element.attr("href"));
                }
                // 选择下一篇所在的节点
                element = document.select(".sx-r a").first();
                detailsInfo.setNextUrl(element.attr("href"));

                // 存储下一篇的URL
                SPUtils.setString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_URL_KEY, detailsInfo.getNextUrl());
                // 写缓存
                CacheUtils.setCache(GlobalConstants.EVERYDAY_CACHE_KEY, detailsInfo.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentState = ERROR;
        }
        return detailsInfo;
    }

    /**
     * 判断当前页面是否是图文，如果不是则继续迭代下去
     * 如果当前页面不存在，则返回null
     *
     * @param url
     * @return
     */
    private Document nextImageText(String url) {
        try {
            Document d = Jsoup.connect(url).get();
            Element element = d.select("#neir img").first();
            if (element == null) {
                Element next = d.select(".sx-r").first();
                String nextUrl = next.select("a").attr("href");
                SPUtils.setString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_URL_KEY, nextUrl);
                d = nextImageText(nextUrl);
            }
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            currentState = ERROR;
        }
        return null;
    }

}
