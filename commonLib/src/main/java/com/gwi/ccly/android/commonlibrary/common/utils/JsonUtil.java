package com.gwi.ccly.android.commonlibrary.common.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JsonUtil {
    public static String toJson(Object o) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();
        return gson.toJson(o);
    }
    
    public static Object fromJson(String val, Type lt) {
        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.enableComplexMapKeySerialization().create();

        return mGson.fromJson(val, lt);
    }

    public static <T> T fromJson(String val, Class<T> t) {
        GsonBuilder builder = new GsonBuilder();
        Gson mGson = builder.enableComplexMapKeySerialization().create();

        return mGson.fromJson(val, t);
    }
    
    public static<T> T toObject(JSONObject jobj,Class<T> t,String node) {
        T instance;
        try {
            instance = t.newInstance();

            for (Method f : t.getMethods()) {
                if (!f.getName().contains("set"))
                    continue;
                String name = f.getName().substring(3);
                if(!name.equalsIgnoreCase(node)) {
                    if (jobj.has(name)) {
                        f.invoke(instance, jobj.get(name)
                                .toString());
                    }
                }
                
            }

            return instance;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(JSONObject jobj, Class<T> t) {
        return toObject(jobj, t, null);
    }

//    public static <T> T toBodyObject(JSONObject jsonObject, Class<T> tClass) {
//        Iterator<String> keys = jsonObject.keys();
//        try {
//            T instance = tClass.newInstance();
//
//            while (keys.hasNext()) {
//
//            }
//        } catch (Exception e) {
//
//        }
//    }


    public static <T> List<T> toListObject(JSONObject jobj,String node,Class<T> t, Type tp,Gson gson) {
        T instance;
        List<T> instances = new ArrayList<T>();

        try {
            instance = t.newInstance();
            Object o = jobj.opt(node);
            if(o.equals(null)){
                return null;
            }

            if (o.getClass() == JSONObject.class) {
                JSONObject jsonObj = jobj.getJSONObject(node);
                for (Method f : t.getMethods()) {
                    if (!f.getName().contains("set"))
                        continue;
                    Object tmp = jsonObj.opt(f.getName().substring(3));
                    if (tmp != null && tmp.getClass() == String.class) {
                        if (jsonObj.has(f.getName().substring(3))) {
                            f.invoke(instance,
                                    jsonObj.get(f.getName().substring(3))
                                            .toString());
                        }
                    }
                }
                instances.add(instance);
            } else {
                JSONArray jsonArray = jobj.getJSONArray(node);
                GsonBuilder builder = new GsonBuilder();
                if(gson==null) {
//                    gson = builder.enableComplexMapKeySerialization()
//                            .create();
                    gson = builder
                            .registerTypeAdapter(Date.class, new BaseRequest.DotNetDateAdapter())
                            .enableComplexMapKeySerialization().create();
                }
                instances = gson.fromJson(jsonArray.toString(), tp);
            }

        } catch (Exception e) {
            LogUtil.e("toListObject", e.toString());
        }
        return instances;
    }

    public static <T> List<T> toListObject(JSONObject jobj, String node,
            Class<T> t, Type tp) {
        return toListObject(jobj,node,t,tp,null);
    }
}
