package com.gwi.selfplatform.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseActivity;
import com.gwi.ccly.android.commonlibrary.ui.view.LoadingDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.BaseApplication;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.HospitalParams;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Activity基类。抽象和管理activity统一的动作和数据。
 * 
 * @author pengyi
 * 
 */
public class HospBaseActivity extends BaseActivity {

    private static final int FLAG_NO = -100;

    /**
     * 异步任务，非UI线程。
     */
    protected List<AsyncTask<Void, Void, Object>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Object>>();

    /**
     * 发送循环消息的Handler，实现广告循环显示
     */
    protected Handler mHandler = null;

    /**
     * 加载dialog
     */
    Dialog mLoadingDialog = null;

    //从中心医院开始添加，特殊的首页图标标题处理
    protected Toolbar mToolBar;
    protected TextView mToolBarTitle;

    /**
     * 初始化视图。
     */
    protected void initViews(){

    }

    /**
     * 初始化事件监听。
     */
    protected void initEvents(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setTranslucentStatus(this, true);
        Logger.d("base", "onCreate");
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        clearAsyncTask();
        AppManager.getInstance().killActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Logger.d("HospBaseActivity", "onPostResume");
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        Logger.d("HospBaseActivity", "onTitleChanged");
        if (mToolBarTitle != null) {
            mToolBarTitle.setText(title);
        }
    }

