package com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response;

/**
 * GWI响应实体通用头
 * @author 彭毅
 * @date 2015/4/22.
 */
public class GHeader {

    private int FunCode;
    private int Status;
    private String ResultMsg;
    private String OpTime;
    private String TranSerNo;

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

    public String getTranSerNo() {
        return TranSerNo;
    }

    public void setTranSerNo(String tranSerNo) {
        TranSerNo = tranSerNo;
    }
}
