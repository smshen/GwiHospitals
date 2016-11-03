package com.gwi.selfplatform.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table T__PHONE__AUTH_CODE.
*/
public class T_Phone_AuthCodeDao extends AbstractDao<T_Phone_AuthCode, Void> {

    public static final String TABLENAME = "T__PHONE__AUTH_CODE";

    /**
     * Properties of entity T_Phone_AuthCode.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property RecNo = new Property(0, Long.class, "RecNo", false, "REC_NO");
        public final static Property PhoneNumber = new Property(1, String.class, "PhoneNumber", false, "PHONE_NUMBER");
        public final static Property AuthCode = new Property(2, String.class, "AuthCode", false, "AUTH_CODE");
        public final static Property BuildTime = new Property(3, String.class, "BuildTime", false, "BUILD_TIME");
        public final static Property SendTimes = new Property(4, Integer.class, "SendTimes", false, "SEND_TIMES");
    };


    public T_Phone_AuthCodeDao(DaoConfig config) {
        super(config);
    }
    
    public T_Phone_AuthCodeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'T__PHONE__AUTH_CODE' (" + //
                "'REC_NO' INTEGER," + // 0: RecNo
                "'PHONE_NUMBER' TEXT," + // 1: PhoneNumber
                "'AUTH_CODE' TEXT," + // 2: AuthCode
                "'BUILD_TIME' TEXT," + // 3: BuildTime
                "'SEND_TIMES' INTEGER);"); // 4: SendTimes
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'T__PHONE__AUTH_CODE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, T_Phone_AuthCode entity) {
        stmt.clearBindings();
 
        Long RecNo = entity.getRecNo();
        if (RecNo != null) {
            stmt.bindLong(1, RecNo);
        }
 
        String PhoneNumber = entity.getPhoneNumber();
        if (PhoneNumber != null) {
            stmt.bindString(2, PhoneNumber);
        }
 
        String AuthCode = entity.getAuthCode();
        if (AuthCode != null) {
            stmt.bindString(3, AuthCode);
        }
 
        String BuildTime = entity.getBuildTime();
        if (BuildTime != null) {
            stmt.bindString(4, BuildTime);
        }
 
        Integer SendTimes = entity.getSendTimes();
        if (SendTimes != null) {
            stmt.bindLong(5, SendTimes);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public T_Phone_AuthCode readEntity(Cursor cursor, int offset) {
        T_Phone_AuthCode entity = new T_Phone_AuthCode( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // RecNo
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // PhoneNumber
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // AuthCode
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // BuildTime
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4) // SendTimes
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, T_Phone_AuthCode entity, int offset) {
        entity.setRecNo(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPhoneNumber(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setAuthCode(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBuildTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSendTimes(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(T_Phone_AuthCode entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(T_Phone_AuthCode entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
