package com.gwi.selplatform;

import android.test.InstrumentationTestCase;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINetConfig;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.BaseRequest;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.security.MD5Util;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.JsonUtil;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Base_DatumClass;
import com.gwi.selfplatform.db.gen.T_FeedBack_Rec;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.gwi.selfplatform.db.gen.T_Phone_AuthCode;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_SignRec;
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
import com.gwi.selfplatform.module.net.beans.PhrAddressDictQuery;
import com.gwi.selfplatform.module.net.beans.PhrSignRecQuery;
import com.gwi.selfplatform.module.net.beans.SymptomToDisease;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.Base_AddressDict;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5110;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5115;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.G5117;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GBase;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.HealthReport;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.request.T1014;
import com.gwi.selfplatform.module.net.response.G1013;
import com.gwi.selfplatform.module.net.response.G1014;
import com.gwi.selfplatform.module.net.response.G1910;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author 彭毅
 * @date 2015/4/23.
 */
public class NewWebServiceTest extends InstrumentationTestCase {

//    private static final String TAG = "execute";
//
//    private static final int MEDICAL_CARD = 1;
//
//    private T_Phr_BaseInfo mMainBaseInfo;
//    private T_UserInfo mMainUserInfo;
//
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//        initGWINet();
//
//        //        userInfo.setUserId(50681L);
////        userInfo.setUserCode("13455458845");
//
//        mMainBaseInfo = new T_Phr_BaseInfo();
//        mMainUserInfo = new T_UserInfo();
//        mMainUserInfo.setUserId(50699L);
//        mMainUserInfo.setUserName("彭毅");
////        mMainUserInfo.setUserCode("18674843603");
////        mMainUserInfo.setMobilePhone("18674843603");
//        mMainUserInfo.setUserCode("18874191990");
//        mMainUserInfo.setMobilePhone("18874191990");
//        mMainUserInfo.setUserPwd(MD5Util.string2MD5("123456"));
//        mMainUserInfo.setEhrId("63d5d52d-67a3-454f-b45c-93b835783de3");
//
//        mMainBaseInfo.setSelfPhone(mMainUserInfo.getMobilePhone());
//        mMainBaseInfo.setName(mMainUserInfo.getMobilePhone());
//        mMainBaseInfo.setIDCard("52032819730924772X");
//        mMainBaseInfo.setEhrID(mMainUserInfo.getEhrId());
//    }
//
//    private void initGWINet() {
//        GWINetConfig.Builder builder = new GWINetConfig.Builder();
//        GWINetConfig config = builder.setBaseUrl(BuildConfig.SERVICE_URL_DEBUG).setContext(getInstrumentation().getContext()).build();
//        GWINet.init(config);
//    }
//
//    public void testJSON() throws Exception{
//        JSONObject jsonObject = new JSONObject(json);
//        //删除json中Response节点
//        JSONObject responseObject = jsonObject.getJSONObject("Response");
//        if(!responseObject.isNull("Body")) {
//            if(responseObject.get("Body") instanceof JSONObject) {
//                JSONObject bodyObject = responseObject.getJSONObject("Body");
//                if(bodyObject.has("Items")) {
//                    JSONObject itemsObject = bodyObject.getJSONObject("Items");
//                    TypeToken<List<G1910>> token = new TypeToken<List<G1910>>(){};
//                    if (itemsObject.get("Item") instanceof JSONObject) {
//                        JSONArray array = new JSONArray();
//                        array.put(itemsObject.get("Item"));
//                        //JsonUtil.toListObject(itemsObject.getJSONObject("Item"),"Item",G1910.class,token.getType());
//                        List<G1910> result = new Gson().fromJson(array.toString(),token.getType());
//                        System.out.print(result);
//                    } else {
//                        System.out.print(JsonUtil.toListObject(itemsObject,"Item", G1910.class, token.getType()));
//                    }
//                }
//            }
//        }
//    }
//
//
//    //--- 用户管理接口 [start]---
//    /**
//     *
//     * @throws Exception
//     */
//    public void testLogin() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//
//            runTestOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Request<TBase> request = new Request<>();
//                    request.setHeader(new THeader());
//                    request.getHeader().setFunCode(5110);
//                    request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());
//
//                    request.setBody(new TBase());
//                    request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//                    request.getBody().setTerminalNo(WebServiceController.TERMINAL_NO);
//
//                    TypeToken<?> typeToken = new TypeToken<GResponse<G5110>>() {
//                    };
//                    GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request))
//                            .setLoadingMessage("测试").fromGWI().mappingInto(typeToken)
//                            .execute("login", new RequestCallback() {
//                                @Override
//                                public void onRequestSuccess(Object o) {
//                                    assertNotNull(o);
//                                    signal.countDown();
//                                }
//
//                                @Override
//                                public void onRequestError(RequestError error) {
//                                    commonException(error);
//                                    signal.countDown();
//                                }
//                            });
//                }
//            });
//
//        commonWait(signal);
//    }
//
//    public void testLoadHospitalParamsAsnc() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//
//        runTestOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Request<T1014> request = new Request<T1014>();
//                request.setHeader(new THeader());
//                request.setBody(new T1014());
//                request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());
//                request.getHeader().setAppTypeCode(WebUtil.APP_TYPE_CODE);
//                request.getHeader().setFunCode(1014);
//                request.getHeader().setReqTime(
//                        CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, new Date()));
//                request.getBody().setHospCode(WebUtil.HOSP_CODE);
//                request.getBody().setParameterCode("");
//                request.getBody().setTerminalNo("fd");
//
//                TypeToken typeToken = new TypeToken<List<G1014>>() {
//                };
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request))
//                        .setLoadingMessage("测试").fromGWI().mappingInto(typeToken)
//                        .execute("login", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                assertNotNull(o);
//                                List<G1014> result = (List<G1014>) o;
//                                Logger.d("test", "Size is " + result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                               commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//
//       commonWait(signal);
//    }
//
//    public void testDateGSON() {
//        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,new BaseRequest.DotNetDateAdapter()).create();
//        Date d = new Date();
//        gson.toJson(d);
//    }
//
//    public void testRegister() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//
//        runTestOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                request.setHeader(new THeader());
//                request.getHeader().setFunCode(5111);
//                request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());
//                request.getHeader().setAppTypeCode(WebUtil.APP_TYPE_CODE);
//                request.getHeader().setReqTime(CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date()));
//                request.getHeader().setTerminalNo(WebServiceController.TERMINAL_NO);
//
//                TBase body = new TBase();
//                T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
//                T_UserInfo userInfo = new T_UserInfo();
//
//                userInfo.setNickName("测试人员B");
//                userInfo.setMobilePhone("18674843605");
//                userInfo.setRecordDate(new Date());
//                userInfo.setUserName("大豆子");
//                userInfo.setUserCode(userInfo.getMobilePhone());
//                userInfo.setUserPwd(MD5Util.string2MD5("123456"));
//
//                //13544212362,140902196502246407
//                //13544212363,140401197910022278
//                baseInfo.setIDCard("610602195708294836");
//                try {
//                    baseInfo.setBirthDay(CommonUtils.getDateFromIDCard(baseInfo.getIDCard()));
//                    baseInfo.setSex(CommonUtils.getSexFromIdCard(baseInfo.getIDCard()));
//                } catch (Exception e) {
//                    System.err.println(e.getLocalizedMessage());
//                    baseInfo.setBirthDay(new Date());
//                    baseInfo.setSex(1);
//                }
//                baseInfo.setSelfPhone(userInfo.getMobilePhone());
//
//                body.setPhr_BaseInfo(baseInfo);
//                body.setUserInfo(userInfo);
//                request.setBody(body);
//
//                TypeToken<T_Phr_BaseInfo> resultTypeToken = new TypeToken<T_Phr_BaseInfo>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(resultTypeToken)
//                        .execute("register", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                assertNotNull(o);
//                                T_Phr_BaseInfo baseInfo1 = (T_Phr_BaseInfo) o;
//                                Logger.d(TAG,"Result is "+baseInfo1.getName());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//
//        commonWait(signal);
//    }
//
//    public void testPasswordModify() throws Throwable{
//        final CountDownLatch signal = new CountDownLatch(1);
//
//        runTestOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request,5112);
//
//                TBase body = new TBase();
//                T_UserInfo userInfo = new T_UserInfo();
//
//                //Old pwd.
//                body.setAccount(mMainUserInfo.getUserCode());
//                body.setAccountPassword(mMainUserInfo.getUserPwd());
//
//                //13544212361,13544212362
//                userInfo.setUserCode(mMainUserInfo.getUserCode());
//                //New pwd.
//                userInfo.setUserPwd(mMainUserInfo.getUserPwd());
//
//                body.setUserInfo(userInfo);
//                request.setBody(body);
//
//                TypeToken<GBase> resultTypeToken = new TypeToken<GBase>() {};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(resultTypeToken)
//                        .execute("modifyPassword", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                assertNotNull(o);
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//
//        commonWait(signal);
//    }
//
//    public void testQueryFamilyMember() throws Throwable{
//        final CountDownLatch signal = new CountDownLatch(1);
//
//        runTestOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request,5113);
//
//                TBase body = new TBase();
//                T_UserInfo userInfo = new T_UserInfo();
//
//                userInfo.setUserId(mMainUserInfo.getUserId());
//
//                body.setUserInfo(userInfo);
//                request.setBody(body);
//                commonUserValidation(request);
//
//                TypeToken<GResponse<List<T_Phr_BaseInfo>>> resultTypeToken = new TypeToken<GResponse<List<T_Phr_BaseInfo>>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(resultTypeToken)
//                        .execute("testQueryFamilyMember", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_BaseInfo> result = (List<T_Phr_BaseInfo>) o;
//                                Logger.d(TAG,result.get(0).getName());
//                                assertNotNull(o);
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testModifyPhone() throws Throwable {
//        //主要是修改手机号码。
//        final CountDownLatch signal = new CountDownLatch(1);
//
//        runTestOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request, 5114);
//
//                final String mobilePhone = "18874191990";
////                final String mobilePhone = "18674843603";
//
//                TBase body = new TBase();
//                T_UserInfo userInfo = new T_UserInfo();
//                userInfo.setUserId(50699L);
//                userInfo.setUserCode("18674843603");
////                userInfo.setUserCode("18874191990");
//                userInfo.setUserName("甘旺");
//                userInfo.setMobilePhone(mobilePhone);
//                userInfo.setUserPwd(MD5Util.string2MD5("123456"));
//
//                T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
//                baseInfo.setSelfPhone(mobilePhone);
//                //140401197910022279
//                baseInfo.setIDCard("431124199006154210");
//                baseInfo.setName(userInfo.getUserName());
//
//                body.setUserInfo(userInfo);
//                body.setPhr_BaseInfo(baseInfo);
//                request.setBody(body);
//                commonUserValidation(request);
//
//                TypeToken<GResponse<T_UserInfo>> resultTypeToken = new TypeToken<GResponse<T_UserInfo>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(resultTypeToken)
//                        .execute("modifyPhone", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                T_UserInfo result = (T_UserInfo) o;
//                                Logger.d(TAG,result.getMobilePhone());
//                                assertEquals(mobilePhone, result.getMobilePhone());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//
//        commonWait(signal);
//    }
//
//    //TODO:获取验证码需要测试
//    public void testPasswordFind()throws Throwable{
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request, 5115);
//
//                request.setBody(new TBase());
//                T_UserInfo userInfo = new T_UserInfo();
//                userInfo.setUserId(mMainUserInfo.getUserId());
//                userInfo.setUserCode(mMainUserInfo.getMobilePhone());
//                userInfo.setUserName(mMainUserInfo.getUserName());
//                userInfo.setMobilePhone(mMainUserInfo.getMobilePhone());
//                request.getBody().setUserInfo(userInfo);
//
//                TypeToken<GResponse<G5115>> typeToken = new TypeToken<GResponse<G5115>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute("5115", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                G5115 g5115 = (G5115) o;
//                                Logger.d(TAG,"Auth code is "+g5115.getPhoneAuth().getAuthCode());
//                                assertNotNull(g5115.getPhoneAuth());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testSetNewPassword() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request, 5116);
//
//                request.setBody(new TBase());
//                T_UserInfo userInfo = new T_UserInfo();
//                userInfo.setUserId(50681L);
//                userInfo.setUserPwd(MD5Util.string2MD5("123456"));
//                request.getBody().setUserInfo(userInfo);
//
//                TypeToken<GResponse<GBase>> typeToken = new TypeToken<GResponse<GBase>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute("5116", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                GBase g5115 = (GBase) o;
//                                Logger.d(TAG, "New password is " + g5115.getUserInfo().getUserPwd());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testSetValidatePhoneRegistered() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5117;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                T_UserInfo userInfo = new T_UserInfo();
//                userInfo.setMobilePhone("13455458845");
//                request.getBody().setUserInfo(userInfo);
//
//                TypeToken<GResponse<G5117>> typeToken = new TypeToken<GResponse<G5117>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                G5117 g5117 = (G5117) o;
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testAddFamilyMember() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5118;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                T_UserInfo userInfo = new T_UserInfo();
//                //50681L,13455458845
//                userInfo.setUserId(mMainUserInfo.getUserId());
//                commonUserValidation(request);
//
//                T_Phr_BaseInfo newBaseInfo = new T_Phr_BaseInfo();
//                newBaseInfo.setUserId(mMainUserInfo.getUserId());
//                newBaseInfo.setName("巫锐进2");
//                newBaseInfo.setSelfPhone("18945455544");
//                newBaseInfo.setIDCard("350322197805146872");
//                try {
//                    newBaseInfo.setBirthDay(CommonUtils.getDateFromIDCard(newBaseInfo.getIDCard()));
//                    newBaseInfo.setSex(CommonUtils.getSexFromIdCard(newBaseInfo.getIDCard()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                request.getBody().setUserInfo(userInfo);
//                request.getBody().setPhr_BaseInfo(newBaseInfo);
//
//
//                TypeToken<GResponse<List<T_Phr_BaseInfo>>> typeToken = new TypeToken<GResponse<List<T_Phr_BaseInfo>>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_BaseInfo> g5117 = (List<T_Phr_BaseInfo>) o;
//                                Logger.d(TAG, "Added a family: " + g5117.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testDeleteFamilyMember() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5119;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                T_UserInfo userInfo = new T_UserInfo();
//                userInfo.setUserId(mMainUserInfo.getUserId());
//                commonUserValidation(request);
//
//                T_Phr_BaseInfo newBaseInfo = new T_Phr_BaseInfo();
//                newBaseInfo.setUserId(mMainUserInfo.getUserId());
//                //TODO:
//                //主档案:2e99753c-1b57-46b7-81bc-2f23b0b23b83
//                newBaseInfo.setEhrID("a759ee23-a9b8-4d85-9400-fdbbf0676fb3");
//                request.getBody().setUserInfo(userInfo);
//                request.getBody().setPhr_BaseInfo(newBaseInfo);
//
//                TypeToken<GResponse<List<T_Phr_BaseInfo>>> typeToken = new TypeToken<GResponse<List<T_Phr_BaseInfo>>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_BaseInfo> g5117 = (List<T_Phr_BaseInfo>) o;
//                                Logger.d(TAG, "size ater delted: " + g5117.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testModifyFamilyMember() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5120;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                T_UserInfo userInfo = new T_UserInfo();
//                userInfo.setUserId(mMainUserInfo.getUserId());
//                commonUserValidation(request);
//
//                T_Phr_BaseInfo newBaseInfo = new T_Phr_BaseInfo();
//                newBaseInfo.setUserId(mMainUserInfo.getUserId());
//                newBaseInfo.setEhrID("20d43182-765c-4a17-899c-e547a105ee9f");
//                newBaseInfo.setSelfPhone("18745452234");
//                newBaseInfo.setName("大空翼2");
//                newBaseInfo.setIDCard("530521195102103445");
//                try {
//                    newBaseInfo.setBirthDay(CommonUtils.getDateFromIDCard(newBaseInfo.getIDCard()));
//                    newBaseInfo.setSex(CommonUtils.getSexFromIdCard(newBaseInfo.getIDCard()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                request.getBody().setUserInfo(userInfo);
//                request.getBody().setPhr_BaseInfo(newBaseInfo);
//
//
//                TypeToken<GResponse<List<T_Phr_BaseInfo>>> typeToken = new TypeToken<GResponse<List<T_Phr_BaseInfo>>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_BaseInfo> result = (List<T_Phr_BaseInfo>) o;
//                                Logger.d(TAG, "Modify family member: " + result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//    //--- 用户管理接口 [end]---
//
//    //--- 体征测量 [start]---
//    public void testUploadSignRec() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request, 5210);
//
//                final String _EHR_ID = mMainUserInfo.getEhrId();
//
//                try {
//                    request.setBody(new TBase());
//                    request.getBody().setPhr_SignRecs(generateAGroupSignRecs(_EHR_ID));
//                    commonUserValidation(request);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                TypeToken<List<T_Phr_SignRec>> typeToken = new TypeToken<List<T_Phr_SignRec>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute("5210", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_SignRec> result = (List<T_Phr_SignRec>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG, "Size is " + result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testModifySignRecs()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request, 5211);
//
//                final String _EHR_ID = mMainUserInfo.getEhrId();
//                final String _Group_ID = "2c40b72b-679b-4411-b6e0-ad1e9c369fd4";
//                //43531
//
//                request.setBody(new TBase());
//
//                T_Phr_SignRec rec = new T_Phr_SignRec();
//                rec.setRecNo(43543l);
//                rec.setEhrID(_EHR_ID);
//                rec.setGroupId(_Group_ID);
//                rec.setSignCode(1);
//                rec.setSignValue("120");
//                request.getBody().setPhr_SignRecs(new ArrayList<T_Phr_SignRec>());
//                request.getBody().getPhr_SignRecs().add(rec);
//                commonUserValidation(request);
//
//                TypeToken<List<T_Phr_SignRec>> typeToken = new TypeToken<List<T_Phr_SignRec>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute("5211", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_SignRec> result = (List<T_Phr_SignRec>) o;
//                                Logger.d(TAG, "Sign rec modifed: " + result.size());
//                                assertNotNull(result);
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testQuerySignRecs() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                commonHeader(request, 5212);
//
//                final String _EHR_ID = mMainUserInfo.getEhrId();
//
//                request.setBody(new TBase());
//                commonUserValidation(request);
//                PhrSignRecQuery query = new PhrSignRecQuery();
//                query.setEhrID(_EHR_ID);
//                query.setSignCode(1);
//                try {
//                    query.setStartTime(CommonUtils.stringPhaseDate("2015-05-01", Constants.FORMAT_ISO_DATE));
//                    query.setEndTime(CommonUtils.stringPhaseDate("2015-05-26", Constants.FORMAT_ISO_DATE));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                request.getBody().setSignRecQuery(query);
//
//                TypeToken<List<T_Phr_SignRec>> typeToken = new TypeToken<List<T_Phr_SignRec>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute("5212", new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_SignRec> recs = (List<T_Phr_SignRec>) o;
//                                assertNotNull(recs.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testDeleteSignRec()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                Request<TBase> request = new Request<>();
//                final int FUN_CODE = 5213;
//                commonHeader(request, FUN_CODE);
//
//                final String _EHR_ID = mMainUserInfo.getEhrId();
//
//                request.setBody(new TBase());
//
//                request.getBody().setPhr_SignRecs(new ArrayList<T_Phr_SignRec>());
//
//                T_Phr_SignRec recs1 = new T_Phr_SignRec();
//                recs1.setRecNo(43538L);
//                        recs1.setEhrID("63d5d52d-67a3-454f-b45c-93b835783de3");
//                recs1.setGroupId("2c40b72b-679b-4411-b6e0-ad1e9c369fd4");
//
////                T_Phr_SignRec rec2 = new T_Phr_SignRec();
////                rec2.setRecNo(43516L);
////                rec2.setEhrID("99e17e31-8c65-43ad-b1b1-4fc4ff57f1fc");
////                rec2.setGroupId("dc5574a0-9331-4a30-afdf-61466b135e1a");
//
//                request.getBody().getPhr_SignRecs().add(recs1);
////                request.getBody().getPhr_SignRecs().add(rec2);
//                commonUserValidation(request);
//
//                TypeToken<GResponse<Void>> typeToken = new TypeToken<GResponse<Void>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_SignRec> result = (List<T_Phr_SignRec>) o;
//                                Logger.d(TAG, "result is " + result.size());
//                                assertNotNull(result.size());
////                                assertEquals(2,g5210.getPhr_SignRecs().size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                               commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    final static String DATE = "2015-5-21 12:00:00";
//
//    private List<T_Phr_SignRec> generateAGroupSignRecs(String ehrId)throws Exception{
//
//        String groupID = UUID.randomUUID().toString();
//
//        Random random = new Random();
//
//
//        List<T_Phr_SignRec> groupSignRecs = new ArrayList<>();
//        T_Phr_SignRec systolicSignRec = new T_Phr_SignRec();
//        systolicSignRec.setEhrID(ehrId);
//        systolicSignRec.setGroupId(groupID);
//        systolicSignRec.setSignCode(1);
//        systolicSignRec.setSignValue(String.valueOf(random.nextInt(140)));
//        systolicSignRec.setRecordDate(CommonUtils.stringPhaseDate(DATE, Constants.FORMAT_ISO_DATE_TIME));
//        groupSignRecs.add(systolicSignRec);
//
//        T_Phr_SignRec diastolicSignRec = new T_Phr_SignRec();
//        diastolicSignRec.setEhrID(ehrId);
//        diastolicSignRec.setGroupId(groupID);
//        diastolicSignRec.setSignCode(2);
//        diastolicSignRec.setSignValue(String.valueOf(random.nextInt(140)));
//        diastolicSignRec.setRecordDate(CommonUtils.stringPhaseDate(DATE, Constants.FORMAT_ISO_DATE_TIME));
//        groupSignRecs.add(diastolicSignRec);
//
//        return groupSignRecs;
//    }
//    //--- 体征测量 [end]---
//
//    //--- 绑定卡接口 [start] ---
//    public void testQueryCardInfo()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5310;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//
//                T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
//                baseInfo.setUserId(mMainUserInfo.getUserId());
//                baseInfo.setEhrID(mMainUserInfo.getEhrId());
//                request.getBody().setPhr_BaseInfo(baseInfo);
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                ExT_Phr_CardBindRec carbindRec = new ExT_Phr_CardBindRec();
//                final int MEDICAL_CARD = 1;
//                carbindRec.setCardType(MEDICAL_CARD);
//                request.getBody().setPhr_CardBindRec(carbindRec);
//                commonUserValidation(request);
//
//                TypeToken<List<T_Phr_CardBindRec>> typeToken = new TypeToken<List<T_Phr_CardBindRec>>(){};
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_CardBindRec> result = (List<T_Phr_CardBindRec>) o;
//                                assertNotNull(result.size());
//                                Logger.d(TAG, "The card bind size is " + result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG,"error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testAddCardBindInfo()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5311;
//                final Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                commonUserValidation(request);
//
////                T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
////                baseInfo.setUserId(50681L);
////                baseInfo.setEhrID("99e17e31-8c65-43ad-b1b1-4fc4ff57f1fc");
////                request.getBody().setPhr_BaseInfo(baseInfo);
//
//                ExT_Phr_CardBindRec carbindRec = new ExT_Phr_CardBindRec();
//                final int MEDICAL_CARD = 1;
//                carbindRec.setCardType(MEDICAL_CARD);
//                carbindRec.setHospitalCode(WebUtil.HOSP_CODE);
//                carbindRec.setEhrId("63d5d52d-67a3-454f-b45c-93b835783de3");
//                carbindRec.setCardNo("000000078131");
//                carbindRec.setBindMan("UITest");
//                request.getBody().setPhr_CardBindRec(carbindRec);
//
//                TypeToken<List<T_Phr_CardBindRec>> typeToken = new TypeToken<List<T_Phr_CardBindRec>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Phr_CardBindRec> result= (List<T_Phr_CardBindRec>) o;
//                                assertNotNull(result.size());
//                                Logger.d(TAG, "result is " + result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testDeleteCardBindInfo()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5312;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                commonUserValidation(request);
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
//                baseInfo.setUserId(mMainUserInfo.getUserId());
//                baseInfo.setEhrID(mMainBaseInfo.getEhrID());
//                request.getBody().setPhr_BaseInfo(baseInfo);
//
//                ExT_Phr_CardBindRec carbindRec = new ExT_Phr_CardBindRec();
//                carbindRec.setRecNo(50542L);
//                carbindRec.setCardType(MEDICAL_CARD);
//                carbindRec.setCardNo("000000078131");
//                carbindRec.setEhrId(mMainUserInfo.getEhrId());
//                request.getBody().setPhr_CardBindRec(carbindRec);
//
//                //TODO:如果是Items->Item,改为TypeToken<List<T_Phr_CardBindRec>>
//                TypeToken<GResponse<List<T_Phr_CardBindRec>>> typeToken = new TypeToken<GResponse<List<T_Phr_CardBindRec>>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    //--- 绑定卡接口 [end] ---
//
//    //--- 查询接口 [start] ---
//    public void testGetHealthCategory() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5414;
//                final Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                TypeToken<List<T_Base_DatumClass>> typeToken = new TypeToken<List<T_Base_DatumClass>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<T_Base_DatumClass> result = (List<T_Base_DatumClass>) o;
//                                assertNotNull(result.size());
//                                Logger.d(TAG,"The result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testQueryHealthEduList()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5410;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//                T_HealthEdu_Datum healthEduTab = new T_HealthEdu_Datum();
//                //TODO:1,2,3...
//                healthEduTab.setDatumClass(1);
//
//                request.getBody().setHealthEdu_Datum(healthEduTab);
//
//                TypeToken<GResponse<Void>> typeToken = new TypeToken<GResponse<Void>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    //TODO:not past.
//    public void testSendValidationCode()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5411;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                T_Phone_AuthCode authCode = new T_Phone_AuthCode();
//                authCode.setPhoneNumber("18674843603");
//                request.getBody().setPhone_AuthCode(authCode);
//
//                TypeToken<GResponse<T_Phone_AuthCode>> typeToken = new TypeToken<GResponse<T_Phone_AuthCode>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                T_Phone_AuthCode code = (T_Phone_AuthCode) o;
//                                assertNotNull(code.getAuthCode());
//                                Logger.d(TAG, "result is " + code.getAuthCode());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testCheckAppVersion()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5412;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//                request.getBody().setAppCode(GlobalSettings.INSTANCE.getAppCode());
//
//                TypeToken<GResponse<MobileVerParam>> typeToken = new TypeToken<GResponse<MobileVerParam>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                MobileVerParam result = (MobileVerParam) o;
//                                assertNotNull(result);
//                                Logger.d(TAG, "result is " + result.getFileName());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testGetHealthReport()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5413;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//                commonUserValidation(request);
//
//                T_Phr_BaseInfo baseInfo = new T_Phr_BaseInfo();
//                baseInfo.setEhrID(mMainBaseInfo.getEhrID());
//
//                request.getBody().setPhr_BaseInfo(baseInfo);
//
//                TypeToken<GResponse<HealthReport>> typeToken = new TypeToken<GResponse<HealthReport>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                HealthReport result = (HealthReport) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.getScore());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testAddressDict()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5415;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//                commonUserValidation(request);
//
//
//                final String TYPE_PROVINCE = "1";
//                final String TYPE_CITY = "2";
//                final String TYPE_TOWN = "3";
//                //查询省
//                PhrAddressDictQuery provinceQuery = new PhrAddressDictQuery();
//                provinceQuery.setAddressType(TYPE_PROVINCE);
////                request.getBody().setAddressDictQuery(provinceQuery);
//
//                //查询市
//                PhrAddressDictQuery cityQuery = new PhrAddressDictQuery();
//                cityQuery.setAddressType(TYPE_CITY);
//                //TODO:
//                String proviceHunan = "43";
//                cityQuery.setAddressCode(proviceHunan);
////                request.getBody().setAddressDictQuery(cityQuery);
//
//                //查询县
//                PhrAddressDictQuery townQuery = new PhrAddressDictQuery();
//                townQuery.setAddressType(TYPE_TOWN);
//                String cityChangsha = "4301";
//                townQuery.setAddressCode(cityChangsha);
//                request.getBody().setAddressDictQuery(townQuery);
//
//                TypeToken<List<Base_AddressDict>> typeToken = new TypeToken<List<Base_AddressDict>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<Base_AddressDict> result = (List<Base_AddressDict>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testHospitalInfo() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5416;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                TypeToken<List<G1013.HospitalInfo>> typeToken = new TypeToken<List<G1013.HospitalInfo>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<G1013.HospitalInfo> result = (List<G1013.HospitalInfo>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testFeedBack()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5510;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                T_FeedBack_Rec feedback = new T_FeedBack_Rec();
//                feedback.setAdvice("我正在测试接口");
//                int fromMobile = 1;
//                feedback.setSource(fromMobile);
//                feedback.setPhoneNumber(mMainUserInfo.getMobilePhone());
//                //feedback.setType(FeedBackActivity.TYPE_SUGESSTION);
//                request.getBody().setFeedBack_Rec(feedback);
//
//                TypeToken<Void> typeToken = new TypeToken<Void>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                Logger.d(TAG,"result is ");
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//    //--- 查询接口 [end] ---
//
//    //--- 健康百科 [start] ---
//    public void testCommonDiseaseList()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5610;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String searchMsg = "头痛";
//                request.getBody().setSearchMsg(searchMsg);
//
//                TypeToken<List<KBDiseaseDetails>> typeToken = new TypeToken<List<KBDiseaseDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDiseaseDetails> result = (List<KBDiseaseDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testgetDiseaseList()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5611;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String bodyPartCode = "0";
//                String deptCode = "0108";
//                request.getBody().setBodyPartCode(bodyPartCode);
//                request.getBody().setDeptCode(deptCode);
//
//                TypeToken<List<KBDiseaseDetails>> typeToken = new TypeToken<List<KBDiseaseDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDiseaseDetails> result = (List<KBDiseaseDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testDiseaseDetailList()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5612;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //肥胖症
//                String diseaseId = "668";
//                request.getBody().setDiseaseId(diseaseId);
//
//                TypeToken<List<KBDiseaseDetails>> typeToken = new TypeToken<List<KBDiseaseDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDiseaseDetails> result = (List<KBDiseaseDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testDiseaseDeptList()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5613;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //0108
//                String deptCode = "";
//                request.getBody().setDeptCode(deptCode);
//
//                TypeToken<List<KBDepart>> typeToken = new TypeToken<List<KBDepart>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDepart> result = (List<KBDepart>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    //TODO:ResultMsg不正确：ResultMsg":"查询科室成功"
//    public void testGetBodyParts()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5614;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //7
//                String bodyPartCode = "7";
//                request.getBody().setBodyPartCode(bodyPartCode);
//
//                TypeToken<List<KBBodyPart>> typeToken = new TypeToken<List<KBBodyPart>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBBodyPart> result = (List<KBBodyPart>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * No need to test for deprecated.
//     * @throws Throwable
//     */
//    @Deprecated
//    public void testGetDrugCommon()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5615;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String useKindCode = "";
//                request.getBody().setUseKindCode(useKindCode);
//
//                TypeToken<List<KBDepart>> typeToken = new TypeToken<List<KBDepart>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDepart> result = (List<KBDepart>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testGetDrugResuce()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5616;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                TypeToken<List<KBDrugDetails>> typeToken = new TypeToken<List<KBDrugDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDrugDetails> result = (List<KBDrugDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 搜索药物，查询药品分类列表
//     * @throws Throwable
//     */
//    public void testGetDrugClassify()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5617;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String searchMsg = "小儿感冒颗粒";
//                String kindCode = "-1";
//                request.getBody().setSearchMsg(searchMsg);
//                request.getBody().setPropertyKindCode(kindCode);
//
//                TypeToken<List<KBDrugDetails>> typeToken = new TypeToken<List<KBDrugDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDrugDetails> result = (List<KBDrugDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    public void testGetDrugDetail()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5618;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //小儿感冒颗粒
//                String drugId = "111642";
//                request.getBody().setDrugId(drugId);
//
//                TypeToken<List<KBDrugDetails>> typeToken = new TypeToken<List<KBDrugDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDrugDetails> result = (List<KBDrugDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 药物用途
//     */
//    public void testGetDrugKindDicts()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5619;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //为空则查询所有药物用途；否则查询当前编码的药物用途信息
//                //感冒：2
//                String userKindCode = "2";
//                request.getBody().setUseKindCode(userKindCode);
//
//                TypeToken<List<KBDrugUseKind>> typeToken = new TypeToken<List<KBDrugUseKind>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDrugUseKind> result = (List<KBDrugUseKind>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     *
//     * @throws Throwable
//     */
//    //TODO:
//    public void testGetDrugProperty()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5620;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //为空则查询所有药物属性列表；为0则查询一级药物属性列表；否则查询其子节点药物属性列表
//                //0
//                //西药:166
//                //抗感染：167
//                String propertyKindCode = "168";
//                request.getBody().setPropertyKindCode(propertyKindCode);
//
//                TypeToken<List<KBDrugProperty>> typeToken = new TypeToken<List<KBDrugProperty>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBDrugProperty> result = (List<KBDrugProperty>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 获取化验知识分类
//     * @throws Throwable
//     */
//    public void testgetTreatmentKinds()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5621;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //为空则查询所有化验单分类列表；为0则查询一级化验单分类列表；否则查询其子节点化验单分类列表
//                String testKindCode = "01";
//                request.getBody().setTestKindCode(testKindCode);
//
//                TypeToken<List<KBTreatmentKind>> typeToken = new TypeToken<List<KBTreatmentKind>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBTreatmentKind> result = (List<KBTreatmentKind>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//
//    /**
//     *
//     * @throws Throwable
//     */
//    public void testGetTreatmentList()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5622;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //按化验知识名称模糊查询，用于搜索功能时TestKindCode值必须为“-1”
//                //0105
//                String testKindCode = "-1";
//                String searchMsg = "血葡萄糖";
//                request.getBody().setTestKindCode(testKindCode);
//                request.getBody().setSearchMsg(searchMsg);
//
//                TypeToken<List<KBTreatmentDetails>> typeToken = new TypeToken<List<KBTreatmentDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBTreatmentDetails> result = (List<KBTreatmentDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     *
//     * @throws Throwable
//     */
//    public void testGetTreatmentDetail()throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5623;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String testId = "1274";
//                request.getBody().setTestId(testId);
//
//                TypeToken<List<KBTreatmentDetails>> typeToken = new TypeToken<List<KBTreatmentDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBTreatmentDetails> result = (List<KBTreatmentDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 查询急救分类
//     * @throws Throwable
//     */
//    public void testGetTestKinds() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5624;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //为空则查询所有急救分类；否则查询当前编码的急救分类
//                String testTreatmentKind = "";
//                request.getBody().setTestKindCode(testTreatmentKind);
//
//                TypeToken<List<KBTestCheckKind>> typeToken = new TypeToken<List<KBTestCheckKind>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBTestCheckKind> result = (List<KBTestCheckKind>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 查询急救知识列表
//     * @throws Throwable
//     */
//    public void testGetTestList() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5625;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String testTreatmentKind = "1";
//                request.getBody().setTreatmentKindCode(testTreatmentKind);
//
//                TypeToken<List<KBTestCheckDetails>> typeToken = new TypeToken<List<KBTestCheckDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBTestCheckDetails> result = (List<KBTestCheckDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 查询急救知识详细
//     * @throws Throwable
//     */
//    public void testGetTestDetail() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5626;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String treatmentId  = "99";
//                request.getBody().setTreatmentId(treatmentId);
//
//                TypeToken<List<KBTreatmentDetails>> typeToken = new TypeToken<List<KBTreatmentDetails>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBTreatmentDetails> result = (List<KBTreatmentDetails>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//    //--- 健康百科 [end] ---
//
//    //--- 智能导诊 [start] ---
//    /**
//     * 3.8.1 查询部位症状列表 [5701]
//     * @throws Throwable
//     */
//    //TODO:
//    public void testGetBodyPartsByColor() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5701;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //胸部
//                String hexColor  = "#3960f1";
//                request.getBody().setHexColor(hexColor);
//
//                TypeToken<List<KBBodyPart>> typeToken = new TypeToken<List<KBBodyPart>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<KBBodyPart> result = (List<KBBodyPart>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 3.8.2 查询症部位对应症状列表 [5702]
//     * @throws Throwable
//     */
//    public void testGetBodyToSymptoms() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5702;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                //胸部
//                String partCode  = "7";
//                String sex = "1";
//                request.getBody().setBodyPartCode(partCode);
//                request.getBody().setSex(sex);
//
//                TypeToken<List<BodyToSymptom>> typeToken = new TypeToken<List<BodyToSymptom>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<BodyToSymptom> result = (List<BodyToSymptom>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG,"result is "+result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//
//    /**
//     * 3.8.3 查询症状对应的疾病列表 [5703]
//     * @throws Throwable
//     */
//    //TODO:
//    public void testgetSymptomsToDisease() throws Throwable {
//        final CountDownLatch signal = new CountDownLatch(1);
//        runTestOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                final int FUN_CODE = 5703;
//                Request<TBase> request = new Request<>();
//                commonHeader(request, FUN_CODE);
//
//                request.setBody(new TBase());
//                request.getBody().setHospitalCode(WebUtil.HOSP_CODE);
//
//                String SId = "438";
//                String sex = "1";
//                request.getBody().setSId(SId);
//                request.getBody().setSex(sex);
//                TypeToken<List<SymptomToDisease>> typeToken = new TypeToken<List<SymptomToDisease>>() {
//                };
//
//                GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request)).fromGWI().mappingInto(typeToken)
//                        .execute(String.valueOf(FUN_CODE), new RequestCallback() {
//                            @Override
//                            public void onRequestSuccess(Object o) {
//                                List<SymptomToDisease> result = (List<SymptomToDisease>) o;
//                                assertNotNull(result);
//                                Logger.d(TAG, "result is " + result.size());
//                                signal.countDown();
//                            }
//
//                            @Override
//                            public void onRequestError(RequestError error) {
//                                Logger.d(TAG, "error");
//                                commonException(error);
//                                signal.countDown();
//                            }
//                        });
//            }
//        });
//        commonWait(signal);
//    }
//    //--- 智能导诊 [end] ---
//
//
//    private <T extends TBase> void commonUserValidation(Request<T> request) {
//        request.getBody().setAccount(mMainUserInfo.getUserCode());
//        request.getBody().setAccountPassword(mMainUserInfo.getUserPwd());
//    }
//
//    private <T> void commonHeader(Request<T> request,int funCode) {
//        request.setHeader(new THeader());
//        request.getHeader().setFunCode(funCode);
//        request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());
//        request.getHeader().setAppTypeCode(WebUtil.APP_TYPE_CODE);
//        request.getHeader().setReqTime(CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date()));
//        request.getHeader().setTerminalNo(WebServiceController.TERMINAL_NO);
//    }
//
//    /**
//     * 等待异步线程结束
//     * @param signal 同步锁信号
//     */
//    private void commonWait(CountDownLatch signal) {
//        try {
//            signal.await();
//        } catch (InterruptedException e) {
//            fail();
//            e.printStackTrace();
//        }
//    }
//
//    private void commonException(RequestError error) {
//        if (error != null && error.getException() != null) {
//            Logger.e(TAG,((VolleyError) error.getException()).getLocalizedMessage());
//        }
//        if(error.getException() instanceof GWIVolleyError) {
//            assertEquals(GResponse.SUCCESS, ((GWIVolleyError) error.getException()).getStatus());
//        }else {
//            assertEquals("0","Common Error occurred.");
//        }
//    }
}
