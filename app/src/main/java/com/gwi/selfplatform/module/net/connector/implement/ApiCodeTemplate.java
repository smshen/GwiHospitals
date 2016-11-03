package com.gwi.selfplatform.module.net.connector.implement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.NetBody;
import com.gwi.ccly.android.commonlibrary.common.net.connector.NetworkManager;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.BaseRequest;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.ccly.android.commonlibrary.common.utils.LogUtil;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.exception.BaseException;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.BankUtil;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.JsonUtil;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_FeedBack_Rec;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.BodyToSymptom;
import com.gwi.selfplatform.module.net.beans.KBBodyPart;
import com.gwi.selfplatform.module.net.beans.KBDepart;
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.module.net.beans.KBDrugDetails;
import com.gwi.selfplatform.module.net.beans.KBDrugProperty;
import com.gwi.selfplatform.module.net.beans.KBDrugUseKind;
import com.gwi.selfplatform.module.net.beans.KBTestCheckDetails;
import com.gwi.selfplatform.module.net.beans.KBTestCheckKind;
import com.gwi.selfplatform.module.net.beans.KBTreatmentDetails;
import com.gwi.selfplatform.module.net.beans.KBTreatmentKind;
import com.gwi.selfplatform.module.net.beans.MobileVerParam;
import com.gwi.selfplatform.module.net.beans.OrderParamater;
import com.gwi.selfplatform.module.net.beans.T1121;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5110;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GAuth;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GBase;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.request.OrderInfo;
import com.gwi.selfplatform.module.net.request.OrderQuery;
import com.gwi.selfplatform.module.net.request.T1011;
import com.gwi.selfplatform.module.net.request.T1013;
import com.gwi.selfplatform.module.net.request.T1014;
import com.gwi.selfplatform.module.net.request.T1017;
import com.gwi.selfplatform.module.net.request.T1033;
import com.gwi.selfplatform.module.net.request.T1034;
import com.gwi.selfplatform.module.net.request.T1035;
import com.gwi.selfplatform.module.net.request.T1036;
import com.gwi.selfplatform.module.net.request.T1210;
import com.gwi.selfplatform.module.net.request.T1211;
import com.gwi.selfplatform.module.net.request.T1212;
import com.gwi.selfplatform.module.net.request.T1213;
import com.gwi.selfplatform.module.net.request.T1214;
import com.gwi.selfplatform.module.net.request.T1216;
import com.gwi.selfplatform.module.net.request.T1217;
import com.gwi.selfplatform.module.net.request.T1218;
import com.gwi.selfplatform.module.net.request.T1219;
import com.gwi.selfplatform.module.net.request.T1310;
import com.gwi.selfplatform.module.net.request.T1411;
import com.gwi.selfplatform.module.net.request.T1412;
import com.gwi.selfplatform.module.net.request.T1413;
import com.gwi.selfplatform.module.net.request.T1414;
import com.gwi.selfplatform.module.net.request.T1510;
import com.gwi.selfplatform.module.net.request.T1512;
import com.gwi.selfplatform.module.net.request.T1610;
import com.gwi.selfplatform.module.net.request.T1611;
import com.gwi.selfplatform.module.net.request.T1612;
import com.gwi.selfplatform.module.net.request.T1615;
import com.gwi.selfplatform.module.net.request.T1710;
import com.gwi.selfplatform.module.net.request.T1911;
import com.gwi.selfplatform.module.net.request.T2011;
import com.gwi.selfplatform.module.net.request.T2110;
import com.gwi.selfplatform.module.net.request.T2210;
import com.gwi.selfplatform.module.net.request.T2510;
import com.gwi.selfplatform.module.net.request.T2511;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1013;
import com.gwi.selfplatform.module.net.response.G1014;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.module.net.response.G1033;
import com.gwi.selfplatform.module.net.response.G1034;
import com.gwi.selfplatform.module.net.response.G1035;
import com.gwi.selfplatform.module.net.response.G1036;
import com.gwi.selfplatform.module.net.response.G1210;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1213;
import com.gwi.selfplatform.module.net.response.G1214;
import com.gwi.selfplatform.module.net.response.G1216;
import com.gwi.selfplatform.module.net.response.G1217;
import com.gwi.selfplatform.module.net.response.G1218;
import com.gwi.selfplatform.module.net.response.G1219;
import com.gwi.selfplatform.module.net.response.G1310;
import com.gwi.selfplatform.module.net.response.G1412;
import com.gwi.selfplatform.module.net.response.G1414;
import com.gwi.selfplatform.module.net.response.G1417;
import com.gwi.selfplatform.module.net.response.G1510;
import com.gwi.selfplatform.module.net.response.G1512;
import com.gwi.selfplatform.module.net.response.G1610;
import com.gwi.selfplatform.module.net.response.G1611;
import com.gwi.selfplatform.module.net.response.G1612;
import com.gwi.selfplatform.module.net.response.G1613;
import com.gwi.selfplatform.module.net.response.G1615;
import com.gwi.selfplatform.module.net.response.G1910;
import com.gwi.selfplatform.module.net.response.G1911;
import com.gwi.selfplatform.module.net.response.G2011;
import com.gwi.selfplatform.module.net.response.G2110;
import com.gwi.selfplatform.module.net.response.G2210;
import com.gwi.selfplatform.module.net.response.G2510;
import com.gwi.selfplatform.module.net.response.G2511;
import com.gwi.selfplatform.module.net.response.OrderQueryResults;
import com.gwi.selfplatform.module.net.response.OrderResult;
import com.gwi.selfplatform.module.net.response.RecipeDetail;
import com.gwi.selfplatform.module.net.response.RecipeInfo;
import com.gwi.selfplatform.module.net.response.RecipeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口调用模板
 *
 * @author 彭毅
 * @date 2015/6/2.
 */
public class ApiCodeTemplate {

    public static final String NODE_ITEM = "Item";

    public static void authenticateAsync(Context context, String taskTag, View loadingView, RequestCallback<GAuth> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, 0, true);


        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI().pathUrl("/Authenticate");
        if (loadingView == null && context != null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, false);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<GResponse<GAuth>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 登录【5110】
     *
     * @param context  所属页面Context
     * @param taskTag  任务TAG
     * @param account  账号
     * @param password 密码（明文）
     * @param callback 接口回调
     */
    public static void loginAsync(Context context, String taskTag,
                                  String account, String password,
                                  RequestCallback<G5110> callback) {
        loginAsync(context, null, taskTag, account, password, callback);
    }

    /**
     * 修改密码【5112】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG，用于取消操作
     * @param userInfo 用户信息,用于验证并设置新密码
     * @param callback 接口回调
     */
    public static void modifyPasswordAsync(Context context, String taskTag, T_UserInfo userInfo, RequestCallback<GBase> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_PASSWORD_MODIFY, true);

