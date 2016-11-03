package com.gwi.selfplatform.module.net.response;


public class GHeader<T>{
    private int FunCode;
    private int Status;
    private String ResultMsg;
    private String OpTime;
    //added by 长沙市中心医院
    private int ErrorCode;
    private T body;
    
    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public int getFunCode() {
        return FunCode;
    }

    public void setFunCode(int funCode) {
        FunCode = funCode;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getResultMsg() {
        return ResultMsg;
    }

    public void setResultMsg(String resultMsg) {
        ResultMsg = resultMsg;
    }

    public String getOpTime() {
        return OpTime;
    }

    public void setOpTime(String opTime) {
        OpTime = opTime;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
    
    
}
