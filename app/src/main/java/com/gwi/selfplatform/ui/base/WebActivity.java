package com.gwi.selfplatform.ui.base;

import android.os.Bundle;
import android.view.KeyEvent;

import com.gwi.selfplatform.config.Constants;

public class WebActivity extends BaseWebActivity {
    
    public final static String KEY_URL = "key_web_url";
    public final static String KEY_TITLE = "key_title";
    
    private boolean mIsNeedCheckNetwork;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ActionBar actionBar = getSupportActionBar();
        initViews();
        initEvents();
        
        loadUrl(mUrl,mIsNeedCheckNetwork);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews() {
        super.initViews();
        Bundle b = getIntent().getExtras();
        if(b!=null&&b.containsKey(KEY_URL)) {
            setTitle(b.getString(KEY_TITLE));
            mUrl = b.getString(KEY_URL);
            mIsNeedCheckNetwork = !mUrl.equals(Constants.URL_AGGREMENT);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void initEvents() {
        super.initEvents();
    }
    
}
