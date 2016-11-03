package com.gwi.selfplatform.common.cache;

import java.util.Date;

public interface IMemoryDateCache extends IMemoryCache{
    <T>void put(String key, T data,Date date);
}
