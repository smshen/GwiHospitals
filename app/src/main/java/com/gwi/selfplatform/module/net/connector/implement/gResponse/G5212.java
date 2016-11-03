package com.gwi.selfplatform.module.net.connector.implement.gResponse;

import com.gwi.selfplatform.db.gen.T_Phr_SignRec;

import java.util.List;

/**
 * 查询体征记录
 * @author 彭毅
 * @date 2015/5/20.
 */
public class G5212 extends GBase {

    private List<T_Phr_SignRec> SignRecQuery;

    public List<T_Phr_SignRec> getSignRecQuery() {
        return SignRecQuery;
    }

    public void setSignRecQuery(List<T_Phr_SignRec> signRecQuery) {
        SignRecQuery = signRecQuery;
    }
}
