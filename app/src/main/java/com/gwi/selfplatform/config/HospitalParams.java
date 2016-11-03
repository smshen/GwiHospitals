package com.gwi.selfplatform.config;

import android.util.SparseArray;

import com.gwi.phr.hospital.BuildConfig;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 医院参数表
 *
 * @author 彭毅
 */
public class HospitalParams {
    // --- Code ---
    public static final String CODE_ORDER_DATE_SELECT_POSITION = "OrderDateSelectPosition";
    public static final String CODE_ENABLE_CANCEL_ORDER = "EnableCancelOrder";
    public static final String CODE_REPORT_SUMMARY_FIELD = "ReportSummaryField";
    public static final String CODE_REGULAR_REPORT_DETAIL_FIELD = "RegularReportDetailField";
    public static final String CODE_MICROBE_REPORT_DETAIL_FIELD = "MicrobeReportDetailField";
    //
    public static final String CODE_DISPLAY_PAYMENT_REGISTER_RECORD = "DisplayPaymentRegisterRecord";
    public static final String CODE_PAYMENT_REGISTER_RECORD_FIELD = "PaymentRegisterRecordField";
    public static final String CODE_DISPLAY_PAYMENT_PRESCRIPTION_RECORD = "DisplayPaymentPrescriptionRecord";
    public static final String CODE_PAYMENT_PRESCRIPTION_RECORD_FIELD = "PaymentPrescriptionRecordField";
    //
    public static final String CODE_CONSUME_RECORD_DATA_SOURCE = "ConsumeRecordDataSource";
    public static final String CODE_CONSUME_RECORD_SUMMARY_FIELD = "ConsumeRecordSummaryField";
    public static final String CODE_MOBILE_FUNCTION_LIST = "MobileFunctionList";
    // --- 湘西 ---
    public static final String CODE_ORDER_IDENTITY = "OrderIdentity";

    // --- 中心医院 ---
    public static final String CODE_HAS_SUB_HOSPITAL = "HasSubHospital";
    public static final String CODE_FUCTION_TOP = "FuctionTop";

    public static final String CODE_DEPT_DEPTH = "DeptDepth";
    public static final String CODE_HAS_ORDER_DETAIL_TIME = "HasOrderDetailTime";
    public static final String CODE_HAS_DEPT_QUEUE = "HasDeptQueue";
    public static final String CODE_HAS_DRUG_QUEUE = "HasDrugQueue";
    public static final String CODE_PAY_TYPE = "PayType";
    public static final String CODE_RECIPE_PAY_TYPE = "RecipePayType";
    public static final String CODE_MAP_NAVIGATION = "MapNavigation";
    public static final String CODE_IS_APPOINT_PAID = "IsAppointPaid";
    public static final String CODE_ALIPAY_PARAMS = "AlipayParams";
    public static final String CODE_HAS_DOCTOR_BRIEF = "HasDoctorBrief";
    public static final String CODE_HAS_FAMILY_MANAGEMENT = "HasFamilyManagement";

    public static final String CODE_ALI_APPID = "AliAppId";
    public static final String PID = "PID";
    public static final String CODE_ALI_SELLER_EMAIL = "AliSeller_email";
    public static final String CODE_ALINOTIFY_URL = "AliNotify_url";
    public static final String CODE_ALICALL_BACK_URL = "AliCall_back_url";
    public static final String CODE_PRIVATE_KEY = "PrivateKey";
    public static final String CODE_PUBLIC_KEY = "PublicKey";

    public static final String CODE_FEMALE_PATIENT_LIMITED_DEPTS = "FemalePatientLimitedDepts";
    public static final String CODE_MALE_PATIENT_LIMITED_DEPTS = "MalePatientLimitedDepts";
    public static final String CODE_IS_VALIDATE_CARD_LOGOUT_ENABLED = "IsValidateCardLogoutEnabled";
    public static final String CODE_IS_CARDBIND_NEED_MOBILE_VALIDATE_ENABLED = "IsCardBindNeedMobileValidateEnabled";
    /**
     * 儿科IDs
     */
    public static final String CODE_PEDS_ID = "PedsID";

    public static final String CODE_IS_DEPTS_LIMITED_ENABLED = "IsDeptsLimitedEnabled";
    public static final String CODE_PEDS_AGE_LIMIT = "PedsAgeLimit";
    /**
     * 门诊费用清单是否需要分组
     */
    public static final String CODE_IS_PAY_RECORD_LIST_NEED_GROUPED = "IsPayRecordListNeedGrouped";
    /**
     * 优惠打折模式
     */
    public static final String CODE_REG_DISCOUNT_MODE = "RegDiscountMode";


