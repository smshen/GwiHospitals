package com.gwi.selfplatform.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BASE__ADDRESS_DICT.
*/
public class Base_AddressDictDao extends AbstractDao<Base_AddressDict, String> {

    public static final String TABLENAME = "BASE__ADDRESS_DICT";

    /**
     * Properties of entity Base_AddressDict.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Code = new Property(0, String.class, "Code", true, "CODE");
        public final static Property ParentCode = new Property(1, String.class, "ParentCode", false, "PARENT_CODE");
        public final static Property Name = new Property(2, String.class, "Name", false, "NAME");
        public final static Property Memo = new Property(3, String.class, "Memo", false, "MEMO");
        public final static Property Status = new Property(4, Integer.class, "Status", false, "STATUS");
        public final static Property WbCode = new Property(5, String.class, "WbCode", false, "WB_CODE");
        public final static Property PyCode = new Property(6, String.class, "PyCode", false, "PY_CODE");
    };


    public Base_AddressDictDao(DaoConfig config) {
        super(config);
    }
    
    public Base_AddressDictDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BASE__ADDRESS_DICT' (" + //
                "'CODE' TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: Code
                "'PARENT_CODE' TEXT," + // 1: ParentCode
                "'NAME' TEXT," + // 2: Name
                "'MEMO' TEXT," + // 3: Memo
                "'STATUS' INTEGER," + // 4: Status
                "'WB_CODE' TEXT," + // 5: WbCode
                "'PY_CODE' TEXT);"); // 6: PyCode
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BASE__ADDRESS_DICT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Base_AddressDict entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getCode());
 
        String ParentCode = entity.getParentCode();
        if (ParentCode != null) {
            stmt.bindString(2, ParentCode);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(3, Name);
        }
 
        String Memo = entity.getMemo();
        if (Memo != null) {
            stmt.bindString(4, Memo);
        }
 
        Integer Status = entity.getStatus();
        if (Status != null) {
            stmt.bindLong(5, Status);
        }
 
        String WbCode = entity.getWbCode();
        if (WbCode != null) {
            stmt.bindString(6, WbCode);
        }
 
        String PyCode = entity.getPyCode();
        if (PyCode != null) {
            stmt.bindString(7, PyCode);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Base_AddressDict readEntity(Cursor cursor, int offset) {
        Base_AddressDict entity = new Base_AddressDict( //
            cursor.getString(offset + 0), // Code
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // ParentCode
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Memo
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // Status
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // WbCode
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6) // PyCode
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Base_AddressDict entity, int offset) {
        entity.setCode(cursor.getString(offset + 0));
        entity.setParentCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMemo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setStatus(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setWbCode(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPyCode(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Base_AddressDict entity, long rowId) {
        return entity.getCode();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Base_AddressDict entity) {
        if(entity != null) {
            return entity.getCode();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
