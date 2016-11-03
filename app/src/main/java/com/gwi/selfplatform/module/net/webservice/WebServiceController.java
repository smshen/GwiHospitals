package com.gwi.selfplatform.module.net.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.exception.BaseException;
import com.gwi.selfplatform.common.security.DESEncryption;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.security.RsaUtil;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Base_DatumClass;
import com.gwi.selfplatform.db.gen.T_FeedBack_Rec;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.BodyToSymptom;
import com.gwi.selfplatform.module.net.beans.HealthReport;
import com.gwi.selfplatform.module.net.beans.KBBodyPart;
import com.gwi.selfplatform.module.net.beans.KBDepart;
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.module.net.beans.KBDrugDetails;
import com.gwi.selfplatform.module.net.beans.KBDrugProperty;
import com.gwi.selfplatform.module.net.beans.KBDrugUseKind;
import com.gwi.selfplatform.module.net.beans.KBTestCheckDetails;
import com.gwi.selfplatform.module.net.beans.KBTestCheckKind;
import com.gwi.selfplatform.module.net.beans.KBTreatmentDetails;
import com.gwi.selfplatform.module.net.beans.KBTreatmentKind;
import com.gwi.selfplatform.module.net.beans.MobileVerParam;
import com.gwi.selfplatform.module.net.beans.PhrSignRecQuery;
import com.gwi.selfplatform.module.net.beans.Request;
import com.gwi.selfplatform.module.net.beans.Response;
import com.gwi.selfplatform.module.net.beans.SymptomToDisease;
import com.gwi.selfplatform.module.net.beans.T1121;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * web service controller.
 * 
 * @author 彭毅
 * 
 */
public class WebServiceController {
    public static final String TAG = WebServiceController.class.getSimpleName();

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD = "Call";
    private static final String SERVICE_URL = "http://phr.gwi.com.cn:7777/Service.asmx";

    public static final String LOGIN_SOAP_ACTION = "";
//    private static final String HTTP_POST_URL = SERVICE_URL + "/" + METHOD;
public static final String HTTP_POST_URL = BuildConfig.SERVICE_URL;

    public static final String PARAMS_REQUEST = "request";
    public static final String PARAMS_RESPONSE = "CallResult";

    private static final int FUNC_CONNECT_TEST = 1010;
    private static final int FUNC_LOGIN = 1110;
    private static final int FUNC_REGISTER = 1111;
    private static final int FUNC_BIND_PUSH_USER_INFO = 1121;
    private static final int FUNC_UPLOAD_SIGN_RECS = 1210;
    private static final int FUNC_MODIFIY_SIGN_REC = 1211;
    private static final int FUNC_GET_SIGN_RECS = 1212;
    private static final int FUNC_DELETE_SIGN_REC = 1213;
    private static final int FUNC_CHANGE_PASSWORD = 1112;
    private static final int FUNC_GET_FAMILY_MEMBER = 1113;
    private static final int FUNC_MODIFY_USER_INFO = 1114;
    private static final int FUNC_PASSWORD_FIND = 1115;
    private static final int FUNC_PASSWORD_NEW_SET = 1116;
    private static final int FUNC_IS_MOBILE_REGISTERED = 1117;
    private static final int FUNC_ADD_FAMILY_MEMBER = 1118;
    private static final int FUNC_DELETE_FAMILY_MEMBER = 1119;
    private static final int FUNC_MODIFY_FAMILY_MEMBER = 1120;
    private static final int FUNC_GET_CARD_BINDING_RECS = 1310;
    private static final int FUNC_BIND_CARD = 1311;
    private static final int FUNC_UNBIND_CARD = 1312;
    private static final int FUNC_GET_EDU_LIST = 1410;
    private static final int FUNC_GET_VALIDATION_CODE = 1411;
    private static final int FUNC_CHECK_APP_VERSION = 1412;
    private static final int FUNC_GET_HEALTH_REPORT = 1413;
    private static final int FUNC_GET_EDU_CATEGORY = 1414;
    private static final int FUNC_FEED_BACK = 1510;
    private static final int FUNC_GET_DISEASE_COMMON = 1610;
    private static final int FUNC_GET_DISEASE = 1611;
    private static final int FUNC_GET_DISEASE_DETAILS = 1612;
    private static final int FUNC_GET_DEPART = 1613;
    private static final int FUNC_GET_BODY_PART = 1614;
    private static final int FUNC_GET_DRUG_COMMON = 1615;
    private static final int FUNC_GET_DRUG_AID = 1616;
    private static final int FUNC_GET_DRUG_GROUP = 1617;
    private static final int FUNC_GET_DRUG_GROUP_DETAILS = 1618;
    private static final int FUNC_GET_DRUG_COMMON_USE = 1619;
    private static final int FUNC_GET_DRUG_PROPERTY_KIND = 1620;
    private static final int FUNC_GET_TEST_CHECK_KIND = 1621;
    private static final int FUNC_GET_TEST_CHECK = 1622;
    private static final int FUNC_GET_TEST_CHECK_DETAILS = 1623;
    private static final int FUNC_GET_TREATMENT_KIND = 1624;
    private static final int FUNC_GET_TREATMENT = 1625;
    private static final int FUNC_GET_TREATMENT_DETAILS = 1626;
    private static final int FUNC_GET_BODY_AND_SYMPTOM = 1701;
    private static final int FUNC_GET_BODY_TO_SYMPTOM = 1702;
    private static final int FUNC_GET_SYMPTOM_TO_DISEASE = 1703;

