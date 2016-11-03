package com.gwi.selfplatform.ui.activity.expand;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.daxi.navi.Navigation;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class IndoorNavActivity extends HospBaseActivity {
    private Navigation mNavigation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏应用程序的标题栏，即当前activity的标题栏
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_indoor_nav);
        initNavigation();
    }

    private void initNavigation() {
        mNavigation = new Navigation();
        mNavigation.setIsWebRes(true);
        mNavigation.setToken("9eb6d5e7ef28a2d82d71fae2e0785506aa5add48");
        mNavigation.Initialize(this, "陈哲", "00100102", "13512345678", "B000A11DA2", mHandler);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Navigation.DATA_LOAD_FROM_LOCAL) {
                mNavigation.ShowMainPage();
            } else if (msg.what == Navigation.DATA_NOT_EXIST) {
                showToast("数据加载失败");
            } else if (msg.what == Navigation.DATA_UPDATING) {
                showToast("数据加载中...");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1020) {
            finish();
        }
    }
}
