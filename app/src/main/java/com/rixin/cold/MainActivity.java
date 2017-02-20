package com.rixin.cold;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.fragment.FragmentFactory;
import com.rixin.cold.utils.UIUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView mTitle;
    private ImageView mUserIcon;
    private BaseFragment mFragment;
    private TabLayout mTabs;
    private ViewPager mViewPager;

    private View headerView;
    private Toolbar mToolbar;
    private long firstTime;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
    }

    private void initView() {

        //获取当前时间
        firstTime = System.currentTimeMillis();

        /** 登录账号 */
        mUserIcon = (ImageView) headerView.findViewById(R.id.iv_userIcon);
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "登录账号", Toast.LENGTH_SHORT).show();
            }
        });

        /** Toolbar标题 */
        mTitle = (TextView) this.findViewById(R.id.tv_app_title);

        /** 图文精选 */
        mTabs = (TabLayout) findViewById(R.id.tab_indicator);
        mViewPager = (ViewPager) findViewById(R.id.vp_category);
        setImageTextVisibility(false);

        /** 每日一冷 */
        mFragment = FragmentFactory.createFragment(FragmentFactory.EVERYDAY_FRAGMENT);
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mFragment).commit();
    }

    /**
     * 设置图文精选的Tab和ViewPager的显示或隐藏
     *
     * @param flag
     */
    private void setImageTextVisibility(boolean flag) {
        if (flag) {
            mTabs.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);

            // 设置Toolbar可滑动
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, UIUtils.dipTopx(46));
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
            mToolbar.setLayoutParams(params);
        } else {
            mTabs.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            // 设置Toolbar不可滑动
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, UIUtils.dipTopx(46));
            params.setScrollFlags(0);
            mToolbar.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_everyday) {
            setImageTextVisibility(false);
            fab.setVisibility(View.GONE);
            mTitle.setText(R.string.nav_everyday);
            mFragment = FragmentFactory.createFragment(FragmentFactory.EVERYDAY_FRAGMENT);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mFragment).commit();
            Toast.makeText(UIUtils.getContext(), R.string.nav_everyday, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_image_txt) {
            setImageTextVisibility(true);
            fab.setVisibility(View.VISIBLE);
            mTitle.setText(R.string.nav_image_txt);
            mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
            mTabs.setupWithViewPager(mViewPager);
            //移除fragment
            getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
        } else if (id == R.id.nav_video) {
            setImageTextVisibility(false);
            fab.setVisibility(View.VISIBLE);
            mTitle.setText(R.string.nav_video);
            mFragment = FragmentFactory.createFragment(FragmentFactory.VIDEO_FRAGMENT);
            this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mFragment).commit();
        } else if (id == R.id.nav_collect) {
            toActivity(UIUtils.getString(R.string.nav_collect));
        } else if (id == R.id.nav_sponsor) {
            toActivity(UIUtils.getString(R.string.nav_sponsor));
        } else if (id == R.id.nav_app) {
            toActivity(UIUtils.getString(R.string.nav_app));
        } else if (id == R.id.nav_setting) {
            toActivity(UIUtils.getString(R.string.nav_setting));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toActivity(String value) {
        Intent intent = new Intent(UIUtils.getContext(), OtherActivity.class);
        intent.putExtra("title", value);
        startActivity(intent);
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] mTitles;

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            mTitles = UIUtils.getStringArray(R.array.tabs_title);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.createPageFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
