package com.rixin.cold;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rixin.cold.fragment.BaseFragment;
import com.rixin.cold.fragment.EverydayFragment;
import com.rixin.cold.fragment.FragmentFactory;
import com.rixin.cold.utils.InviteCommentUtil;
import com.rixin.cold.utils.UIUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PushAgent.getInstance(this).onAppStart();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_base);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

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

        /** 显示当前版本号和渠道号 */
        mUserIcon = (ImageView) headerView.findViewById(R.id.iv_userIcon);
        mUserIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    PackageManager manager = getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                    Toast.makeText(MainActivity.this, "当前版本：v" + info.versionName, Toast.LENGTH_SHORT).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        /** Toolbar标题 */
        mTitle = (TextView) this.findViewById(R.id.tv_app_title);

        /** 图文精选 */
        mTabs = (TabLayout) findViewById(R.id.tab_indicator);
        mViewPager = (ViewPager) findViewById(R.id.vp_category);
        setImageTextVisibility(false);

        /** 每日一冷 */
        mFragment = new EverydayFragment();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mFragment).commit();

        /** 邀请好评 */
        InviteCommentUtil.startCommet(this, false);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "禁用这些权限可能会导致分享功能异常哦~~", Toast.LENGTH_LONG).show();
        }
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
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(46));
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
            mToolbar.setLayoutParams(params);
        } else {
            mTabs.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            // 设置Toolbar不可滑动
            AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, UIUtils.dip2px(46));
            params.setScrollFlags(0);
            mToolbar.setLayoutParams(params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onResume(UIUtils.getContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPause(UIUtils.getContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {
                Snackbar.make(getWindow().getDecorView(), "再按一次退出应用", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
            Intent intent = new Intent(UIUtils.getContext(), SearchActivity.class);
            startActivity(intent);
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
            mTitle.setText(R.string.nav_everyday);
            mFragment = new EverydayFragment();
            this.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mFragment).commit();
        } else if (id == R.id.nav_image_txt) {
            setImageTextVisibility(true);
            mTitle.setText(R.string.nav_image_txt);
            mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
            mTabs.setupWithViewPager(mViewPager);
            //移除fragment
            getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
        } else if (id == R.id.nav_collect) {
            toActivity(UIUtils.getString(R.string.nav_collect), 0);
        } else if (id == R.id.nav_sponsor) {
            toActivity(UIUtils.getString(R.string.nav_sponsor), 1);
        } else if (id == R.id.nav_apps) {
            Snackbar.make(getWindow().getDecorView(), "暂无推荐应用，敬请期待~", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        } else if (id == R.id.nav_discus) {
            InviteCommentUtil.startCommet(this, true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void toActivity(String value, int flag) {
        Intent intent = new Intent(UIUtils.getContext(), OtherActivity.class);
        intent.putExtra("title", value);
        intent.putExtra("flag", flag);
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
