package com.gwi.selfplatform.module.net.beans;

import java.util.Date;

public class PhrSignRecQuery {
    private String EhrID;
    private int SignCode;
    private int Count;
    private Date StartTime;
    private Date EndTime;

    public PhrSignRecQuery() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PhrSignRecQuery(String ehrID, int signCode, int count) {
        super();
        EhrID = ehrID;
        SignCode = signCode;
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    public String getEhrID() {
        return EhrID;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public int getSignCode() {
        return SignCode;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setCount(int count) {
        Count = count;
    }

    public void setEhrID(String ehrID) {
        EhrID = ehrID;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
    }

    public void setSignCode(int signCode) {
        SignCode = signCode;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

}
