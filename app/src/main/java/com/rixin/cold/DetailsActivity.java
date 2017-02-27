package com.rixin.cold;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.NetworkUtils;
import com.rixin.cold.utils.RandomUtils;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DetailsActivity extends AppCompatActivity {

    private ColdDetailsInfo mDetailsInfo;
    private ImageView mPic;
    private TextView mTitle;
    private TextView mContent;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        toolbar.setTitle(R.string.title_activity_details);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initView();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setImageResource(R.mipmap.ic_star_selected);
                Snackbar.make(view, "收藏成功，在我的收藏中查看", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initView(){
        mPic = (ImageView) findViewById(R.id.iv_details_pic);
        mTitle = (TextView) findViewById(R.id.tv_details_title);
        mContent = (TextView) findViewById(R.id.tv_details_content);

        final String url = getIntent().getStringExtra(GlobalConstants.DETAILS_URL_KEY);
        if (NetworkUtils.isNetworkConnected(UIUtils.getContext())) {
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    mDetailsInfo = getServiceData(url);
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDetailsInfo != null) {
                                Glide.with(UIUtils.getContext()).load(mDetailsInfo.picUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(mPic);
                                mTitle.setText(mDetailsInfo.title + "?");
                                mContent.setText(Html.fromHtml(mDetailsInfo.pContent));
                            }
                        }
                    });
                }
            });
        }else{
            toolbar.setTitle("网络不可用");
        }
    }

    /**
     * 访问网络获取数据
     */
    private ColdDetailsInfo getServiceData(String url) {
        ColdDetailsInfo detailsInfo = new ColdDetailsInfo();
        try {
            // 从一个URL加载一个Document对象
            Document document = Jsoup.connect(url).get();
            if (document != null) {
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
                    sb.append("\u3000\u3000" + elements.get(i).html() + "<br/><br/>");
                }
                detailsInfo.pContent = sb.toString();
                // 选择上一篇所在的节点
                element = document.select(".article-nav-prev a").first();
                if (element != null) {
                    detailsInfo.prevUrl = element.attr("href");
                }
                // 选择下一篇所在的节点
                element = document.select(".article-nav-next a").first();
                detailsInfo.nextUrl = element.attr("href");
                // 设置阅读数
                detailsInfo.read = RandomUtils.getReadRandom();
                // 设置赞数
                detailsInfo.star = RandomUtils.getStarRandom();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailsInfo;
    }
}
