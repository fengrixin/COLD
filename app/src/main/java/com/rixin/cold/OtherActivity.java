package com.rixin.cold;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.rixin.cold.fragment.others.SendAppFragment;
import com.rixin.cold.fragment.others.SponsorFragment;
import com.rixin.cold.fragment.others.StarFragment;
import com.umeng.analytics.MobclickAgent;

public class OtherActivity extends AppCompatActivity {

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        /** 设置Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_other);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        setSupportActionBar(toolbar);   //设置后无法响应NavigationIcon的点击事件

        initView();
    }

    private void initView() {
        /** 设置Title */
        TextView mTitle = (TextView) this.findViewById(R.id.tv_app_other_title);
        String title = this.getIntent().getStringExtra("title");
        mTitle.setText(title);
        switch (getIntent().getIntExtra("flag", 0)) {
            case 0:
                mFragment = new StarFragment();
                break;
            case 1:
                mFragment = new SponsorFragment();
                break;
            case 2:
                mFragment = new SendAppFragment();
                break;
        }
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content_other, mFragment).commit();
    }

    /**
     *  友盟统计
     */
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
