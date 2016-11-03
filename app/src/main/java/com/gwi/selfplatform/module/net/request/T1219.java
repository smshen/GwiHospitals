package com.gwi.selfplatform.module.net.request;

/**
 * 诊间报到[1219]
 * Created by 毅 on 2016-1-15.
 */
public class T1219 extends TBody {
    private String PatientID;
    private String RegID;

    public String getPatientID() {
        return PatientID;
    }

    public void setPatientID(String patientID) {
        PatientID = patientID;
    }

    public String getRegID() {
        return RegID;
    }

    public void setRegID(String regID) {
        RegID = regID;
    }
}
