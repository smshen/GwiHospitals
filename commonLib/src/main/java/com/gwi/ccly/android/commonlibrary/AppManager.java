package com.gwi.ccly.android.commonlibrary;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 页面管理类，主要维护页面堆栈，确保APP能完全退出
 * 
 * @author Peng Yi
 * 
 */
public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mAppManager;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    public Activity getTopActivity() {
        if (mActivityStack == null||mActivityStack.size()==0)
            return null;
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    public void killActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
            }
        }
    }

    public void killAllActivityExcept(Class<?> cls) {
        Activity except=null;
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                if (mActivityStack.get(i).getClass().equals(cls)) {
                    except = mActivityStack.get(i);
                } else {
                    mActivityStack.get(i).finish();
                }
            }
        }
        mActivityStack.clear();
        if (except != null) {
            mActivityStack.push(except);
        }
    }

    public void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    public void AppExit(Context context) {
        try {
            killAllActivity();
        } catch (Exception e) {
        }
    }
}
