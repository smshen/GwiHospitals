package com.gwi.selfplatform.module.net.response;

public class G1216 extends GBodyDatable{
    private String DeptName;
    private String RegSourceName;
    private String ExecLocation;
    private String WaitNo;
    private String CurrentNo;
    
    private String WaitNum;
    
    //长沙市中心医院
    private String FrontCount;
    
    
    public String getFrontCount() {
        return FrontCount;
    }
    public void setFrontCount(String frontCount) {
        FrontCount = frontCount;
    }
    public String getWaitNum() {
        return WaitNum;
    }
    public void setWaitNum(String waitNum) {
        WaitNum = waitNum;
    }
    public String getDeptName() {
        return DeptName;
    }
    public String getRegSourceName() {
        return RegSourceName;
    }
    public String getExecLocation() {
        return ExecLocation;
    }
    public String getWaitNo() {
        return WaitNo;
    }
    public String getCurrentNo() {
        return CurrentNo;
    }
    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
    public void setRegSourceName(String regSourceName) {
        RegSourceName = regSourceName;
    }
    public void setExecLocation(String execLocation) {
        ExecLocation = execLocation;
    }
    public void setWaitNo(String waitNo) {
        WaitNo = waitNo;
    }
    public void setCurrentNo(String currentNo) {
        CurrentNo = currentNo;
    }
}