    private void initToolbar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolBar != null) {
//            mToolBar.setBackgroundResource(R.drawable.bg_action_bar);
            mToolBar.setNavigationIcon(R.drawable.arrow_left);
            setSupportActionBar(mToolBar);
            mToolBarTitle = (TextView) findViewById(R.id.toolbar_title);
            if (mToolBarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
    }

    @TargetApi(19)
    public void setTranslucentStatus(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);

            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.colorPrimary));
            tintManager.setNavigationBarTintColor(activity.getResources().getColor(R.color.colorPrimary));
            tintManager.setStatusBarTintResource(R.color.colorPrimary);
        }
    }

    /**
     * 添加一个异步任务
     * 
     * @param asyncTask
     */
    protected void putAsyncTask(AsyncTask<Void, Void, Object> asyncTask) {
        mAsyncTasks.add(asyncTask.execute());
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
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle, int[] animRes) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        if (animRes == null) {
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            overridePendingTransition(animRes[0], animRes[1]);
        }
    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        openActivityForResult(pClass, requestCode, null);
    }

    /**
     * 
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
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public void showLoadingDialog(final String text) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        mLoadingDialog = new LoadingDialog(HospBaseActivity.this, text);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.show();
    }

    public Dialog getLoadingDialog() {
        return mLoadingDialog;
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    @Override
    public void showConfirmDialog(String title, DialogInterface.OnClickListener listener) {

    }

    @Override
    public void showConfirmDialog(String title, int type, DialogInterface.OnClickListener listener) {

    }

    private Toast mToast;
    public void showToast(int resId) {
        if(mToast==null) {
            mToast = Toast.makeText(this, getResources().getString(resId),Toast.LENGTH_SHORT);
        }else {
            mToast.setText(resId);
        }
//        Toast.makeText(this, getResources().getString(resId),
//                Toast.LENGTH_SHORT).show();
        mToast.show();
    }

    public void showToast(String text) {
        if(mToast==null) {
            mToast = Toast.makeText(this, text,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void showLongToast(int resId) {
        if(mToast==null) {
            mToast = Toast.makeText(this, getResources().getString(resId),Toast.LENGTH_LONG);
        }else {
            mToast.setText(resId);
        }
        mToast.show();
    }

    public void showLongToast(String text) {
        if(mToast==null) {
            mToast = Toast.makeText(this, text,Toast.LENGTH_LONG);
        }else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 获取Application唯一的BaseApplication实例
     * 
     * @return
     */
    public BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
    }


    public <T> void doCancellableAsyncTask(Context context,
                                           CharSequence message, AsyncCallback<T> callback) {
        AsyncTasks.doAsyncTask(context, message, callback, true);
    }

    protected <T> void doForcableAsyncTask(Context context,
                                           CharSequence message, AsyncCallback<T> callback) {
        AsyncTasks.doAsyncTask(context, message, callback, false);
    }
    protected void clearAsyncTask() {
        AsyncTasks.clearAsyncTask();
    }


    /**
     * 异步任务显示Indeterminate ProgressBar
     *
     * @param progressbar
     * @param callback
     */
    public <T> void doProgressAsyncTask(View progressbar,
            AsyncCallback<T> callback) {
        AsyncTasks.doAsyncTask(progressbar, callback);
    }

    public void finish(int enterAnim, int exitAnim) {
        super.finish();
        overridePendingTransition(enterAnim, exitAnim);
    }

    private int misHomeShow = 0x00;
    private int mIsFamilyChangeShow = 0x00;

    /**
     * 添加home返回键
     */
    protected void addHomeButton() {
        this.misHomeShow = 0x01;
    }
    
    protected void addChangeFamilyButton() {
        Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
        if(HospitalParams.hasFunction(params, HospitalParams.CODE_HAS_FAMILY_MANAGEMENT)) {
            mIsFamilyChangeShow = 0x2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int mask = misHomeShow|mIsFamilyChangeShow;
        if (mask==3) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_base, menu);
            MenuItem home = menu.findItem(R.id.home);
            MenuItem family = menu.findItem(R.id.family);
//            T_Phr_BaseInfo member = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
//            if(member.getName()!=null) {
//                family.setTitle(member.getName());
//            }

            home.setVisible(true);
            family.setVisible(true);
            MenuItemCompat.setShowAsAction(family, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM|MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
            MenuItemCompat.setShowAsAction(home, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        }else if(mask==1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_base, menu);
            MenuItem home = menu.findItem(R.id.home);
            MenuItem family = menu.findItem(R.id.family);
            home.setVisible(true);
            family.setVisible(false);
            MenuItemCompat.setShowAsAction(home, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        }else if(mask==2) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_base, menu);
            MenuItem home = menu.findItem(R.id.home);
            MenuItem family = menu.findItem(R.id.family);
            home.setVisible(false);
            family.setVisible(true);
//            T_Phr_BaseInfo member = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
//            if(member!=null&&member.getName()!=null) {
//                family.setTitle(member.getName());
//            }
            MenuItemCompat.setShowAsAction(family, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM|MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
        }
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        //显示图标
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Logger.e("HospBaseActivity", "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
//            PackageManager pm = getPackageManager();
//            Intent mainIntent = new Intent(Intent.ACTION_MAIN);
//            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//            List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);
//            if(list!=null) {
//                for(ResolveInfo resolveInfo:list) {
//                    if(resolveInfo.activityInfo.packageName.equals(getPackageName())) {
//                        Intent i = new Intent();
//                        String mainPackageName = resolveInfo.activityInfo.packageName;
//                        String mainClass = resolveInfo.activityInfo.name;
//                        //TODO:降版本后报错
//                        i.setComponent(new ComponentName(mainPackageName, mainClass));
////                        i.setClass(this, HomeActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivity(i);
//                        finish();
//                        overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
//                        break;
//                    }
//                }
//            }
            Intent i = new Intent();
            String home = GlobalSettings.INSTANCE.getHomeActivity();
            if(home==null) {
                home = "com.gwi.selfplatform.ui.activity.start.HomeActivity";
            }
            i.setClassName(getApplicationContext(),home);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                          | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
        } else if (item.getItemId() == android.R.id.home) {
            finish(R.anim.push_right_in, R.anim.push_right_out);
        }else if(item.getItemId()==R.id.family) {
            //TODO:
//            if(GlobalSettings.INSTANCE.isIsLogined()) {
//                openActivityForResult(FamilyExchangeActivity.class,1);
//            }else {
//                openActivity(LoginActivity.class);
//            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int[] animInOut;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (animInOut != null) {
            overridePendingTransition(animInOut[0], animInOut[1]);
        }
    }

    protected void setBackPressAnim(Integer enterAnim, Integer extiAnim) {
        animInOut = new int[]{enterAnim, extiAnim};
    }

    protected void addBackListener() {
        addBackListener(R.anim.push_right_in, R.anim.push_right_out);
    }

    protected void addBackListener(final int enterAnim, final int exitAnim) {
        setBackPressAnim(enterAnim, exitAnim);
        if (mToolBar != null) {
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HospBaseActivity.this.onBackPressed();
                    HospBaseActivity.this.overridePendingTransition(enterAnim, exitAnim);
                }
            });
        }
    }
}
