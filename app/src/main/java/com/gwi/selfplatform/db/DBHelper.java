package com.gwi.selfplatform.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.db.DBController.DataRange;
import com.gwi.selfplatform.db.gen.DaoMaster.OpenHelper;
import com.gwi.selfplatform.db.gen.DaoSession;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfoDao;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;
import com.gwi.selfplatform.db.gen.T_Phr_SignRecDao;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.db.gen.T_UserInfoDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.Query;

public class DBHelper {

    public static final int[] SIGNCODES = new int[] { 2/*����ѹ*/, 1/*����ѹ*/, 4/*Ѫ��*/,
            3/*����*/, 7/*���*/, 6/*����*/,5/*��֬*/};

    public static final int[] CARD_TYPE = new int[] {
        1/*������*/,2/*���ƿ�*/,3/*���п�*/,4/*�籣��*/,5/*����*/
    };

    private T_UserInfoDao mT_UserInfoDao = null;
    private T_Phr_SignRecDao mScDao = null;
    private T_Phr_BaseInfoDao mBaseInfoDao = null;
    
    private Context mContext;
    
    private Object mLockOjbect = new Object();
    
    

    /**
     * Initialization-on-demand holder idiom����֤�ӳټ��ز����̰߳�ȫ.
     * 
     * @author ����
     * 
     */
    private static class DBHelperHolder {
        private static final DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getSingleInstance() {
        return DBHelperHolder.INSTANCE;
    }

    public void init(Context context) {
        mT_UserInfoDao = DBManager.INSTANCE.getT_UserInfoDao();
        mScDao = DBManager.INSTANCE.getT_Phr_SignRecDao();
        mBaseInfoDao = DBManager.INSTANCE.getT_Phr_BaseInfoDao();
        mContext = context;
    }
    
    private void notifyDataChanged() {
        mContext.getContentResolver().notifyChange(DBManager.CONTENT_URI, null);
    }

    // **********************T_UserInfo**********************
    public void addUserInfo(T_UserInfo T_UserInfo) throws Exception{
        synchronized (mLockOjbect) {
//            if (mT_UserInfoDao.load(T_UserInfo.getUserId()) == null) {
//                mT_UserInfoDao.insert(T_UserInfo);
//            }else {
                mT_UserInfoDao.insertOrReplace(T_UserInfo);
//            }
        }
    }

    public void delUser(T_UserInfo T_UserInfo) {
        synchronized (mLockOjbect) {
        mT_UserInfoDao.delete(T_UserInfo);
        }
    }

    public T_UserInfo getUser(long userId) throws Exception {
        Query<T_UserInfo> query = mT_UserInfoDao
                .queryBuilder()
                .where(T_UserInfoDao.Properties.UserId.eq(userId))
                .build();
        return query.uniqueOrThrow();
    }

    public void clearUser() {
        mT_UserInfoDao.deleteAll();
    }

    // **********************T_UserInfo**********************

    // **********************Sign Collect**********************
    public boolean addSignData(T_Phr_SignRec sc) {
        synchronized (mLockOjbect) {
        if (mScDao.load(sc.getRecNo()) == null) {
            mScDao.insert(sc);
            notifyDataChanged();
            return true;
        }
        return false;
        }
    }

    public void addSignDatas(List<T_Phr_SignRec> entities) throws Exception {
        synchronized (mLockOjbect) {
        mScDao.insertOrReplaceInTx(entities);
        notifyDataChanged();
        }
    }

    public void updateSignDatas(List<T_Phr_SignRec> entities) throws Exception {
        synchronized (mLockOjbect) {
        mScDao.updateInTx(entities);
        notifyDataChanged();
        }
    }

    public boolean updateSignData(T_Phr_SignRec sc) {
        synchronized (mLockOjbect) {
        if (mScDao.load(sc.getRecNo()) != null) {
            mScDao.update(sc);
            notifyDataChanged();
            return true;
        }
        return false;
        }
    }

    public boolean delSignData(T_Phr_SignRec sc) {
        synchronized (mLockOjbect) {
        if(sc!=null) {
            mScDao.delete(sc);
            notifyDataChanged();
            return true;
        }
        return false;
        }
    }

    public boolean delSignDatas(List<T_Phr_SignRec> list) throws Exception{
        synchronized (mLockOjbect) {
        mScDao.deleteInTx(list);
        return true;
        }
    }

    public T_Phr_SignRec getSign(String enrId, int signCode, Date recDate) {
        Query<T_Phr_SignRec> query = mScDao
                .queryBuilder()
                .where(T_Phr_SignRecDao.Properties.EhrID.eq(enrId),
                        T_Phr_SignRecDao.Properties.SignCode.eq(signCode),
                        T_Phr_SignRecDao.Properties.RecordDate.eq(recDate))
                .build();
        if(query.list().size()>1) {
            return query.list().get(0);
        }
        return query.unique();

    }

    public List<T_Phr_SignRec> getGroupedSign(String ehrId,Date recDate) throws Exception{
        Query<T_Phr_SignRec> query = mScDao
                .queryBuilder()
                .where(T_Phr_SignRecDao.Properties.EhrID.eq(ehrId)
                        ,T_Phr_SignRecDao.Properties.RecordDate.eq(recDate)
                        )
                .build();
        return query.list();
    }