    //市一医院
    public static final String CODE_TERMINAL_NO = "TerminalNo";
    public static final String CODE_HAS_FEE_SUMMARY = "HasFeeSummary";
    public static final String CODE_RECIPE_PAY_ONLY_ONE = "onlyOnePay";

    //邵阳市第一医院
    /**
     * 是否动态加载子科室
     */
    public static final String CODE_IS_DYNAMIC_LOADING_SUB_DEPTS = "IsDynamicLoadingSubDepts";
    public static final String CODE_HAS_DEPART_POS = "HasDepartPos";
    public static final String CODE_CAN_ORDER_REGISTER_RECORD_FILTERED = "CanOrderRegisterRecordFiltered";

    /**
     * 三真康复医院 HasDeptIntrodutcion
     */
    public static final String CODE_HAS_DEPT_INTRODUTCION = "HasDeptIntrodutcion";

    /**
     * 东莞社保平台
     */
    public static final String CODE_MEDICAL_CARD_FORMAT = "MedicalCardFormat";

    //佛山第五人民医院
    public static final String CODE_HAS_DOCTORS_CATEGORY = "HasDoctorsCategory";
    public static final String CODE_REGISTER_PAY_TYPE = "RegisterPayType";
    public static final String CODE_ORDER_REGISTER_PAY_TYPE = "OrderRegisterPayType";
    public static final String CODE_NEED_LOCK_REGISTER = "NeedLockRegister";
    public static final String CODE_NEED_LOCK_ORDERREGISTER = "NeedLockOrderRegister";


    //百度推送
    public static final String CODE_BAIDU_AK = "BaiduAK";

    // --- Code ---

    /**
     *
     */
    public static final String VALUE_ONE = "1";
    public static final String VALUE_ZERO = "0";
    public static final String VALUE_TWO = "2";

    public static final String VALUE_REGIST_APPOINT = "1001";
    public static final String VALUE_REGIST = "1002";

    private SparseArray<String> mMobileFunctionList = null;


    public static final String VALUE_FUNCTION_LIST_CHARGE = "1201";

    /**
     * 首页功能列表
     */
    public static final HashMap<Integer, String> HOME_MAP = new HashMap<Integer, String>();
    private static final String MODULE_SHOW = "Show";
    private static final String MODULE_HIDE = "Hide";

    static {
        HOME_MAP.put(R.string.label_smart_leading_examining, "1701");
        HOME_MAP.put(R.string.label_registered, "1001-1002");
        HOME_MAP.put(R.string.label_pay_cost, "1202");
        HOME_MAP.put(R.string.label_top_up, "1201");

        HOME_MAP.put(R.string.label_waiting_in_line, "1501-1502");
        HOME_MAP.put(R.string.label_electronic_leading_examining, "1901");
        HOME_MAP.put(R.string.label_registration_record, "1003");
        HOME_MAP.put(R.string.label_outpatient_service_charge, "1203");

        HOME_MAP.put(R.string.label_payment_order, "1204");
        HOME_MAP.put(R.string.label_among, "1101");
        HOME_MAP.put(R.string.label_inspection_report, "1102");
        HOME_MAP.put(R.string.label_price_inquiry, "1801");

        HOME_MAP.put(R.string.label_health_information, "1301");
        HOME_MAP.put(R.string.label_medical_encyclopedia, "1302");
        HOME_MAP.put(R.string.label_map_navigation, "1402");
        HOME_MAP.put(R.string.label_pharmacy, "2001");

        HOME_MAP.put(R.string.label_admitted_to_hospital, "2201");
        HOME_MAP.put(R.string.label_parking, "2101");
        HOME_MAP.put(R.string.label_hospital_is_introduced, "1401");
        HOME_MAP.put(R.string.label_user_center, MODULE_SHOW);
        HOME_MAP.put(R.string.label_treatment_guidelines, /* BuildConfig.DEBUG ? MODULE_SHOW : */MODULE_HIDE);
        HOME_MAP.put(R.string.label_hospital_news, /*BuildConfig.DEBUG ? MODULE_SHOW :*/ MODULE_HIDE);
        HOME_MAP.put(R.string.label_indoor_navigation, /*BuildConfig.DEBUG ? MODULE_SHOW :*/ MODULE_HIDE);
    }

