package com.gwi.selfplatform.config;

import android.os.Environment;

/**
 * 常量定义类
 *
 * @author Peng Yi
 */
public class Constants {
    // 保存参数文件夹名称
    public static final String SHARED_PREFERENCE_NAME = "gwi_phr_prefs";
    // SDCard路径
    public static final String SD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    // 图片存储路径
    public static final String BASE_PATH = SD_PATH + "/gwi/phr/xyfe/";
    //    public static final String SHARE_PATH = Environment.DIRECTORY_DOWNLOADS;
    public static final String SHARE_PATH = BASE_PATH + "hospitals";
    // 缓存图片路径
    public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";
    public static final String DATE_TIME_FORMAT = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_DATE_TIME_ALL = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy年MM月dd日";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String YEAR_AND_MONTH_FORMAT = "yyyy年MM月";
    public static final String FORMAT_ISO_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_ISO_DATE = "yyyy-MM-dd";
    public static final String FORMAT_WEEK = "E";
    public static final String FORMAT_MONTH = "d日";
    public static final String VALUE_TYPE_ID_DEFAULT = "2";
    public static final String ENCODE = "UTF-8";
    public static final String ACTION_BROADCAST_DATA = "com.gwi.phr.ACTION_BROADCAST_DATA";
    public static final String ACTION_SERVICE_DATA = "com.gwi.phr.ACTION_SERVICE_DATA";
    public static final String ACTION_BORADCAST_LOGOUT = "com.gwi.phr.ACTION_BORADCAST_LOGOUT";
    public static final String ACTION_BOROADCAST_VALIDATION_CODE = "com.gwi.phr.ACTION_BOROADCAST_VALIDATION_CODE";
    public static final String EXTRA_VALIDATION_CODE = "com.gwi.phr.EXTRA_VALIDATION_CODE";
    public static final String EXTRA_OFFLINE_DATA = "com.gwi.phr.EXTRA_OFFLINE_DATA";
    public static final int REQUEST_CODE_LOGIN = 0x9521;
    public static final int MSG_LAZY_LOADING = 0x20001;
    /**
     * 验证码最长有效期：10分钟
     */
    public static final long VADILITY_MAX = 600000;
    //    public static final String URL_AGGREMENT = "file:///android_asset/agreement.html";
    public static final String URL_AGGREMENT = "http://phr.gwi.com.cn:7778/Content/ServiceTerm.htm";
    public static final String FAKE_LOGIN_KEY_USER_INFO = "userinfo";
    public static final String FAKE_LOGIN_KEY_FAMILY_INFO = "familyinfo";
    public static final String TINGYUN_APP_ID = "0eea9f5240b645669dac401932115333";
    //延迟返回
    public final static long MILLIMS_DELAY_BACK = 1000;
    public final static int MSG_SHOULD_FINISH = 0x2001;
    //通用的key
    public final static String KEY_IS_TYPE_REGIST = "is_type_regist";
    public final static String KEY_ORDER_DATE = "OrderDate";
    public final static String KEY_NEXT_ACTIVITY = "key_next_activity";
    public final static String KEY_IS_FROM_PHR = "key_is_from_phr";
    public final static String KEY_SUB_HOSPITAL = "key_sub_hospital";
    public final static String KEY_HASDETAIL_TIME = "hasDetailTime";
    public final static String KEY_PATIENT_INFO = "patientInfo";
    public static final String KEY_DEPT = "Dept";
    public static final String KEY_DCT = "Dct";
    public static final String KEY_DCTS = "Dcts";
    public static final String KEY_TYPE_ID = "type_id";
    public static final String KEY_CARD_BINDER = "key_card_binder";
    public static final String KEY_HAS_DETAIL_TIME = "key_has_detail_time";
    public final static String KEY_BUNDLE = "key_bundle";

    /**
     * 不付费 = 0,
     * 诊疗卡 = 1,
     * 银行卡 = 2,
     * 个人医保账户 = 3,
     * 医保预交金混合支付 = 4,
     * 医保银行卡混合支付 = 5,
     * 微信支付 = 6,
     * 支付宝 = 7,
     * 现金 = 8,
     * 银联支付 = 9,
     * 中国银行 = 10,
     * 免费 = 11,
     */
    //PAY MODE
    public static final String PAY_MODE_NONE = "0";
    public static final String PAY_MODE_CARD_MEDICAL = "1";
    public static final String PAY_MODE_CARD_BANK = "2";
    public static final String PAY_MODE_HOSP_ACCOUNT_PESONAL = "3";
    public static final String PAY_MODE_HYBRID_PRE_PAY = "4";
    public static final String PAY_MODE_HYBRID_BANK_PAY = "5";
    public static final String PAY_MODE_WEIXIN = "6";
    public static final String PAY_MODE_ALIPAY = "7";
    public static final String PAY_MODE_CASH = "8";
    public static final String PAY_MODE_UNIONPAY = "9";
    public static final String PAY_MODE_BOC = "10";
    public static final String PAY_FOR_FREE = "11";
    public static final String PAY_MODE_ICBC = "12";
    public static final String PAY_MODE_BCM = "13";
    public static final String PAY_MODE_CCB = "14";

    //    //TRAN TYPE DICTS
//    public static final String TRAN_TYPE_BANK = "1";
//    public static final String TRAN_TYPE_UNIONPAY = "2";
//    public static final String TRAN_TYPE_ALIPAY = "3";
//    public static final String TRAN_TYPE_WEIXIN = "4";
    public static final int TIME_REGION_BEFOR_NOON = 1;
    public static final int TIME_REGION_NOON = 4;
    public static final int TIME_REGION_ATRER_NOON = 2;
    public static final int TIME_REGION_ALL_DAY = 3;
    /**
     * 9 stands for mobilephone.
     */
    public static final String APP_TYPE_CODE = "9";

    //WEB GWI API parameter
    public static final int CARD_TYPE_MEDICAL = 1;
    // 应用名称
    public static String APP_NAME = "";

    public final static String DEFAULT_TYPE_ID = "2";//普通挂号;

    public static class HospInfoQuery {
        public static final String TREATMENT_GUIDELINES = "1";  // 就诊指南
        public static final String HOSPITAL_NEWS = "2";         // 医院动态
    }

    public static class ThirdPart {
        public static final String SOURCE = "SOURCE";
        public static final String USER_NAME = "USER_NAME";
        public static final String MOBILE_NO = "MOBILE_NO";
        public static final String ID_CARD_NO = "ID_CARD_NO";
    }
}
