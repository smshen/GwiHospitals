package com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response;

/**
 * @author 彭毅
 * @date 2015/4/22.
 */
public class GResponse<R> {

    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int ERROR_EXCEPTION = 3;

    GHeader Header;

    R Body;

    public GHeader getHeader() {
        return Header;
    }

    public void setHeader(GHeader header) {
        Header = header;
    }

    public R getBody() {
        return Body;
    }

    public void setBody(R body) {
        Body = body;
    }
}
