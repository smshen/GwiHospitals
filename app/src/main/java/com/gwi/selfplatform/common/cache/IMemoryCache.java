package com.gwi.selfplatform.common.cache;



public interface IMemoryCache {
     <T> void put(String key,T data);
     <T> T get(String key);
     void clearAll();
     void remove(String key);
}