        request.setBody(new TBase());
        Request.commonUserValidation(request, GlobalSettings.INSTANCE.getCurrentUser());
        request.getBody().setUserInfo(userInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_hint_modifying))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<GResponse<GBase>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 设置新密码,用于忘记密码【5116】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG，用于取消操作
     * @param userInfo 用户信息,用于验证并设置新密码
     * @param callback 接口回调
     */
    public static void setNewPasswordAsync(Context context, String taskTag, T_UserInfo userInfo, RequestCallback<GBase> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_PASSWORD_SET_NEW, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(userInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<GResponse<GBase>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 登录【5110】
     *
     * @param context     所属页面Context
     * @param loadingView 加载控件
     * @param taskTag     任务TAG
     * @param account     账号
     * @param password    密码（明文）
     * @param callback    接口回调
     */
    public static void loginAsync(final Context context, final View loadingView, final String taskTag,
                                  final String account, final String password,
                                  final RequestCallback<G5110> callback) {
        //验证token
        if (GlobalSettings.INSTANCE.getToken() == null) {
            ApiCodeTemplate.authenticateAsync(context, taskTag, loadingView, new RequestCallback<GAuth>() {
                @Override
                public void onRequestSuccess(GAuth result) {
                    GlobalSettings.INSTANCE.setToken(result.getToken());
                    login(context, loadingView, taskTag, account, password, callback);
                }

                @Override
                public void onRequestError(RequestError error) {
                    callback.onRequestError(error);
                }
            });
        } else {
            login(context, loadingView, taskTag, account, password, callback);
        }
    }

    private static void login(Context context, View loadingView, String taskTag,
                              String account, String password,
                              RequestCallback<G5110> callback) {
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        request.getHeader().setFunCode(Request.FUN_CODE_USER_LOGIN);
        request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());

        request.setBody(new TBase());
        request.getBody().setAccount(account);
        request.getBody().setAccountPassword(MD5Util.string2MD5(password));
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getAppCode());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage("登录中...")
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<GResponse<G5110>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 上传百度推送用户
     *
     * @param taskTag       任务TAG,用于取消操作
     * @param baiduPushUser 百度推送用户（即当前登录用户）
     * @param callback      接口回调
     */
    public static void uploadBaiduPushUser(String taskTag, T1121 baiduPushUser, RequestCallback<Void> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_BAIDU_PUSH_USER_ADD, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(new T_UserInfo());
        request.getBody().setBaiduUserInfo(baiduPushUser);
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, userInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request))
                .fromGWI()
                .mappingInto(new TypeToken<GResponse<Void>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 加载家庭成员【5113】
     *
     * @param context     所属页面Context
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param callback    接口回调
     */
    public static void loadFamilyMembersAsync(Context context, String taskTag, View loadingView, RequestCallback<List<T_Phr_BaseInfo>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FAMILY_INFO_QUERY, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(new T_UserInfo());
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, userInfo);
        request.getBody().getUserInfo().setUserId(userInfo.getUserId());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<List<T_Phr_BaseInfo>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 获取家庭成员【5113】
     *
     * @return
     * @throws Exception
     */
    public static List<T_Phr_BaseInfo> getFamilyMembers() throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FAMILY_INFO_QUERY, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(new T_UserInfo());
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, userInfo);
        request.getBody().getUserInfo().setUserId(userInfo.getUserId());

        JSONObject bodyData = WebUtil.httpExecute(request, true);

        return JsonUtil.toListObject(bodyData, NODE_ITEM, T_Phr_BaseInfo.class, new TypeToken<List<T_Phr_BaseInfo>>() {
        }.getType());
    }

    /**
     * 获取验证码【5411】
     *
     * @param context     所属页面的context
     * @param taskTag     任务TAG，用于取消操作
     * @param phoneNumber 要验证的手机号码
     * @param callback    接口回调
     */
    public static void getValidationCodeAsync(Context context, String taskTag, String phoneNumber, RequestCallback<T_Phone_AuthCode> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_VALIDATION_CODE_GET, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        T_Phone_AuthCode authCode = new T_Phone_AuthCode();
        authCode.setPhoneNumber(phoneNumber);
        request.getBody().setPhone_AuthCode(authCode);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_mobile_send))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<GResponse<T_Phone_AuthCode>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 检测版本更新【5412】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG，用于取消操作
     * @param callback 接口回调
     */
    public static void checkAppNewVersionAsync(Context context, String taskTag, RequestCallback<MobileVerParam> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_CHECK_APP_VERSION, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setAppCode(GlobalSettings.INSTANCE.getAppCode());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (context != null) {
            builder.setLoadingMessage("正在检测版本更新...").showLoadingDlg(context, true);
        }
        builder.mappingInto(new TypeToken<GResponse<MobileVerParam>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 手机号码是否已经注册【5117】
     *
     * @param context     所属页面的context
     * @param taskTag     任务TAG，用于取消操作
     * @param mobilePhone 要验证的手机号码
     * @param callback    接口回调
     */
    public static void isMobileRegisteredAsync(Context context, String taskTag, String mobilePhone, RequestCallback<G5117> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_IS_MOBILE_REGISTERED, true);
        request.setBody(new TBase());
        T_UserInfo userInfo = new T_UserInfo();
        userInfo.setMobilePhone(mobilePhone);
        request.getBody().setUserInfo(userInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_loading_validate))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<GResponse<G5117>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 添加家庭成员【5118】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG，用于取消操作
     * @param member   新增的家庭成员
     * @param callback 接口回调
     */
    public static void addNewMemberNewAsync(Context context, String taskTag, T_Phr_BaseInfo member, RequestCallback<List<T_Phr_BaseInfo>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FAMILY_INFO_ADD, true);

        request.setBody(new TBase());
        Request.commonUserValidation(request, GlobalSettings.INSTANCE.getCurrentUser());
        request.getBody().setPhr_BaseInfo(member);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_loading_operation))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<List<T_Phr_BaseInfo>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 用户注册
     *
     * @param context
     * @param TAG
     * @param userInfo
     * @param baseInfo
     * @param callback
     */
    public static void registerAsync(Context context, String TAG, T_UserInfo userInfo, T_Phr_BaseInfo baseInfo, RequestCallback<GResponse<T_Phr_BaseInfo>> callback) {
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        Request.commonHeader(request, Request.FUN_CODE_USER_REGISTER, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(userInfo);
        request.getBody().setPhr_BaseInfo(baseInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_loading_register))
                .mappingInto(new TypeToken<GResponse<T_Phr_BaseInfo>>() {
                })
                .execute(TAG, callback);

    }

    public static void cardLoginAsync(Context context, String taskTag, View loadingView, ExT_Phr_CardBindRec cardBindRec, RequestCallback<GBase> callback) {
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        Request.commonHeader(request, Request.FUN_CODE_CARD_LOGIN, true);
        request.setBody(new TBase());
        request.getBody().setPhr_CardBindRec(cardBindRec);

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage("登录中...")
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<GResponse<G5110>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 修改手机号码
     *
     * @param context
     * @param taskTag
     * @param mobilePhone
     * @param callback
     * @see ApiCodeTemplate#modifyUserInfoAsync(Context, String, T_UserInfo, T_UserInfo, T_Phr_BaseInfo, RequestCallback)
     */
    public static void modifyPhoneAsync(Context context, String taskTag, String mobilePhone, RequestCallback<T_UserInfo> callback) {
        T_UserInfo curUser = GlobalSettings.INSTANCE.getCurrentUser();
        T_UserInfo user = new T_UserInfo();
        user.setUserName(curUser.getUserName());
        user.setUserId(curUser.getUserId());
        user.setUserCode(curUser.getUserCode());
        user.setEhrId(curUser.getEhrId());
        user.setEMail(curUser.getEMail());
        user.setNickName(curUser.getNickName());
        user.setMobilePhone(mobilePhone);

        modifyUserInfoAsync(context, taskTag, curUser, user, null, callback);
    }

    /**
     * 获取家庭成员列表【5113】
     *
     * @param context
     * @param taskTag
     * @param callback
     */
    public static void getFamilyMembersAsync(Context context, String taskTag, RequestCallback<List<T_Phr_BaseInfo>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FAMILY_INFO_QUERY, true);

        request.setBody(new TBase());
        request.getBody().setUserInfo(new T_UserInfo());
        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, userInfo);
        request.getBody().getUserInfo().setUserId(userInfo.getUserId());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<T_Phr_BaseInfo>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 删除家庭成员【5119】
     *
     * @param context
     * @param taskTag
     * @param member
     * @param callback
     */
    public static void deleteFamilyInfoAsync(Context context, String taskTag, T_Phr_BaseInfo member, RequestCallback<List<T_Phr_BaseInfo>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FAMILY_INFO_DELETE, true);

        request.setBody(new TBase());
        T_UserInfo mainUserInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, mainUserInfo);

        request.getBody().setUserInfo(new T_UserInfo());
        request.getBody().getUserInfo().setUserId(mainUserInfo.getUserId());
        request.getBody().setPhr_BaseInfo(member);


        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_loading_operation))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<List<T_Phr_BaseInfo>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 修改家庭成员【5120】
     *
     * @param context
     * @param taskTag
     * @param member
     * @param callback
     */
    public static void modifyFamilyInfoAsync(Context context, String taskTag, T_Phr_BaseInfo member, RequestCallback<List<T_Phr_BaseInfo>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FAMILY_INFO_MODIFY, true);

        request.setBody(new TBase());
        T_UserInfo mainUserInfo = GlobalSettings.INSTANCE.getCurrentUser();
        Request.commonUserValidation(request, mainUserInfo);

        request.getBody().setUserInfo(new T_UserInfo());
        request.getBody().getUserInfo().setUserId(mainUserInfo.getUserId());
        request.getBody().setPhr_BaseInfo(member);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_hint_modifying))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<List<T_Phr_BaseInfo>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 修改用户信息【5114】
     *
     * @param context       所属页面Context
     * @param taskTag       任务TAG
     * @param curUserInfo   当前的用户信息，用于头验证
     * @param newUserInfo   新的用户信息
     * @param newFamilyInfo 新的家庭成员信息（可选，如果有改动的话）
     * @param callback      接口回调
     */
    public static void modifyUserInfoAsync(Context context, String taskTag,
                                           T_UserInfo curUserInfo, T_UserInfo newUserInfo, T_Phr_BaseInfo newFamilyInfo,
                                           RequestCallback<T_UserInfo> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_USER_INFO_MODIFY, true);

        request.setBody(new TBase());
        Request.commonUserValidation(request, curUserInfo);

        request.getBody().setUserInfo(newUserInfo);
        request.getBody().setPhr_BaseInfo(newFamilyInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<GResponse<T_UserInfo>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 用户反馈【5510】
     *
     * @param context     所属页面Context
     * @param taskTag     任务TAG
     * @param feedBackRec 反馈信息实体
     * @param callback    接口回调
     */
    public static void commitFeedBackNewAsync(Context context, String taskTag,
                                              T_FeedBack_Rec feedBackRec,
                                              RequestCallback<Void> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_FEED_BACK, true);
        request.setBody(new TBase());
        request.getBody().setFeedBack_Rec(feedBackRec);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<GResponse<T_UserInfo>>() {
                })
                .execute(taskTag, callback);
    }


    /**
     * 加载{@code member }绑定的卡信息【5310】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG，用于取消操作
     * @param member   指定家庭成员信息
     * @param callback 接口回调
     */
    public static void loadBindedCardAsync(Context context, String taskTag, final T_Phr_BaseInfo member, RequestCallback<List<ExT_Phr_CardBindRec>> callback) {
        if (null != member) {
            loadBindedCardAsync(context, null, taskTag, member, callback);
        }
    }

    /**
     * @param context
     * @param loadingView 页面加载提示控件
     * @param taskTag
     * @param member
     * @param callback
     * @see ApiCodeTemplate#loadBindedCardAsync(Context, String, T_Phr_BaseInfo, RequestCallback)
     */
    public static void loadBindedCardAsync(Context context, View loadingView, String taskTag, final T_Phr_BaseInfo member, final RequestCallback<List<ExT_Phr_CardBindRec>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_CARD_INFO_QUERY, true);

        request.setBody(new TBase());
        if (null != GlobalSettings.INSTANCE.getCurrentUser()) {
            Request.commonUserValidation(request, GlobalSettings.INSTANCE.getCurrentUser());
        }
        // Request.commonUserValidation(request, GlobalSettings.INSTANCE.getCurrentUser());

        T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
        baseInfo.setUserId(member.getUserId());
        baseInfo.setEhrID(member.getEhrID());
        request.getBody().setPhr_BaseInfo(baseInfo);
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        final ExT_Phr_CardBindRec carbindRec = new ExT_Phr_CardBindRec();
        carbindRec.setCardType(Constants.CARD_TYPE_MEDICAL);
        request.getBody().setPhr_CardBindRec(carbindRec);

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null && context != null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<ExT_Phr_CardBindRec>>() {
        }).execute(taskTag, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                if (member.getEhrID().equals(GlobalSettings.INSTANCE.getCurrentFamilyAccountId())) {
                    //如果是当前的家庭成员，设置其patientID
                    if (result != null && !result.isEmpty()) {
                        G1011 curPatientInfo = new G1011();
                        curPatientInfo.setPatientID(result.get(0).getPatientID());
                        GlobalSettings.INSTANCE.setCurPatientInfo(curPatientInfo);
                    }
                }
                callback.onRequestSuccess(result);
            }

            @Override
            public void onRequestError(RequestError error) {
                callback.onRequestError(error);
            }
        });
    }

    /**
     * 绑定诊疗卡
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param member      家庭成员
     * @param cardInfo    绑定的卡信息
     * @param callback    接口回调
     */
    public static void bindCardAsync(Context context, String taskTag, View loadingView,
                                     final T_Phr_BaseInfo member, ExT_Phr_CardBindRec cardInfo,
                                     RequestCallback<List<ExT_Phr_CardBindRec>> callback) {
        final Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_CARD_INFO_ADD, true);

        request.setBody(new TBase());
        Request.commonUserValidation(request, GlobalSettings.INSTANCE.getCurrentUser());
        request.getBody().setPhr_CardBindRec(cardInfo);
        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage("绑定中，请稍后...")
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<List<T_Phr_CardBindRec>>() {
                })
                .execute(taskTag, callback);
    }

    public static void unBindAsync(Context context, String taskTag, View loadingView,
                                   final T_Phr_BaseInfo member, ExT_Phr_CardBindRec cardInfo,
                                   RequestCallback<List<ExT_Phr_CardBindRec>> callback) {
        final Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_CARD_INFO_UNBIND, true);

        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
        request.setBody(new TBase());
        Request.commonUserValidation(request, userInfo);

        T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
        baseInfo.setUserId(member.getUserId());
        baseInfo.setEhrID(member.getEhrID());
        request.getBody().setPhr_BaseInfo(baseInfo);

        request.getBody().setPhr_CardBindRec(cardInfo);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage("解除中...")
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<List<ExT_Phr_CardBindRec>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 获取指定member的诊疗卡信息
     *
     * @param member 家庭成员
     * @return List<ExT_Phr_CardBindRec>
     * @throws Exception
     */
    public static List<ExT_Phr_CardBindRec> getBindedCard(T_Phr_BaseInfo member) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_CARD_INFO_QUERY, true);

        request.setBody(new TBase());
        Request.commonUserValidation(request, GlobalSettings.INSTANCE.getCurrentUser());

        T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
        baseInfo.setUserId(member.getUserId());
        baseInfo.setEhrID(member.getEhrID());
        request.getBody().setPhr_BaseInfo(baseInfo);
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        final ExT_Phr_CardBindRec carbindRec = new ExT_Phr_CardBindRec();
        carbindRec.setCardType(Constants.CARD_TYPE_MEDICAL);
        request.getBody().setPhr_CardBindRec(carbindRec);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        List<ExT_Phr_CardBindRec> result = JsonUtil.toListObject(bodyData, NODE_ITEM, ExT_Phr_CardBindRec.class, new TypeToken<List<ExT_Phr_CardBindRec>>() {
        }.getType());

        if (member.getEhrID().equals(GlobalSettings.INSTANCE.getCurrentFamilyAccountId())) {
            //如果是当前的家庭成员，设置其patientID
            if (result != null && !result.isEmpty()) {
                G1011 curPatientInfo = new G1011();
                curPatientInfo.setPatientID(result.get(0).getPatientID());
                GlobalSettings.INSTANCE.setCurPatientInfo(curPatientInfo);
            }
        }

        return result;
    }

    /////////////////////////////////////////////////////////

    /**
     * 查询本地的科室列表【1031】
     *
     * @param parentDeptID 父科室ID
     * @return
     * @throws Exception
     */
    public static List<G1211> getDepartFromPhr(String parentDeptID) throws Exception {
        Request<T1211> request = new Request<T1211>();
        Request.commonHeader(request, 1031, true);

        request.setBody(new T1211());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID(parentDeptID);
        request.getBody().setPinYinCode(null);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1211.class, new TypeToken<List<G1211>>() {
        }.getType());
    }

    /**
     * 如果recordId为空，获取所有的医生列表否则获取医生详细信息【1033】
     *
     * @param recordId
     * @return
     * @throws Exception
     */
    public static List<G1033> getDoctListOrDetails(String recordId) throws Exception {
        Request<T1033> request = new Request<>();
        Request.commonHeader(request, 1033, true);

        request.setBody(new T1033());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setRecordId(recordId);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1033.class, new TypeToken<List<G1033>>() {
        }.getType());
    }

    /**
     * 获取疾病详细信息【5612】
     *
     * @param diseaseId 疾病ID
     * @return 返回疾病信息数据
     * @throws Exception
     */
    public static List<KBDiseaseDetails> getDiseaseDetailsAsync(String diseaseId) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DISEASE_DETAILS, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setDiseaseId(diseaseId);
        JSONObject bodyData = WebUtil.httpExecute(request, true);

        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDiseaseDetails.class, new TypeToken<List<KBDiseaseDetails>>() {
        }.getType());

    }


    /**
     * 获取疾病对应的科室列表【5613】
     *
     * @param deptCode 科室ID
     * @return
     * @throws Exception
     */
    public static List<KBDepart> getDiseaseDepartAsync(String deptCode) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DISEASE_DEPT_LIST, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setDeptCode(deptCode);
        JSONObject bodyData = WebUtil.httpExecute(request, true);

        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDepart.class, new TypeToken<List<KBDepart>>() {
        }.getType());

    }

    /**
     * 获取身体部位列表【5614】
     *
     * @return
     * @throws Exception
     */
    public static List<KBBodyPart> getBodyPartsAsync() throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_BODY_PARTS_GET, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setBodyPartCode(null);

        JSONObject bodyData = WebUtil.httpExecute(request, true);

        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBBodyPart.class, new TypeToken<List<KBBodyPart>>() {
        }.getType());
    }


    /**
     * 获取疾病列表【5611】
     *
     * @param bodyPartCode 身体部位代码
     * @param deptCode     科室代码
     * @return
     * @throws Exception
     */
    public static List<KBDiseaseDetails> getDiseaseList(String bodyPartCode, String deptCode) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DISEASE_LIST, true);

        request.setBody(new TBase());
        request.getBody().setBodyPartCode(bodyPartCode);
        request.getBody().setDeptCode(deptCode);
        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDiseaseDetails.class, new TypeToken<List<KBDiseaseDetails>>() {
        }.getType());
    }

    public static List<KBTreatmentDetails> getTreatmentList(String treatmentKindCode, String searchMsg)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUNC_CODE_GET_TREATMENT, true);
        request.setBody(new TBase());
        request.getBody().setTreatmentKindCode(treatmentKindCode);
        request.getBody().setSearchMsg(searchMsg);

        JSONObject bodyData = WebUtil.httpExecute(request, true);

        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBTreatmentDetails.class, new TypeToken<List<KBTreatmentDetails>>() {
        }.getType());
    }

    public static List<KBTestCheckDetails> getTestDetail(String testId)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUNC_GET_TEST_CHECK_DETAILS, true);

        request.setBody(new TBase());
        request.getBody().setTestId(testId);
        JSONObject bodyData = WebUtil.httpExecute(request, true);

        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBTestCheckDetails.class, new TypeToken<List<KBTestCheckDetails>>() {
        }.getType());
    }

    /**
     * 获取医生分类列表：医生按照科室分类【1034】
     *
     * @return
     * @throws Exception
     */
    public static List<G1034> getDeptListOfDoct(String classId) throws Exception {
        Request<T1034> request = new Request<>();
        Request.commonHeader(request, 1034, true);

        request.setBody(new T1034());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setDoctorClassId(classId);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1034.class,
                new TypeToken<List<G1034>>() {
                }.getType());
    }

    /**
     * 查询就诊指南/医院动态列表【1035】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG，用于取消操作
     * @param typeID   要验证的手机号码  空值：查询所有 1：就诊指南 2：医院动态
     * @param callback 接口回调
     */
    public static void queryHospInfo(Context context, String taskTag, String typeID, RequestCallback<List<G1035>> callback) {
        Request<T1035> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_HOSPITAL_INFO, true);

        request.setBody(new T1035());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setTypeID(typeID);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_loading_validate))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<G1035>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 查询就诊指南/医院动态明细【1036】
     *
     * @param context     所属页面的context
     * @param taskTag     任务TAG，用于取消操作
     * @param directoryId 指南信息唯一编码
     * @param callback    接口回调
     */
    public static void queryHospDetail(Context context, String taskTag, String directoryId, RequestCallback<G1036> callback) {
        Request<T1036> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_HOSPITAL_DETAIL, true);

        request.setBody(new T1036());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setDirectoryId(directoryId);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_loading_validate))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<GResponse<G1036>>() {
                })
                .execute(taskTag, callback);
    }

    public static List<KBDiseaseDetails> getCommonDisease(String searchMsg)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUNC_GET_DISEASE_COMMON, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setSearchMsg(searchMsg);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDiseaseDetails.class,
                new TypeToken<List<KBDiseaseDetails>>() {
                }.getType());
    }


    /**
     * 获取药品分类【5617】
     *
     * @param searchMsg
     * @param kindCode
     * @return
     * @throws Exception
     */
    public static List<KBDrugDetails> getDrugClassify(String searchMsg, String kindCode) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DRUG_CLASSIFY, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setSearchMsg(searchMsg);
        request.getBody().setPropertyKindCode(kindCode);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDrugDetails.class,
                new TypeToken<List<KBDrugDetails>>() {
                }.getType());
    }

    /**
     * 获取常用药物【5615】
     *
     * @param useKindCode
     * @return
     * @throws Exception
     */
    public static List<KBDrugDetails> getDrugCommons(String useKindCode)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DRUG_COMMON, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setUseKindCode(useKindCode);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDrugDetails.class,
                new TypeToken<List<KBDrugDetails>>() {
                }.getType());
    }

    /**
     * 获取常用药物用途【5619】
     *
     * @param useKindCode
     * @return
     * @throws Exception
     */
    public static List<KBDrugUseKind> getCommonDrugKindDicts(String useKindCode)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_COMMON_DRUG_DICTS, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        //为空则查询所有药物用途；否则查询当前编码的药物用途信息
        request.getBody().setUseKindCode(useKindCode);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDrugUseKind.class,
                new TypeToken<List<KBDrugUseKind>>() {
                }.getType());
    }

    /**
     * 获取药瓶详细【5618】
     *
     * @param drugId
     * @return
     * @throws Exception
     */
    public static List<KBDrugDetails> getDrugDetail(String drugId)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DRUG_DETAIL, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setDrugId(drugId);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDrugDetails.class,
                new TypeToken<List<KBDrugDetails>>() {
                }.getType());
    }

    /**
     * 药物属性列表【5620】
     *
     * @param propertyKindCode 药物属性编码
     * @return List<KBDrugProperty>
     * @throws Exception
     */
    public static List<KBDrugProperty> getDrugProperty(String propertyKindCode)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DRUG_PROPERTY, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        //为空则查询所有药物属性列表；为0则查询一级药物属性列表；否则查询其子节点药物属性列表
        request.getBody().setPropertyKindCode(propertyKindCode);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDrugProperty.class,
                new TypeToken<List<KBDrugProperty>>() {
                }.getType());
    }

    /**
     * 获取化验知识分类【5621】
     *
     * @param testKindCode 为空则查询所有化验单分类列表；为0则查询一级化验单分类列表；否则查询其子节点化验单分类列表
     * @return List<KBTestCheckKind>
     * @throws Exception
     */
    public static List<KBTestCheckKind> getTestKinds(String testKindCode)
            throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_TEST_KINDS, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setTestKindCode(testKindCode);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBTestCheckKind.class,
                new TypeToken<List<KBTestCheckKind>>() {
                }.getType());
    }

    /**
     * 获取抢救药物【5616】
     *
     * @return List<KBDrugDetails>
     * @throws Exception
     */
    public static List<KBDrugDetails> getDrugResuce() throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_DRUG_RESCUE, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBDrugDetails.class,
                new TypeToken<List<KBDrugDetails>>() {
                }.getType());
    }

    /**
     * 获取化验知识列表【5622】
     *
     * @param testKindCode 化验类型编码
     * @param searchMsg    按化验知识名称模糊查询，用于搜索功能时TestKindCode值必须为“-1”
     * @return List<KBTestCheckDetails>
     * @throws Exception
     */
    public static List<KBTestCheckDetails> getTestList(String testKindCode,
                                                       String searchMsg) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_TEST_LIST, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setTestKindCode(testKindCode);
        request.getBody().setSearchMsg(searchMsg);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBTestCheckDetails.class,
                new TypeToken<List<KBTestCheckDetails>>() {
                }.getType());
    }

    /**
     * 查询急救分类【5624】
     *
     * @param testTreatmentKind 为空则查询所有急救分类；否则查询当前编码的急救分类
     * @return List<KBTreatmentKind>
     * @throws Exception
     */
    public static List<KBTreatmentKind> getTreatmentKinds(String testTreatmentKind) throws Exception {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_TREATMENT_KINDS, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setTestKindCode(testTreatmentKind);

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, KBTreatmentKind.class,
                new TypeToken<List<KBTreatmentKind>>() {
                }.getType());
    }


    /**
     * 根据颜色获取身体部位编码【5701】
     *
     * @param context  所属页面Context
     * @param taskTag  任务TAG
     * @param hexColor 颜色编码
     * @param callback 接口回调
     * @throws Exception
     */
    public static void getBodyPartsByColorAsync(Context context, String taskTag,
                                                String hexColor,
                                                RequestCallback<List<KBBodyPart>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_BODY_PARTS_BY_COLOR, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setHexColor(hexColor);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<KBBodyPart>>() {
                })
                .execute(taskTag, callback);
    }


    /**
     * 查询症部位对应症状列表【5702】
     *
     * @param partCode
     * @param sex
     * @return
     * @throws Exception
     */
    public static void getBodyToSymptomsAsync(Context context, String taskTag, String partCode,
                                              String sex, RequestCallback<List<BodyToSymptom>> callback) {
        Request<TBase> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_BODY_TO_SYMPTOM, true);

        request.setBody(new TBase());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        request.getBody().setBodyPartCode(partCode);
        request.getBody().setSex(sex);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<BodyToSymptom>>() {
                })
                .execute(taskTag, callback);
    }


    //---医院【start】---
    public static void getPatientInfoAsync(final Context context, String taskTag, View loadingView, T_Phr_CardBindRec cardInfo, final RequestCallback<G1011> callback) {
        Request<T1011> request = new Request<>();
        Request.commonHeader(request, 1011, false);

        T1011 t1011 = new T1011();
        t1011.setHospCode(GlobalSettings.INSTANCE.getHospCode());
        t1011.setCardNo(cardInfo.getCardNo());
        t1011.setCardType(cardInfo.getCardType() + "");
        t1011.setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        t1011.setSecurityNo("");
        t1011.setPassword("");
        request.setBody(t1011);

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView != null) {
            builder.setLoadingView(loadingView);
        } else if (context != null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading)).showLoadingDlg(context, true);
        }
        builder.mappingInto(new TypeToken<GResponse<G1011>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 【1011】
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param cardInfo
     * @param callback
     */
    public static void loadPatientInfoAsync(final Context context, String taskTag, View loadingView, T_Phr_CardBindRec cardInfo, final RequestCallback<G1011> callback) {
        Request<T1011> request = new Request<>();
        Request.commonHeader(request, 1011, false);

        T1011 t1011 = new T1011();
        t1011.setHospCode(GlobalSettings.INSTANCE.getHospCode());
        t1011.setCardNo(cardInfo.getCardNo());
        t1011.setCardType(cardInfo.getCardType() + "");
        t1011.setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        t1011.setSecurityNo("");
        t1011.setPassword("");
        request.setBody(t1011);

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView != null) {
            builder.setLoadingView(loadingView);
        } else if (context != null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading)).showLoadingDlg(context, true);
        }
        builder.mappingInto(new TypeToken<GResponse<G1011>>() {
        })
                .execute(taskTag, new RequestCallback<G1011>() {
                    @Override
                    public void onRequestSuccess(G1011 result) {
                        callback.onRequestSuccess(result);
//                        if (checkPatientInfo(result, GlobalSettings.INSTANCE.getCurrentFamilyAccount())) {
//
//                        } else {
//                            showCardInfoDeprecatedWarningDlg(context,null);
//                            callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
//                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        if (error != null && error.getException() != null) {
                            if (error.getException() instanceof GWIVolleyError) {
                                GWIVolleyError gwiVolleyError = (GWIVolleyError) error.getException();
                                if (gwiVolleyError.getStatus() == GResponse.ERROR) {
                                    showCardInfoDeprecatedWarningDlg(context, gwiVolleyError.getCause().getLocalizedMessage());
                                }
                            }
                        }
                        callback.onRequestError(error);
                    }
                });
    }

    /**
     * 加载门诊患者信息【1011】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG
     * @param cardInfo 患者卡信息
     * @param callback 接口回调
     */
    public static void loadPatientInfoAsync(final Context context, String taskTag, T_Phr_CardBindRec cardInfo, final RequestCallback<G1011> callback) {
        loadPatientInfoAsync(context, taskTag, null, cardInfo, callback);
    }

    /**
     * 获取门诊患者信息【1011】
     *
     * @param cardInfo 诊疗卡信息
     * @return G1011
     * @throws Exception
     */
    public static G1011 getPatientInfo(final Context context, ExT_Phr_CardBindRec cardInfo) throws Exception {
        JSONObject bodyData;
        try {
            Request<T1011> request = new Request<>();
            Request.commonHeader(request, 1011, false);

            T1011 t1011 = new T1011();
            t1011.setHospCode(GlobalSettings.INSTANCE.getHospCode());
            t1011.setCardNo(cardInfo.getCardNo());
            t1011.setCardType(cardInfo.getCardType() + "");
            t1011.setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
            t1011.setSecurityNo("");
            t1011.setPassword("");
            request.setBody(t1011);

            bodyData = WebUtil.httpExecute(request, false);
        } catch (Exception e) {
            if (e instanceof BaseException) {
                final BaseException baseException = ((BaseException) e);
                if (baseException.getErrorCode() == GResponse.ERROR) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            showCardInfoDeprecatedWarningDlg(context, baseException.getLocalizedMessage());
                        }
                    });
                }
            }

            throw e;
        }
