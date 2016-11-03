package com.gwi.selfplatform.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.NetworkUtil;

/**
 * 主要用于显示《服务条款》。
 * 
 * @author Peng Yi
 * 
 */
public class WebDialog extends BaseDialog {

    private static String TAG = "WebDialog";

    private WebView mWebView = null;
    private View mLoadingView = null;
    
    private boolean mIsCheckNetwork;

    private OnWebDialogErrorListener mOnWebDialogErrorListener;

    public WebDialog(Context context) {
        super(context);
        initViews();
        initEvents();
    }
    
    public void setCheckNetwork(boolean isCheck) {
        mIsCheckNetwork = isCheck;
    }

    private void initViews() {
        setDialogContentView(R.layout.layout_baseweb);
        mWebView = (WebView) findViewById(R.id.baseweb_web_container);
        mLoadingView = findViewById(R.id.baseweb_loading_indicator);

        showHeader(true);
        showFooter(true);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initEvents() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Logger.d(TAG, "onPageStarted");
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Logger.d(TAG, "onPageFinished");
                dismissProgress();
            }

            @Override
            public void onReceivedSslError(WebView view,
                    SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            private void showProgress() {
                mLoadingView.setVisibility(View.VISIBLE);
            }

            private void dismissProgress() {
                mLoadingView.setVisibility(View.GONE);
            }
        });

    }

    public void loadUrl(String url) {
        if (url == null) {
            if (mOnWebDialogErrorListener != null) {
                mOnWebDialogErrorListener.urlError();
            }
            return;
        }
        if(mIsCheckNetwork) {
            if (!NetworkUtil.isNetworkAvailable(getContext())) {
                if (mOnWebDialogErrorListener != null) {
                    mOnWebDialogErrorListener.networkError();
                }
                return;
            }
        }
        mWebView.loadUrl(url);
    }

    public void setOnWebDialogErrorListener(OnWebDialogErrorListener listener) {
        mOnWebDialogErrorListener = listener;
    }

    /**
     * 监听web dialog加载错误事件。
     * 
     * @author Peng Yi
     * 
     */
    public interface OnWebDialogErrorListener {

        void urlError();

        void networkError();
    }

}
