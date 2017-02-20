package com.rixin.cold.fragment;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rixin.cold.R;
import com.rixin.cold.bean.ColdDetailsInfo;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * 每日一冷
 * Created by 飘渺云轩 on 2017/2/5.
 */

public class EverydayFragment extends BaseFragment {

    private ColdDetailsInfo detailsInfo;
    private ImageView mPic;
    private TextView mTitle;
    private TextView mContent;
    private TextView mTime;

    @Override
    public View onCreateSuccessPage() {
//        LinearLayoutManager manager = new LinearLayoutManager(UIUtils.getContext());
//        EverydayRecyclerViewAdapter adapter = new EverydayRecyclerViewAdapter(getTestList());
//        MyRecyclerView myRecyclerView = new MyRecyclerView(manager, adapter);
        View view = UIUtils.inflate(R.layout.recycler_list_item_everyday);
        mPic = (ImageView) view.findViewById(R.id.iv_everyday_list_pic);
        mTitle = (TextView) view.findViewById(R.id.tv_everyday_list_title);
        mContent = (TextView) view.findViewById(R.id.tv_everyday_list_content);
        mTime = (TextView) view.findViewById(R.id.tv_everyday_list_time);

        Glide.with(UIUtils.getContext()).load(detailsInfo.picUrl).asBitmap().into(mPic);
        mTitle.setText(detailsInfo.title);
        mContent.setText(Html.fromHtml(detailsInfo.pContent.toString()));
//        mContent.setText(detailsInfo.pContent.get(0));

        return view;
    }

    private ArrayList<String> getTestList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 30; i++) {
            list.add("第" + i + "条数据");
        }
        return list;
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        getServiceData();
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    public void getServiceData(){
        detailsInfo = new ColdDetailsInfo();
        try {
            // 从一个URL加载一个Document对象
            Document document = Jsoup.connect("http://www.lengzs100.com/21.html").get();
            // 选择标题所在节点
            Element element = document.select(".article-title").first();
            detailsInfo.title = element.select("a").text();
            // 选择图片所在节点
            element = document.select("article img").first();
            if(element == null){
                getNextServiceData();
            }
            detailsInfo.picUrl = element.attr("src");
            // 选择内容所在节点
            Elements elements = document.select("article > p");
            if (elements.size() == 1) {
                elements = document.select("article > div");
            }
//            ArrayList<String> list = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            for (int i = 1; i < elements.size() - 1; i++) {
//                list.add(elements.get(i).html());
                if (elements.get(i).html().contains("&nbsp;")) {
                    continue;
                }
                sb.append("\u3000\u3000" + elements.get(i).html() + "<br/>");
            }
            detailsInfo.pContent = sb;
//            detailsInfo.pContent = list;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNextServiceData(){

    }

}
