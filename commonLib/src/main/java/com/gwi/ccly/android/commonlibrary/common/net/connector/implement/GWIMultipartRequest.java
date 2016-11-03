package com.gwi.ccly.android.commonlibrary.common.net.connector.implement;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by 彭毅 on 2015/3/5.
 */
public class GWIMultipartRequest<R> extends Request<R> {

    public static final String TAG = GWIMultipartRequest.class.getSimpleName();

    //TODO:
    public static final String KEY_PICTURE = "image";
    public static final String KEY_PICTURE_NAME = "title";

    private HttpEntity mHttpEntity;
    private Gson mGson;
    private Response.Listener mListener;

    private Class<R> mClass;

    public GWIMultipartRequest(String url, String filePath, Gson gson,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mGson = gson;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(filePath);
    }

    public GWIMultipartRequest(String url, File file, Gson gson,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mGson = gson;
        mListener = listener;
        mHttpEntity = buildMultipartEntity(file);
    }

    public GWIMultipartRequest(String url, Bitmap bitmap, String fileName,
                               Gson gson,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        if(gson==null) {
            mGson = new Gson();
        }else {
            mGson = gson;
        }
        mListener = listener;
        mHttpEntity = buildMultipartEntity(bitmap,fileName);
    }

    private HttpEntity buildMultipartEntity(String filePath) {
        File file = new File(filePath);
        return buildMultipartEntity(file);
    }

    private HttpEntity buildMultipartEntity(File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        String fileName = file.getName();
        FileBody fileBody = new FileBody(file);
        builder.addPart(KEY_PICTURE, fileBody);
        builder.addTextBody(KEY_PICTURE_NAME, fileName);
        return builder.build();
    }

    private HttpEntity buildMultipartEntity(Bitmap bitmap,String fileName) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //send params by addTextPart
        builder.addBinaryBody(KEY_PICTURE, bao.toByteArray(), ContentType.DEFAULT_BINARY, fileName);
//            builder.addPart(KEY_PICTURE_NAME,new StringBody(new String(bao.toByteArray()), ContentType.DEFAULT_BINARY));
        // builder.addBinaryBody(KEY_PICTURE, file, ContentType.create("image/jpeg"), fileName);
//        builder.addPart(KEY_PICTURE_NAME,new ByteArrayBody(bao.toByteArray(),fileName));
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mHttpEntity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }



    @Override
    protected Response<R> parseNetworkResponse(NetworkResponse response) {
        String json;
        try {
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            LogUtil.e(TAG, String.format("Encoding problem parsing API response. NetworkResponse:%s", response.toString()));
            return Response.error(new ParseError(e));
        }
        try {
            //TODO:未完成
//            return Response.success(mGson.fromJson(json, mClass), HttpHeaderParser.parseCacheHeaders(response));
            return Response.success((R)json,HttpHeaderParser.parseCacheHeaders(response));
        } catch (JsonSyntaxException e) {
            LogUtil.e(TAG, String.format("Couldn't API parse JSON response. NetworkResponse:%s", response.toString()));
            LogUtil.e(TAG, String.format("Couldn't API parse JSON response. Json dump: %s", json));
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(R response) {
        mListener.onResponse(response);
    }
}
