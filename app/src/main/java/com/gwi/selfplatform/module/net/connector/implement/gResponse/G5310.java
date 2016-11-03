package com.gwi.selfplatform.module.net.connector.implement.gResponse;

import com.gwi.selfplatform.ui.view.ExTabPageIndicator;

import java.util.List;

/**
 * 查询绑定卡记录
 * @author 彭毅
 * @date 2015/5/21.
 */
public class G5310 extends GBase {
    List<ExTabPageIndicator> Phr_CardBindRec;

    public List<ExTabPageIndicator> getPhr_CardBindRec() {
        return Phr_CardBindRec;
    }

    public void setPhr_CardBindRec(List<ExTabPageIndicator> phr_CardBindRec) {
        Phr_CardBindRec = phr_CardBindRec;
    }
}
