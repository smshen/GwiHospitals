package com.gwi.selfplatform.module.net.connector.implement.gResponse;

import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;

/**
 * @author 彭毅
 * @date 2015/5/20.
 */
public class GBase {
    T_UserInfo UserInfo;
    T_Phr_BaseInfo BaseInfo;

    public T_UserInfo getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(T_UserInfo userInfo) {
        UserInfo = userInfo;
    }

    public T_Phr_BaseInfo getBaseInfo() {
        return BaseInfo;
    }

    public void setBaseInfo(T_Phr_BaseInfo baseInfo) {
        BaseInfo = baseInfo;
    }
}
