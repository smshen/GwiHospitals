package com.gwi.selfplatform.ui.base;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseFragment;
import com.gwi.phr.hospital.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Fragemnt基类
 * @author 彭毅 
 *
 */
public class HospBaseFragment extends BaseFragment {
    
    private String mPageName = HospBaseFragment.class.getSimpleName();

    AlertDialog.Builder mBuilder;
    View mLoadingView;
    AlertDialog mAlertDialog;
    
    protected HospBaseActivity getBaseActivity() {
        return (HospBaseActivity) getActivity();
    }
    
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    protected void showLoadingDialog(boolean cancelable) {
        //显示基于Layout的AlertDialog
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(getContext(), R.style.DialogFull);
            if (mLoadingView == null) {
                mLoadingView = View.inflate(getContext(), R.layout.layout_loading, null);
                mBuilder.setView(mLoadingView);
            }
        }
        if (mAlertDialog == null) {
            mAlertDialog = mBuilder.create();
        }
        mAlertDialog.setCanceledOnTouchOutside(cancelable);
        mAlertDialog.setCancelable(cancelable);
        Window win = mAlertDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);

      /*  Window window = mAlertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        mAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mAlertDialog.show();*/

        mAlertDialog.show();
    }


    protected void hideLoadingDialog() {
        if (mAlertDialog != null&&mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public void showLoadingDialog(String text) {
        //显示基于Layout的AlertDialog
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(getContext(), R.style.DialogFull);
            if (mLoadingView == null) {
                mLoadingView = View.inflate(getContext(), R.layout.layout_loading, null);
                mBuilder.setView(mLoadingView);
            }
        }
        if (mAlertDialog == null) {
            mAlertDialog = mBuilder.create();
        }
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(true);
        Window win = mAlertDialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);

      /*  Window window = mAlertDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        mAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mAlertDialog.show();*/

        mAlertDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mAlertDialog != null&&mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }
}
