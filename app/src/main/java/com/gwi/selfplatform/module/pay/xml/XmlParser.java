package com.gwi.selfplatform.module.pay.xml;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.BaseRequest;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.request.T1214;
import com.gwi.selfplatform.module.net.request.T1414;
import com.gwi.selfplatform.module.net.request.T1612;
import com.gwi.selfplatform.module.net.request.T1710;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * XML ==> JSON
 */
public class XmlParser {
    /**
     * 充值json数据
     *
     */
    public static String getChargeJSON(Request<T1710> request) {
        return transfer(request);
    }

    /**
     * 挂号xml数据
     *
     * @return
     */
    public static String getRegisterJSON(Request<T1214> request) {
        return transfer(request);
    }

    /**
     * UrlEncode
     *
     * @param data
     * @return
     */
    private static String transfer(Object data) {
        String requestJson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Date.class, new BaseRequest.DotNetDateAdapter())
                .create().toJson(data);
        try {
            try {
                JSONObject requestJsonObject = new JSONObject(requestJson);
                JSONObject wrapperedJsonObject = new JSONObject();
                wrapperedJsonObject.put("Request", requestJsonObject);
                Logger.d("In execute", wrapperedJsonObject.toString());
                return URLEncoder.encode(wrapperedJsonObject.toString(), "UTF-8");
            } catch (JSONException e) {
                e.printStackTrace();
                requestJson = " {\"Request\":" + requestJson + "}";
            }
            Logger.d("In execute", requestJson);
            return URLEncoder.encode(requestJson, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 预约挂号json数据
     *
     * @return
     */
    public static String getOrderRegisterJSON(Request<T1414> request) {
        return transfer(request);
    }

    /**
     * 缴费json数据
     *
     * @return
     */
    public static String getRecipePayJSON(Request<T1612> request) {
        return transfer(request);
    }
}
