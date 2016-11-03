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

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.TRequest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 *网络请求构造：get,post,put,delete.
 */
public final class NetworkCreator {
    private static final String TAG = NetworkCreator.class.getSimpleName();
    private final String baseUrl;
    private final Context context;

    NetworkCreator(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    /**
     * @param headers Headers request, it can be null
     */
    public NetworkManager.INetworkManagerBuilder get(@NonNull NetHeader headers) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.GET).setHeaders(headers.getHeaders());
    }

    /**
     *
     */
    public NetworkManager.INetworkManagerBuilder get() {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.GET);
    }

    /**
     * @param headers    Headers request, it can be null
     * @param bodyObject Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder post(@NonNull NetHeader headers, @NonNull Object bodyObject) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyRequest(generateBodyRequest(bodyObject)).setHeaders(headers.getHeaders());
    }


    /**
     * @param headers     Headers request, it can be null
     * @param netBodyRequest Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder post(@NonNull NetHeader headers, @NonNull NetBody netBodyRequest) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyRequest(netBodyRequest.getBody()).setHeaders(headers.getHeaders());
    }

    /**
     *
     * @param headers
     * @param netBodyParams
     * @return
     */
    public NetworkManager.INetworkManagerBuilder post(@NonNull NetHeader headers,@NonNull Map<String,String> netBodyParams) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyData(netBodyParams).setHeaders(headers.getHeaders());
    }

    /**
     * @param bodyObject Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder post(@NonNull Object bodyObject) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyRequest(generateBodyRequest(bodyObject));
    }

    /**
     * post gwi request
     * @param headers headers can be null
     * @param data request data to post
     * @return
     */
    public NetworkManager.INetworkManagerBuilder postGWI(NetHeader headers,@NonNull TRequest<?> data) {
        if(headers==null) {
            return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyData(data);
        }else {
            return new NetworkManager.Builder()
                    .setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setHeaders(headers.getHeaders()).setBodyData(data);
        }
    }

    /**
     * post gwi request
     * @param headers headers can be null
     * @param netBodyRequest key-value boty to post
     * @return
     */
    public NetworkManager.INetworkManagerBuilder postGWI(NetHeader headers,@NonNull NetBody netBodyRequest) {
        if(headers==null) {
            return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyRequest(netBodyRequest.getBody());
        }else {
            return new NetworkManager.Builder()
                    .setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setHeaders(headers.getHeaders()).setBodyRequest(netBodyRequest.getBody());
        }
    }


    /**
     * @param netBodyRequest Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder post(@NonNull NetBody netBodyRequest) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.POST).setBodyRequest(netBodyRequest.getBody());
    }

    /**
     * @param headers    Headers request, it can be null
     * @param bodyObject Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder put(@NonNull NetHeader headers, @NonNull Object bodyObject) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.PUT).setBodyRequest(generateBodyRequest(generateBodyRequest(bodyObject))).setHeaders(headers.getHeaders());
    }

    /**
     * @param headers     Headers request, it can be null
     * @param netBodyRequest Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder put(@NonNull NetHeader headers, @NonNull NetBody netBodyRequest) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.PUT).setBodyRequest(generateBodyRequest(netBodyRequest.getBody())).setHeaders(headers.getHeaders());
    }

    /**
     * @param bodyObject Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder put(@NonNull Object bodyObject) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.PUT).setBodyRequest(generateBodyRequest(bodyObject));
    }

    /**
     * @param netBodyRequest Body request, it not null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder put(@NonNull NetBody netBodyRequest) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.PUT).setBodyRequest(netBodyRequest.getBody());
    }

    /**
     * @param headers Headers request, it can be null
     * @return
     */
    public NetworkManager.INetworkManagerBuilder delete(@NonNull NetHeader headers) {
        return new NetworkManager.Builder().setBaseUrl(baseUrl).setContext(context).setMethod(Request.Method.DELETE).setHeaders(headers.getHeaders());
    }

    private HashMap<String, Object> generateBodyRequest(Object bodyRequest) {
        String bodyJson = new Gson().toJson(bodyRequest);
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        HashMap<String, Object> body = new Gson().fromJson(bodyJson, type);
        return body;
    }

}
