package com.gwi.selfplatform.module.net.response;

import java.util.Date;

import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.config.Constants;

/**
 * Date comparable.(Date format yy-MM-dd)
 * @author 彭毅
 *
 */
public class GBodyDatable implements Comparable<GBodyDatable>{
    private String Date;
    
    private String Note;
    

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    @Override
    public int compareTo(GBodyDatable another) {
        try {
            Date myDate = CommonUtils.stringPhaseDate(getDate(), Constants.FORMAT_ISO_DATE);
            Date anDate = CommonUtils.stringPhaseDate(another.getDate(), Constants.FORMAT_ISO_DATE);
           return myDate.compareTo(anDate);
        }catch(Exception e) {
            
        }
        return 0;
    }
}
