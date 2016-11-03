package com.gwi.selfplatform.db;

import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;

import java.util.List;

/**
 * ����DB���ҵ���߼�
 * 
 * @author Peng Yi
 * 
 */
public enum DBController {
    INSTANCE;
    public static final String TAG = "DBController";

    public enum DataRange {
        day, week, month, season, year
    }

    public T_UserInfo getLastLoginedUser(Long userId) {
        try {
            return DBHelper.getSingleInstance().getUser(userId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean saveUser(T_UserInfo userInfo) {
        try {
            DBHelper.getSingleInstance().addUserInfo(userInfo);
        } catch (Exception e) {
           Logger.e(TAG,e.getLocalizedMessage());
           return false;
        }
        return true;
    }

    public List<T_Phr_SignRec> getNotUpdatedSignDataList(String ehrId) {
        return DBHelper.getSingleInstance().getNotUpdatedSignList(ehrId);
    }
    
    public boolean saveFamilyAccount(T_Phr_BaseInfo baseinfo){
        try {
            DBHelper.getSingleInstance().addBaseInfo(baseinfo);
        } catch (Exception e) {
            Logger.e(TAG, "saveFamilyAccount",e);
            return false;
        }
        
        return true;
    }

    public T_Phr_BaseInfo getFamilyAccount(String ehrId,long userId) {
        try {
            return DBHelper.getSingleInstance().getBaseInfo(ehrId, userId);
        } catch (Exception e) {
            Logger.e(TAG, "getFamilyAccount",e);
        }
        return null;
    }

}