    public static final String APPCODE = "2";
    public static final String TERMINAL_NO = android.os.Build.MODEL;

    private static final int COUNT = 50;

    public static final int SUCCESS = 1;

    /**
     * 用户登录,创建一个{@link Request}对象向服务器验证用户信息
     * 
     * @return
     */
    public static Response login(final String userCode, final String password,
            boolean isMD5Pwd) throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();

        Request request = new Request();
        request.setAccount(userCode);
        request.setAccountPassword(isMD5Pwd ? password : MD5Util
                .string2MD5(password));
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_LOGIN);
        request.setTerminalNo(TERMINAL_NO);
        request.setHospitalCode(WebUtil.HOSP_CODE);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before login:\n" + requestJson);
        String responsetext;
        if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
            requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key)
                    + "|" + encryption.encrypt(requestJson));
            SoapObject result = WebServiceUtils.serviceConnect(requestObject);
            if (result == null) {
                return null;
            }
            responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
            responsetext = encryption.decrypt(responsetext);

        } else {
            responsetext = GlobalSettings.INSTANCE.getConfig().getString(
                    "R" + FUNC_LOGIN, null);
        }
        Logger.d(TAG, "login():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            throw new BaseException(response.getStatus(),response.getResultMsg());
        } else {
            return response;
        }

    }

    public static Response register(T_UserInfo userInfo, T_Phr_BaseInfo baseinfo)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();

        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_REGISTER);
        request.setTerminalNo(TERMINAL_NO);
        request.setUserInfo(userInfo);
        request.setPhr_BaseInfo(baseinfo);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setHospitalCode(WebUtil.HOSP_CODE);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before register:\n" + requestJson);

        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            throw new Exception("result == null.");
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "register():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            throw new Exception("Response == null.");
        }
        return response;

    }

    /**
     * 上传体征信息
     * 
     * @param list
     * @return
     */
    public static List<T_Phr_SignRec> uploadSignRec(List<T_Phr_SignRec> list)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_UPLOAD_SIGN_RECS);
        request.setTerminalNo(TERMINAL_NO);
        request.setPhr_SignRecs(list);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before uploadSignRec:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "uploadSignRec():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            return null;
        }
        return response.getPhr_SignRecs();
    }

    /**
     * 
     * @param signCode
     * @param start
     * @param end
     * @return
     */
    public static List<T_Phr_SignRec> getListFromService(int signCode,
            Date start, Date end) {
        final List<T_Phr_SignRec> EMPTY_LIST = new ArrayList<T_Phr_SignRec>();

        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_GET_SIGN_RECS);
        request.setTerminalNo(TERMINAL_NO);
        PhrSignRecQuery query = new PhrSignRecQuery();
        query.setEhrID(GlobalSettings.INSTANCE.getCurrentFamilyAccountId());
        query.setSignCode(signCode);
        query.setStartTime(start);
        query.setEndTime(end);
        query.setCount(COUNT);
        request.setSignRecQuery(query);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String json = gson.toJson(request);
        Logger.d(TAG, "Before getSignRecsOfAll():\n" + json);
        HttpRequestObject requestObject = new HttpRequestObject();
        requestObject.url = HTTP_POST_URL;
        String key = DESEncryption.generateClearTextKey();
        try {
            DESEncryption encryption = new DESEncryption(key);
            requestObject.params.add(new BasicNameValuePair(PARAMS_REQUEST,
                    RsaUtil.encrypt(key) + "|" + encryption.encrypt(json)));

            String responseJson = WebServiceUtils.serviceConnect(requestObject);
            if (responseJson != null) {
                Logger.d(TAG, "After getSignRecsOfAll():\n" + responseJson);
                Response response = gson.fromJson(
                        encryption.decrypt(responseJson), Response.class);
                if (response == null) {
                    return EMPTY_LIST;
                }
                if (response.getStatus() != SUCCESS) {
                    return EMPTY_LIST;
                }
                return response.getPhr_SignRecs();
            } else {
                return EMPTY_LIST;
            }
        } catch (Exception e) {
            Logger.e(TAG, "getSignRecsOfAll", e);
            return EMPTY_LIST;
        }
    }

    public static boolean isServiceAvaliable() throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        // T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount("TEST");
        request.setAccountPassword("TEST");
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_CONNECT_TEST);
        request.setTerminalNo(TERMINAL_NO);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        // Logger.d(TAG, "Before isServiceAvaliable:\n" + requestJson);
        Logger.d(TAG, "Before isServiceAvaliable:\n" + RsaUtil.encrypt(key)
                + "|" + encryption.encrypt(requestJson));
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return false;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "isServiceAvaliable():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return false;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return false;
        } else {
            return true;
        }
    }

    public static T_UserInfo modifyPhone(String mobilePhone) throws Exception {
        T_UserInfo curUser = GlobalSettings.INSTANCE.getCurrentUser();
        T_UserInfo user = new T_UserInfo();
        user.setUserName(curUser.getUserName());
        user.setUserId(curUser.getUserId());
        user.setUserCode(curUser.getUserCode());
        user.setEhrId(curUser.getEhrId());
        user.setEMail(curUser.getEMail());
        user.setNickName(curUser.getNickName());
        user.setMobilePhone(mobilePhone);
        return modifyUserInfo(user);
    }

    public static List<T_Phr_SignRec> modifySignRec(List<T_Phr_SignRec> list)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_MODIFIY_SIGN_REC);
        request.setTerminalNo(TERMINAL_NO);
        request.setPhr_SignRecs(list);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before modifySignRec:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "modifySignRec():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return null;
        } else {
            return response.getPhr_SignRecs();
        }
    }

    public static Response validateBeforeResetPassword(T_UserInfo user)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        Request request = new Request();
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_PASSWORD_FIND);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setTerminalNo(TERMINAL_NO);
        request.setUserInfo(user);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before validateBeforeResetPassword:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        responsetext = encryption.decrypt(responsetext);
        Logger.d(TAG, "validateBeforeResetPassword():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            throw new Exception(response.getResultMsg());
        } else {
            return response;
        }
    }

    public static T_UserInfo resetNewPassword(T_UserInfo user) throws Exception {
        return password(user, FUNC_PASSWORD_NEW_SET);
    }

    public static T_UserInfo changePassword(T_UserInfo user) throws Exception {
        return password(user, FUNC_CHANGE_PASSWORD);
    }

    private static T_UserInfo password(T_UserInfo user, int funcode)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        if (userInfo != null) {
            request.setAccount(userInfo.getUserCode());
            request.setAccountPassword(userInfo.getUserPwd());
        }
        request.setHospitalCode(WebUtil.HOSP_CODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(funcode);
        request.setTerminalNo(TERMINAL_NO);
        request.setUserInfo(user);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before password:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "password():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return null;
        } else {
            return response.getUserInfo();
        }
    }

    public static List<T_Phr_BaseInfo> getFamilyMembers() {
        final List<T_Phr_BaseInfo> EMPTY_LIST = new ArrayList<T_Phr_BaseInfo>();
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_GET_FAMILY_MEMBER);
        request.setTerminalNo(TERMINAL_NO);
        request.setUserInfo(userInfo);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        try {
            DESEncryption encryption = new DESEncryption(key);
            String requestJson = gson.toJson(request);
            Logger.d(TAG, "Before getFamilyMembers:\n" + requestJson);
            requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key)
                    + "|" + encryption.encrypt(requestJson));
            SoapObject result = WebServiceUtils.serviceConnect(requestObject);
            if (result == null) {
                return EMPTY_LIST;
            }
            String responsetext = result
                    .getPropertySafelyAsString(PARAMS_RESPONSE);
            Logger.d(TAG, "getFamilyMembers():\n" + responsetext);
            Response response = gson.fromJson(encryption.decrypt(responsetext),
                    Response.class);
            if (response == null) {
                return EMPTY_LIST;
            }
            if (response.getStatus() != SUCCESS) {
                Logger.e(TAG, response.getResultMsg());
                return EMPTY_LIST;
            } else {
                return response.getPhr_BaseInfos();
            }
        } catch (Exception e) {
            Logger.e(TAG, "getFamilyMembers", e);
            return EMPTY_LIST;
        }
    }

    public static MobileVerParam checkAppVersion() throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_CHECK_APP_VERSION);
        request.setTerminalNo(TERMINAL_NO);
        request.setHospitalCode(WebUtil.HOSP_CODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before checkAppVersion:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));

        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        responsetext = encryption.decrypt(responsetext);
        Logger.d(TAG, "checkAppVersion():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return null;
        } else {
            return response.getMobile_VerParam();
        }
    }

    public static boolean deleteSignRec(List<T_Phr_SignRec> list)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_DELETE_SIGN_REC);
        request.setTerminalNo(TERMINAL_NO);
        request.setPhr_SignRecs(list);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before deleteSignRec:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return false;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "deleteSignRec():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return false;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return false;
        } else {
            return true;
        }
    }

    public static T_Phone_AuthCode getValidationCode(String phone)
            throws Exception {
        // TODO:
        // T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        T_Phone_AuthCode authCode = new T_Phone_AuthCode();
        authCode.setPhoneNumber(phone);
        SoapRequestObject requestObject = soapRequestBuilder();
        Request request = new Request();
        // request.setAccount(userInfo.getUserCode());
        // request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_GET_VALIDATION_CODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setPhone_AuthCode(authCode);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before getValidationCode:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));

        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        responsetext = encryption.decrypt(responsetext);
        Logger.d(TAG, "getValidationCode():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return null;
        }
        if (response.getStatus() != SUCCESS) {
            throw new Exception(response.getResultMsg());
        } else {
            return response.getPhone_AuthCode();
        }
    }

    public static List<ExT_Phr_CardBindRec> getCarBindingRecs(
            T_Phr_BaseInfo member) throws Exception {
        final List<ExT_Phr_CardBindRec> EMPTY_LIST = new ArrayList<ExT_Phr_CardBindRec>();
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_GET_CARD_BINDING_RECS);
        request.setTerminalNo(TERMINAL_NO);
        // 指定医院
        request.setHospitalCode(WebUtil.HOSP_CODE);
        request.setPhr_BaseInfo(member);
        ExT_Phr_CardBindRec cardInfo = new ExT_Phr_CardBindRec();
        // 院内诊疗卡
        cardInfo.setCardType(1);
        request.setPhr_CardBindRec(cardInfo);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before getCarBindingRecs:\n" + requestJson);
        String responsetext;
        if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
            requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key)
                    + "|" + encryption.encrypt(requestJson));
            SoapObject result = WebServiceUtils.serviceConnect(requestObject);
            if (result == null) {
                throw new Exception("无法连接到服务器");
            }
            responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
            responsetext = encryption.decrypt(responsetext);
        } else {
            responsetext = GlobalSettings.INSTANCE.getConfig().getString(
                    "R" + FUNC_GET_CARD_BINDING_RECS, null);
        }

        Logger.d(TAG, "getCarBindingRecs():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return EMPTY_LIST;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return EMPTY_LIST;
        } else {
            return response.getPhr_CardBindRecs();
        }
    }

    public static boolean bindCard(ExT_Phr_CardBindRec card) throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_BIND_CARD);
        request.setTerminalNo(TERMINAL_NO);
        request.setPhr_CardBindRec(card);
        request.setHospitalCode(WebUtil.HOSP_CODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        // 格式：RSA加密的DESkey|DESkey加密的Request json.
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before bindCard:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return false;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "bindCard():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return false;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            throw new Exception(response.getResultMsg());
        } else {
            return true;
        }
    }

    public static boolean unBindCard(ExT_Phr_CardBindRec card) throws Exception {
        T_Phr_BaseInfo member = GlobalSettings.INSTANCE
                .getCurrentFamilyAccount();
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_UNBIND_CARD);
        request.setTerminalNo(TERMINAL_NO);
        request.setPhr_BaseInfo(member);
        request.setPhr_CardBindRec(card);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before unBindCard:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return false;
        }
        Logger.d(
                TAG,
                "unBindCard():\n"
                        + result.getPropertySafelyAsString(PARAMS_RESPONSE));
        Response response = gson.fromJson(encryption.decrypt(result
                .getPropertySafelyAsString(PARAMS_RESPONSE)), Response.class);
        if (response == null) {
            return false;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return false;
        } else {
            return true;
        }
    }

    public static List<T_Base_DatumClass> getHealthEduCategory()
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        if(userInfo!=null){
            request.setAccount(userInfo.getUserCode());
            request.setAccountPassword(userInfo.getUserPwd());
        }
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_GET_EDU_CATEGORY);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setTerminalNo(TERMINAL_NO);
        request.setHospitalCode(WebUtil.HOSP_CODE);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before getHealthEduCategory:\n" + requestJson);
        String responsetext;
        if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
            requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key)
                    + "|" + encryption.encrypt(requestJson));
            SoapObject result = WebServiceUtils.serviceConnect(requestObject);
            if (result == null) {
                return Collections.emptyList();
            }
            responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
            responsetext = encryption.decrypt(responsetext);
        } else {
            responsetext = GlobalSettings.INSTANCE.getConfig().getString(
                    "R" + FUNC_GET_EDU_CATEGORY, null);
        }

        Logger.d(TAG, "getHealthEduCategory():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return Collections.emptyList();
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return Collections.emptyList();
        } else {
            return response.getBase_DatumClasses();
        }
    }

    public static List<T_HealthEdu_Datum> getHealthEduList(
            T_HealthEdu_Datum curHealth) throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request request = new Request();
        if(userInfo!=null){
            request.setAccount(userInfo.getUserCode());
            request.setAccountPassword(userInfo.getUserPwd());
        }
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_GET_EDU_LIST);
        request.setTerminalNo(TERMINAL_NO);
        request.setHealthEdu_Datum(curHealth);
        request.setHospitalCode(WebUtil.HOSP_CODE);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before getHealthEduList:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        String responsetext = null;
        if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
            SoapObject result = WebServiceUtils.serviceConnect(requestObject);
            if (result == null) {
                return Collections.emptyList();
            }
            responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
            responsetext = encryption.decrypt(responsetext);
        } else {
            responsetext = GlobalSettings.INSTANCE.getConfig().getString(
                    "R1410_" + curHealth.getDatumClass(), null);
        }

        Logger.d(TAG, "getHealthEduList():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response == null) {
            return Collections.emptyList();
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return Collections.emptyList();
        } else {
            return response.getHealthEdu_Datums();
        }
    }

    public static boolean feedBack(T_FeedBack_Rec rec) throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();

        Request request = new Request();
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_FEED_BACK);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setTerminalNo(TERMINAL_NO);
        request.setFeedBack_Rec(rec);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before feedBack:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return false;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "feedBack():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response == null) {
            return false;
        }
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return false;
        } else {
            return true;
        }

    }

    public static T_UserInfo modifyNickName(T_UserInfo newInfo)
            throws Exception {
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        newInfo.setUserName(userInfo.getUserName());
        newInfo.setUserId(userInfo.getUserId());
        newInfo.setUserCode(userInfo.getUserCode());
        newInfo.setEhrId(userInfo.getEhrId());
        newInfo.setEMail(userInfo.getEMail());
        newInfo.setMobilePhone(userInfo.getMobilePhone());
        return modifyUserInfo(newInfo);
    }

    public static boolean modifyInfo(T_UserInfo newUserInfo,
            T_Phr_BaseInfo newFamilyInfo) throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();

        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_MODIFY_USER_INFO);
        request.setTerminalNo(TERMINAL_NO);
        request.setUserInfo(newUserInfo);
        request.setPhr_BaseInfo(newFamilyInfo);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before modifyUserInfo:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return false;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        responsetext = encryption.decrypt(responsetext);
        Logger.d(TAG, "modifyUserInfo():\n" + responsetext);
        Response response = gson.fromJson(responsetext, Response.class);
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            throw new Exception(response.getResultMsg());
        } else {
            return true;
        }
    }

    private static T_UserInfo modifyUserInfo(T_UserInfo newInfo)
            throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();

        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_MODIFY_USER_INFO);
        request.setTerminalNo(TERMINAL_NO);
        request.setUserInfo(newInfo);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before modifyUserInfo:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        Logger.d(TAG, "modifyUserInfo():\n" + responsetext);
        Response response = gson.fromJson(encryption.decrypt(responsetext),
                Response.class);
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            throw new Exception(response.getResultMsg());
        } else {
            return response.getUserInfo();
        }

    }

    public static HealthReport getHealthReport() throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        T_Phr_BaseInfo baseinfo = new T_Phr_BaseInfo();
        baseinfo.setEhrID(GlobalSettings.INSTANCE.getCurrentFamilyAccountId());
        Request request = new Request();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_GET_HEALTH_REPORT);
        request.setPhr_BaseInfo(baseinfo);
        request.setTerminalNo(TERMINAL_NO);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before getHealthReport:\n" + requestJson);
        requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key) + "|"
                + encryption.encrypt(requestJson));
        SoapObject result = WebServiceUtils.serviceConnect(requestObject);
        if (result == null) {
            return null;
        }
        String responsetext = result.getPropertySafelyAsString(PARAMS_RESPONSE);
        String json = encryption.decrypt(responsetext);
        Logger.d(TAG, "getHealthReport():\n" + json);
        Response response = gson.fromJson(json, Response.class);
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return null;
        } else {
            return response.getHealthReport();
        }
    }

    public static boolean isMobileRegistered(T_UserInfo phoneInfo)
            throws Exception {
        Request request = new Request();
        request.setUserInfo(phoneInfo);
        request.setAppTypeCode(APPCODE);
        request.setFunCode(FUNC_IS_MOBILE_REGISTERED);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setTerminalNo(TERMINAL_NO);
        Response response = execute(request, false);
        final int REGISTERED = 1;// 0 for unregister.
        if(response.getMobilePhoneExist()==REGISTERED) {
            throw new BaseException(response.getResultMsg());
        }else {
            return false;
        }
//        return response.getMobilePhoneExist() == REGISTERED;
    }

    public static List<KBDiseaseDetails> getCommonDisease(String searchMsg)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DISEASE_COMMON);
        request.setSearchMsg(searchMsg);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);

        Response response = execute(request, true);
        return response.getKB_Diseases();
    }

    public static List<KBDiseaseDetails> getDisease(String bodyPartCode,
            String deptCode) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DISEASE);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);

        request.setBodyPartCode(bodyPartCode);
        request.setDeptCode(deptCode);

        Response response = execute(request, true);
        return response.getKB_Diseases();
    }

    public static List<KBDiseaseDetails> getDiseaseDetails(String diseaseId)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DISEASE_DETAILS);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);

        request.setDiseaseId(diseaseId);

        Response response = execute(request, true);
        return response.getKB_Diseases();
    }

    public static List<KBBodyPart> getBodyParts() throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_BODY_PART);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setBodyPartCode(null);

        Response response = execute(request, true);
        return response.getBase_BodyPartDicts();
    }

    public static List<KBDepart> getDiseaseDepart(String deptCode)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DEPART);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setDeptCode(deptCode);

        Response response = execute(request, true);
        return response.getBase_DeptDicts();
    }

    public static List<KBDrugDetails> getDrugClassify(String kindCode,
            String searchKey) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DRUG_GROUP);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setSearchMsg(searchKey);
        if (kindCode != null) {
            request.setPropertyKindCode(kindCode);
        } else {
            request.setPropertyKindCode("010101");
        }
        Response response = execute(request, true);
        return response.getKB_Drugs();
    }

    public static List<KBDrugUseKind> getDrugKindDicts(String useKindCode)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DRUG_COMMON_USE);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setUseKindCode(useKindCode);
        Response response = execute(request, true);
        return response.getBase_UseKindDicts();
    }

    public static List<KBDrugDetails> getDrugCommons(String useKindCode)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DRUG_COMMON);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setUseKindCode(useKindCode);

        Response response = execute(request, true);
        return response.getKB_Drugs();
    }

    public static List<KBDrugDetails> getDrugResuce() throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DRUG_AID);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);

        Response response = execute(request, true);
        return response.getKB_Drugs();
    }

    public static List<KBDrugDetails> getDrugDetail(String drugId)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DRUG_GROUP_DETAILS);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setDrugId(drugId);

        Response response = execute(request, true);
        return response.getKB_Drugs();
    }

    public static List<KBDrugProperty> getDrugProperty(String code)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_DRUG_PROPERTY_KIND);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        if (code == null) {
            request.setPropertyKindCode("0");
        } else {
            request.setPropertyKindCode(code);
        }
        Response response = execute(request, true);
        return response.getBase_PropertyKindDicts();
    }

    public static List<KBTreatmentKind> getTreatmentKinds() throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_TREATMENT_KIND);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setTreatmentKindCode(null);

        Response response = execute(request, true);
        return response.getBase_TreatmentKindDicts();
    }

    public static List<KBTreatmentDetails> getTreatmentList(
            String treatmentKindCode, String searchMsg) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_TREATMENT);
        request.setAppTypeCode(APPCODE);
        request.setSearchMsg(searchMsg);
        request.setTerminalNo(TERMINAL_NO);
        request.setTreatmentKindCode(treatmentKindCode);

        Response response = execute(request, true);
        return response.getKB_Treatments();
    }

    public static List<KBTreatmentDetails> getTreatmentDetail(String treatmentId)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_TREATMENT_DETAILS);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setTreatmentId(treatmentId);

        Response response = execute(request, true);
        return response.getKB_Treatments();
    }

    public static List<KBTestCheckKind> getTestKinds(String testKindCode)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_TEST_CHECK_KIND);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setTestKindCode(testKindCode);

        Response response = execute(request, true);
        return response.getBase_TestKindDicts();
    }

    public static List<KBTestCheckDetails> getTestList(String testKindCode,
            String searchMsg) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_TEST_CHECK);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setTestKindCode(testKindCode);
        request.setSearchMsg(searchMsg);
        Response response = execute(request, true);
        return response.getKB_TestChecks();
    }

    public static List<KBTestCheckDetails> getTestDetail(String testId)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_TEST_CHECK_DETAILS);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setTestId(testId);

        Response response = execute(request, true);
        return response.getKB_TestChecks();
    }

    public static List<KBBodyPart> getBodyParts(String hexColor)
            throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_BODY_AND_SYMPTOM);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setHexColor(hexColor);
        Response response = execute(request, true);
        return response.getBase_BodyPartDicts();
    }

    /**
     * 
     * @param partCode
     * @param sex
     * @return
     * @throws Exception
     */
    public static List<BodyToSymptom> getBodyToSymptoms(String partCode,
            String sex) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_BODY_TO_SYMPTOM);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setBodyPartCode(partCode);
        request.setSex(sex);
        Response response = execute(request, true);
        return response.getBase_SymptomDicts();
    }

    public static List<KBDiseaseDetails> getSymptomsToDisease(
            String sysmptomId, String sex) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_SYMPTOM_TO_DISEASE);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setSymptomId(sysmptomId);
        request.setSex(sex);
        Response response = execute(request, true);
        return response.getKB_Diseases();
    }

    /**
     * [智能导诊]
     * @param sid
     * @param sex
     * @return
     */
    public static List<SymptomToDisease> getSymptomsToDisease_rev(String sid, String sex) throws Exception {
        Request request = new Request();
        request.setFunCode(FUNC_GET_SYMPTOM_TO_DISEASE);
        request.setAppTypeCode(APPCODE);
        request.setTerminalNo(TERMINAL_NO);
        request.setSId(sid);
        request.setSex(sex);
        Response response = execute(request, true);
        return response.getKB_SymptomAndDiseases();
    }

    public static boolean addFamilyMember(T_Phr_BaseInfo newMember)
            throws Exception {
        try {
            T_UserInfo userinfo = GlobalSettings.INSTANCE.getCurrentUser();
            Request request = new Request();
            request.setAccount(userinfo.getUserCode());
            request.setAccountPassword(userinfo.getUserPwd());
            request.setAppTypeCode(APPCODE);
            request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
            request.setTerminalNo(TERMINAL_NO);
            request.setFunCode(FUNC_ADD_FAMILY_MEMBER);
            request.setPhr_BaseInfo(newMember);
            Response response = execute(request, true);
            if(response.getStatus()!=1) {
                throw new BaseException(response.getResultMsg());
            }
            return !response.getPhr_BaseInfos().isEmpty();
        } catch (Exception e) {
            Logger.e(TAG, e.getLocalizedMessage());
            throw e;
        }
    }

    public static boolean deleteFamilyMember(T_Phr_BaseInfo member)
            throws Exception {
        try {
            T_UserInfo userinfo = GlobalSettings.INSTANCE.getCurrentUser();
            Request request = new Request();
            request.setAccount(userinfo.getUserCode());
            request.setAccountPassword(userinfo.getUserPwd());
            request.setAppTypeCode(APPCODE);
            request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
            request.setTerminalNo(TERMINAL_NO);
            request.setFunCode(FUNC_DELETE_FAMILY_MEMBER);
            request.setPhr_BaseInfo(member);
            Response response = execute(request, true);
            if (response.getStatus() != SUCCESS) {
                throw new Exception(response.getResultMsg());
            }
            return true;
        } catch (Exception e) {
            Logger.e(TAG, e.getLocalizedMessage());
            throw e;
        }
    }

    public static boolean modifyFamilyInfo(T_Phr_BaseInfo baseinfo)
            throws Exception {
        try {
            T_UserInfo userinfo = GlobalSettings.INSTANCE.getCurrentUser();
            T_UserInfo userForValidate = new T_UserInfo();
            userForValidate.setUserId(userinfo.getUserId());
            Request request = new Request();
            request.setAccount(userinfo.getUserCode());
            request.setAccountPassword(userinfo.getUserPwd());
            request.setAppTypeCode(APPCODE);
            request.setTerminalNo(TERMINAL_NO);
            request.setFunCode(FUNC_MODIFY_FAMILY_MEMBER);
            request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
            request.setUserInfo(userForValidate);
            request.setPhr_BaseInfo(baseinfo);
            Response response = execute(request, true);
            if (response.getStatus() != SUCCESS) {
                throw new Exception(response.getResultMsg());
            }
            return true;
        } catch (Exception e) {
            Logger.e(TAG, e.getLocalizedMessage());
            throw e;
        }
    }
    
    public static boolean bindPushUserInfo(T1121 pushUserInfo)
            throws Exception {
        Request request = new Request();
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        request.setAccount(userInfo.getUserCode());
        request.setAccountPassword(userInfo.getUserPwd());
        request.setBaiduUserInfo(pushUserInfo);
        request.setAppTypeCode(APPCODE);
        request.setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.setFunCode(FUNC_BIND_PUSH_USER_INFO);
        request.setTerminalNo(TERMINAL_NO);
        Response response = execute(request, false);
        if (response.getStatus() != SUCCESS) {
            Logger.e(TAG, response.getResultMsg());
            return false;
        } else {
            return true;
        }
       
    }

    public static Response execute(Request request, boolean isHttpPost)
            throws Exception {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new DateAdapter()).create();
        String key = DESEncryption.generateClearTextKey();
        DESEncryption encryption = new DESEncryption(key);
        String requestJson = gson.toJson(request);
        Logger.d(TAG, "Before execute:\n" + requestJson);
        Response response = null;
        if (!isHttpPost) {
            SoapRequestObject requestObject = soapRequestBuilder();
            requestObject.paramsPair.put(PARAMS_REQUEST, RsaUtil.encrypt(key)
                    + "|" + encryption.encrypt(requestJson));
            SoapObject result = WebServiceUtils.serviceConnect(requestObject);
            String responsetext = result
                    .getPropertySafelyAsString(PARAMS_RESPONSE);
            String json = encryption.decrypt(responsetext);
            Logger.d(TAG, "after execute():\n" + json);
            response = gson.fromJson(json, Response.class);
        } else {
            HttpRequestObject requestObject = new HttpRequestObject();
            requestObject.url = HTTP_POST_URL;
            requestObject.params.add(new BasicNameValuePair(PARAMS_REQUEST,
                    RsaUtil.encrypt(key) + "|"
                            + encryption.encrypt(requestJson)));

            String responseJson = WebServiceUtils.serviceConnect(requestObject);
            if (responseJson != null) {
                responseJson = encryption.decrypt(responseJson);
                Logger.d(TAG, "After execute():\n" + responseJson);
                response = gson.fromJson(responseJson, Response.class);
            }
        }

        return response;

    }

    /**
     * {@link Date}类型适配器，序列化和反序列化:/Date(1234567890123+0800)/
     * 
     * @author 彭毅
     * 
     */
    public static class DateAdapter implements JsonSerializer<Date>,
            JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement arg0, Type arg1,
                JsonDeserializationContext arg2) throws JsonParseException {
            return new Date(Long.parseLong(CommonUtils
                    .fromDotNetDateTimeString(arg0.getAsString())));
        }

        @Override
        public JsonElement serialize(Date arg0, Type arg1,
                JsonSerializationContext arg2) {
            return new JsonPrimitive(CommonUtils.toDotNetDateTimeString(arg0
                    .getTime()));
        }
    }

    public static SoapRequestObject soapRequestBuilder() {
        SoapRequestObject requestObject = new SoapRequestObject();
        requestObject.namespaces = NAMESPACE;
        requestObject.methodName = METHOD;
        requestObject.serviceUrl = SERVICE_URL;
        requestObject.soapAction = LOGIN_SOAP_ACTION;
        requestObject.versionType = SoapEnvelope.VER12;
        return requestObject;
    }

    /**
     * SOAP 请求对象
     * 
     * @author 彭毅
     * 
     */
    public static class SoapRequestObject {
        public String namespaces;
        public String methodName;
        public String serviceUrl;
        public String soapAction;
        public int versionType;
        public Map<String, String> paramsPair = new HashMap<String, String>();
    }

    public static class HttpRequestObject {
        public String url;
        public List<NameValuePair> params = new ArrayList<NameValuePair>();
    }
}
