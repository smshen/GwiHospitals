package com.gwi.selfplatform.common.utils.validator;

public abstract class Validator {
    
    public static final int SUCCESS = 0;
    public static final int ERROR_EMPTY = 1;
    public static final int ERROR_COMMON = 2;
    
    public abstract int validate(String value, int type);
}
