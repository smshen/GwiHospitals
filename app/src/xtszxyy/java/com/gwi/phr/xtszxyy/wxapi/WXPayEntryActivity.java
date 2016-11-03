package com.gwi.phr.xtszxyy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.pay.wechat.WXConstants;
import com.gwi.selfplatform.module.pay.wechat.WXPayActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付回调路径:包名+wxapi+WXPayEntryActivity
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI mWXApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.wx_activity_pay_result);

        mWXApi = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);
        mWXApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWXApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

//        String msg = "支付失败";
//        if (BaseResp.ErrCode.ERR_OK == resp.errCode) {
//            msg = "支付成功";
//        } else if (BaseResp.ErrCode.ERR_COMM == resp.errCode) {
//            msg = "支付失败";
//        } else if (BaseResp.ErrCode.ERR_USER_CANCEL == resp.errCode) {
//            msg = "已取消支付";
//        } else if (BaseResp.ErrCode.ERR_SENT_FAILED == resp.errCode) {
//            msg = "发送失败";
//        } else if (BaseResp.ErrCode.ERR_AUTH_DENIED == resp.errCode) {
//            msg = "认证被否决";
//        } else if (BaseResp.ErrCode.ERR_UNSUPPORT == resp.errCode) {
//            msg = "不支持错误";
//        }

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            // Toast.makeText(this, resp.errCode + "", Toast.LENGTH_SHORT).show();
            WXPayActivity.PAY_STATUS = resp.errCode;
            finish();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
}