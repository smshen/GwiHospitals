package com.gwi.selfplatform;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.common.cache.AppMemoryCache;
import com.gwi.selfplatform.common.utils.JsonUtil;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.config.SettingConfig;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1013.HospitalInfo;
import com.gwi.selfplatform.module.net.response.G1014;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GlobalSettings {
    INSTANCE;

    private static final String DATABASE_NAME = "phr";

    private static final String KEY_LAST_FAMILY_ACCOUNT = "key_last_family_account";
    private static final String KEY_LAST_USER = "key_last_user";
    private static final String KEY_INSTALLED_APK = "key_installed_apk";

    private static final String KEY_HOSPITAL_SELECTED = "key_hospital_selected";

    private static final String KEY_HOSPITAL_PARAMS = "key_hospital_params";

    private static final String KEY_IS_FIRST_ENTER_APP = "key_is_first_enter_app";

    private static final String KEY_FONT_SIZE = "KEY_FONT_SIZE";

    private static final String KEY_TOKEN = "KEY_TOKEN";

    public final boolean MODE_LOCAL = false;
//    public final boolean MODE_LOCAL = false;

    private String mToken;

    /**
     * 当前app的运行模式
     */
    public final boolean DEBUG = BuildConfig.DEBUG;
    private Context mContext;
    private SharedPreferences mPrefrences;
    private String mLastFamilyAccountId = null;
    private String mHospitalName = null;
    private String mCityOfHospital = null;
    private String mHomeActivity = null;
    private boolean mIsFirstEnterApp;
    private int mFontSize;
    private boolean mIsHospitalMode = true;
    /** 应用标识 */
    private String mAppCode;
    private long DEFVALUE = -1l;
    private Long mLastUserId = DEFVALUE;
    private SettingConfig mConfig = null;
    private G1011 mCurPatientInfo;
    private Editor mEditor = null;
    private AppMemoryCache mAppMemoryCache = null;
    private HospitalInfo mCurrentHosInfo = null;
    private Map<String,String> mHospitalParams = null;
    private boolean mIsLogined = false;
    private T_UserInfo mCurrentUser = null;
    private T_Phr_BaseInfo mCurFamilymember = null;

    private String mTerminalNO;
    private String mSubHospCode;
    private String mHospCode;
    private String mNewestVersionName;
    private ExT_Phr_CardBindRec mCurCardInfo;

    public String getNewestVersionName() {
        return mNewestVersionName;
    }

    public void setNewestVersionName(String versionCode) {
        this.mNewestVersionName = versionCode;
    }

    /**
     * 获取令牌,如果为空从本地存储中获取，如果已过期，清空数据，重新获取
     * @return
     */
    public String getToken() {
        if (mToken == null) {
            mToken = mPrefrences.getString(KEY_TOKEN,null);
        }
        Date cacheTime = mAppMemoryCache.getCacheTime(KEY_TOKEN);
        if (cacheTime != null) {
            long timeMillis = new Date().getTime()-cacheTime.getTime();
            int expireTime = 2;
            if (timeMillis / AlarmManager.INTERVAL_HOUR >= expireTime) {
                mToken = null;
                mEditor.remove(KEY_TOKEN);
                mAppMemoryCache.remove(KEY_TOKEN);
            }
        }
        return mToken;
    }

    /**
     * 网络通信令牌，从服务端获取
     * @param token 令牌
     */
    public void setToken(String token) {
        mAppMemoryCache.put(KEY_TOKEN,token,new Date());
        mToken = token;
        mEditor.putString(KEY_TOKEN,token);
        mEditor.commit();
    }

    /**
     * 得到医院代码
     * @return
     */
    public String getHospCode() {
        return mHospCode;
    }

    /**
     * 设置医院代码（新）
     * @param hospCode 医院代码
     */
    public void setHospCode(String hospCode) {
        mHospCode = hospCode;
    }

    /**
     * 得到分院代码
     * @return
     */
    public String getSubHospCode() {
        return mSubHospCode;
    }

    /**
     * 设置分院代码
     * @param subHospCode 分院代码
     */
    public void setSubHospCode(String subHospCode) {
        mSubHospCode = subHospCode;
    }

    /**
     * 得到设备No,如果当前内存中缓存的NO为空，则从本地存储中获取（防止app异常或处于后台线程过久导致数据丢失的情况）
     * @return 设备NO
     */
    public String getTerminalNO() {
        if(TextUtils.isEmpty(mTerminalNO)) {
            Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
            mTerminalNO = HospitalParams.getValue(params, HospitalParams.CODE_TERMINAL_NO);
        }
        return mTerminalNO;
    }

    /**
     * 获取设备NO
     * @param terminalNO 设备NO
     */
    public void setTerminalNO(String terminalNO) {
        mTerminalNO = terminalNO;
    }

    public String getAppCode() {
        return mAppCode;
    }

    /** 设置应用标识 */
    public void setAppCode(String appCode) {
        mAppCode = appCode;
    }

    public int getFontSize() {
        return mFontSize;
    }

    public void setFontSize(int fontSize) {
        mFontSize = fontSize;
        mEditor.putInt(KEY_FONT_SIZE,mFontSize);
        mEditor.commit();
    }

    public boolean isHospitalMode() {
        return mIsHospitalMode;
    }

    public void setIsHospitalMode(boolean isHospitalMode) {
        mIsHospitalMode = isHospitalMode;
    }

    public boolean isFirstEnterApp() {
        return mIsFirstEnterApp;
    }

    public void firstEnterApp() {
        mEditor.putBoolean(KEY_IS_FIRST_ENTER_APP,false);
        mEditor.commit();
    }

    public String getHomeActivity() {
        return mHomeActivity;
    }

    /**
     * For phr:设置主页
     * @param homeActivity
     */
    public void setHomeActivity(String homeActivity) {
        this.mHomeActivity = homeActivity;
    }

    public String getCityOfHospital() {
        return mCityOfHospital;
    }

    public void setCityOfHospital(String cityOfHospital) {
        mCityOfHospital = cityOfHospital;
    }

    public String getHospitalName() {
        if(TextUtils.isEmpty(mHospitalName)) {
            Map<String,String> params = getLocalParams();
            mHospitalParams = params;
            List<String> mapParams = HospitalParams.getFields(params.get(HospitalParams.CODE_MAP_NAVIGATION));
            setCityOfHospital(mapParams.get(0));
            mHospitalName = mapParams.get(1);
        }
        return mHospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.mHospitalName = hospitalName;
    }
    
    public Map<String,String> getHospitalParams() {
        //如果内存中params被回收，重新从文件缓存中恢复
        if(mHospitalName==null||mHospitalName.isEmpty()) {
           mHospitalParams = getLocalParams();
        }
        return mHospitalParams;
    }

    public void setHospitalParams(List<G1014> hospitalParams) {
        mHospitalParams = new HashMap<String,String>();
        for(G1014 g1014:hospitalParams) {
            mHospitalParams.put(g1014.getCode(), g1014.getValue());
        }
        //每次启动程序更新医院参数缓存
        Gson gson = new Gson();
        mEditor.putString(KEY_HOSPITAL_PARAMS, gson.toJson(hospitalParams));
        mEditor.commit();
    }

    private  Map<String,String> getLocalParams() {
        Map<String,String> params = new HashMap<>();
        if(mPrefrences.contains(KEY_HOSPITAL_PARAMS)){
            Gson gson = new Gson();
            String paramStr = mPrefrences.getString(KEY_HOSPITAL_PARAMS, null);
            if(paramStr!=null) {
                Type lt = new TypeToken<List<G1014>>() {}.getType();
                List<G1014> hospitalParams= gson.fromJson(paramStr,lt);
                params = new HashMap<String,String>();
                for(G1014 g1014:hospitalParams) {
                    params.put(g1014.getCode(), g1014.getValue());
                }
            }
        }
        return params;
    }

    public HospitalInfo getCurrentHospital() {
        return mCurrentHosInfo;
    }

    /**
     * 设置当前医院
     * @platorm
     * @param currentHosInfo
     */
    public void setCurrentHospital(HospitalInfo currentHosInfo) {
        mCurrentHosInfo = currentHosInfo;
        mEditor.putString(KEY_HOSPITAL_SELECTED,
                JsonUtil.toJson(currentHosInfo));
        mEditor.commit();
    }

    public ExT_Phr_CardBindRec getCurCardInfo() {
        return mCurCardInfo;
    }

    public void setCurCardInfo(ExT_Phr_CardBindRec cardInfo) {
        mCurCardInfo = cardInfo;
    }

    /**
     * 获取当前患者信息
     *
     * @return
     */
    public G1011 getCurPatientInfo() {
        return mCurPatientInfo;
    }

    /**
     * 设置获取当前患者信息
     *
     * @param curPatientInfo
     */
    public void setCurPatientInfo(G1011 curPatientInfo) {
        mCurPatientInfo = curPatientInfo;
    }

    public void setCurPatientId(String patientId) {
        if (TextUtil.isEmpty(patientId)) {
            mCurPatientInfo = new G1011();
            mCurPatientInfo.setPatientID(patientId);
            setCurPatientInfo(mCurPatientInfo);
        }
    }

    public SettingConfig getConfig() {
        return mConfig;
    }
    
    public Long getLastUserId() {
        return mLastUserId;
    }

    private void setLastUserId(Long lastUserId) {
        mLastUserId = lastUserId;
        mEditor.putLong(KEY_LAST_USER, mLastUserId == null ? -1 : mLastUserId);
        mEditor.commit();
    }

    public AppMemoryCache getAppMemoryCache() {
        return mAppMemoryCache;
    }

    /**
     * EhrId
     *
     * @return
     */
    public String getCurrentFamilyAccountId() {
        return mLastFamilyAccountId;
    }

    public void setCurrentFamilyAccountId(String ehrId) {
        mLastFamilyAccountId = ehrId;
        mEditor.putString(KEY_LAST_FAMILY_ACCOUNT, mLastFamilyAccountId);
        mEditor.commit();
    }

    public T_UserInfo getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(T_UserInfo currentUser) {
        this.mCurrentUser = currentUser;
        setLastUserId(currentUser == null ? -1l : currentUser.getUserId());
    }

    public T_Phr_BaseInfo getCurrentFamilyAccount() {
        return mCurFamilymember;
    }

    public void setCurrentFamilyAccount(T_Phr_BaseInfo member) {
        mCurFamilymember = member;
        if (member == null) {
            setCurrentFamilyAccountId(null);
        } else {
            setCurrentFamilyAccountId(member.getEhrID());
        }
    }

    public String getInstalledApkName() {
        return mPrefrences.getString(KEY_INSTALLED_APK, "");
    }

    public void setInstalledApkName(String filePath) {
        mEditor.putString(KEY_INSTALLED_APK, filePath);
        mEditor.commit();
    }

    public boolean isIsLogined() {
        return mIsLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.mIsLogined = isLogined;
    }

    /**
     * 检查DB是否存有用户信息,启动APP时调用
     */
    public void checkIsLoginedWhenStart() {
        T_UserInfo user = DBController.INSTANCE
                .getLastLoginedUser(getLastUserId());
        if (user != null) {
            T_Phr_BaseInfo curFamilyAccount = DBController.INSTANCE
                    .getFamilyAccount(
                            mLastFamilyAccountId == null ? user.getEhrId()
                                    : mLastFamilyAccountId, user.getUserId());
            if (curFamilyAccount != null) {
                setIsLogined(true);
                mCurrentUser = user;
                mCurFamilymember = curFamilyAccount;
            }
        }
    }

    public void logout() {
        mIsLogined = false;
        mCurrentUser = null;
        mCurPatientInfo = null;
        mCurFamilymember = null;
        mCurrentHosInfo = null;
        mLastFamilyAccountId = null;
        mLastUserId = null;
        // TODO:
        setIsLogined(mIsLogined);
        setCurrentFamilyAccount(mCurFamilymember);
        setCurrentFamilyAccountId(mLastFamilyAccountId);
        setLastUserId(mLastUserId);
        setCurrentHospital(mCurrentHosInfo);
        mAppMemoryCache.clearAll();
    }

    /**
     * 必须在DBManager之后初始化
     * 
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        mPrefrences = mContext.getSharedPreferences(DATABASE_NAME,
                Context.MODE_PRIVATE);
        mEditor = mPrefrences.edit();
        mAppMemoryCache = new AppMemoryCache();

        mLastFamilyAccountId = mPrefrences.getString(KEY_LAST_FAMILY_ACCOUNT,
                null);
        mLastUserId = mPrefrences.getLong(KEY_LAST_USER, DEFVALUE);

        mCurrentHosInfo = JsonUtil.fromJson(
                mPrefrences.getString(KEY_HOSPITAL_SELECTED, null),
                HospitalInfo.class);

        mIsFirstEnterApp = mPrefrences.getBoolean(KEY_IS_FIRST_ENTER_APP,true);

        mFontSize = mPrefrences.getInt(KEY_FONT_SIZE,16);

        mConfig = new SettingConfig(mContext);

        checkIsLoginedWhenStart();

//        mHospitalName = context.getString(R.string.HomeActivity);
//        mCityOfHospital = context.getString(R.string.province);

//        String hospitalParamsJson = mPrefrences.getString(KEY_HOSPITAL_PARAMS,null);
//        Type lt = new TypeToken<List<String>>() {}.getType();
//        mHospitalParams = new Gson().fromJson(hospitalParamsJson, lt);
    }

}
