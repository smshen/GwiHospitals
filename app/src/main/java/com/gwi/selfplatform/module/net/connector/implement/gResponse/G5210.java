package com.gwi.selfplatform.module.net.connector.implement.gResponse;

import com.gwi.selfplatform.db.gen.T_Phr_SignRec;

import java.util.List;

/**
 * @author 彭毅
 * @date 2015/5/20.
 */
public class G5210 extends GBase {
    private List<T_Phr_SignRec> Phr_SignRecs;

    public List<T_Phr_SignRec> getPhr_SignRecs() {
        return Phr_SignRecs;
    }

    public void setPhr_SignRecs(List<T_Phr_SignRec> phr_SignRecs) {
        Phr_SignRecs = phr_SignRecs;
    }
}
