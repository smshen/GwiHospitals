package com.gwi.selfplatform.module.net.request;

/**
 * 执行挂号锁号
 * @author 彭毅
 * @date 2015/5/10.
 */
public class T1213 extends TBody {
    private String RegSourceID;
    private String TypeID;
    private String DeptID;
    private String DoctID;
    private String RankID;
    private String PatientID;
    private String TimeRegion;
    private String Date;
    private double TotalRegFee;
    private double RegFee;
    private double TreatFee;
    private double ServicesFee;
    private double MetaFee;
    private double PatientBookFee;
    private double OtherFee;
    private double TerTranSerNo;
    private String Note;

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getRegSourceID() {
        return RegSourceID;
    }

    public void setRegSourceID(String regSourceID) {
        RegSourceID = regSourceID;
    }

    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getDeptID() {
        return DeptID;
    }

    public void setDeptID(String deptID) {
        DeptID = deptID;
    }

    public String getDoctID() {
        return DoctID;
    }

    public void setDoctID(String doctID) {
        DoctID = doctID;
    }

    public String getRankID() {
        return RankID;
    }

    public void setRankID(String rankID) {
        RankID = rankID;
    }

    public String getTimeRegion() {
        return TimeRegion;
    }

    public void setTimeRegion(String timeRegion) {
        TimeRegion = timeRegion;
    }

    public double getTotalRegFee() {
        return TotalRegFee;
    }

    public void setTotalRegFee(double totalRegFee) {
        TotalRegFee = totalRegFee;
    }

    public double getRegFee() {
        return RegFee;
    }

    public void setRegFee(double regFee) {
        RegFee = regFee;
    }

    public double getTreatFee() {
        return TreatFee;
    }

    public void setTreatFee(double treatFee) {
        TreatFee = treatFee;
    }

    public double getServicesFee() {
        return ServicesFee;
    }

    public void setServicesFee(double servicesFee) {
        ServicesFee = servicesFee;
    }

    public double getMetaFee() {
        return MetaFee;
    }

    public void setMetaFee(double metaFee) {
        MetaFee = metaFee;
    }

    public double getPatientBookFee() {
        return PatientBookFee;
    }

    public void setPatientBookFee(double patientBookFee) {
        PatientBookFee = patientBookFee;
    }

    public double getOtherFee() {
        return OtherFee;
    }

    public void setOtherFee(double otherFee) {
        OtherFee = otherFee;
    }

    public double getTerTranSerNo() {
        return TerTranSerNo;
    }

    public void setTerTranSerNo(double terTranSerNo) {
        TerTranSerNo = terTranSerNo;
    }
}
