package com.gwi.selfplatform.module.net.request;

/**
 * 查询价格信息的数量
 * @author 彭毅
 *
 */
public class T2010 extends TBody {
    private String PinYinCode;
    private String Type;
    public String getPinYinCode() {
        return PinYinCode;
    }
    public void setPinYinCode(String pinYinCode) {
        PinYinCode = pinYinCode;
    }
    public String getType() {
        return Type;
    }
    public void setType(String type) {
        Type = type;
    }
    
}
