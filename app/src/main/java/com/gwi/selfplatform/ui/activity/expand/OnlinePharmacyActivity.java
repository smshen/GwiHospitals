package com.gwi.selfplatform.ui.activity.expand;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.NetworkUtil;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.FontProgressBar;

/**
 * 在线药房
 */
public class OnlinePharmacyActivity extends HospBaseActivity implements FontProgressBar.onValueChangeListener{

    public final static String KEY_URL = "key_web_url";
    public final static String KEY_TITLE = "key_title";

    protected WebView mWebView = null;
    private ProgressBar mProgressBar = null;
    private View mIndicator = null;

    protected String mUrl = null;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pharmacy);
        initViews();
        initEvents();

        loadUrl(mUrl,true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_web_base, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void initViews() {
        Bundle b = getIntent().getExtras();
        if(b!=null&&b.containsKey(KEY_URL)) {
            setTitle(b.getString(KEY_TITLE));
            mUrl = b.getString(KEY_URL);
        }

        mIndicator = findViewById(R.id.baseweb_loading_indicator);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
        mWebView = (WebView) findViewById(R.id.baseweb_web_container);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setTextZoom(GlobalSettings.INSTANCE.getFontSize() * 100 / 16);
    }

    @Override
    protected void initEvents() {
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgress();
            }

            private void showProgress() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            private void dismissProgress() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_font_change:
                showFontChangePopWindow();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    BaseDialog dialog;

    private void showFontChangePopWindow() {
        if (dialog == null) {
            dialog = new BaseDialog(this);
            ViewGroup content = (ViewGroup) dialog.setDialogContentView(R.layout.layout_pop_font);
            FontProgressBar fpb = (FontProgressBar) content.findViewById(R.id.pop_font_pb);
            fpb.setOnValueChangeListner(this);
            dialog.setTitle("提示");
            dialog.showHeader(true);
            dialog.showFooter(false);
        }
        dialog.show();
    }

    protected void loadUrl(String url, boolean isNeedCheckNetwork) {
        if (url == null) {
            showToast("url 错误，请检查url地址");
            return;
        }
        if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
            if (isNeedCheckNetwork) {
                if (!NetworkUtil.isNetworkAvailable(this)) {
                    showToast(R.string.msg_network_disconnected);
                    return;
                }
            }
        }

        mWebView.loadUrl(url);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onValueChanged(int value) {
        WebSettings settings = mWebView.getSettings();
        settings.setTextZoom(100 * value / 16);
        GlobalSettings.INSTANCE.setFontSize(value);
    }
}
