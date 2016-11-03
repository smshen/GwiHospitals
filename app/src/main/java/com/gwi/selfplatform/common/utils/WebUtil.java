package com.gwi.selfplatform.common.utils;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.TRequest;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.exception.BaseException;
import com.gwi.selfplatform.common.security.DESEncryption;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.security.RsaUtil;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.response.G1013;
import com.gwi.selfplatform.module.net.webservice.WebServiceController.HttpRequestObject;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WebUtil {
    private static final String TAG = "WebUtil";
    public static final int SUCCESS = 1;
    public static final int FALSE = 2;

    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String METHOD = "Call";
    private static final String SERVICE_URL = "http://phr.gwi.com.cn:7780/Service.asmx";
    private static final String LOGIN_SOAP_ACTION = "";

    private static final String HTTP_POST_URL = BuildConfig.SERVER_URL_FLAVOR + "/Call";

    private static final String PARAMS_REQUEST = "request";
    private static final String PARAMS_RESPONSE = "CallResult";
    public static final String APPCODE = "2";
    public static final String APP_TYPE_CODE = "9";

    public static final String APP_TYPE = "phr";

    private static String _TERMINO_NO;

    public static String TERMINAL_NO() {
        if (TextUtils.isEmpty(_TERMINO_NO)) {
            Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
            _TERMINO_NO = HospitalParams.getValue(params, HospitalParams.CODE_TERMINAL_NO);
        }
        return _TERMINO_NO;
    }

    public static String HOSP_CODE;

//     public static String HOSP_CODE = "MNYY";

    public static final String CorpCode = "1001";

    public static String SUB_HOSP_CODE = null;

    public static void setHospCode(String hospCode) {
        HOSP_CODE = hospCode;
    }

    public static void setSubHospCode(String hospCode) {
        SUB_HOSP_CODE = hospCode;
    }

    public static void setTerminalNo(String TerNo) {
        _TERMINO_NO = TerNo;
    }

    public interface JSONResponseHandler {
        void handleJsonResponse(JSONObject obj);
    }

    public static void httpExecute(Object object, JSONResponseHandler handler) throws Exception {
        HttpRequestObject requestObject = new HttpRequestObject();
        String jsonRequest;
        String responsedata;
        try {
            String data = new GsonBuilder().registerTypeAdapter(Date.class, new DateAdapter())
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create().toJson(object);
            JSONObject body = new JSONObject(data);
            JSONObject json = new JSONObject();
            json.put(Request.class.getSimpleName(), body);
            jsonRequest = json.toString();
            Logger.d(TAG, "Before httpExecute:\n" + jsonRequest);
            if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
                requestObject.url = HTTP_POST_URL;
                requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_PARTNER_ID, ApiCodeTemplate.PATERNER_ID));
                requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_CONTENT,
                        jsonRequest));
                String token = GlobalSettings.INSTANCE.getToken();
                if (token == null) {
                    requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_SIGN, jsonRequest + ApiCodeTemplate.SIGN_PART));
                } else {
                    requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_SIGN, jsonRequest + token + ApiCodeTemplate.SIGN_PART));
                }
                requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_TOKEN, GlobalSettings.INSTANCE.getToken()));
                responsedata = WebServiceUtils.serviceConnect(requestObject);
                if (responsedata == null) {
                    throw new Exception();
                }
            } else {
                int funCode = ((TRequest<?>) object).getHeader().getFunCode();
                responsedata = GlobalSettings.INSTANCE.getConfig().getString("G" + funCode, null);
            }

            Logger.d(TAG, "after httpExecute:\n" + responsedata);
            JSONObject responseObj = new JSONObject(responsedata);
            handler.handleJsonResponse(responseObj);
        } catch (Exception e) {
            Logger.e(TAG, "execute2", e);
            throw e;
        }
    }


    /**
     * @param object
     * @param isList
     * @return 网络错误返回或数据错误返回null
     * @throws Exception
     */
    public static JSONObject httpExecute(Object object, boolean isList) throws Exception {
        HttpRequestObject requestObject = new HttpRequestObject();
        String jsonRequest;
        String responsedata;
        JSONObject obj;
        try {
            String data = new GsonBuilder().registerTypeAdapter(Date.class, new DateAdapter())
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create().toJson(object);
            JSONObject body = new JSONObject(data);
            JSONObject json = new JSONObject();
            json.put(Request.class.getSimpleName(), body);
            jsonRequest = json.toString();
            Logger.d(TAG, "Before httpExecute:\n" + jsonRequest);
            if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
                requestObject.url = HTTP_POST_URL;
                requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_PARTNER_ID, ApiCodeTemplate.PATERNER_ID));
                requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_CONTENT,
                        jsonRequest));
                String token = GlobalSettings.INSTANCE.getToken();
                if (token == null) {
                    requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_SIGN,
                            MD5Util.string2MD5(jsonRequest + ApiCodeTemplate.SIGN_PART)));
                } else {
                    requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_SIGN,
                            MD5Util.string2MD5(jsonRequest + token + ApiCodeTemplate.SIGN_PART)));
                }
                requestObject.params.add(new BasicNameValuePair(ApiCodeTemplate.PARAM_FORM_KEY_TOKEN, GlobalSettings.INSTANCE.getToken()));
                responsedata = WebServiceUtils.serviceConnect(requestObject);
                if (responsedata == null) {
                    throw new Exception();
                }
            } else {
                int funCode = ((Request<?>) object).getHeader().getFunCode();
                responsedata = GlobalSettings.INSTANCE.getConfig().getString("G" + funCode, null);
            }

            Logger.d(TAG, "after httpExecute:\n" + responsedata);
            JSONObject responseObj = new JSONObject(responsedata).getJSONObject("Response");
            JSONObject headerObj = responseObj.getJSONObject("Header");
            int status = headerObj.getInt("Status");
            //TODO:
            if (status != 1) {
                String resultMsg = headerObj.getString("ResultMsg");
                throw new BaseException(status, resultMsg);
            }
            if (isList) {
                if (responseObj.getJSONObject("Body").isNull("Items")) {
                    return null;
                }
                obj = responseObj.getJSONObject("Body").getJSONObject("Items");
            } else {
                if (responseObj.isNull("Body")) {
                    return null;
                }
                obj = responseObj.getJSONObject("Body");
            }
        } catch (Exception e) {
            Logger.e(TAG, "execute2", e);
            throw e;
        }
        return obj;
    }

    /**
     * 加密数据后的http请求
     *
     * @param object
     * @param isList
     * @return 网络错误返回或数据错误返回null
     * @throws Exception
     */
    public static JSONObject httpExecuteWithEncryption(Object object, boolean isList) throws Exception {
        HttpRequestObject requestObject = new HttpRequestObject();
        final String DIV = "|";
        StringBuilder builder = new StringBuilder();
        builder.append(APP_TYPE);
        builder.append(DIV);
        String desKey = DESEncryption.generateClearTextKey();
        String jsonRequest;
        String responsedata;
        JSONObject obj = null;
        try {
            builder.append(RsaUtil.encrypt(desKey));
            builder.append(DIV);
            String data = JsonUtil.toJson(object);
            JSONObject body = new JSONObject(data);
            JSONObject json = new JSONObject();
            json.put(Request.class.getSimpleName(), body);
            jsonRequest = json.toString();
            Logger.d(TAG, "Before httpExecute:\n" + jsonRequest);
            if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
                DESEncryption encrypt = new DESEncryption(desKey);
                jsonRequest = encrypt.encrypt(jsonRequest);
                builder.append(jsonRequest);
                Logger.d(TAG, "httpExecute:" + builder.toString());
                requestObject.url = HTTP_POST_URL;
                requestObject.params.add(new BasicNameValuePair(PARAMS_REQUEST,
                        builder.toString()));
                String result = WebServiceUtils.serviceConnect(requestObject);
                if (result == null) {
                    throw new Exception();
                }
                responsedata = encrypt.decrypt(result);
            } else {
                int funCode = ((TRequest<?>) object).getHeader().getFunCode();
                responsedata = GlobalSettings.INSTANCE.getConfig().getString("G" + funCode, null);
            }

            Logger.d(TAG, "after httpExecute:\n" + responsedata);
            JSONObject responseObj = new JSONObject(responsedata).getJSONObject("Response");
            JSONObject headerObj = responseObj.getJSONObject("Header");
            int status = headerObj.getInt("Status");
            //TODO:
            if (status != 1) {
                String resultMsg = headerObj.getString("ResultMsg");
                throw new BaseException(status, resultMsg);
            }
            if (isList) {
                if (responseObj.getJSONObject("Body").isNull("Items")) {
                    return null;
                }
                obj = responseObj.getJSONObject("Body").getJSONObject("Items");
            } else {
                if (responseObj.isNull("Body")) {
                    return null;
                }
                obj = responseObj.getJSONObject("Body");
            }
        } catch (Exception e) {
            Logger.e(TAG, "execute2", e);
            throw e;
        }
        return obj;
    }

    /**
     * @param object
     * @param isList 返回的数据是否带有List
     * @return
     * @throws Exception
     */
    public static JSONObject execute(Object object, boolean isList) throws Exception {
        SoapRequestObject requestObject = soapRequestBuilder();
        final String DIV = "|";
        StringBuilder builder = new StringBuilder();
        builder.append(APP_TYPE);
        builder.append(DIV);
        String desKey = DESEncryption.generateClearTextKey();
        String jsonRequest = null;
        String responsedata = null;
        JSONObject obj = null;
        try {
            builder.append(RsaUtil.encrypt(desKey));
            builder.append(DIV);
            String data = JsonUtil.toJson(object);
            JSONObject body = new JSONObject(data);
            JSONObject json = new JSONObject();
            json.put(Request.class.getSimpleName(), body);
            jsonRequest = json.toString();
            Logger.d(TAG, "Before execute:\n" + jsonRequest);
            if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
                DESEncryption encryption = new DESEncryption(desKey);
                jsonRequest = encryption.encrypt(jsonRequest);
                builder.append(jsonRequest);
                requestObject.paramsPair.put(PARAMS_REQUEST, builder.toString());
                SoapObject result = WebServiceUtils.serviceConnect(requestObject);
                if (result == null) {
                    throw new Exception();
                }
                responsedata = result.getPropertySafelyAsString(PARAMS_RESPONSE);
                responsedata = encryption.decrypt(responsedata);
            } else {
                int funCode = ((TRequest<?>) object).getHeader().getFunCode();
                responsedata = GlobalSettings.INSTANCE.getConfig().getString("G" + funCode, null);
            }
            Logger.d(TAG, "execute:\n" + responsedata);

            JSONObject responseObj = new JSONObject(responsedata).getJSONObject("Response");
            JSONObject headerObj = responseObj.getJSONObject("Header");
            int status = headerObj.getInt("Status");
            //TODO:
            if (status != 1) {
                String resultMsg = headerObj.getString("ResultMsg");
                throw new BaseException(status, resultMsg);
            }

            if (isList) {
                if (responseObj.getJSONObject("Body").isNull("Items")) {
                    return null;
                }
                obj = responseObj.getJSONObject("Body").getJSONObject("Items");
            } else {
                if (responseObj.isNull("Body")) {
                    return null;
                }
                obj = responseObj.getJSONObject("Body");
            }
        } catch (Exception e) {
            Logger.e(TAG, "execute", e);
            throw e;
        }
        return obj;
    }

    /**
     * @param object java object.
     * @return
     */
    public static G1013 getHospitalInfo(Object object) {
        SoapRequestObject requestObject = soapRequestBuilder();
        final String DIV = "|";
        StringBuilder builder = new StringBuilder();
        builder.append(APP_TYPE);
        builder.append(DIV);
        String desKey = DESEncryption.generateClearTextKey();
        String jsonRequest = null;
        String responsedata = null;
        G1013 g1013 = null;
        JSONObject obj = null;
        try {
            builder.append(RsaUtil.encrypt(desKey));
            builder.append(DIV);
            String data = JsonUtil.toJson(object);
            JSONObject body = new JSONObject(data);
            JSONObject json = new JSONObject();
            json.put(Request.class.getSimpleName(), body);
            jsonRequest = json.toString();
            Logger.d(TAG, "Before getHospitalInfo:\n" + jsonRequest);
            if (!GlobalSettings.INSTANCE.MODE_LOCAL) {
                DESEncryption encryption = new DESEncryption(desKey);
                jsonRequest = encryption.encrypt(jsonRequest);
                builder.append(jsonRequest);
                requestObject.paramsPair.put(PARAMS_REQUEST, builder.toString());
                SoapObject result = WebServiceUtils.serviceConnect(requestObject);
                if (result == null) {
                    return null;
                }
                responsedata = result.getPropertySafelyAsString(PARAMS_RESPONSE);
                responsedata = encryption.decrypt(responsedata);
            } else {
                responsedata = GlobalSettings.INSTANCE.getConfig().getString("G1013", null);
            }

            Logger.d(TAG, "getHospitalInfo:\n" + responsedata);
            obj = new JSONObject(responsedata);
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                    new DateAdapter()).create();
            JSONObject g1013Obj = obj.getJSONObject("Response");
            g1013 = gson.fromJson(g1013Obj.toString(), G1013.class);
        } catch (Exception e) {
            Logger.e(TAG, "getHospitalInfo", e);
        }
        return g1013;
    }


    private static class DateAdapter implements JsonSerializer<Date>,
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

    private static SoapRequestObject soapRequestBuilder() {
        SoapRequestObject requestObject = new SoapRequestObject();
        requestObject.namespaces = NAMESPACE;
        requestObject.methodName = METHOD;
        requestObject.serviceUrl = SERVICE_URL;
        requestObject.soapAction = LOGIN_SOAP_ACTION;
        requestObject.versionType = SoapEnvelope.VER12;
        return requestObject;
    }

    public static class SoapRequestObject {
        public String namespaces;
        public String methodName;
        public String serviceUrl;
        public String soapAction;
        public int versionType;
        public Map<String, String> paramsPair = new HashMap<String, String>();
    }
}
