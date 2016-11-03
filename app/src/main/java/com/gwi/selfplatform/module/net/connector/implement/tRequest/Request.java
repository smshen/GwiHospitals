package com.gwi.selfplatform.module.net.connector.implement.tRequest;

import android.os.Build;

import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.TRequest;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.T_UserInfo;

import java.util.Date;

/**
 * API请求实体
 * @author 彭毅
 * @date 2015/4/23.
 */
public class Request<T> extends TRequest<T>{

    public static final int FUN_CODE_HOSPITAL_PARAMS = 1014;
    public static final int FUN_CODE_HOSPITAL_INFO = 1035;
    public static final int FUN_CODE_HOSPITAL_DETAIL = 1036;
    public static final int FUN_CODE_USER_LOGIN = 5110;
    public static final int FUN_CODE_USER_REGISTER = 5111;
    public static final int FUN_CODE_PASSWORD_MODIFY = 5112;

    public static final int FUN_CODE_FAMILY_INFO_QUERY = 5113;
    public static final int FUN_CODE_USER_INFO_MODIFY = 5114;
    public static final int FUN_CODE_PASSWORD_FIND_VALIDATION = 5115;
    public static final int FUN_CODE_PASSWORD_SET_NEW = 5116;
    public static final int FUN_CODE_IS_MOBILE_REGISTERED = 5117;
    public static final int FUN_CODE_FAMILY_INFO_ADD = 5118;
    public static final int FUN_CODE_FAMILY_INFO_DELETE = 5119;
    public static final int FUN_CODE_FAMILY_INFO_MODIFY = 5120;
    public static final int FUN_CODE_BAIDU_PUSH_USER_ADD = 5121;

    public static final int FUN_CODE_CARD_INFO_QUERY = 5310;
    public static final int FUN_CODE_CARD_INFO_ADD = 5311;
    public static final int FUN_CODE_CARD_INFO_UNBIND = 5312;
    public static final int FUN_CODE_CARD_LOGIN = 5313;

    public static final int FUN_CODE_FEED_BACK = 5510;

    public static final int FUN_CODE_HEALTH_EDU_LIST = 5410;
    public static final int FUN_CODE_VALIDATION_CODE_GET = 5411;
    public static final int FUN_CODE_HEALTH_EDU_CATEGORY = 5414;

    public static final int FUN_CODE_CHECK_APP_VERSION = 5412;

    public static final int FUNC_GET_DISEASE_COMMON = 5610;
    public static final int FUN_CODE_DISEASE_LIST = 5611;
    public static final int FUN_CODE_DISEASE_DETAILS = 5612;
    public static final int FUN_CODE_DISEASE_DEPT_LIST = 5613;
    public static final int FUN_CODE_BODY_PARTS_GET = 5614;
    public static final int FUN_CODE_DRUG_COMMON= 5615;
    public static final int FUN_CODE_DRUG_RESCUE= 5616;
    public static final int FUN_CODE_DRUG_CLASSIFY = 5617;
    public static final int FUN_CODE_DRUG_DETAIL= 5618;
    public static final int FUN_CODE_COMMON_DRUG_DICTS = 5619;
    public static final int FUN_CODE_DRUG_PROPERTY = 5620;
    public static final int FUN_CODE_TEST_KINDS = 5621;
    public static final int FUN_CODE_TEST_LIST = 5622;
    public static final int FUNC_GET_TEST_CHECK_DETAILS = 5623;
    public static final int FUN_CODE_TREATMENT_KINDS = 5624;
    public static final int FUNC_CODE_GET_TREATMENT = 5625;
    public static final int FUN_CODE_TEST_DETAIL= 5626;

    public static final int FUN_CODE_BODY_PARTS_BY_COLOR = 5701;
    public static final int FUN_CODE_BODY_TO_SYMPTOM = 5702;
    public static final int FUN_CODE_SYMPTOM_TO_DISEASE = 5703;

    /**
     * 通用用户验证
     * @param request 请求实体
     * @param userInfo 验证所需要的用户信息实例
     * @param <T> 请求实体类型
     */
    public static <T extends TBase> void commonUserValidation(Request<T> request,T_UserInfo userInfo) {
        //usercode ==> userId
        request.getBody().setAccount(userInfo.getUserCode());
        request.getBody().setAccountPassword(userInfo.getUserPwd());
    }

    /**
     * 调用接口通用头
     * @param request 请求实体
     * @param funCode 接口代码
     * @param isToGWI 是否来自健康平台的接口（此外还有一个便捷就医平台）
     * @param <T> 请求实体类型
     */
    public static <T> void commonHeader(Request<T> request,int funCode,boolean isToGWI) {
        request.setHeader(new THeader());
        request.getHeader().setFunCode(funCode);
        request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());
        request.getHeader().setAppTypeCode(Constants.APP_TYPE_CODE);
        request.getHeader().setReqTime(CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date()));
        if(isToGWI) {
            request.getHeader().setTerminalNo(Build.MODEL);
        }
// else {
//            request.getHeader().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
//        }
    }

}
