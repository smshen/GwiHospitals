package com.gwi.selfplatform.module.net.beans;

/**
 * 版本检测
 * @author 彭毅
 *
 */
public class MobileVerParam {

    private int RecNo;
    private String FileName;
    private String FilePath;
    private String VerCode;
    private String VerName;
    private java.util.Date UpdateTime;
    private String UpdateInfo;
    private String UpdateMan;

    public MobileVerParam() {
    }

    public MobileVerParam(int RecNo) {
        this.RecNo = RecNo;
    }

    public MobileVerParam(int RecNo, String FileName, String FilePath, String VerCode, java.util.Date UpdateTime, String UpdateInfo, String UpdateMan) {
        this.RecNo = RecNo;
        this.FileName = FileName;
        this.FilePath = FilePath;
        this.VerCode = VerCode;
        this.UpdateTime = UpdateTime;
        this.UpdateInfo = UpdateInfo;
        this.UpdateMan = UpdateMan;
    }
    
    

    public String getVerName() {
        return VerName;
    }

    public void setVerName(String verName) {
        VerName = verName;
    }

    public int getRecNo() {
        return RecNo;
    }

    public void setRecNo(int RecNo) {
        this.RecNo = RecNo;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }

    public String getVerCode() {
        return VerCode;
    }

    public void setVerCode(String VerCode) {
        this.VerCode = VerCode;
    }

    public java.util.Date getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(java.util.Date UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getUpdateInfo() {
        return UpdateInfo;
    }

    public void setUpdateInfo(String UpdateInfo) {
        this.UpdateInfo = UpdateInfo;
    }

    public String getUpdateMan() {
        return UpdateMan;
    }

    public void setUpdateMan(String UpdateMan) {
        this.UpdateMan = UpdateMan;
    }

}
