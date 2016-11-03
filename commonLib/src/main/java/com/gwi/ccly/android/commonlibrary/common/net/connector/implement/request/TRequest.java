package com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request;

/**
 * API请求实体
 * @author 彭毅
 * @date 2015/4/23.
 */
public class TRequest<T> {
    THeader Header;
    T Body;

    public THeader getHeader() {
        return Header;
    }

    public void setHeader(THeader header) {
        Header = header;
    }

    public T getBody() {
        return Body;
    }

    public void setBody(T body) {
        Body = body;
    }

}
