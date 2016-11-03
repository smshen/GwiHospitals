package com.gwi.ccly.android.commonlibrary.common.net.connector.implement;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * API 请求基类
 * @author 彭毅
 * @date 2015/4/29.
 */
public abstract class BaseRequest<T> extends Request<T> {
    public BaseRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
    }

    /**
     * {@link Date}类型适配器，序列化和反序列化:/Date(1234567890123+0800)/
     *
     * @author 彭毅
     *
     */
    public static class DotNetDateAdapter implements JsonSerializer<Date>,
            JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement arg0, Type arg1,
                                JsonDeserializationContext arg2) throws JsonParseException {
            return new Date(Long.valueOf(fromDotNetDateTimeString(arg0.getAsString())));
//            try {
//                //修改于 2014/05/21
//                return CommonUtil.stringPhaseDate(arg0.getAsString(), Constants.FORMAT_ISO_DATE_TIME);
//            } catch (ParseException e) {
//                Logger.e("DotNetDateAdapter","deserialize",e);
//                return null;
//            }
        }

        @Override
        public JsonElement serialize(Date arg0, Type arg1,
                                     JsonSerializationContext arg2) {
            return new JsonPrimitive(toDotNetDateTimeString(arg0
                    .getTime()));
//            //修改于 2014/05/21
//            return new JsonPrimitive(CommonUtil.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME,arg0));
        }
    }

    static String toDotNetDateTimeString(long milliseconds) {
        String prefix = "/Date(";
        String suffix = ")/";
        String timeZone = "+0800";
        return prefix + milliseconds + timeZone + suffix;
    }

    static String fromDotNetDateTimeString(String dotNetDateTime) {
        int start = 6;
        int end = dotNetDateTime.length() - 7;
        return dotNetDateTime.substring(start, end);
    }
}
