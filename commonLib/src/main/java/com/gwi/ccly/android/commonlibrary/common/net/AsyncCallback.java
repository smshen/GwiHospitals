package com.gwi.ccly.android.commonlibrary.common.net;


/**
 * 异步回调接口
 * @author 彭毅
 *
 * @param <Result>
 */
public interface AsyncCallback<Result> {
    /**
     * 执行异步线程任务，返回执行结果。如果不能完成，抛异常
     * @return 
     * @throws Exception
     */
    Result callAsync() throws Exception;
    /**
     * 异步任务回调，任务完成后被调用
     * @param result
     */
    void onPostCall(Result result);
    /**
     * 异步任务执行失败时或者被取消时，被调用,处理相应异常
     * @param exception
     */
    void onCallFailed(Exception exception);
}
