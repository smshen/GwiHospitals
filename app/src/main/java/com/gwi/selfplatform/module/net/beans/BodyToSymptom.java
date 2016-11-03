package com.gwi.selfplatform.module.net.beans;

public class BodyToSymptom{
    private long Code;
    private String Name;
    private String Sex;
    private String WbCode;
    private String PyCode;
    /**
     * 2015-03-09 添加
     */
    private String SId;
    

    public String getSId() {
        return SId;
    }

    public void setSId(String sId) {
        SId = sId;
    }

    public long getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getSex() {
        return Sex;
    }

    public String getWbCode() {
        return WbCode;
    }

    public String getPyCode() {
        return PyCode;
    }

    public void setCode(long code) {
        Code = code;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSex(String sex) {
        this.Sex = sex;
    }

    public void setWbCode(String wbCode) {
        WbCode = wbCode;
    }

    public void setPyCode(String pyCode) {
        PyCode = pyCode;
    }

}