//        G1011 patientInfo = JsonUtil.toObject(bodyData, G1011.class);
//        if (!checkPatientInfo(patientInfo, member)) {
//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    showCardInfoDeprecatedWarningDlg(context,null);
//                }
//            });
//            throw new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！");
//        } else return patientInfo;
        return JsonUtil.toObject(bodyData, G1011.class);
    }

    private static boolean checkPatientInfo(G1011 patientInfo, T_Phr_BaseInfo member) {
        Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
        if (HospitalParams.VALUE_ONE.equals(HospitalParams.getValue(params, HospitalParams.CODE_IS_VALIDATE_CARD_LOGOUT_ENABLED))) {
            return patientInfo != null
                    && (TextUtil.isEmpty(patientInfo.getName()) || patientInfo.getName().equals(member.getName()))
                    && (TextUtil.isEmpty(patientInfo.getIDCardNo()) || patientInfo.getIDCardNo().equals(member.getIDCard()))
                    && (TextUtil.isEmpty(patientInfo.getPatientID()) || patientInfo.getPatientID().equals(GlobalSettings.INSTANCE.getCurPatientInfo().getPatientID()));
        } else
            return true;
    }

    private static void showCardInfoDeprecatedWarningDlg(final Context context, String errorMsg) {
        BaseDialog baseDialog = new BaseDialog(context);
        baseDialog.setCanceledOnTouchOutside(false);
        baseDialog.setTitle("提示");
        baseDialog.setContent(errorMsg == null ? "您的诊疗卡不存在或已被注销，请重新绑卡！" : errorMsg);
        baseDialog.showHeader(true);
        baseDialog.showFooter(true);
        baseDialog.setLeftButton(context.getString(R.string.dialog_cofirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO：
                        //((Activity) context).startActivityForResult(new Intent(context, HosCardOperationActivity.class), 1);
                        ((Activity) context).finish();
                        dialog.dismiss();
                    }
                });
        baseDialog.show();
    }

    /**
     * 获取医院信息【1013】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param callback    接口回调
     */
    public static void loadHospitalInfoAsync(String taskTag, View loadingView, RequestCallback<List<G1013.HospitalInfo>> callback) {
        loadHospitalInfoAsync(null, taskTag, loadingView, callback);
    }

    /**
     * @param context     所属页面Context
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param callback    接口回调
     */
    public static void loadHospitalInfoAsync(Context context, String taskTag, View loadingView, RequestCallback<List<G1013.HospitalInfo>> callback) {
        Request<T1013> request = new Request<>();
        Request.commonHeader(request, 1013, true);

        request.setBody(new T1013());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();

        if (context != null) {
            builder.showLoadingDlg(context, true)
                    .setLoadingMessage(context.getString(R.string.dialog_content_loading));
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<G1013.HospitalInfo>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 加载医院参数信息【1014】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param paramCode   参数编码，为空获取所有参数
     * @param callback    接口回调
     */
    public static void loadHospitalParamsAsync(String taskTag, View loadingView,
                                               String paramCode,
                                               RequestCallback<List<G1014>> callback) {
//        Request<T1014> request = new Request<>();
//        Request.commonHeader(request, Request.FUN_CODE_HOSPITAL_PARAMS, true);
//        request.setBody(new T1014());
//        request.getBody().setParameterCode(paramCode);
//        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
//        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request))
//                .setLoadingView(loadingView)
//                .fromGWI()
//                .mappingInto(new TypeToken<List<G1014>>() {
//                })
//                .execute(taskTag, callback);
        loadHospitalParamsAsync(null, taskTag, loadingView, paramCode, callback);
    }

    /**
     * 加载医院参数信息【1014】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param paramCode   参数编码，为空获取所有参数
     * @param callback    接口回调
     */
    public static void loadHospitalParamsAsync(Context context, String taskTag, View loadingView,
                                               String paramCode,
                                               RequestCallback<List<G1014>> callback) {
        Request<T1014> request = new Request<>();
        Request.commonHeader(request, Request.FUN_CODE_HOSPITAL_PARAMS, true);
        request.setBody(new T1014());
        request.getBody().setParameterCode(paramCode);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();

        if (context != null) {
            builder.showLoadingDlg(context, true)
                    .setLoadingMessage(context.getString(R.string.dialog_content_loading));
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<G1014>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 验证patientID(卡注销).
     *
     * @param context
     * @param patientInfo
     * @param baseInfo
     * @param callback
     * @return
     */
    public static boolean checkMedicalCardLogout(final Context context, G1011 patientInfo, T_Phr_BaseInfo baseInfo, RequestCallback callback) {
        if (!checkPatientInfo(patientInfo, baseInfo)) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showCardInfoDeprecatedWarningDlg(context, null);
                }
            });
            if (callback != null) {
                callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
            }
            return false;
        } else return true;
    }

    /**
     * 创建订单（挂号、预交金充值、预约挂号,缴费）【9010】
     *
     * @param context   所属页面上下文
     * @param taskTag   任务TAG
     *                  //     * @param userInfo 用户信息
     *                  //     * @param baseInfo 家庭成员信息
     * @param paramater 订单参数
     *                  //     * @param regIds 处方单缴费时,required
     *                  //     * @param tranType 交易类型
     *                  //     * @param bussinessType 业务类型
     *                  //     * @param payMode 支付类型
     *                  //     * @param dct 挂号、预约挂号的医生
     *                  //     * @param patientInfo 门诊患者信息
     *                  //     * @param transactionValue 交易金额
     *                  //     * @param cardInfo 诊疗卡信息
     * @param callback  接口回调
     */
    public static void createOrderAsync(Context context, String taskTag,
                                        OrderParamater paramater,
                                        /*String regIds,
                                   String tranType,String bussinessType,String payMode,
                                   G1417 dct,G1211 dept, G1011 patientInfo,double transactionValue,
                                   T_Phr_CardBindRec cardInfo,*/
                                        RequestCallback<OrderResult> callback) {

//        if (!checkPatientInfo(paramater.getPatientInfo(), paramater.getBaseInfo())) {
//            showCardInfoDeprecatedWarningDlg(context, null);
//            callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
//            return;
//        }
        if (!checkMedicalCardLogout(context, paramater.getPatientInfo(), paramater.getBaseInfo(), callback))
            return;

        Request<OrderInfo> request = new Request<>();
        final int FUN_CODE = 9010;//1015
        Request.commonHeader(request, FUN_CODE, false);

        String hospCode = GlobalSettings.INSTANCE.getHospCode();
        String terminalNo = GlobalSettings.INSTANCE.getTerminalNO();

        request.setBody(new OrderInfo());
        request.getBody().setHospCode(hospCode);
        request.getBody().setPayType(paramater.getPayType());
        request.getBody().setAccount(paramater.getUserInfo().getUserId() + "");
        request.getBody().setBusinessType(paramater.getBussinessType());
        request.getBody().setAmount(paramater.getTransactionValue());
        request.getBody().setUserName(paramater.getBaseInfo().getName());
        request.getBody().setPatientID(paramater.getPatientInfo().getPatientID());
        request.getBody().setCardNo(paramater.getCardInfo().getCardNo());
        request.getBody().setCardType(paramater.getCardInfo().getCardType() + "");
        request.getBody().setIDCardNo(paramater.getBaseInfo().getIDCard());
        request.getBody().setPhoneNo(paramater.getUserInfo().getMobilePhone());
        request.getBody().setTerminalNo(terminalNo);
        if (Constants.PAY_MODE_WEIXIN.equals(paramater.getPayType())) {
            request.getBody().setPayActionType("2");
        } else {
            request.getBody().setPayActionType("0");//其他：0
        }

        if (paramater.getBussinessType().equals(BankUtil.BusinessType_Rgt)) {//挂号
            request.getBody().setOrderDescription("挂号");
            Request<T1214> r1214 = new Request<>();
            Request.commonHeader(r1214, 1214, false);
            r1214.setBody(new T1214());
            r1214.getBody().setHospCode(hospCode);
            r1214.getBody().setCardNo(paramater.getCardInfo().getCardNo());
            r1214.getBody().setCardType(paramater.getCardInfo().getCardType() + "");
            r1214.getBody().setDeptID(paramater.getDept().getDeptID());
            r1214.getBody().setDoctID(paramater.getDct().getDoctID());
            r1214.getBody().setPayMode(paramater.getPayType());
            r1214.getBody().setPatientID(paramater.getPatientInfo().getPatientID());
            r1214.getBody().setRegSourceID(paramater.getDct().getRegSourceID());
            r1214.getBody().setTerminalNo(terminalNo);
            r1214.getBody().setRegFee(paramater.getDct().getRegFee());
            r1214.getBody().setTotalRegFee(paramater.getDct().getTotalRegFee());
            r1214.getBody().setTimeRegion(paramater.getDct().getTimeRegion());

            r1214.getBody().setDiscountFee(paramater.getDct().getDiscountFee());
            if (paramater.getSubHosp() != null) {
                r1214.getBody().setHospAreaID(paramater.getSubHosp().getSubHospCode());
            }

            r1214.getBody().setPayInfo(BankUtil.initPayInfo(paramater.getUserInfo(), paramater.getTransactionValue()));
            request.getBody().setBusinessRequestData(BankUtil.getBusXMLy(paramater.getBussinessType(), r1214));
        } else if (paramater.getBussinessType().equals(BankUtil.BusinessType_CARD_CHARGE)) {//充值
            request.getBody().setOrderDescription("充值");
            Request<T1710> r1710 = new Request<>();
            Request.commonHeader(r1710, 1710, false);
            r1710.setBody(new T1710());
            r1710.getBody().setHospCode(hospCode);
            r1710.getBody().setCardNo(paramater.getCardInfo().getCardNo());
            r1710.getBody().setCardType(paramater.getCardInfo().getCardType() + "");
            r1710.getBody().setPatientID(paramater.getPatientInfo().getPatientID());
            r1710.getBody().setPayMode(paramater.getPayType());
            r1710.getBody().setSecurityNo(null);
            r1710.getBody().setTerminalNo(terminalNo);
            r1710.getBody().setTerTranSerNo(null);
            r1710.getBody().setPayInfo(BankUtil.initPayInfo(paramater.getUserInfo(), paramater.getTransactionValue()));
            request.getBody().setBusinessRequestData(BankUtil.getBusXMLy(paramater.getBussinessType(), r1710));
        } else if (paramater.getBussinessType().equals(BankUtil.BusinessType_Recipe)) {//缴费
            request.getBody().setOrderDescription("门诊缴费");
            Request<T1612> t1612 = new Request<>();
            Request.commonHeader(t1612, 1612, false);

            t1612.setBody(new T1612());
            t1612.getBody().setHospCode(hospCode);
            t1612.getBody().setCardNo(paramater.getCardInfo().getCardNo());
            t1612.getBody().setCardType(paramater.getCardInfo().getCardType() + "");
            t1612.getBody().setTerminalNo(terminalNo);
            t1612.getBody().setRegID("");
            t1612.getBody().setRecipeID(paramater.getRegIds());
            t1612.getBody().setPayMode(paramater.getPayType());
            t1612.getBody().setPatientID(paramater.getPatientInfo().getPatientID());
            t1612.getBody().setPersonalFee("0");
            t1612.getBody().setMedInsureStr("");
            t1612.getBody().setPreChargeFee("0");
            t1612.getBody().setMedInsureFee("0");
            t1612.getBody().setTotalFee(paramater.getTransactionValue() + "");
            t1612.getBody().setPayInfo(BankUtil.initPayInfo(paramater.getUserInfo(), paramater.getTransactionValue()));
            t1612.getBody().setTerTranSerNo("");
            request.getBody().setBusinessRequestData(BankUtil.getBusXMLy(paramater.getBussinessType(), t1612));
        } else if (paramater.getBussinessType().equals(BankUtil.BusinessType_Rgt_Order)) {//预约挂号
            request.getBody().setOrderDescription("预约挂号");
            Request<T1414> r1414 = new Request<>();
            Request.commonHeader(r1414, 1414, false);
            r1414.setBody(new T1414());
            r1414.getBody().setHospCode(hospCode);
            r1414.getBody().setCardNo(paramater.getCardInfo().getCardNo());
            r1414.getBody().setTerminalNo(terminalNo);
            r1414.getBody().setCardType(String.valueOf(paramater.getCardInfo().getCardType()));
            r1414.getBody().setDeptID(paramater.getDept().getDeptID() + "");
            r1414.getBody().setDoctID(paramater.getDct().getDoctID() + "");
            r1414.getBody().setRegSourceID(paramater.getDct().getRegSourceID() + "");

            r1414.getBody().setDeptName(paramater.getDept().getDeptName() + "");
            r1414.getBody().setDeptLocation(paramater.getDept().getLocation() + "");
            r1414.getBody().setDoctName(paramater.getDct().getDoctName());
            r1414.getBody().setStartTime(paramater.getDct().getStartTime());
            r1414.getBody().setEndTime(paramater.getDct().getEndTime() + "");
            r1414.getBody().setOrderDate(paramater.getOrderDate());
            r1414.getBody().setRankName(paramater.getDct().getRankName() + "");

            r1414.getBody().setPayMode(paramater.getPayType());
            r1414.getBody().setTotalRegFee(paramater.getDct().getTotalRegFee());
            r1414.getBody().setRegFee(paramater.getDct().getRegFee());
            r1414.getBody().setTimeRegion(paramater.getDct().getTimeRegion());
            r1414.getBody().setOrderDate(paramater.getDct().getOrderDate());
            r1414.getBody().setTimeRegion(paramater.getDct().getTimeRegion());
            r1414.getBody().setPatientID(paramater.getPatientInfo().getPatientID());
            r1414.getBody().setPayInfo(BankUtil.initPayInfo(paramater.getUserInfo(), paramater.getTransactionValue()));
            r1414.getBody().setUserInfo(BankUtil.initUserInfo(paramater.getBaseInfo()));

            r1414.getBody().setDiscountFee(paramater.getDct().getDiscountFee());
            r1414.getBody().setHospAreaID(paramater.getSubHosp().getSubHospCode());

            request.getBody().setBusinessRequestData(BankUtil.getBusXMLy(paramater.getBussinessType(), r1414));
        }

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                .showLoadingDlg(context, false)
                .mappingInto(new TypeToken<GResponse<OrderResult>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * @param taskTag
     * @param loadingView
     * @param orderNO
     * @param member
     * @param cardInfo
     * @param pageIndex
     * @param pageSize
     * @param callback
     */
    public static void loadOrderResultsAsync(String taskTag, View loadingView,
                                             String orderNO, T_Phr_BaseInfo member, T_Phr_CardBindRec cardInfo, G1011 patientInfo,
                                             int pageIndex, int pageSize,
                                             String startDate, String endDate,
                                             final RequestCallback<OrderQueryResults> callback) {

        final Request<OrderQuery> request = new Request<>();
        Request.commonHeader(request, 9011, true);//1016

        request.setBody(new OrderQuery());
        request.getBody().setPageIndex(pageIndex);
        request.getBody().setPageSize(pageSize);
        request.getBody().setCardNo(cardInfo.getCardNo());
        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        } else {
            request.getBody().setPatientID(cardInfo.getPatientID());
        }
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setOrderNo(orderNO);
        request.getBody().setPayType(Constants.PAY_MODE_ALIPAY);
        request.getBody().setTransType(Constants.PAY_MODE_ALIPAY);
        request.getBody().setIDCardNo(member.getIDCard());
        request.getBody().setBeginDate(startDate);
        request.getBody().setEndDate(endDate);

        AsyncTasks.doSilAsyncTask(loadingView, new AsyncCallback<OrderQueryResults>() {
            @Override
            public OrderQueryResults callAsync() throws Exception {
                JSONObject bodyData = WebUtil.httpExecute(request, false);
                OrderQueryResults result = new OrderQueryResults();
                result.setTotalCount(bodyData.getInt("TotalCount"));
                try {
                    result.setItems(JsonUtil.toListObject(bodyData.getJSONObject("Items"), NODE_ITEM, OrderQueryResults.OrderQueryResult.class,
                            new TypeToken<List<OrderQueryResults.OrderQueryResult>>() {
                            }.getType()));
                } catch (JSONException e) {
                    e.printStackTrace();
                    throw new BaseException("没有查询到数据");
                }
                return result;
            }


            @Override
            public void onPostCall(OrderQueryResults results) {
                callback.onRequestSuccess(results);
            }

            @Override
            public void onCallFailed(Exception exception) {
                LogUtil.e("loadOrderResultsAsync", exception.getLocalizedMessage());
                callback.onRequestError(new RequestError(exception));
            }
        });


//        if (cardInfo != null && cardInfo.getCardNo() != null) {
//            request.getBody().setCardNo(cardInfo.getCardNo());
//        }
//        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
//                .setLoadingView(loadingView)
//                .mappingInto(new TypeToken<GResponse<OrderQueryResults>>() {
//                })
//                .execute(taskTag, callback);
    }

    /**
     * 订单查询【9011】
     *
     * @param orderNO  订单编号，为空查询所有
     * @param member   家庭成员信息
     * @param cardInfo 家庭成员的卡绑定信息
     * @return GResponse<OrderQueryResults>
     * @throws Exception
     */
    public static OrderQueryResults getOrderResults(String orderNO, T_Phr_BaseInfo member, T_Phr_CardBindRec cardInfo,
                                                    int pageIndex, int pageSize) throws Exception {
        Request<OrderQuery> request = new Request<>();
        Request.commonHeader(request, 9011, true);//1016

        request.setBody(new OrderQuery());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setOrderNo(orderNO);
        request.getBody().setPageIndex(pageIndex);
        request.getBody().setPageSize(pageSize);
        request.getBody().setIDCardNo(member.getIDCard());
        request.getBody().setPayType(Constants.PAY_MODE_ALIPAY);
        request.getBody().setBeginDate(CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, new Date()));
        request.getBody().setEndDate(CommonUtils.parsebeforeMonthDate(new Date()));
        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
        }

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toObject(bodyData, OrderQueryResults.class);
    }

    /**
     * 获取分院信息【1017】
     *
     * @param context  所属页面context
     * @param taskTag  任务TAG
     * @param callback 接口回调
     */
    public static void getSubHospitalInfoAsync(Context context, String taskTag, RequestCallback<List<G1017>> callback) {
        Request<T1017> request = new Request<T1017>();
        Request.commonHeader(request, 1017, false);
        request.setBody(new T1017());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<G1017>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 获取诊间报到列表
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param cardInfo
     * @param patientInfo
     * @param callback
     */
    public static void loadQueueUncheckInAsync(Context context, String taskTag, View loadingView, ExT_Phr_CardBindRec cardInfo, G1011 patientInfo, RequestCallback<List<G1218>> callback) {
        Request<T1218> request = new Request<>();
        Request.commonHeader(request, 1218, false);
        request.setBody(new T1218());
        request.getBody().setPatientID(patientInfo.getPatientID());
        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, false);

        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<G1218>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 执行诊间报道
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param cardInfo
     * @param patientInfo
     * @param regId
     * @param callback
     */
    public static void doQueueCheckInAysnc(Context context, String taskTag, View loadingView, ExT_Phr_CardBindRec cardInfo, G1011 patientInfo,
                                           String regId, RequestCallback<G1219> callback) {
        Request<T1219> request = new Request<>();
        Request.commonHeader(request, 1219, false);
        request.setBody(new T1219());
        request.getBody().setPatientID(patientInfo.getPatientID());
        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setRegID(regId);
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, false);

        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<GResponse<G1219>>() {
        }).execute(taskTag, callback);
    }


    /**
     * 获取挂号科室【1211】
     *
     * @param orderdate    挂号时间
     * @param subHosCode   分院代码
     * @param parentDeptID 父科室ID
     * @return List<G1211>
     * @throws Exception
     */
    public static List<G1211> getRegistDepart(String orderdate, String subHosCode, String parentDeptID, String typeID) throws Exception {
        Request<T1211> request = new Request<>();
        Request.commonHeader(request, 1211, false);

        request.setBody(new T1211());
        if (orderdate != null) {
            request.getBody().setDate(orderdate);
        }
        request.getBody().setTypeID(typeID);
        request.getBody().setSubHospCode(subHosCode);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID(parentDeptID == null ? "" : parentDeptID);
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, "Item", G1211.class, new TypeToken<List<G1211>>() {
        }.getType());
    }

    public static void getRegistDepartAsync(Context context, String taskTag, View loadingView,
                                            String orderdate, String subHosCode, String parentDeptID, String typeID, RequestCallback<List<G1211>> callback) {
        Request<T1211> request = new Request<>();
        Request.commonHeader(request, 1211, false);

        request.setBody(new T1211());
        if (orderdate != null) {
            request.getBody().setDate(orderdate);
        }
        request.getBody().setTypeID(typeID);
        request.getBody().setSubHospCode(subHosCode);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID(parentDeptID == null ? "" : parentDeptID);
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<G1211>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 执行挂号【1214】
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param cardInfo    诊疗卡信息
     * @param patientInfo 门诊患者信息
     * @param regSourceID 号源ID
     * @param dct         挂号的医生
     * @param dept        所属的科室
     * @param callback    接口回调
     */
    public static void doRegistAsync(Context context, String taskTag,
                                     T_Phr_CardBindRec cardInfo, String typeID,
                                     G1011 patientInfo, String regSourceID, G1417 dct, G1211 dept, G1017 subHospInfo,
                                     RequestCallback<G1214> callback) {
        doRegistAsync(context, taskTag, null, cardInfo, typeID, patientInfo, regSourceID, dct, dept, subHospInfo, callback);
    }

    /**
     * 执行挂号【1214】
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param cardInfo    诊疗卡信息
     * @param patientInfo 门诊患者信息
     * @param regSourceID 号源ID
     * @param dct         挂号的医生
     * @param dept        所属的科室
     * @param callback    接口回调
     */
    public static void doRegistAsync(Context context, String taskTag, View loadingView,
                                     T_Phr_CardBindRec cardInfo, String typeID,
                                     G1011 patientInfo, String regSourceID, G1417 dct, G1211 dept, G1017 subHospInfo,
                                     RequestCallback<G1214> callback) {
        if (!checkPatientInfo(patientInfo, GlobalSettings.INSTANCE.getCurrentFamilyAccount())) {
            showCardInfoDeprecatedWarningDlg(context, null);
            callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
            return;
        }
        Request<T1214> request = new Request<>();
        Request.commonHeader(request, 1214, false);

        request.setBody(new T1214());
        //预交金支付
        request.getBody().setPayMode(Constants.PAY_MODE_CARD_MEDICAL);
        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        }
        request.getBody().setTypeID(typeID);
        request.getBody().setRankID("");
        request.getBody().setRegSourceID(regSourceID);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setTerTranSerNo("");
        if (subHospInfo != null) {
            request.getBody().setHospAreaID(subHospInfo.getSubHospCode());
        }
        if (dct != null) {
            request.getBody().setDiscountFee(dct.getDiscountFee());
        }

        if (dct != null) {
            if (dct.getDeptID() != null) {
                request.getBody().setDeptID(dct.getDeptID());
            } else {
                request.getBody().setDeptID(dept.getDeptID());
            }
            double payableFee = dct.getTotalRegFee() - dct.getDiscountFee();
            request.getBody().setTimeRegion(dct.getTimeRegion());
            request.getBody().setDoctID(dct.getDoctID() == null ? "" : dct.getDoctID());
            request.getBody().setRankID(dct.getRankID() == null ? "" : dct.getRankID());
            request.getBody().setTotalRegFee(payableFee);
            request.getBody().setPayInfo(
                    BankUtil.initPayInfo(GlobalSettings.INSTANCE.getCurrentUser(),
                            dct.getTotalRegFee()));
        }
        if (cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
        }
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                    .showLoadingDlg(context, false);

        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<GResponse<G1214>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 加载挂号医生列表【1212】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param typeID      挂号类别
     * @param deptID      科室ID
     * @param callback    接口回调
     */
    public static void loadRegistDoctorsAsync(String taskTag, View loadingView,
                                              int typeID, String deptID,
                                              RequestCallback<List<G1417>> callback) {
        Request<T1212> request = new Request<>();
        Request.commonHeader(request, 1212, false);

        request.setBody(new T1212());
        request.getBody().setTypeID(String.valueOf(typeID));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setDeptID(deptID);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<List<G1417>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 加载科室队列【1217】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param deptID      科室ID
     * @param callback    接口回调
     */
    public static void loadDepartQueueListAsync(String taskTag, View loadingView, String deptID, RequestCallback<List<G1217>> callback) {
        Request<T1217> request = new Request<>();
        Request.commonHeader(request, 1217, false);

        request.setBody(new T1217());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setDeptID(deptID);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<List<G1217>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 获取就诊队列列表【1216】
     *
     * @param cardInfo 诊疗卡信息
     * @return List<G1216>
     * @throws Exception
     */
    public static List<G1216> getQueueList(Context context, G1011 patientFromServer, T_Phr_CardBindRec cardInfo) throws Exception {
        if (!checkMedicalCardLogout(context, patientFromServer, GlobalSettings.INSTANCE.getCurrentFamilyAccount(), null)) {
            return new ArrayList<>();
        }

        Request<T1216> request = new Request<T1216>();
        Request.commonHeader(request, 1216, false);

        request.setBody(new T1216());

        G1011 patientInfo = GlobalSettings.INSTANCE.getCurPatientInfo();
        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        }

        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        JSONObject bodyData = WebUtil.httpExecute(request, true);
        Type lt = new TypeToken<List<G1216>>() {
        }.getType();
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1216.class, lt);
    }

    /**
     * 获取取药队列【1613】
     *
     * @param cardInfo 诊疗卡信息
     * @return List<G1613>
     * @throws Exception
     */
    public static List<G1613> getDrugQueueList(Context context, G1011 patientFromServer, T_Phr_CardBindRec cardInfo) throws Exception {
        if (!checkMedicalCardLogout(context, patientFromServer, GlobalSettings.INSTANCE.getCurrentFamilyAccount(), null)) {
            return new ArrayList<>();
        }
        Request<T1216> request = new Request<T1216>();
        Request.commonHeader(request, 1613, false);
        request.setBody(new T1216());
        request.getBody().setCardNo(cardInfo.getCardNo());
        G1011 patientInfo = GlobalSettings.INSTANCE.getCurPatientInfo();
        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        }
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        JSONObject bodyData = WebUtil.httpExecute(request, true);
        Type lt = new TypeToken<List<G1613>>() {
        }.getType();
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1613.class, lt);
    }

    /**
     * 获取预约挂号科室【1411】
     *
     * @param orderDate    预约时间
     * @param subHosCode   分院代码
     * @param parentDeptID 父科室ID
     * @return List<G1211>
     * @throws Exception
     */
    public static List<G1211> getAppointDepart(String orderDate, String subHosCode, String parentDeptID, String typeID) throws Exception {
        Request<T1411> request = new Request<T1411>();
        Request.commonHeader(request, 1411, false);

        request.setBody(new T1411());
        if (orderDate != null) {
            request.getBody().setDate(orderDate);
        }
        request.getBody().setTypeID(typeID);
        request.getBody().setHospCode(subHosCode);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID(parentDeptID == null ? "" : parentDeptID);
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        JSONObject bodyData = WebUtil.httpExecute(request, true);
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1211.class, new TypeToken<List<G1211>>() {
        }.getType());
    }

    /**
     * 获取预约挂号科室【1411】
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param orderDate
     * @param subHosCode
     * @param parentDeptID
     * @param typeID
     * @param callback
     */
    public static void getAppintDepartAsync(Context context, String taskTag, View loadingView,
                                            String orderDate, String subHosCode, String parentDeptID,
                                            String typeID, RequestCallback<List<G1211>> callback) {
        Request<T1411> request = new Request<>();
        Request.commonHeader(request, 1411, false);

        request.setBody(new T1411());
        if (orderDate != null) {
            request.getBody().setDate(orderDate);
        }
        request.getBody().setTypeID(typeID);
        request.getBody().setHospCode(subHosCode);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID(parentDeptID == null ? "" : parentDeptID);
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<G1211>>() {
        })
                .execute(taskTag, callback);
    }

    /**
     * 获取可预约时间列表【1412】
     *
     * @param context  所属页面的context
     * @param taskTag  任务TAG
     * @param typeID   挂号类别(普通挂号、专家挂号...)
     * @param deptID   科室ID
     * @param doctID   医生ID
     * @param callback 接口回调
     */
    public static void getAppointMentDateAsync(Context context, String taskTag,
                                               int typeID, String deptID, String doctID,
                                               RequestCallback<List<G1412>> callback) {
        getAppointMentDateAsync(context, taskTag, null, typeID, deptID, doctID, callback);
    }

    /**
     * 获取可预约时间列表【1412】
     *
     * @param context     所属页面的context
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param typeID      挂号类别(普通挂号、专家挂号...)
     * @param deptID      科室ID
     * @param callback    接口回调
     */
    public static void getAppointMentDateAsync(Context context, String taskTag, View loadingView,
                                               int typeID, String deptID, String doctID,
                                               RequestCallback<List<G1412>> callback) {
        Request<T1412> request = new Request<>();
        Request.commonHeader(request, 1412, false);

        request.setBody(new T1412());
        request.getBody().setDeptID(deptID);
        request.getBody().setTypeID(String.valueOf(typeID));
        request.getBody().setDoctID(doctID);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setSubHospCode(GlobalSettings.INSTANCE.getSubHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_loading_appointment_get))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<G1412>>() {
        })
                .execute(taskTag, callback);
    }

    /**
     * 加载预约医生列表【1413】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param typeID      挂号类别
     * @param deptID      科室ID
     * @param dateStart   预约开始时间
     * @param dateEnd     预约结束时间
     * @param callback    接口回调
     */
    public static void loadAppointDoctorsAsync(String taskTag, View loadingView,
                                               int typeID, String deptID, String dateStart, String dateEnd,
                                               RequestCallback<List<G1417>> callback) {
        Request<T1413> request = new Request<>();
        Request.commonHeader(request, 1413, false);

        request.setBody(new T1413());
        request.getBody().setTypeID(String.valueOf(typeID));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setSubHospCode(GlobalSettings.INSTANCE.getSubHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setDeptID(deptID);

        request.getBody().setOrderDate(dateStart);
        request.getBody().setDate(dateStart);
        request.getBody().setBeginDate(dateEnd);
        request.getBody().setEndDate(dateEnd);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<List<G1417>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 执行预约挂号【1414】
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param cardInfo    诊疗卡信息
     * @param payMode     支付方式
     * @param patientInfo 门诊患者信息
     * @param regSourceID 号源ID
     * @param dct         预约医生
     * @param dept        所属科室
     * @param callback    接口回调
     */
    public static void doOrderRegistAsync(Context context, String taskTag, View loadingView,
                                          T_Phr_CardBindRec cardInfo, String typeID, String payMode,
                                          G1011 patientInfo, String regSourceID, G1417 dct, G1211 dept,
                                          RequestCallback<G1414> callback) {
        if (!checkPatientInfo(patientInfo, GlobalSettings.INSTANCE.getCurrentFamilyAccount())) {
            showCardInfoDeprecatedWarningDlg(context, null);
            callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
            return;
        }

        Request<T1414> request = new Request<>();
        Request.commonHeader(request, 1414, false);

        request.setBody(new T1414());
        request.getBody().setRegSourceID(regSourceID);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setSubHospCode(GlobalSettings.INSTANCE.getSubHospCode());
        request.getBody().setTypeID(typeID);

        if (dct != null) {
            request.getBody().setDiscountFee(dct.getDiscountFee());
            request.getBody().setDeptName(dept.getDeptName());
            request.getBody().setDeptLocation(dept.getLocation());
            request.getBody().setStartTime(dct.getStartTime());
            request.getBody().setEndTime(dct.getEndTime());
            request.getBody().setRankName(dct.getRankName());
            request.getBody().setRankID(dct.getRankID());
        }

        if (dct != null && dct.getDeptID() != null) {
            request.getBody().setDeptID(dct.getDeptID());
        } else {
            request.getBody().setDeptID(dept.getDeptID());
        }
        if (cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
        }
        request.getBody().setPatientID(patientInfo.getPatientID());
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setPayMode(payMode);
        request.getBody().setTerTranSerNo("");
        if (dct != null) {
            if (dct.getTypeID() != null) {
                request.getBody().setTypeID(dct.getTypeID());
            }
            request.getBody().setDoctID(dct.getDoctID());
            request.getBody().setTotalRegFee(dct.getTotalRegFee() - dct.getDiscountFee());
            request.getBody().setOrderDate(TextUtil.isEmpty(dct.getOrderDate()) ? dct.getDate() : dct.getOrderDate());
            request.getBody().setTimeRegion(dct.getTimeRegion());
            request.getBody().setPayInfo(BankUtil.initPayInfo(GlobalSettings.INSTANCE.getCurrentUser(), dct.getTotalRegFee()));
        }
        request.getBody().setUserInfo(BankUtil.initUserInfo(GlobalSettings.INSTANCE.getCurrentFamilyAccount()));

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                    .showLoadingDlg(context, false);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<GResponse<G1414>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 获取挂号记录【1310】
     *
     * @param cardInfo
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<G1310> getRegisertInfo(ExT_Phr_CardBindRec cardInfo, String startDate, String endDate) throws Exception {
        Request<T1310> request = new Request<>();
        Request.commonHeader(request, 1310, false);
        request.setBody(new T1310());
        G1011 patientInfo = GlobalSettings.INSTANCE.getCurPatientInfo();
        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        }
        request.getBody().setVisitStartDate(startDate);
        request.getBody().setVisitEndDate(endDate);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(cardInfo.getCardType() + "");
        }
        JSONObject bodyData = WebUtil.httpExecute(request, true);
        Type lt = new TypeToken<List<G1310>>() {
        }.getType();
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1310.class,
                lt);
    }

    /**
     * 获取预约挂号记录【1510】
     *
     * @param cardInfo
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<G1510> getAppointInfo(ExT_Phr_CardBindRec cardInfo, String startDate, String endDate) throws Exception {
        Request<T1510> request = new Request<>();
        Request.commonHeader(request, 1510, false);

        request.setBody(new T1510());
        request.getBody().setVisitStartDate(startDate);
        request.getBody().setVisitEndDate(endDate);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());

        G1011 g1011 = GlobalSettings.INSTANCE.getCurPatientInfo();
        if (g1011 != null) {
            request.getBody().setPatientID(g1011.getPatientID());
        }

        request.getBody().setCorpCode(WebUtil.CorpCode);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getHospCode());

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(cardInfo.getCardType() + "");
        }
        JSONObject bodyData = WebUtil.httpExecute(request, true);

        Type lt = new TypeToken<List<G1510>>() {
        }.getType();
        return JsonUtil.toListObject(bodyData, NODE_ITEM, G1510.class, lt);
    }

    /**
     * 取消预约【1512】
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param patientInfo
     * @param cardInfo
     * @param record
     * @param callback
     */
    public static void doOrderRegistCancelAsync(Context context, String taskTag, View loadingView,
                                                G1011 patientInfo, T_Phr_CardBindRec cardInfo, G1510 record,
                                                RequestCallback<G1512> callback) {
        Request<T1512> request = new Request<>();
        Request.commonHeader(request, 1512, false);
        request.setBody(new T1512());
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setIDCardNo(
                GlobalSettings.INSTANCE.getCurrentFamilyAccount().getIDCard());
        request.getBody().setCorpCode(WebUtil.CorpCode);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(WebUtil.TERMINAL_NO());
        request.getBody().setPatientID(patientInfo.getPatientID());

        request.getBody().setOrderIdentity(record.getOrderIdentity());
        request.getBody().setRegSourceID(record.getRegSourceID());
        request.getBody().setOrderNo(record.getOrderNo());
        request.getBody().setPayMode(record.getPayMode());
        request.getBody().setTranSerNo(record.getTranSerNo());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (context != null) {
            builder.setLoadingMessage(context.getString(R.string.cancel_appoint))
                    .showLoadingDlg(context, false);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<GResponse<G1512>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 查询就诊记录（待缴费）[1610]
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param patientInfo 患者信息
     * @param cardInfo    诊疗卡信息
     * @param callback    接口回调
     */
    public static void loadReciepeOrdersAsync(Context context, String taskTag, View loadingView,
                                              G1011 patientInfo, T_Phr_CardBindRec cardInfo,
                                              RequestCallback<List<G1610>> callback) {
        Request<T1610> request = new Request<>();
        Request.commonHeader(request, 1610, false);

        request.setBody(new T1610());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        //佛山第五人民医院
        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        }

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(
                    cardInfo.getCardType() + "");
        }

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (context != null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, false);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<List<G1610>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 获取处方单及处方明细【1611】
     *
     * @param cardInfo    诊疗卡信息
     * @param patientInfo 门诊患者信息
     * @return
     * @throws Exception
     */
    public static G1611 getRecipeListAsync(T_Phr_CardBindRec cardInfo, G1011 patientInfo) throws Exception {
        Request<T1611> request = new Request<>();
        Request.commonHeader(request, 1611, true);

        request.setBody(new T1611());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        if (patientInfo != null) {
            request.getBody().setPatientID(patientInfo.getPatientID());
        }
        request.getBody().setRegID("");
        if (cardInfo != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(cardInfo.getCardType() + "");
        }
        String date = CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, new Date());
        request.getBody().setStartDate(date);
        request.getBody().setEndDate(date);

        JSONObject bodyData = WebUtil.httpExecute(request, false);
        G1611 result = new G1611();
        result.setRecipeList(new ArrayList<RecipeList>());
        if (bodyData != null) {
            if (!bodyData.isNull("TotalFee")) {
                result.setTotalFee(bodyData.getString("TotalFee"));
            }
            if (!bodyData.isNull("Items")) {
                JSONObject itemsJSONObj = bodyData.getJSONObject("Items");
                Object recipeDataList = itemsJSONObj.get("RecipeList");
                JSONArray recipeListJSONArray;
                if (recipeDataList instanceof JSONObject) {//如果是单条数据，需要转为JSONArray,方便统一遍历
                    recipeListJSONArray = new JSONArray();
                    JSONObject emptyObject = new JSONObject();
                    emptyObject.put("RecipeList", "{}");
                    recipeListJSONArray.put(emptyObject);
                    recipeListJSONArray.put(recipeDataList);
                } else {//如果有多条数据
                    //recipeDataList instanceof JSONArray
                    recipeListJSONArray = (JSONArray) recipeDataList;
                }
                for (int recipeIndex = 0; recipeIndex < recipeListJSONArray.length(); recipeIndex++) {
                    JSONObject recipeListJSON = (JSONObject) recipeListJSONArray.get(recipeIndex);
                    RecipeList recipeList = new RecipeList();

                    if (!recipeListJSON.isNull("RecipeInfo")) {//过滤空数据
                        //解析<RecipeInfo>
                        RecipeInfo recipeInfo = JsonUtil.toObject(recipeListJSON.getJSONObject("RecipeInfo"), RecipeInfo.class);
                        //解析<RecipeDetailList>
                        JSONObject recipeDetailData = recipeListJSON.getJSONObject("RecipeDetailList");
                        List<RecipeDetail> recipeDetails = JsonUtil.toListObject(recipeDetailData, "RecipeDetail", RecipeDetail.class,
                                new TypeToken<List<RecipeDetail>>() {
                                }.getType());
                        recipeList.setRecipeInfo(recipeInfo);
                        recipeList.setRecipeDetailList(recipeDetails);
                        result.getRecipeList().add(recipeList);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取医生挂号类别【1210,1410】
     *
     * @param context
     * @param taskTag
     * @param isTypeRegist
     * @param callback
     */
    public static void loadDoctorsCategoryAsync(Context context, String taskTag, View loadingView,
                                                boolean isTypeRegist,
                                                RequestCallback<List<G1210>> callback) {
        String registMsg = "正在获取挂号类别...";
        String appoitMsg = "正在获取预约类别...";
        String msg = isTypeRegist ? registMsg : appoitMsg;
        final int funCode = isTypeRegist ? 1210 : 1410;

        Request<T1210> request = new Request<>();
        Request.commonHeader(request, funCode, false);

        request.setBody(new T1210());
        request.getBody().setDeptID(null);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        builder.setLoadingMessage(msg)
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<List<G1210>>() {
                }).execute(taskTag, callback);
    }

    /**
     * 执行锁号【1213】
     *
     * @param context
     * @param taskTag
     * @param typeID
     * @param dct
     * @param dept
     * @param callback
     */
    public static void lockRegistRegResourceAsync(Context context, String taskTag, Dialog dialog,
                                                  String typeID, G1417 dct, G1211 dept, G1011 patientInfo,
                                                  RequestCallback<G1213> callback) {
        if (!checkPatientInfo(patientInfo, GlobalSettings.INSTANCE.getCurrentFamilyAccount())) {
            showCardInfoDeprecatedWarningDlg(context, null);
            callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
            return;
        }
        Request<T1213> request = new Request<>();
        Request.commonHeader(request, 1213, false);

        request.setBody(new T1213());
        request.getBody().setTypeID(typeID);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setRankID("");
        request.getBody().setPatientID(patientInfo != null ? patientInfo.getPatientID() : "");
        request.getBody().setDate(CommonUtils.phareDateFormat("yyyyMMdd", new Date()));
        if (dept != null) {
            request.getBody().setDeptID(dept.getDeptID());
        } else {
            request.getBody().setDeptID("");
        }
        if (dct != null) {
            request.getBody().setDoctID(dct.getDoctID() == null ? "" : dct.getDoctID());
            request.getBody().setRegSourceID(dct.getRegSourceID());
            request.getBody().setTimeRegion(dct.getTimeRegion());
        } else {
            request.getBody().setTimeRegion("");
            request.getBody().setDoctID("");
            request.getBody().setRegSourceID("");
        }
        request.getBody().setNote("");

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        builder.setCustomLoadingDlg(dialog)
                .mappingInto(new TypeToken<GResponse<G1213>>() {
                }).execute(taskTag, callback);

    }

    /**
     * 处方单缴费【1612】
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param cardInfo
     * @param totalFee
     * @param patientInfo
     * @param regIds
     * @param callback
     */
    public static void doPayTreatmentOrdersAsync(Context context, String taskTag, View loadingView,
                                                 T_Phr_CardBindRec cardInfo, double totalFee, G1011 patientInfo,
                                                 String regIds,
                                                 RequestCallback<G1612> callback) {
        if (!checkPatientInfo(patientInfo, GlobalSettings.INSTANCE.getCurrentFamilyAccount())) {
            showCardInfoDeprecatedWarningDlg(context, null);
            callback.onRequestError(new RequestError(new Exception("您的诊疗卡不存在或已被注销，请重新绑卡！")));
            return;
        }

        Request<T1612> request = new Request<>();
        Request.commonHeader(request, 1612, false);

        request.setBody(new T1612());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setCardType(cardInfo.getCardType() + "");
        request.getBody().setTerminalNo(WebUtil.TERMINAL_NO());
        request.getBody().setTerTranSerNo(CommonUtils.phareDateFormat("yyyyMMddHHmm", new Date()));
        request.getBody().setPatientID(patientInfo.getPatientID());
        //中心医院开始，使用ReciepeID
//        request.getBody().setRegID(regIds);
        request.getBody().setRegID(regIds);
        request.getBody().setRecipeID(regIds);
        request.getBody().setPayMode(BankUtil.PayMode_Card);
        request.getBody().setPersonalFee("");
        request.getBody().setPreChargeFee("");
        request.getBody().setMedInsureFee("");
        request.getBody().setMedInsureStr("");
        request.getBody().setTotalFee(totalFee + "");
        request.getBody().setPayInfo(BankUtil.initPayInfo(GlobalSettings.INSTANCE.getCurrentUser(), totalFee));

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.msg_dialog_commit))
                    .showLoadingDlg(context, false);
        } else {
            builder.setLoadingView(loadingView);
        }
        builder.mappingInto(new TypeToken<GResponse<G1612>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 加载门诊费用记录【1910】
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param cardInfo    诊疗卡信息
     * @param dateStart   开始时间
     * @param dateEnd     结束时间
     * @param callback    接口回调
     */
    public static void loadPayRecordsAsync(Context context, String taskTag, View loadingView,
                                           T_Phr_CardBindRec cardInfo, String dateStart, String dateEnd,
                                           RequestCallback<List<G1910>> callback) {
        Request<T1610> request = new Request<>();
        Request.commonHeader(request, 1910, false);

        request.setBody(new T1610());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setPatientID(cardInfo.getPatientID());
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(
                    cardInfo.getCardType() + "");
        }
        request.getBody().setStartDate(dateStart);
        request.getBody().setEndDate(dateEnd);

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<List<G1910>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 费用记录详细【1911】
     *
     * @param context     所属页面context
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param payRecord   费用记录信息
     * @param recipeInfo  处方单信息
     * @param callback    接口回调
     */
    public static void loadPayRecordDetailsAsync(Context context, String taskTag, View loadingView,
                                                 G1910 payRecord, G1615 recipeInfo,
                                                 RequestCallback<List<G1911>> callback) {
        Request<T1911> request = new Request<>();
        Request.commonHeader(request, 1911, false);

        request.setBody(new T1911());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        if (payRecord != null) {
            request.getBody().setItemID(payRecord.getItemID());
        } else {
            request.getBody().setItemID(recipeInfo.getRecipeNo());
        }

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<List<G1911>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 获取查询的价格信息数据【2011】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param type        查询的类型(药品/非药品)
     * @param pyinCode    拼音码
     * @param pageNum     当前页号
     * @param returnNum   返回页号
     * @param callback    接口回调
     */
    public static void getPriceQueryListAsync(String taskTag, View loadingView,
                                              int type, String pyinCode, int pageNum, int returnNum,
                                              RequestCallback<List<G2011>> callback) {
        Request<T2011> request = new Request<>();
        Request.commonHeader(request, 2011, false);

        request.setBody(new T2011());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
        request.getBody().setPinYinCode(pyinCode);
        request.getBody().setType(String.valueOf(type));
        request.getBody().setPageNum(String.valueOf(pageNum));
        request.getBody().setReturnNum(String.valueOf(returnNum));

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<List<G2011>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 加载住院费用列表
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载view
     * @param cardInfo    当前用户的卡信息
     * @param dateStart   开始时间
     * @param dateEnd     结束时间
     * @param callback    接口回调
     */
    public static void loadHospitalizationFeeListAsync(String taskTag, View loadingView,
                                                       T_Phr_CardBindRec cardInfo, String dateStart, String dateEnd,
                                                       RequestCallback<G2210> callback) {
        Request<T2210> request = new Request<>();
        Request.commonHeader(request, 2210, false);

        request.setBody(new T2210());
        request.getBody().setCardNo(cardInfo.getCardNo());
        request.getBody().setCardType(String.valueOf(cardInfo.getCardType()));
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setStartDate(dateStart);
        request.getBody().setEndDate(dateEnd);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<GResponse<G2210>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 获取检查报告简要列表【2510】
     *
     * @param taskTag     任务TAG
     * @param loadingView 加载控件
     * @param cardInfo    绑定卡信息
     * @param dateStart   开始日期
     * @param dateEnd     结束日期
     * @param callback    接口回调
     */
    public static void getCheckReportAsync(String taskTag, View loadingView,
                                           ExT_Phr_CardBindRec cardInfo, String dateStart, String dateEnd,
                                           RequestCallback<List<G2510>> callback) {
        Request<T2510> request = new Request<>();
        Request.commonHeader(request, 2510, false);

        request.setBody(new T2510());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(cardInfo.getCardType() + "");
            request.getBody().setPatientID(cardInfo.getPatientID());
        }
        request.getBody().setStartDate(dateStart);
        request.getBody().setEndDate(dateEnd);

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingView(loadingView)
                .mappingInto(new TypeToken<List<G2510>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 获取检查报告详细列表【2511】
     *
     * @param context  所属页面context
     * @param taskTag  任务TAG
     * @param repNo    处方ID
     * @param callback 接口回调
     */
    public static void getCheckReportDetailAsync(Context context, String taskTag, T_Phr_CardBindRec cardInfo, final String repNo, RequestCallback<List<G2511>> callback) {
        Request<T2511> request = new Request<>();
        Request.commonHeader(request, 2511, false);

        request.setBody(new T2511());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        if (cardInfo != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
        }
        request.getBody().setRepNo(repNo);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<List<G2511>>() {
                })
                .execute(taskTag, callback);
    }

    public static void getCheckReportDetailAsync2(Context context, String taskTag, T_Phr_CardBindRec cardInfo, final String repNo, RequestCallback<G2511> callback) {
        Request<T2511> request = new Request<>();
        Request.commonHeader(request, 2511, false);

        request.setBody(new T2511());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        if (cardInfo != null) {
            request.getBody().setCardNo(cardInfo.getCardNo());
        }
        request.getBody().setRepNo(repNo);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI()
                .setLoadingMessage(context.getString(R.string.dialog_content_loading))
                .showLoadingDlg(context, true)
                .mappingInto(new TypeToken<GResponse<G2511>>() {
                })
                .execute(taskTag, callback);
    }

    /**
     * 查询导诊信息[1615] for tangjy test
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param cardInfo
     * @param callback
     */
    public static void loadGuidePatientsAsync(Context context, String taskTag, View loadingView,
                                              T_Phr_CardBindRec cardInfo,
                                              RequestCallback<List<G1615>> callback) {
        Request<T1615> request = new Request<>();
        Request.commonHeader(request, 1615, false);

        request.setBody(new T1615());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            // request.getBody().setPatientID(cardInfo.getPatientID());
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(cardInfo.getCardType() + "");
        }

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<List<G1615>>() {
        }).execute(taskTag, callback);
    }

    /**
     * 查询校验报告单简要信息[2110] for tangjy test
     *
     * @param context
     * @param taskTag
     * @param loadingView
     * @param cardInfo
     * @param startDate
     * @param endtDate
     * @param callback
     */
    public static void loadAssayReportAsync(Context context, String taskTag, View loadingView,
                                            T_Phr_CardBindRec cardInfo, String startDate, String endtDate,
                                            RequestCallback<List<G2110>> callback) {
        Request<T2110> request = new Request<>();
        Request.commonHeader(request, 2110, false);

        request.setBody(new T2110());
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        if (cardInfo != null && cardInfo.getCardNo() != null) {
            request.getBody().setPatientID(cardInfo.getPatientID());
            request.getBody().setCardNo(cardInfo.getCardNo());
            request.getBody().setCardType(cardInfo.getCardType() + "");
        }

        request.getBody().setStartDate(startDate);
        request.getBody().setEndDate(endtDate);

        NetworkManager.INetworkManagerBuilder builder = GWINet.connect().createRequest().postGWI(null, generateBodyRequest(request)).fromGWI();
        if (loadingView == null) {
            builder.setLoadingMessage(context.getString(R.string.dialog_content_loading))
                    .showLoadingDlg(context, true);
        } else {
            builder.setLoadingView(loadingView);
        }

        builder.mappingInto(new TypeToken<List<G2110>>() {
        }).execute(taskTag, callback);
    }
    //---医院【end】---

    public static final String PARAM_FORM_KEY_PARTNER_ID = "partnerid";
    public static final String PARAM_FORM_KEY_CONTENT = "content";
    public static final String PARAM_FORM_KEY_SIGN = "sign";
    public static final String PARAM_FORM_KEY_TOKEN = "token";
    public static String SIGN_PART = "p6c7s4nanolegy5vczj94vdr9rros77t";
    public static String PATERNER_ID = "phr";

    public static NetBody generateBodyRequest(Object request) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new BaseRequest.DotNetDateAdapter()).setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create();

        String dataJson = gson.toJson(request);
        JSONObject body;
        JSONObject requestJsonObj = new JSONObject();
        try {
            body = new JSONObject(dataJson);
            requestJsonObj.put("Request", body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NetBody.Builder builder = new NetBody.Builder();
        builder.add(PARAM_FORM_KEY_PARTNER_ID, PATERNER_ID);
        builder.add(PARAM_FORM_KEY_CONTENT, requestJsonObj.toString());

        String token = GlobalSettings.INSTANCE.getToken();
        String sign;
        if (token != null) {
            builder.add(PARAM_FORM_KEY_TOKEN, token);
            //得到MD5校验码
            sign = MD5Util.string2MD5(requestJsonObj.toString() + token + SIGN_PART);
        } else {
            sign = MD5Util.string2MD5(requestJsonObj.toString() + SIGN_PART);
        }

        builder.add(PARAM_FORM_KEY_SIGN, sign);
        return builder.build();
    }
}
