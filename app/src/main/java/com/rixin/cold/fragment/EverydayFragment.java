package com.rixin.cold.fragment;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rixin.cold.R;
import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.RandomUtils;
import com.rixin.cold.utils.SPUtils;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 每日一冷
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class EverydayFragment extends BaseFragment {

    private ColdDetailsInfo detailsInfo;
    private ImageView mPic;
    private ImageView mArrow;
    private TextView mTextArrow;
    private TextView mTitle;
    private TextView mContent;
    private TextView mTime;
    private TextView mRead;
    private TextView mStar;
    private Document document;

    // 当前要显示的页面
    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private final static int EMPTY = 2;
    private int currentState = SUCCESS;

    @Override
    public View onCreateSuccessPage() {
        View view = UIUtils.inflate(R.layout.recycler_list_item_everyday);
        mPic = (ImageView) view.findViewById(R.id.iv_everyday_pic);
        mArrow = (ImageView) view.findViewById(R.id.iv_everyday_arrow);
        mTextArrow = (TextView) view.findViewById(R.id.tv_everyday_arrow);
        mTitle = (TextView) view.findViewById(R.id.tv_everyday_title);
        mContent = (TextView) view.findViewById(R.id.tv_everyday_content);
        mTime = (TextView) view.findViewById(R.id.tv_everyday_time);
        mRead = (TextView) view.findViewById(R.id.tv_everyday_read);
        mStar = (TextView) view.findViewById(R.id.tv_everyday_star);

        if (detailsInfo != null) {
            Glide.with(UIUtils.getContext()).load(detailsInfo.picUrl).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_thumb_bg).into(mPic);
            mTitle.setText(detailsInfo.title);
            mContent.setText(Html.fromHtml(detailsInfo.pContent.toString()));
            mArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContent.setMaxLines(1000);
                    mArrow.setVisibility(View.GONE);
                    mTextArrow.setVisibility(View.GONE);
                }
            });
            mTime.setText("发布时间：" + getCurrentDate());
            mRead.setText("阅读(" + RandomUtils.getReadRandom() + ")");
            mStar.setText("赞(" + RandomUtils.getStarRandom() + ")");
        }

        return view;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        // 加载服务器的数据
        getServiceData();

        String currentTime = SPUtils.getString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_TIME, getCurrentDate());
        // 判断当前日期和上次打开的日期是否一致，如果不一致则加载下一篇文章
        if(!currentTime.equals(getCurrentDate())){
            SPUtils.setString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_TIME, getCurrentDate());
            SPUtils.setString(UIUtils.getContext(), GlobalConstants.EVERYDAY_URL_KEY, detailsInfo.nextUrl);
        }

        // 加载对应的页面
        if (currentState == SUCCESS) {
            SPUtils.setString(UIUtils.getContext(),GlobalConstants.EVERYDAY_CURRENT_TIME,getCurrentDate());
            return LoadingPage.ResultState.STATE_SUCCESS;
        } else if (currentState == EMPTY) {
            return LoadingPage.ResultState.STATE_EMPTY;
        } else if (currentState == ERROR) {
            return LoadingPage.ResultState.STATE_ERROR;
        }
        return null;
    }

    private void getServiceData() {
        detailsInfo = new ColdDetailsInfo();
        try {
            // 从一个URL加载一个Document对象
            document = nextImageText(SPUtils.getString(UIUtils.getContext(), GlobalConstants.EVERYDAY_URL_KEY, GlobalConstants.EVERYDAY_SERVICE_URL));
            if (document != null) {
                currentState = SUCCESS;
                // 选择标题所在节点
                Element element = document.select(".article-title").first();
                detailsInfo.title = element.select("a").text();
                // 选择图片所在节点
                element = document.select("article img").first();
                detailsInfo.picUrl = element.attr("src");
                // 选择内容所在节点
                Elements elements = document.select("article > p");
                if (elements.size() == 1) {
                    elements = document.select("article > div");
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < elements.size() - 1; i++) {
                    if (elements.get(i).html().contains("&nbsp;")) {
                        continue;
                    }
                    sb.append("\u3000\u3000" + elements.get(i).html() + "<br/>");
                }
                detailsInfo.pContent = sb;
                // 选择下一篇所在的节点
                element = document.select(".article-nav-next a").first();
                detailsInfo.nextUrl = element.attr("href");
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentState = ERROR;
        }
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
            Element element = d.select("article img").first();
            if (element == null) {
                Element next = d.select(".article-nav-next").first();
                String nextUrl = next.select("a").attr("href");
                SPUtils.setString(UIUtils.getContext(), GlobalConstants.EVERYDAY_URL_KEY, nextUrl);
                d = nextImageText(nextUrl);
            }
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            HttpStatusException ex = new HttpStatusException("HTTP error fetching URL", 404, url);
            if (ex != null) {
                currentState = EMPTY;
            } else {
                currentState = ERROR;
            }
        }
        return null;
    }

    /**
     * @return
     */
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String time = dateFormat.format(date);
        return time;
    }

}
