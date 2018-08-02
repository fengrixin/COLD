package com.rixin.cold.fragment.others;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.rixin.cold.R;
import com.rixin.cold.adapter.TabsRecyclerViewAdapter;
import com.rixin.cold.domain.ColdInfo;
import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.utils.ColdDBOpenHelper;
import com.rixin.cold.utils.DBUtils;
import com.rixin.cold.utils.UIUtils;
import com.rixin.cold.widget.LoadingPage;
import com.rixin.cold.widget.MyRecyclerView;

import java.util.ArrayList;

/**
 * 我的收藏
 * Created by 飘渺云轩 on 2017/3/19.
 */

public class StarFragment extends BaseFragment {

    private ArrayList<ColdInfo> mData;
    private ColdDBOpenHelper helper;
    private MyRecyclerView myRecyclerView;
    private TabsRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateSuccessPage() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mAdapter = new TabsRecyclerViewAdapter(UIUtils.getContext(), mData, R.layout.recycler_list_item_stagger);
        myRecyclerView = new MyRecyclerView(manager, mAdapter) {

            @Override
            public void onRefresh() {
                onLoadData();
                UIUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setDataChangeListener(mData);
                    }
                });
            }

            @Override
            public void onLoadMore() {
                Snackbar.make(getView(), "到底了哦，没有更多的收藏冷知识了...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }
        };
        return myRecyclerView.getView();
    }

    @Override
    public LoadingPage.ResultState onLoadData() {
        helper = new ColdDBOpenHelper(UIUtils.getContext());
        mData = DBUtils.query(helper);
        if (mData.isEmpty()) {
            UIUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("您暂时还没有收藏内容哦，请收藏后再查看哈~")
                            .setCancelable(false)
                            .setPositiveButton("好的呢", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getActivity().finish();
                                }
                            }).show();
                }
            });
            return null;
        }
        return check(mData);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
