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

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.VolleyError;

import java.util.Map;

/**
 * 请求错误信息实体类
 */
public final class RequestError {
    public final static int REQUEST_RESPONSE_OK = 200;
    public final static int REQUEST_RESPONSE_CREATED = 201;
    public final static int REQUEST_RESPONSE_ACCEPTED = 202;
    public final static int REQUEST_RESPONSE_NO_CONTENT = 204;
    public final static int REQUEST_RESPONSE_BAD_REQUEST = 400;
    public final static int REQUEST_RESPONSE_UNAUTHORIZED = 401;
    public final static int REQUEST_RESPONSE_FORBIDDEN = 403;
    public final static int REQUEST_RESPONSE_PAYMENT_REQUIRED = 402;
    public final static int REQUEST_RESPONSE_NOT_FOUND = 404;
    public final static int REQUEST_RESPONSE_GONE = 410;
    public final static int REQUEST_RESPONSE_UNPROCESSABLE_ENTITY = 422;
    public final static int REQUEST_RESPONSE_INTERNAL_SERVER_ERROR = 500;
    public final static int REQUEST_RESPONSE_SERVICE_UNAVAILABLE = 503;
    public final static int REQUEST_RESPONSE_MULTIPLE_DEVICE = 429;
    public final static int REQUEST_RESPONSE_NOT_PERMITTED = 301;
    public final static int REQUEST_RESPONSE_RESET_PASSWORD_SUCCESS = 204;

    public final static int ERR_TYPE_NETWORK = 0x1001;
    public final static int ERR_TYPE_DATA = 0x1002;

    final int errorType;

    final int errorCode;
    final Map<String,String> headers;
    byte[] data;

    final Object exception;

    public RequestError(NetworkResponse response) {
        errorType = ERR_TYPE_NETWORK;
        this.errorCode = response.statusCode;
        this.headers = response.headers;
        exception = new ParseError(response);
    }

    public RequestError(Exception e) {
        errorType = ERR_TYPE_DATA;
        errorCode = -1;
        headers = null;
        exception = e;
    }

    public RequestError(VolleyError e) {
        errorType = ERR_TYPE_DATA;
        errorCode = -1;
        headers = null;
        exception = e;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getErrorType() {
        return errorType;
    }

    public Object getException() {
        return exception;
    }
}
