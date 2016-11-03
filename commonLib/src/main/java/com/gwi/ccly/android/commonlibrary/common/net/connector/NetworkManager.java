/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.gwi.ccly.android.commonlibrary.common.net.connector;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.GWIRequest;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.ccly.android.commonlibrary.ui.view.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 网络请求管理：提供网络请求配置工厂类，处理不同的响应数据类型
 */
public final class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();

    private static final int MASK_SHOW_DIALOG = 0x1;
    private static final int MASK_DIALOG_CANCELLABLE = 0x2;

    /**
     * 客户端期望的数据类型
     */
    public enum RESULT {
        JSONOBJECT,
        JSONARRAY,
        STRING,
        GWI,
    }

    private final Context localContext;
    private final String baseUrl;
    private final NetworkHelper networkHelper;
    private final String pathUrl;
    private final int method;
    private final TypeToken<?> classTarget;
    private final RESULT resultType;
    private final HashMap<String, Object> bodyRequest;
    private final Object bodyData;
    private final HashMap<String, String> headers;
    private final View loadingView;
    private final int showLoadingDlgMask;
    private final String loadingMessage;
    private Dialog loadingDialog;

    public NetworkManager(Builder builder) {
        this.localContext = builder.localContext;
        this.baseUrl = builder.baseUrl;
        this.networkHelper = NetworkHelper.getInstance(builder.context);
        this.pathUrl = builder.pathUrl;
        this.method = builder.method;
        this.classTarget = builder.targetType;
        this.resultType = builder.resultType;
        this.bodyRequest = builder.bodyRequest;
        this.bodyData = builder.bodyData;
        this.headers = builder.headers;
        this.loadingView = builder.loadingView;
        this.loadingMessage = builder.loadingMessage;
        this.showLoadingDlgMask = builder.showLoadingDlgFlag;
        this.loadingDialog = builder.dialog;
    }

    private String getUrlConnection(String pathUrl) {
        StringBuilder builder = new StringBuilder();
        if(pathUrl==null) {
            if(resultType== RESULT.GWI) {
                //“/Call” by default.
                pathUrl = "/Call";
            }
        }
        builder.append(baseUrl).append(pathUrl);

        return builder.toString();
    }

    protected JSONObject createBodyRequest(HashMap<String, Object> bodyRequest) {
        return bodyRequest == null ? null : new JSONObject(bodyRequest);
    }

    protected String createStringBodyRequest(HashMap<String, Object> bodyRequest)  {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Set<String> keySet = bodyRequest.keySet();
        for (String key : keySet) {
            if (first)
                first = false;
            else
                result.append("&");

            try {
                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode((String) bodyRequest.get(key), "UTF-8"));
            } catch (Exception e) {
                LogUtil.e("NetworkManager",e);
            }
        }

        return result.toString();
    }

    protected void fromJsonObject(final HashMap<String, String> headers, HashMap<String, Object> bodyRequest, String requestTag, final RequestCallback requestCallback) {
        showLoading();
        JsonObjectRequest request = new JsonObjectRequest(method, getUrlConnection(pathUrl), createStringBodyRequest(bodyRequest), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                hideLoading();
                Object t = new Gson().fromJson(jsonObject.toString(), classTarget.getType());
                if (requestCallback != null)
                    requestCallback.onRequestSuccess(t);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                if (requestCallback != null) {
                    NetworkResponse response = error.networkResponse;
                    if (response != null)
                        requestCallback.onRequestError(new RequestError(response));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };

        networkHelper.addToRequestQueue(request, requestTag);
    }

    private void fromJsonArray(final Map<String, String> headers, String requestTag, final RequestCallback requestCallback) {
        showLoading();
        JsonArrayRequest request = new JsonArrayRequest(method,getUrlConnection(pathUrl),
                createStringBodyRequest(bodyRequest),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                showLoading();
                Object t = new Gson().fromJson(jsonArray.toString(), classTarget.getType());
                if (requestCallback != null)
                    requestCallback.onRequestSuccess(t);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                if (requestCallback != null) {
                    NetworkResponse response = error.networkResponse;
                    if (response != null)
                        requestCallback.onRequestError(new RequestError(response));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        networkHelper.addToRequestQueue(request, requestTag);
    }

    /**
     * 处理非GWI标准接口数据，暴露原始json字符串
     * @param headers
     * @param requestTag
     * @param requestCallback
     */
    private void fromString(final Map<String, String> headers, String requestTag, final RequestCallback requestCallback) {
        showLoading();
        StringRequest request = new StringRequest(getUrlConnection(pathUrl), new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                hideLoading();
                requestCallback.onRequestSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                if (requestCallback != null) {
                    NetworkResponse response = error.networkResponse;
                    if (response != null)
                        requestCallback.onRequestError(new RequestError(response));
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //从外部传入key-value body.
                return bodyRequest!=null?  transferGoodBodyRequest(bodyRequest):super.getParams();
            }
        };

        networkHelper.addToRequestQueue(request, requestTag);
    }

    //Map<String,Object>==>Map<String,String>
    protected Map<String,String> transferGoodBodyRequest(Map<String, Object> bodyRequest) {
        Map<String, String> result = new HashMap<>(bodyRequest.size());
        Set<String> keySet = bodyRequest.keySet();
        Object tempValue;
        for (String key : keySet) {
            tempValue = bodyRequest.get(key);
            if (tempValue instanceof String) {
                result.put(key, (String) tempValue);
            }
        }
        return result;
    }

    /**
     * 标准的GWI 实体请求与json解析处理过程，返回客户端期望的实体结果与列表（Items>Item）
     * @param headers
     * @param requestTag
     * @param requestCallback
     * @param <R>
     */
    private<R> void fromGWIObject(final Map<String, String> headers,String requestTag, final RequestCallback requestCallback) {
        showLoading();
        GWIRequest<R> request = new GWIRequest<>(method, headers,getUrlConnection(pathUrl), bodyRequest, classTarget.getType(),
                new Response.Listener<R>() {
                    @Override
                    public void onResponse(R response) {
                        hideLoading();
                        requestCallback.onRequestSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideLoading();
                requestCallback.onRequestError(new RequestError(error));
            }
        });
        networkHelper.addToRequestQueue(request, requestTag);
    }

    protected void showLoading() {
        if((showLoadingDlgMask&MASK_SHOW_DIALOG)!=0&&localContext!=null) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(localContext, loadingMessage);
            }
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setCancelable((showLoadingDlgMask & MASK_DIALOG_CANCELLABLE) != 0);
            loadingDialog.show();
        }
        if(loadingView!=null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }
    protected void hideLoading() {
        if(loadingDialog !=null) {
            loadingDialog.dismiss();
        }
        if(loadingView!=null) {
            loadingView.setVisibility(View.GONE);
        }
    }


    /**
     * 依据{@link com.gwi.ccly.android.commonlibrary.common.net.connector.NetworkManager.RESULT}
     * 执行网络请求，并回调相应结果
     * @param requestTag
     * @param callback
     * @param <R>
     */
    public <R> void execute(String requestTag, RequestCallback<R> callback) {
        if (resultType == null) {
            throw new IllegalArgumentException("result type must not be null.");
        }

        if (classTarget == null) {
            throw new IllegalArgumentException("class target must not be null.");
        }

        if (pathUrl == null) {
            if(resultType!= RESULT.GWI) {
                throw new IllegalArgumentException("path url must not be null.");
            }
        }

        switch (resultType) {
            case JSONARRAY:
                fromJsonArray(headers, requestTag, callback);
                break;
            case JSONOBJECT:
                if (method == Request.Method.POST)
                    if (bodyRequest == null)
                        throw new IllegalArgumentException("body request must not be null.");

                fromJsonObject(headers, bodyRequest, requestTag, callback);
                break;
            case STRING:
                fromString(headers, requestTag, callback);
                break;
            case GWI:
                fromGWIObject(headers,requestTag,callback);
                break;
            default:
                throw new IllegalArgumentException("response type not found");
        }
    }

    /**
     * 请求配置构造工厂
     */
    public static class Builder implements INetworkManagerBuilder {
        private Dialog dialog;
        private String baseUrl;
        private Context context;
        private Context localContext;
        private String pathUrl;
        private int method;
        private RESULT resultType;
        private TypeToken<?> targetType;
        private HashMap<String, Object> bodyRequest;
        private Object bodyData;
        private HashMap<String, String> headers;
        private View loadingView;
        //0x01:show;0x2:cancellable
        private int showLoadingDlgFlag;
        private String loadingMessage;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder setBodyRequest(@NonNull HashMap<String, Object> bodyRequest) {
            this.bodyRequest = bodyRequest;
            return this;
        }

        public Builder setBodyData(@NonNull Object bodyData) {
            this.bodyData = bodyData;
            return this;
        }

        public Builder setLoadingView(View loadingView) {
            this.loadingView = loadingView;
            return this;
        }

        public Builder showLoadingDlg(Context localContext,boolean cancellable) {
            this.localContext = localContext;
            showLoadingDlgFlag |=MASK_SHOW_DIALOG;
            showLoadingDlgFlag |=cancellable?MASK_DIALOG_CANCELLABLE: showLoadingDlgFlag;
            return this;
        }

        public Builder setCustomLoadingDlg(Dialog dialog) {
            this.dialog = dialog;
            return this;
        }

        public Builder setLoadingMessage(String message) {
            this.loadingMessage = message;
            return this;
        }

        public Builder setHeaders(@NonNull HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        @Override
        public INetworkManagerBuilder pathUrl(@NonNull String pathUrl) {
            this.pathUrl = pathUrl;
            return this;
        }

        @Override
        public INetworkManagerBuilder fromJsonObject() {
            this.resultType = RESULT.JSONOBJECT;
            return this;
        }

        @Override
        public INetworkManagerBuilder fromJsonArray() {
            this.resultType = RESULT.JSONARRAY;
            return this;
        }

        @Override
        public INetworkManagerBuilder fromString() {
            this.resultType = RESULT.STRING;
            this.targetType = TypeToken.get(String.class);
            return this;
        }

        public INetworkManagerBuilder fromGWI() {
            this.resultType = RESULT.GWI;
            return this;
        }

        @Override
        public NetworkManager mappingInto(@NonNull Class classTarget) {
            this.targetType = TypeToken.get(classTarget);
            return new NetworkManager(this);
        }

        @Override
        public NetworkManager mappingInto(@NonNull TypeToken typeToken) {
            this.targetType = typeToken;
            return new NetworkManager(this);
        }
    }

    /**
     * 请求配置构造接口，主要包括相对路径的设置，数据来源及类型的设置，响应类型（加载框，或是加载view）及信息，以及返回的数据类型映射
     */
    public interface INetworkManagerBuilder {
        /**
         * @param pathUrl
         * @return
         */
        INetworkManagerBuilder pathUrl(@NonNull String pathUrl);

        INetworkManagerBuilder fromJsonObject();

        INetworkManagerBuilder fromJsonArray();

        INetworkManagerBuilder fromString();

        INetworkManagerBuilder fromGWI();

        INetworkManagerBuilder setLoadingView(View loadingView);

        INetworkManagerBuilder showLoadingDlg(Context localContext, boolean cancellable);

        INetworkManagerBuilder setCustomLoadingDlg(Dialog dialog);

        INetworkManagerBuilder setLoadingMessage(String message);

        NetworkManager mappingInto(@NonNull Class classTarget);

        NetworkManager mappingInto(@NonNull TypeToken typeToken);
    }
}
