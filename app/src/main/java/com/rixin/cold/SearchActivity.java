package com.rixin.cold;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.rixin.cold.fragment.others.SearchResultFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SearchActivity extends AppCompatActivity {

    private Button btnSearch;
    private EditText etSearch;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /** 设置Toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
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

    public void initView() {
        etSearch = (EditText) this.findViewById(R.id.et_search);
        btnSearch = (Button) this.findViewById(R.id.btn_search);

        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                etSearch.setCursorVisible(true);
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etSearch.requestFocus() && !etSearch.getText().toString().isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
                try {
                    text = URLEncoder.encode(etSearch.getText().toString(), "UTF-8");
                    text = etSearch.getText().toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (!text.isEmpty()) {
                    FragmentManager manager = getSupportFragmentManager();
                    // 获取事务
                    FragmentTransaction transaction = manager.beginTransaction();
                    SearchResultFragment fragment = new SearchResultFragment();
                    // 使用 Bundle 进行传值
                    Bundle bundle = new Bundle();
                    bundle.putString("search_text", text);
                    // 将 Bundle 对象添加到 Fragment 中
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.content_main, fragment).commit();  // 提交事务
                } else {
                    Snackbar.make(view, "请输入内容后再进行搜索", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
                etSearch.setText("");
                etSearch.setCursorVisible(false);
            }
        });

    }

}
