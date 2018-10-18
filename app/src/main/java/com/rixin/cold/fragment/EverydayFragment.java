package com.rixin.cold.fragment;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.rixin.cold.R;
import com.rixin.cold.domain.ColdDetailsInfo;
import com.rixin.cold.global.GlobalConstants;
import com.rixin.cold.utils.Logger;
import com.rixin.cold.utils.SPUtils;
import com.rixin.cold.utils.ThreadManager;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.CardConfig;
import com.rixin.cold.widget.CardItemTouchHelperCallback;
import com.rixin.cold.widget.CardLayoutManager;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.OnSwipeListener;
import com.rixin.cold.adapter.TanTanAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 开屏即冷
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class EverydayFragment extends BaseFragment {

    private Document document;
    private RecyclerView rvList;
    private TanTanAdapter mAdapter;
    private List<ColdDetailsInfo> mList = new ArrayList<>();

    // 当前要显示的页面
    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private int currentState = SUCCESS;

    @Override
    public View onCreateSuccessPage() {
        View view = UIUtils.inflate(R.layout.fragment_everyday);
        rvList = (RecyclerView) view.findViewById(R.id.rv_list);

        if (mList.size() != 0) {
            initView();
        }

        return view;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {

        // 加载图文的服务器数据
        mList = getServiceData();

        // 加载对应的页面
        if (currentState == SUCCESS) {
            return LoadingPage.ResultState.STATE_SUCCESS;
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }

    private void initView() {
        mAdapter = new TanTanAdapter(getContext(), mList);
        rvList.setAdapter(mAdapter);

        CardItemTouchHelperCallback callback = new CardItemTouchHelperCallback(mAdapter, mList);
        callback.setOnSwipedListener(new OnSwipeListener<ColdDetailsInfo>() {
            @Override
            public void onSwiping(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                TanTanAdapter.TanTanViewHolder holder = (TanTanAdapter.TanTanViewHolder) viewHolder;
                holder.itemView.setAlpha(1 - Math.abs(ratio) * 0.2f);
                if (direction == CardConfig.SWIPING_LEFT) {
                    holder.ivDisLike.setAlpha(Math.abs(ratio));
                } else if (direction == CardConfig.SWIPING_RIGHT) {
                    holder.ivLike.setAlpha(Math.abs(ratio));
                } else {
                    holder.ivLike.setAlpha(0f);
                    holder.ivDisLike.setAlpha(0f);
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, ColdDetailsInfo o, int direction) {
                TanTanAdapter.TanTanViewHolder holder = (TanTanAdapter.TanTanViewHolder) viewHolder;
                holder.itemView.setAlpha(1f);
                holder.ivDisLike.setAlpha(0f);
                holder.ivLike.setAlpha(0f);

                // 存储最后查看的图文 URL
                SPUtils.setString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_URL_KEY, o.getNextUrl());
            }

            @Override
            public void onSwipedClear() {
                Snackbar.make(rvList, "这一批阅读完啦，下一批正在拼命赶来中...", Snackbar.LENGTH_LONG).show();
                // 加载下一批图文的服务器数据
                ThreadManager.getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<ColdDetailsInfo> list = getServiceData();
                        UIUtils.runOnUIThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.inflateData(list);
                            }
                        });
                    }
                });
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        CardLayoutManager manager = new CardLayoutManager(rvList, touchHelper);
        rvList.setLayoutManager(manager);
        touchHelper.attachToRecyclerView(rvList);
    }

    /**
     * 访问网页获取数据
     *
     * @return
     */
    private List<ColdDetailsInfo> getServiceData() {
        List<ColdDetailsInfo> list = new ArrayList<>();
        String url = SPUtils.getString(UIUtils.getContext(), GlobalConstants.EVERYDAY_CURRENT_URL_KEY, GlobalConstants.EVERYDAY_SERVICE_URL);
        for (int j = 0; j < 20; j++) {
            try {
                ColdDetailsInfo detailsInfo = new ColdDetailsInfo();
                detailsInfo.setUrl(url);
                // 从一个URL加载一个Document对象
                document = nextImageText(url);
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
                    detailsInfo.setNextUrl(url = element.attr("href"));

                    list.add(detailsInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
                currentState = ERROR;
            }
        }
        return list;
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
