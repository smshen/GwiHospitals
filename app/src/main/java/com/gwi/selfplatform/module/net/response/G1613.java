/**
 * 
 */
package com.gwi.selfplatform.module.net.response;

/**
 * @author 彭毅
 *
 */
public class G1613 extends GBodyDatable {
    private String ReceiptID;
    private String ReceiptName;
    /** 窗口编号 */
    private String ExecLocation;
    private String WaitNo;
    private String CurrentNo;
    
    private String DeptName;
    
    public String getDeptName() {
        return DeptName;
    }
    public void setDeptName(String deptName) {
        DeptName = deptName;
    }
    public String getReceiptID() {
        return ReceiptID;
    }
    public String getReceiptName() {
        return ReceiptName;
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
    public void setReceiptID(String receiptID) {
        ReceiptID = receiptID;
    }
    public void setReceiptName(String receiptName) {
        ReceiptName = receiptName;
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
