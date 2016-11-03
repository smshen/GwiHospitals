package com.gwi.selfplatform.db;

import android.content.Context;
import android.net.Uri;

import com.gwi.selfplatform.db.gen.DaoMaster;
import com.gwi.selfplatform.db.gen.DaoMaster.OpenHelper;
import com.gwi.selfplatform.db.gen.DaoSession;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfoDao;
import com.gwi.selfplatform.db.gen.T_Phr_SignRecDao;
import com.gwi.selfplatform.db.gen.T_UserInfoDao;


/**
 * Sqlite 数据库管理类,包括数据库初始化，数据库操作。
 * @author Peng Yi
 *
 */
public enum DBManager {
    INSTANCE;
    
    
    
    private static final String DB_NAME = "gwi-phr-db";
    //自定义URI，用于数据监听
    public static final Uri CONTENT_URI = Uri.parse("mycontent://com.gwi.phr"
            + DB_NAME);
    
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private T_UserInfoDao mT_UserInfoDao;
    private T_Phr_SignRecDao mT_Phr_SignRecDao;
    private T_Phr_BaseInfoDao mT_Phr_BaseInfoDao;
    


    public void initialize(Context context) {
//        OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        //TODO:
        OpenHelper helper = new DBHelper.DBOpenHelper(context, DB_NAME, null);
        mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
        mT_UserInfoDao = mDaoSession.getT_UserInfoDao();
        mT_Phr_SignRecDao = mDaoSession.getT_Phr_SignRecDao();
        mT_Phr_BaseInfoDao = mDaoSession.getT_Phr_BaseInfoDao();
        
        DBHelper.getSingleInstance().init(context);
    }
    
    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }


    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    public T_UserInfoDao getT_UserInfoDao() {
        return mT_UserInfoDao;
    }
    
    public T_Phr_SignRecDao getT_Phr_SignRecDao() {
        return mT_Phr_SignRecDao;
    }
    
    public T_Phr_BaseInfoDao getT_Phr_BaseInfoDao() {
        return mT_Phr_BaseInfoDao;
    }
}
