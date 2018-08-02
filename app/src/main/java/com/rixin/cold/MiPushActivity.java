package com.rixin.cold;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.umeng.message.UmengNotifyClickActivity;

public class MiPushActivity extends UmengNotifyClickActivity {

    private static String TAG = MiPushActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_push);
    }
}
