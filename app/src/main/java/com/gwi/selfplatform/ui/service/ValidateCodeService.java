package com.gwi.selfplatform.ui.service;

import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.config.Constants;

import java.util.Date;

/**
 * 验证码验证服务
 * @author 彭毅
 *
 */
public class ValidateCodeService {

    private Date mStartDate = null;
    private String mCode = null;
    /**
     * 与验证码匹配的手机号、、、、、、、、、、
     */
    private String mMobilePhone = null;

    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    public ValidateCodeService() {
        super();
    }
    
    /**
     * 开始验证,每次获取了验证码后调用该方法
     * @param code
     */
    public void start(String mobilePhone,String code) {
        if(mStartDate!=null) {
            mStartDate = null;
        }
        mStartDate = new Date();
        mCode = code;
        mMobilePhone = mobilePhone;
    }
    
    public boolean isExpired() {
        if(IS_DEBUG) return false;
        return new Date().getTime() - mStartDate.getTime() > Constants.VADILITY_MAX;
    }
    
    public boolean isValid(String mobilePhone,String code) {
        if(IS_DEBUG) return true;
        return  !(code==null||mCode==null||mobilePhone==null||this.mMobilePhone==null)
                && code.equals(mCode)&&mobilePhone.equals(mMobilePhone);
    }
    
}
