package com.gwi.selfplatform.module.net.connector.implement.gResponse;

/**
 * @author 彭毅
 * @date 2015/5/25.
 */
public class Base_AddressDict {
    private String Code;
    private String ParentCode;
    private String Name;
    private String Memo;
    private String WbCode;
    private String PyCode;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParentCode() {
        return ParentCode;
    }

    public void setParentCode(String parentCode) {
        ParentCode = parentCode;
    }

    public String getPyCode() {
        return PyCode;
    }

    public void setPyCode(String pyCode) {
        PyCode = pyCode;
    }

    public String getWbCode() {
        return WbCode;
    }

    public void setWbCode(String wbCode) {
        WbCode = wbCode;
    }
}
