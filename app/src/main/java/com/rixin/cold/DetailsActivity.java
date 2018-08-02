package com.rixin.cold;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.ColdDBOpenHelper;
import com.rixin.cold.utils.DBUtils;
import com.rixin.cold.utils.NetworkUtils;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DetailsActivity extends AppCompatActivity {

    private ColdDetailsInfo mDetailsInfo;
    private ImageView mPic;
    private TextView mFrom;
    private TextView mTitle;
    private TextView mContent;
    private TextView mReadCount;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private boolean isStar = false; // 是否已经收藏
    private ColdDBOpenHelper helper;
    private String url;
    private String title;
    private String thumb;
    private String des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        PushAgent.getInstance(this).onAppStart();

        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setTitle(R.string.title_activity_details);
        toolbar.inflateMenu(R.menu.details_main);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_share) {
                    // TODO 分享
                    UMWeb web = new UMWeb(url, title, des, new UMImage(DetailsActivity.this, thumb));
                    new ShareAction(DetailsActivity.this).withMedia(web)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                            .setCallback(new UMShareListener() {
                                @Override
                                public void onStart(SHARE_MEDIA share_media) {
                                    Log.e("---start---", share_media.toString());
                                }

                                @Override
                                public void onResult(SHARE_MEDIA share_media) {
                                    Log.e("---result---", share_media.toString());
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    Log.e("---error---", share_media.toString());
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {
                                    Log.e("---cancel---", share_media.toString());
                                }
                            }).open();
                }
                return false;
            }
        });

        initView();

    }

    private void initView() {
        helper = new ColdDBOpenHelper(UIUtils.getContext());

        mPic = (ImageView) findViewById(R.id.iv_details_pic);
        mFrom = (TextView) findViewById(R.id.tv_details_from);
        mTitle = (TextView) findViewById(R.id.tv_details_title);
        mContent = (TextView) findViewById(R.id.tv_details_content);
        mReadCount = (TextView) findViewById(R.id.tv_details_reader);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        url = getIntent().getStringExtra(GlobalConstants.DETAILS_URL_KEY);

        if (NetworkUtils.isNetworkConnected(UIUtils.getContext())) {
            ThreadManager.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    mDetailsInfo = getServiceData(url);
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDetailsInfo != null) {
                                Glide.with(UIUtils.getContext()).load(thumb = mDetailsInfo.getPicUrl()).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(mPic);
                                mFrom.setText("文章来源：" + url);
                                mTitle.setText(title = mDetailsInfo.getTitle() + "?");
                                mContent.setText(Html.fromHtml(des = mDetailsInfo.getpContent()));
                                mReadCount.setText("阅读(" + mDetailsInfo.getRead() + ")");
                                if (DBUtils.query(helper, mDetailsInfo.getTitle())) {
                                    isStar = true;
                                    fab.setImageResource(R.drawable.ic_star_selected);
                                }
                                // 图文显示后才允许收藏
                                starAble();
                            }
                        }
                    });
                }
            });
        } else {
            toolbar.setTitle("当前网络不可用");
            mReadCount.setVisibility(View.GONE);
        }
    }

    /**
     * 收藏
     */
    public void starAble() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStar) {
                    isStar = true;
                    // 收藏
                    boolean success = DBUtils.insert(helper, mDetailsInfo.getTitle(), mDetailsInfo.getPicUrl(), url, mDetailsInfo.getRead());
                    if (success) {
                        fab.setImageResource(R.drawable.ic_star_selected);
                        Snackbar.make(view, "收藏成功，在我的收藏中查看", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    isStar = false;
                    boolean success = DBUtils.delete(helper, mDetailsInfo.getTitle());
                    if (success) {
                        fab.setImageResource(R.drawable.ic_star_normal);
                        Snackbar.make(view, "取消收藏，感谢您的阅读", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
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
                Element element = document.select("#title").first();
                detailsInfo.setTitle(element.select("h1").text());
                // 选择图片所在节点
                element = document.select("#neir img").first();
                detailsInfo.setPicUrl(element.attr("src"));
                // 选择内容所在节点
                Elements elements = document.select("#neir p");
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i < elements.size() - 1; i++) {
                    if (elements.get(i).html().contains("&nbsp;")) {
                        continue;
                    }
                    sb.append(elements.get(i).html() + "<br/><br/>");
                }
                detailsInfo.setpContent(sb.toString());
                // 选择阅读数所在节点
                String str = document.select("#title em").text();
                String[] strs = str.split("个");
                detailsInfo.setRead(Integer.parseInt(strs[0].substring(27)) + 100);
                // 选择上一篇所在的节点
                element = document.select("#sx span a").first();
                if (element != null) {
                    detailsInfo.setPrevUrl(element.attr("href"));
                }
                // 选择下一篇所在的节点
                element = document.select(".sx-r a").first();
                detailsInfo.setNextUrl(element.attr("href"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailsInfo;
    }

    /**
     * 友盟统计
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(UIUtils.getContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(UIUtils.getContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
