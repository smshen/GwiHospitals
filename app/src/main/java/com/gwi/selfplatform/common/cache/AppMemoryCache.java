package com.gwi.selfplatform.common.cache;

import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.Date;

/**
 * 应用程序APP内存缓存
 * 
 * @author 彭毅
 * 
 */
public class AppMemoryCache implements IMemoryDateCache {

    private static final int MAX_SIZE = 50;
    public static final String KEY_HEALTH_CATEGORY = "key_health_category";

    private LruCache<String, SoftReference<Object>> mMemoryCache;
    private LruCache<String, Date> mMemoryCacheTime;

    private Object mLock;

    public AppMemoryCache() {
        mMemoryCache = new LruCache<String, SoftReference<Object>>(MAX_SIZE);
        mMemoryCacheTime = new LruCache<String, Date>(MAX_SIZE);
        mLock = new Object();
    }

    /**
     * 
     */
    @Override
    public <T> void put(String key, T data, Date date) {
        synchronized (mLock) {
            mMemoryCache.put(key, new SoftReference<Object>(data));
            mMemoryCacheTime.put(key, date);
        }
    }

    @Override
    public <T>void put(String key, T data) {
        mMemoryCache.put(key, new SoftReference<Object>(data));
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key) {
        synchronized (mLock) {
            SoftReference<T> value = (SoftReference<T>) mMemoryCache.get(key);
            if (value != null) {
                return (T) value.get();
            }
        }
        return null;
    }

    @Override
    public void clearAll() {
        mMemoryCache.evictAll();
    }

    @Override
    public void remove(String key) {
        synchronized (mLock) {
            mMemoryCache.remove(key);
            mMemoryCacheTime.remove(key);
        }
    }

    public Date getCacheTime(String key) {
        return mMemoryCacheTime.get(key);
    }
}