    public static boolean isShowHomeItem(int resId, List<String> keys) {
        String key = HOME_MAP.get(resId);
        for (String value : keys) {
            if (key.contains(value) || key.contains(MODULE_SHOW)) {
                return true;
            }
        }
        return false;
    }

//    public int initHomeParamsMapping() {
//        mMobileFunctionList = new SparseArray<String>();
//        mMobileFunctionList.append(0, "1001002");// 挂号
//        mMobileFunctionList.append(1, "1101");// 化验报告
//        mMobileFunctionList.append(2, "1201");// 预交金充值
//        mMobileFunctionList.append(3, "1203");// 门诊费用查询
//        mMobileFunctionList.append(4, "1202");// 门诊缴费
//        mMobileFunctionList.append(5, "1003");// 挂号查询
//        mMobileFunctionList.append(6, "1301");// 健康知识
//        mMobileFunctionList.append(7, "1401");// 医院介绍
//        // 用户中心=8
//        mMobileFunctionList.append(9, "1402");// 地图导航
//        mMobileFunctionList.append(10, "1302");// 健康百科
//        mMobileFunctionList.append(11, "1102");// 检查报告
//        mMobileFunctionList.append(12, "1501");// 候诊排队
//        mMobileFunctionList.append(13, "1601");// 住院费用查询
//        mMobileFunctionList.append(14, "1701");// 智能导诊==>智能分诊(中心医院required)
//        mMobileFunctionList.append(15, "1204");// 支付订单查询
//        mMobileFunctionList.append(16, "1801");// 药价查询
//        mMobileFunctionList.append(17, "1901");// 电子导诊
//        //邵阳市第一人民医院添加
//        mMobileFunctionList.append(18, "1205");//门诊费用详细查询
//        return mMobileFunctionList.size();
//    }
//
//    public int isShowHomeItem(String value, int n) {
//        for (int i = 0; i < n; i++) {
//            if (mMobileFunctionList.get(i, "").contains(value)) {
//                return i;
//            }
//        }
//        return -1;
//    }

    public static String getDateSelectPosition() {
        Map<String, String> list = GlobalSettings.INSTANCE.getHospitalParams();
        String value = list.get(CODE_ORDER_DATE_SELECT_POSITION);
        return value;
    }

    public static boolean canCancelAppointment() {
        Map<String, String> list = GlobalSettings.INSTANCE.getHospitalParams();
        String value = list.get(CODE_ENABLE_CANCEL_ORDER);
        return VALUE_ONE.equals(value);
    }

    public static int isShowOrderIdentity() {
        Map<String, String> list = GlobalSettings.INSTANCE.getHospitalParams();
        String value = list.get(CODE_ORDER_IDENTITY);
        return Integer.parseInt(value);
    }

    public static boolean isShowDoctorCategory() {
        Map<String, String> list = GlobalSettings.INSTANCE.getHospitalParams();
        String value = list.get(CODE_HAS_DOCTORS_CATEGORY);
        return VALUE_ONE.equals(value);
    }


    /**
     * 获取显示的字段
     *
     * @param value
     * @return
     */
    public static List<String> getFields(String value) {
        List<String> list = new ArrayList<>();
        String[] vals = value.split(",");
        list.addAll(Arrays.asList(vals));
        return list;
    }

    /**
     * 是否显示字段
     *
     * @param feildStr
     * @param list
     * @return
     */
    public static boolean isShow(String feildStr, List<String> list) {
        return list.contains(feildStr);
    }

    public static boolean hasSubHospitals(Map<String, String> list) {
        try {
            String value = list.get(CODE_HAS_SUB_HOSPITAL);
            return value.equalsIgnoreCase(VALUE_ONE);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasOrderDetailTime() {
        try {
            Map<String, String> list = GlobalSettings.INSTANCE
                    .getHospitalParams();
            String value = list.get(CODE_HAS_ORDER_DETAIL_TIME);
            return value.equalsIgnoreCase(VALUE_ONE);
        } catch (Exception e) {
            return false;
        }
    }

    public static int getDepthOfDept(Map<String, String> list) {
        try {
            String value = list.get(CODE_DEPT_DEPTH);
            return Integer.parseInt(value);
        } catch (Exception e) {
            // Default.
            return 2;
        }
    }

    public static boolean hasDrugQueue(Map<String, String> list) {
        try {
            String value = list.get(CODE_HAS_DRUG_QUEUE);
            return value.equalsIgnoreCase(VALUE_ONE);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasDeptQueue(Map<String, String> list) {
        try {
            String value = list.get(CODE_HAS_DEPT_QUEUE);
            return value.equalsIgnoreCase(VALUE_ONE);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasFunction(Map<String, String> list, String key) {
        try {
            String value = list.get(key);
            return VALUE_ONE.equalsIgnoreCase(value);
        } catch (Exception e) {
            return false;
        }
    }

    public static String getValue(Map<String, String> list, String key) {
        return list.get(key);
    }
}
