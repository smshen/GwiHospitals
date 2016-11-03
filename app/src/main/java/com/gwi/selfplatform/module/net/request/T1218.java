package com.gwi.selfplatform.module.net.request;

/**
 * 查询需报到的候诊信息[1218]
 * Created by 毅 on 2016-1-15.
 */
public class T1218 extends TBody {
    private String PatientID;

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }
}
