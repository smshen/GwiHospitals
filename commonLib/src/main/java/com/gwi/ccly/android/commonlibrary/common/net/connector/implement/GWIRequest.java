package com.gwi.ccly.android.commonlibrary.common.net.connector.implement;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.ccly.android.commonlibrary.common.utils.JsonUtil;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.ccly.android.commonlibrary.common.utils.TextUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

/**
 * 后台接口请求对象
 *
 * @author 彭毅
 * @date 2015/4/20.
 */
public class GWIRequest<R> extends BaseRequest<R> {

    private static final String TAG = GWINet.class.getSimpleName();

    public static String PARAM_FORM_KEY_PARTNER_ID = "partnerid";
    public static String PARAM_FORM_KEY_CONTENT = "content";
    public static String PARAM_FORM_KEY_SIGN = "sign";
    public static String PARAM_FORM_KEY_TOKEN = "token";

    public static final String RESPONSE_NODE_Response = "Response";
    public static final String RESPONSE_NODE_BODY = "Body";
    public static final String RESPONSE_NODE_ITEMS = "Items";

    public static String PATERNER_ID = "phr";


    public static final String SIGN_PART = "p6c7s4nanolegy5vczj94vdr9rros77t"/*JniManager.INSTANCE.getSignKey()*/;

    protected final Gson mGson;
    protected final Type mResType;
    private final Object mBodyRequest;
    private Map<String, String> mParams;
    private final Response.Listener<R> mListener;

    /**
     * Constructor with sepcified gson instance.
     *
     * @param method
     * @param url
     * @param request
     * @param listener
     * @param errorListener
     * @param gson
     */
    public GWIRequest(int method, Map<String, String> params,
                      String url, Object request,
                      Type resType,
                      Response.Listener<R> listener,
                      Response.ErrorListener errorListener, Gson gson) {
        super(method, url, errorListener);
        mParams = params;
        mBodyRequest = request;
        this.mResType = resType;
        this.mListener = listener;
        if (gson == null) {
            mGson = new GsonBuilder().registerTypeAdapter(Date.class,
                    new DotNetDateAdapter()).setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        } else {
            mGson = gson;
        }
    }

    /**
     * Constructor with sepcified gson instance.
     *
     * @param method
     * @param url
     * @param request
     * @param listener
     * @param errorListener
     */
    public GWIRequest(int method, Map<String, String> params,
                      String url, Object request,
                      Type resType,
                      Response.Listener<R> listener,
                      Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mParams = params;
        mBodyRequest = request;
        this.mResType = resType;
        this.mListener = listener;
        mGson = new GsonBuilder().registerTypeAdapter(Date.class, new DotNetDateAdapter()).setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mParams == null ? super.getHeaders() : mParams;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
//        try {
//            return createBodyParams(mBodyRequest);
//        } catch (Exception e) {
//            LogUtil.e(TAG, e);
//        }
        LogUtil.d("Before execute", mGson.toJson(mBodyRequest));
        if (mBodyRequest instanceof Map) return (Map<String, String>) mBodyRequest;
        return super.getParams();
    }

//    private Map<String, String> createBodyParams(Object request) throws Exception {
//        Map<String, String> params = new HashMap<>();
//        params.put(PARAM_FORM_KEY_PARTNER_ID, PATERNER_ID);
//
//        String dataJson = mGson.toJson(request);
//        JSONObject body = new JSONObject(dataJson);
//        JSONObject requestJsonObj = new JSONObject();
//        requestJsonObj.put("Request", body);
//
//        params.put(PARAM_FORM_KEY_CONTENT, requestJsonObj.toString());
//        String token = GlobalSettings.INSTANCE.getToken();
//        String sign;
//        if (token != null) {
//            params.put(PARAM_FORM_KEY_TOKEN, token);
//            //得到MD5校验码
//            sign = MD5Util.string2MD5(params.get(PARAM_FORM_KEY_CONTENT) + token + SIGN_PART);
//        } else {
//            sign = MD5Util.string2MD5(params.get(PARAM_FORM_KEY_CONTENT) + SIGN_PART);
//        }
//
//        params.put(PARAM_FORM_KEY_SIGN, sign);
//
//        Logger.d("Before execute", mGson.toJson(params));
//
//        return params;
//    }



    @Override
    protected Response<R> parseNetworkResponse(NetworkResponse response) {
        try {
            String responsedata = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            LogUtil.d(TAG, "After execute\n" + responsedata);
            JSONObject jsonObject = new JSONObject(responsedata);
            //删除json中Response节点
            JSONObject responseObject = jsonObject.getJSONObject("Response");
            int status= Integer.valueOf(responseObject.getJSONObject("Header").getString("Status"));
            if (GResponse.SUCCESS!=status) {
                return Response.error(new GWIVolleyError(status,
                        new Exception(responseObject.getJSONObject("Header")
                        .getString("ResultMsg"))));
            }

            if (!responseObject.isNull("Body")) {
                if (responseObject.get("Body") instanceof JSONObject) {
//                    if (((JSONObject) responseObject.get("Body")).length() <= 1) {
                    JSONObject bodyObject = responseObject.getJSONObject("Body");
                    if (bodyObject.has("Items")) {
                        if (bodyObject.isNull("Items")) {
                            JSONObject object = new JSONObject();
                            JSONArray array = new JSONArray();
                            object.put("Item", array);
                            bodyObject.put("Items", object);
                        }
                        JSONObject itemsObject = bodyObject.getJSONObject("Items");
                        TypeToken<R> token = new TypeToken<R>() {
                        };
                        Object result = JsonUtil.toListObject(itemsObject, "Item", token.getRawType(), mResType, mGson);
                        return Response.success((R) result, HttpHeaderParser.parseCacheHeaders(response));

                    }
                }
//                }
            } else {
                //如果body为空，则返回没有获取到数据，但是有些接口body为空，返回Status为1：例如删除体征，这里要把statusCode传回去，让页面根据statusCode特殊处理
                JSONObject headerObject = responseObject.getJSONObject("Header");
                int statusCode = headerObject.getInt("Status");
                return Response.error(new GWIVolleyError(statusCode,
                        new Exception(TextUtil.isEmpty(headerObject.getString("ResultMsg")) ? "没有数据" : headerObject.getString("ResultMsg"))));
            }

            GResponse<R> gResponse = mGson.fromJson(responseObject.toString(), mResType);
            if (gResponse.getHeader().getStatus() != GResponse.SUCCESS) {
                return Response.error(new ParseError(new GWIVolleyError(gResponse.getHeader().getStatus(), new Exception(gResponse.getHeader().getResultMsg()))));
            }

            return Response.success(gResponse.getBody(), HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            LogUtil.e(TAG,  e);
            return Response.error(new GWIVolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(R response) {
        mListener.onResponse(response);
    }
}
