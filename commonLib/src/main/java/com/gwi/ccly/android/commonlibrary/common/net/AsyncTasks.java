package com.gwi.ccly.android.commonlibrary.common.net;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.gwi.ccly.android.commonlibrary.ui.view.LoadingDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 彭毅
 */
public class AsyncTasks {

    public static final String TAG = "AsyncTasks";
    /**
     * 异步任务，非UI线程。
     */
    @SuppressWarnings("rawtypes")
    private static List<AsyncTask> mAsyncTasks = new ArrayList<AsyncTask>();

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAXIMUM_POOL_SIZE = 128;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "ModernAsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
            10);

    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory);

    /**
     * 提示性异步加载
     *
     * @param message
     * @param callback
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <T> void doAsyncTask(final Context context, final CharSequence message, final AsyncCallback<T> callback, final boolean cancellable) {
        AsyncTask<Void, Void, T> task = new AsyncTask<Void, Void, T>() {
            private LoadingDialog mLD;
            Exception mException = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                this.mLD = new LoadingDialog(context, message);
                mLD.setCancelable(cancellable);
                mLD.setCanceledOnTouchOutside(false);
                mLD.show();
            }

            @Override
            protected T doInBackground(Void... params) {
                try {
                    if (isCancelled()) {
                        return null;
                    }
                    return callback.callAsync();
                } catch (Exception e) {
                    mException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(T result) {
                mLD.dismiss();
                if (isCancelled()) {
                    mException = new CancelledException("任务取消");
                }
                if (mException == null) {
                    callback.onPostCall(result);
                } else {
                    callback.onCallFailed(mException);
                }
                super.onPostExecute(result);
            }
        };
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            mAsyncTasks.add(task.executeOnExecutor(THREAD_POOL_EXECUTOR,
                    (Void[]) null));
        } else {
            mAsyncTasks.add(task.execute());
        }
    }

    public static <T> void doSilenceAsyncTask(
            final AsyncCallback<T> callback) {
        doAsyncTask(null, callback);
    }

    /**
     * 非阻塞UI的异步加载
     *
     * @param loadingView
     * @param callback
     */
    public static <T> void doAsyncTask(final View loadingView,
                                       final AsyncCallback<T> callback) {
        AsyncTask<Void, Void, T> task = new AsyncTask<Void, Void, T>() {
            Exception mException = null;

            @Override
            protected void onPreExecute() {
                if (loadingView != null) {
                    loadingView.setVisibility(View.VISIBLE);
                }
                super.onPreExecute();
            }

            @Override
            protected T doInBackground(Void... params) {
                try {
                    if (isCancelled()) {
                        return null;
                    }
                    return callback.callAsync();
                } catch (Exception e) {
                    mException = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(T result) {
                if (loadingView != null) {
                    loadingView.setVisibility(View.GONE);
                }

                if (isCancelled()) {
                    mException = new CancelledException("任务取消");
                }
                if (mException == null) {
                    callback.onPostCall(result);
                } else {
                    callback.onCallFailed(mException);
                }
                super.onPostExecute(result);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mAsyncTasks.add(task.executeOnExecutor(THREAD_POOL_EXECUTOR,
                    (Void[]) null));
        } else {
            mAsyncTasks.add(task.execute());
        }
    }

    /**
     * 清除所有的异步任务
     */
    @SuppressWarnings("rawtypes")
    public static void clearAsyncTask() {
        Iterator<AsyncTask> iterator = mAsyncTasks.iterator();
        while (iterator.hasNext()) {
            AsyncTask asyncTask = iterator.next();
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        mAsyncTasks.clear();
    }

    public static <T> void doSilAsyncTask(
            View loadingView,
            final AsyncCallback<T> callback) {
        doAsyncTask(loadingView, callback);
    }

    /**
     * 异步任务取消异常
     *
     * @author user
     */
    public static class CancelledException extends Exception {

        public CancelledException(String message) {
            super(message);
        }

        /**
         *
         */
        private static final long serialVersionUID = 9156321953005607267L;

    }
}
