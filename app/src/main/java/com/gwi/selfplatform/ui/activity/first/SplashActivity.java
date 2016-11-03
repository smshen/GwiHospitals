package com.gwi.selfplatform.ui.activity.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.ServerError;
import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWIVolleyError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.TRequest;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.response.GResponse;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.exception.BaseException;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.JsonUtil;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.DBController;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.beans.Response;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GAuth;
import com.gwi.selfplatform.module.net.connector.implement.gResponse.GBase;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.TBase;
import com.gwi.selfplatform.module.net.request.T1014;
import com.gwi.selfplatform.module.net.response.G1014;
import com.gwi.selfplatform.module.net.webservice.WebServiceController;
import com.gwi.selfplatform.module.pay.zhifubao.Keys;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.activity.start.LoginV2Activity;
import com.gwi.selfplatform.ui.activity.start.RegisterV3Activity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SplashActivity extends FragmentActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private View mSplashView = null;

    private View mLoadingView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 建设银行支付 test
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        PayMode.getInstance().createFakeCCBPay(this);

        // 如果是重新唤醒APP，则不进入splash页面
        boolean needLoadHosparams = true;
        if (AppManager.getInstance().getTopActivity() != null) {
            needLoadHosparams = false;
            Intent i = new Intent(this, AppManager.getInstance().getTopActivity().getClass());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (!needLoadHosparams) {
            finish();
        }
        initViews();
        initEvents();
        getThirdpartData();
        if (needLoadHosparams) {
            authFirst();
//            loadHospitalParamsAsync();
//            loadHospitalParamsAsync2();
//            testMethod();
//            testUpload();
        }
        deleteInstalledApk();
        MobclickAgent.updateOnlineConfig(this);
    }


    private String mTpSource;
    private String mTpName;
    private String mTpMobile;
    private String mTpIDCardNo;

    /**
     * 获取第三方登陆数据
     */
    private void getThirdpartData() {
        Intent intent = getIntent();
        if (null != intent) {
            mTpSource = intent.getStringExtra("SOURCE");
            mTpName = intent.getStringExtra("USER_NAME");
            mTpMobile = intent.getStringExtra("MOBILE_NO");
            mTpIDCardNo = intent.getStringExtra("ID_CARD_NO");
            String toast = "SOURCE=" + mTpSource + "\nUSER_NAME=" + mTpName + "\nMOBILE_NO=" + mTpMobile + "\nID_CARD_NO=" + mTpIDCardNo;
            Log.i(TAG, toast);
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 是否第三方登陆
     *
     * @return
     */
    private boolean isFromThirdpart() {
        return (null != mTpMobile) ? true : false;
    }

    protected void initViews() {
        mSplashView = findViewById(R.id.splash_page);
        mLoadingView = findViewById(R.id.splash_progress_bar);
    }

    protected void initEvents() {

    }

    /**
     * 当安装了新的版本之后，删除之前下载的APK
     */
    private void deleteInstalledApk() {
        try {
            String filePath = GlobalSettings.INSTANCE.getInstalledApkName();
            if (filePath == null) {
                return;
            }
            File file = new File(filePath);
            file.delete();
        } catch (Exception e) {
            Logger.e(TAG, "deleteInstalledApk()", e);
        }
    }

    /**
     * 获取token.
     */
    private void authFirst() {
        ApiCodeTemplate.authenticateAsync(this, TAG, mLoadingView, new RequestCallback<GAuth>() {
            @Override
            public void onRequestSuccess(GAuth auth) {
//                GAuth auth = response.getBody();
                GlobalSettings.INSTANCE.setToken(auth.getToken());
                loadHospitalParamsNewAsync();
            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null && error.getException() != null) {
                    if (error.getException() instanceof ServerError) {
                        ServerError serverError = (ServerError) error.getException();
                        showWarningDialog(getString(R.string.msg_service_interval_eror) + "（" + serverError.networkResponse.statusCode + "）");
                    } else if (error.getException() instanceof GWIVolleyError) {
                        GWIVolleyError gwiVolleyError = (GWIVolleyError) error.getException();
                        Logger.e(TAG, "onRequestError", gwiVolleyError);
                        if (gwiVolleyError.getCause() != null) {
                            showWarningDialog(gwiVolleyError.getCause().getLocalizedMessage());
                        }
                    } else {
                        showWarningDialog(getString(R.string.msg_service_disconnected));
                    }
                }
            }
        });
    }

    /**
     * 加载医院参数
     */
    private void loadHospitalParamsNewAsync() {
        ApiCodeTemplate.loadHospitalParamsAsync(TAG, mLoadingView, "", new RequestCallback<List<G1014>>() {
            @Override
            public void onRequestSuccess(List<G1014> result) {
                if (result != null && !result.isEmpty()) {
                    GlobalSettings.INSTANCE.setHospitalParams(result);
                    handleMapNavParams();
                    if (GlobalSettings.INSTANCE.isIsLogined()) {
                        updateUserInfoAndUpdateInfoNewAsync();
                    } else {
                        // toLogin();
                        if (isFromThirdpart()) {
                            gotoRigister();
                        } else {
                            gotoHome();
                        }
                    }
                } else {
                    showWarningDialog("未加载到医院数据，请稍后再试~");
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null && error.getException() != null) {
                    if (error.getException() instanceof ServerError) {
                        ServerError serverError = (ServerError) error.getException();
                        showWarningDialog(getString(R.string.msg_service_interval_eror) + "（" + serverError.networkResponse.statusCode + "）");
                    } else if (error.getException() instanceof GWIVolleyError) {
                        GWIVolleyError gwiVolleyError = (GWIVolleyError) error.getException();
                        Logger.e(TAG, "onRequestError", gwiVolleyError);
                        if (gwiVolleyError.getCause() != null) {
                            showWarningDialog(gwiVolleyError.getCause().getLocalizedMessage());
                        }
                    } else {
                        showWarningDialog(getString(R.string.msg_service_disconnected));
                    }
                }
            }
        });
    }

    private void loadHospitalParamsAsync() {
        AsyncTasks.doAsyncTask(mLoadingView, new AsyncCallback<List<G1014>>() {

            @Override
            public List<G1014> callAsync() throws Exception {
                TRequest<T1014> request = new TRequest<T1014>();
                request.setHeader(new THeader());
                request.setBody(new T1014());
                request.getHeader().setAppCode(WebUtil.APPCODE);
                request.getHeader().setAppTypeCode(WebUtil.APP_TYPE_CODE);
                request.getHeader().setFunCode(1014);
                request.getHeader().setReqTime(
                        CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE,
                                new Date()));
                request.getBody().setHospCode(WebUtil.HOSP_CODE);
                request.getBody().setParameterCode(null);
                JSONObject bodyData = WebUtil.httpExecute(request, true);
                Type lt = new TypeToken<List<G1014>>() {
                }.getType();
                List<G1014> result = new ArrayList<G1014>();
                result = JsonUtil.toListObject(bodyData, "Item", G1014.class,
                        lt);
                return result;
            }

            @Override
            public void onPostCall(List<G1014> result) {
                if (result != null && !result.isEmpty()) {
                    GlobalSettings.INSTANCE.setHospitalParams(result);
                    handleMapNavParams();
                    if (GlobalSettings.INSTANCE.isIsLogined()) {
                        updateUserInfoAndUpdateInfoAsync();
                    } else {
                        gotoHome();
                    }
                } else {
                    showWarningDialog("加载服务器数据失败，请稍后再试！");
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e(TAG, "onCallFailed", exception);
                showWarningDialog(getString(R.string.msg_service_disconnected));
            }
        });
    }

    /**
     * 获取地图参数：省市
     */
    private void handleMapNavParams() {
        try {
            Map<String, String> params = GlobalSettings.INSTANCE.getHospitalParams();
            List<String> mapParams = HospitalParams.getFields(params.get(HospitalParams.CODE_MAP_NAVIGATION));
            GlobalSettings.INSTANCE.setCityOfHospital(mapParams.get(0));
            GlobalSettings.INSTANCE.setHospitalName(mapParams.get(1));
            String terminalNo = HospitalParams.getValue(params, HospitalParams.CODE_TERMINAL_NO);
            String aliPartnerId = HospitalParams.getValue(params, HospitalParams.PID);
            Keys.DEFAULT_PARTNER = aliPartnerId;
            Keys.DEFAULT_SELLER = aliPartnerId;
            Keys.PRIVATE = HospitalParams.getValue(params, HospitalParams.CODE_PRIVATE_KEY);
            Keys.PUBLIC = HospitalParams.getValue(params, HospitalParams.CODE_PUBLIC_KEY);

            //Demo:
            // terminalNo = "12524";
            WebUtil.setTerminalNo(terminalNo);
            GlobalSettings.INSTANCE.setTerminalNO(terminalNo);
        } catch (Exception e) {
            Logger.e(TAG, "handleMapNavParams", e);
        }
    }

    /**
     * 验证用户信息
     */
    private void updateUserInfoAndUpdateInfoNewAsync() {
        final String REQUEST_TAG = "login";
        Request<TBase> request = new Request<>();
        request.setHeader(new THeader());
        request.getHeader().setFunCode(Request.FUN_CODE_USER_LOGIN);
        request.getHeader().setAppCode(GlobalSettings.INSTANCE.getAppCode());

        T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();

        request.setBody(new TBase());
        request.getBody().setAccount(userInfo.getUserCode());
        request.getBody().setAccountPassword(userInfo.getUserPwd());
        request.getBody().setHospitalCode(GlobalSettings.INSTANCE.getAppCode());
        request.getBody().setTerminalNo(WebServiceController.TERMINAL_NO);

        GWINet.connect().createRequest().postGWI(null, ApiCodeTemplate.generateBodyRequest(request))
                .setLoadingView(mLoadingView)
                .fromGWI()
                .mappingInto(new TypeToken<GResponse<GBase>>() {
                })
                .execute(REQUEST_TAG, new RequestCallback() {
                    @Override
                    public void onRequestSuccess(Object o) {
                        GBase response = (GBase) o;
                        String MSG = "用户信息已过期，需要重新登录";
                        try {
                            T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
                            String md5Pwd = userInfo.getUserPwd();
                            if (response != null) {
                                T_UserInfo user = response.getUserInfo();
                                T_Phr_BaseInfo baseinfo = response.getBaseInfo();
                                if (user != null && baseinfo != null) {
                                    user.setUserPwd(md5Pwd);
                                    boolean a = DBController.INSTANCE.saveUser(user);
                                    if (a) {
                                        GlobalSettings.INSTANCE.setIsLogined(true);
                                        GlobalSettings.INSTANCE.setCurrentUser(user);

                                        //验证通过，进入home页面
                                        gotoHome();
                                    } else {
                                        throw new BaseException(ERROR_CODE, MSG);
                                    }
                                } else {
                                    throw new BaseException(ERROR_CODE, MSG);
                                }
                            } else {
                                throw new BaseException(ERROR_CODE, MSG);
                            }
                        } catch (Exception e) {
                            Toast toast = Toast.makeText(SplashActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            toLogin();
                        }
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        if (error != null && error.getException() != null) {
                            if (error.getException() instanceof GWIVolleyError) {
                                GWIVolleyError gwiVolleyError = (GWIVolleyError) error.getException();
                                if (gwiVolleyError.getStatus() == GResponse.ERROR) {
                                    toLogin();
                                }
                            }
                            Logger.e(TAG, "onRequestError", (Exception) error.getException());
                        } else {
                            showWarningDialog(getString(R.string.msg_service_disconnected));
                        }
                    }
                });

    }

    private void updateUserInfoAndUpdateInfoAsync() {
        AsyncTasks.doSilAsyncTask(mLoadingView, new AsyncCallback<Boolean>() {

            @Override
            public Boolean callAsync() throws Exception {
                String MSG = "用户信息已过期，需要重新登录";
                try {
                    T_UserInfo userInfo = GlobalSettings.INSTANCE.getCurrentUser();
                    String md5Pwd = userInfo.getUserPwd();
                    Response response = WebServiceController.login(
                            userInfo.getUserCode(), userInfo.getUserPwd(), true);
                    if (response != null) {
                        T_UserInfo user = response.getUserInfo();
                        user.setUserPwd(md5Pwd);
                        T_Phr_BaseInfo baseinfo = response.getPhr_BaseInfo();
                        if (user != null && baseinfo != null) {
                            boolean a = DBController.INSTANCE.saveUser(user);
//                            boolean b = DBController.INSTANCE
//                                    .saveFamilyAccount(baseinfo);
                            if (a /*& b*/) {
                                GlobalSettings.INSTANCE.setIsLogined(true);
                                GlobalSettings.INSTANCE.setCurrentUser(user);
//                                GlobalSettings.INSTANCE.setCurrentFamilyAccount(baseinfo);
                                return true;
                            } else {
                                throw new BaseException(ERROR_CODE, MSG);
                            }
                        } else {
                            throw new BaseException(ERROR_CODE, MSG);
                        }
                    } else {
                        throw new BaseException(ERROR_CODE, MSG);
                    }
                } catch (Exception e) {
                    //用户信息已过期，需要重新登录
                    throw new BaseException(ERROR_CODE, MSG);
                }
            }

            @Override
            public void onPostCall(Boolean result) {
                if (result != null && result.booleanValue()) {
                    gotoHome();
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                if (exception instanceof BaseException) {
                    BaseException baseEx = (BaseException) exception;
                    if (baseEx.getErrorCode() == ERROR_CODE) {
                        toLogin();
                    }
                } else {
                    showWarningDialog(getString(R.string.msg_service_disconnected));
                }
            }

        });

    }

    final int ERROR_CODE = 10001;

    private void toLogin() {
        Intent loginIntent = new Intent(this, LoginV2Activity.class);
        startActivityForResult(loginIntent, 1);
        finish();
        overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
    }

    private void gotoRigister() {
        Intent intent = new Intent(this, RegisterV3Activity.class);
        intent.putExtra(Constants.ThirdPart.SOURCE, "BOC");                       // 来着中国银行(请不要修改)
        intent.putExtra(Constants.ThirdPart.USER_NAME, "LKF");                    // 用户名
        intent.putExtra(Constants.ThirdPart.MOBILE_NO, mTpMobile);                // 用户手机号码
        intent.putExtra(Constants.ThirdPart.ID_CARD_NO, "4305XXXXXXXXXXX");       // 用户身份证号码
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void gotoHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void showWarningDialog(String content) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.content(content)
                .positiveText(R.string.dialog_cofirm)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
}
