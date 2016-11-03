package com.gwi.selfplatform.common.exception;


/**
 * 封装Exception的基类
 * @author Peng Yi
 *
 */
public class BaseException extends Exception {
    
    private Integer mErrorCode;

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BaseException() {
        super();
    }

    public BaseException(String detailMessage) {
        super(detailMessage);
    }
    
    public BaseException(int errorCode,String detailMessage) {
        super(detailMessage);
        mErrorCode = errorCode;
    }

    public Integer getErrorCode() {
        return mErrorCode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setmErrorCode(Integer mErrorCode) {
        this.mErrorCode = mErrorCode;
    }
    
    

}
