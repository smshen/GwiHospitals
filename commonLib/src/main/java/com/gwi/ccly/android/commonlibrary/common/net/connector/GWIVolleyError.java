package com.gwi.ccly.android.commonlibrary.common.net.connector;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * 保存header status code,用于业务处理.
 * @author 彭毅
 * @date 2015/5/25.
 */
public class GWIVolleyError extends VolleyError {
    private int mStatus;

    public GWIVolleyError(int status,Throwable throwable) {
        super(throwable);
        this.mStatus = status;
    }

    public GWIVolleyError(Throwable throwable) {
       this(-1,throwable);
    }

    public GWIVolleyError(NetworkResponse response) {
        super(response);
        this.mStatus = -1;
    }

    public int getStatus() {
        return mStatus;
    }
}
