package com.gwi.ccly.android.commonlibrary.ui.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by  Kuofei Liu  on 2015-11-30.
 */
public abstract class BaseActivity extends AppCompatActivity{
    private static final int FLAG_NO = -100;
    private Toast mToast;
    /**
     * 异步任务，非UI线程。
     */
    protected List<AsyncTask<Void, Void, Object>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, FLAG_NO);
    }

    public void openActivity(Class<?> pclass, int flag) {
        openActivity(pclass, null, flag);
    }

    public void openActivity(Class<?> pclass, Bundle pBundle) {
        openActivity(pclass, pBundle, FLAG_NO);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle, int flag) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (flag != FLAG_NO) {
            intent.addFlags(flag);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle, int[] animRes) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        if (animRes == null) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            overridePendingTransition(animRes[0], animRes[1]);
        }
    }

    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    protected void openActivity(String pAction, Bundle pBundle) {
        openActivity(pAction, pBundle, null);
    }

    protected void openActivity(String pAction, Bundle pBundle, int[] animRes) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        if (animRes == null) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            overridePendingTransition(animRes[0], animRes[1]);
        }
    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        openActivityForResult(pClass, requestCode, null);
    }

    /**
     * @param pClass
     * @param requestCode
     * @param pBundle
     * @see {@link android.app.Activity#startActivityForResult}
     */
    protected void openActivityForResult(Class<?> pClass, int requestCode,
                                         Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    public void showToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void showLongToast(int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_LONG);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void showLongToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }


    public abstract void showLoadingDialog(final String text);


    public abstract void dismissLoadingDialog();

    public abstract void showConfirmDialog(final String title,
                                           DialogInterface.OnClickListener listener);

    public abstract void showConfirmDialog(final String title, int type,
                                           DialogInterface.OnClickListener listener);


    public void dismissConfirmDialog() {
        dismissLoadingDialog();
    }

    /**
     * 添加一个异步任务
     *
     * @param asyncTask
     */
    protected void putAsyncTask(AsyncTask<Void, Void, Object> asyncTask) {
        mAsyncTasks.add(asyncTask.execute());
    }

    /**
     * 静默加载
     *
     * @param callback
     */
    protected <T> void doSilenceAsyncTask(AsyncCallback<T> callback) {
        AsyncTasks.doSilenceAsyncTask(callback);
    }

    /**
     * 异步任务显示Indeterminate ProgressBar
     *
     * @param callback
     */
    public <T> void doProgressAsyncTask(
            AsyncCallback<T> callback) {
        AsyncTasks.doAsyncTask(null, callback);
    }

}