    public List<T_Phr_SignRec> getNotUpdatedSignList(String ehrId) {
        Query<T_Phr_SignRec> query = mScDao
                .queryBuilder()
                .where(T_Phr_SignRecDao.Properties.EhrID.eq(ehrId),T_Phr_SignRecDao.Properties.UpdateFlag.eq(false))
                .orderDesc(T_Phr_SignRecDao.Properties.RecordDate).build();
        return query.list();
    }

    public List<T_Phr_SignRec> getNotUpdatedSignList(String ehrId,int signCode,Calendar recDate,DataRange range) {
        Date[] ranges = new Date[2];
        ranges = CommonUtils.getDateRange(recDate, range);
        Query<T_Phr_SignRec> query = mScDao
                .queryBuilder()
                .where(T_Phr_SignRecDao.Properties.EhrID.eq(ehrId),
                        T_Phr_SignRecDao.Properties.SignCode.eq(signCode),
                        T_Phr_SignRecDao.Properties.RecordDate.between(ranges[0], ranges[1]),
                        T_Phr_SignRecDao.Properties.UpdateFlag.eq(false))
                .orderDesc(T_Phr_SignRecDao.Properties.RecordDate).build();
        return query.list();
    }

    public List<T_Phr_SignRec> getSignList(String ehrId, int signCode,
            Calendar recDate, DataRange range) {
        Date[] ranges = new Date[2];
        ranges = CommonUtils.getDateRange(recDate, range);
        Query<T_Phr_SignRec> query = mScDao
                .queryBuilder()
                .where(T_Phr_SignRecDao.Properties.EhrID.eq(ehrId),
                        T_Phr_SignRecDao.Properties.SignCode.eq(signCode),
                        T_Phr_SignRecDao.Properties.RecordDate.between(ranges[0], ranges[1]))
                .orderDesc(T_Phr_SignRecDao.Properties.RecordDate).build();
        return query.list();
    }

    public List<T_Phr_SignRec> getSignList(String ehrId,int signCode,Date startDate,Date endDate) {
        Query<T_Phr_SignRec> query = mScDao
                .queryBuilder()
                .where(T_Phr_SignRecDao.Properties.EhrID.eq(ehrId),
                        T_Phr_SignRecDao.Properties.SignCode.eq(signCode),
                        T_Phr_SignRecDao.Properties.RecordDate.between(
                                startDate, endDate))
                .orderDesc(T_Phr_SignRecDao.Properties.RecordDate).build();
        return query.list();

    }

    public void clearAllSigns() {
        synchronized (mLockOjbect) {
        mScDao.deleteAll();
        }
    }
    // **********************Sign Collect**********************

    // **********************Base Info**********************
    public void addBaseInfo(T_Phr_BaseInfo baseinfo) throws Exception {
        synchronized (mLockOjbect) {
        mBaseInfoDao.insertOrReplace(baseinfo);
        }
    }
    
    public T_Phr_BaseInfo getBaseInfo(String ehrId,long userId)throws Exception{
        Query<T_Phr_BaseInfo> query = mBaseInfoDao
                .queryBuilder()
                .where(T_Phr_BaseInfoDao.Properties.EhrID.eq(ehrId),
                        T_Phr_BaseInfoDao.Properties.UserId.eq(userId))
                        .build();
        return query.uniqueOrThrow();
    }
    
    public void addBaseInfoList(List<T_Phr_BaseInfo> baseinfos) throws Exception {
        synchronized (mLockOjbect) {
        mBaseInfoDao.insertOrReplaceInTx(baseinfos);
        }
    }
    
    public List<T_Phr_BaseInfo> getBaseInfoList(long userId)throws Exception{
        Query<T_Phr_BaseInfo> query = mBaseInfoDao
                .queryBuilder()
                .where(T_Phr_BaseInfoDao.Properties.UserId.eq(userId)).build();
        return query.list();
    }
    
    public Cursor executeRaw(String sql,String[] args) {
        DaoSession sesson = DBManager.INSTANCE.getDaoSession();
        SQLiteDatabase database = sesson.getDatabase();
        return database.rawQuery(sql, args);
    }
    // **********************Base Info**********************
    
    public static class DBOpenHelper extends OpenHelper {

        public DBOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }
        
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Logger.i(getClass().getSimpleName(), "onUpgrade");
            if(oldVersion==9&&newVersion==10) {
                db.execSQL("ALTER TABLE 'T__PHR__CARD_BIND_REC' ADD 'HOSPITAL_CODE' TEXT");
                db.execSQL("ALTER TABLE 'T__PHR__CARD_BIND_REC' ADD 'HOSPITAL_NAME' TEXT");
            }
            if (oldVersion < 12) {
                db.execSQL("ALTER TABLE 'T__PHR__BASE_INFO' ADD 'Alias' TEXT");
            }
            if (oldVersion < 13) {
                db.execSQL("ALTER TABLE 'T__PHR__CARD_BIND_REC' ADD 'PatientID' TEXT");
            }
        }
        
    }
}
