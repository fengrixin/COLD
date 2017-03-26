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
import com.rixin.cold.utils.ColdDBOpenHelper;
import com.rixin.cold.utils.DBUtils;
import com.rixin.cold.utils.NetworkUtils;
import com.rixin.cold.utils.RandomUtils;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class DetailsActivity extends AppCompatActivity {

    private ColdDetailsInfo mDetailsInfo;
    private ImageView mPic;
    private ImageView mShare;
    private TextView mTitle;
    private TextView mContent;
    private TextView mReadCount;
    private TextView mStarCount;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private int readCount;
    private int starCount;
    private boolean isStar = false; // 是否已经收藏
    private ColdDBOpenHelper helper;
    private String url;

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

        ShareSDK.initSDK(this);

        initView();

    }

    private void initView() {
        helper = new ColdDBOpenHelper(UIUtils.getContext());

        mPic = (ImageView) findViewById(R.id.iv_details_pic);
        mShare = (ImageView) findViewById(R.id.iv_details_share);
        mTitle = (TextView) findViewById(R.id.tv_details_title);
        mContent = (TextView) findViewById(R.id.tv_details_content);
        mReadCount = (TextView) findViewById(R.id.tv_details_reader);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mStarCount = (TextView) findViewById(R.id.tv_details_star);

        readCount = getIntent().getIntExtra(GlobalConstants.READCOUNT_KEY, 2341);
        starCount = getIntent().getIntExtra(GlobalConstants.STARCOUNT_KEY, 1547);

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
                                Glide.with(UIUtils.getContext()).load(mDetailsInfo.picUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(mPic);
                                mTitle.setText(mDetailsInfo.title + "?");
                                mContent.setText(Html.fromHtml(mDetailsInfo.pContent));
                                if (DBUtils.query(helper, mDetailsInfo.title)) {
                                    isStar = true;
                                    fab.setImageResource(R.mipmap.ic_star_selected);
                                }
                                // 图文显示后才允许收藏
                                starAble();
                            }
                        }
                    });
                }
            });
        } else {
            toolbar.setTitle("网络不可用");
        }
        mReadCount.setText("阅读(" + readCount + ")");
        mStarCount.setText("赞(" + starCount + ")");
    }

    /**
     *  收藏
     */
    public void starAble(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isStar) {
                    isStar = true;
                    // 收藏
                    boolean success = DBUtils.insert(helper, mDetailsInfo.title, mDetailsInfo.picUrl, url, readCount, starCount);
                    if (success) {
                        fab.setImageResource(R.mipmap.ic_star_selected);
                        Snackbar.make(view, "收藏成功，在我的收藏中查看", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    isStar = false;
                    boolean success = DBUtils.delete(helper, mDetailsInfo.title);
                    if (success) {
                        fab.setImageResource(R.mipmap.ic_star_normal);
                        Snackbar.make(view, "取消收藏，感谢您的阅读", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare();
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

    /**
     * 一键分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(mDetailsInfo.title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mDetailsInfo.pContent);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mDetailsInfo.picUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("请输入评论内容...");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(GlobalConstants.SERVICE_URL);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 隐藏编辑页面
        oks.setSilent(true);
        // 启动分享GUI
        oks.show(this);
    }
}
