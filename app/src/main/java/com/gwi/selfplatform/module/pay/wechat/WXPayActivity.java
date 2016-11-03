package com.gwi.selfplatform.module.pay.wechat;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import com.gwi.phr.hospital.BuildConfig;
import com.gwi.selfplatform.module.pay.common.AbstractPayActivity;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WXPayActivity extends AbstractPayActivity {
    private static final String TAG = "WXPayActivity";
    private static final int MSG_FAIL = 0;
    private static final int MSG_SUCCESS = 1;
    private static final int DELAY = 500;
    public static int PAY_STATUS;
    private IWXAPI mWXApi;
    private WXPayReqInfo mWXPayInfo;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FAIL:
                    Toast.makeText(WXPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SUCCESS:
                    Bundle bundle = msg.getData();
                    WXPayRespInfo wxPayRespInfo = (WXPayRespInfo) bundle.getSerializable("key");
                    paymentFromLocal(wxPayRespInfo);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.wx_activity_pay);
        initData();
        doPayAsync();
    }

    private void initData() {
        mWXApi = WXAPIFactory.createWXAPI(this, WXConstants.APP_ID);
        PAY_STATUS = WXConstants.STATUS_IDLE;

        mWXPayInfo = new WXPayReqInfo();
        mWXPayInfo.setAppid(WXConstants.APP_ID);
        mWXPayInfo.setMch_id(WXConstants.PARTNER_ID);
        // mWXPayInfo.setAttach("支付测试001");
        mWXPayInfo.setBody("APP支付测试001");
        mWXPayInfo.setNonce_str(WXUtil.getUUID());
        mWXPayInfo.setNotify_url("http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php"); // BuildConfig.WX_PAY_URL;
        mWXPayInfo.setOut_trade_no(orderNo);
        mWXPayInfo.setSpbill_create_ip(WXUtil.getLocalIpAddress(this));
        int fee = (int) (Double.parseDouble(product.price) * 100);
        mWXPayInfo.setTotal_fee(String.format("%s", fee));
        mWXPayInfo.setTrade_type("APP");
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleResult();
    }

    /**
     * 微信支付成功后重新打开WXPayActivity界面,根据支付标志进行UI重绘制
     */
    private void handleResult() {
        int status = PAY_STATUS;
        if (WXConstants.STATUS_IDLE != status) {
            String result = getWechatStatus(status);
            orderLayout.setVisibility(View.VISIBLE);
            payResult.setText(result);
            mIsSuccess = (BaseResp.ErrCode.ERR_OK == status) ? true : false;
        }
    }

    @NonNull
    private String getWechatStatus(int status) {
        String msg = "支付失败";
        if (BaseResp.ErrCode.ERR_OK == status) {
            msg = "支付成功";
        } else if (BaseResp.ErrCode.ERR_COMM == status) {
            msg = "支付失败";
        } else if (BaseResp.ErrCode.ERR_USER_CANCEL == status) {
            msg = "已取消支付";
        } else if (BaseResp.ErrCode.ERR_SENT_FAILED == status) {
            msg = "发送失败";
        } else if (BaseResp.ErrCode.ERR_AUTH_DENIED == status) {
            msg = "认证被否决";
        } else if (BaseResp.ErrCode.ERR_UNSUPPORT == status) {
            msg = "不支持错误";
        }
        return msg;
    }

    @Override
    public void doPayAsync() {
        doWechatPay();
    }

    private void doWechatPay() {
        if (!mWXApi.isWXAppInstalled()) {
            Toast.makeText(this, "请安装微信应用", Toast.LENGTH_SHORT).show();
            delayFinish();
            return;
        }

        if (!mWXApi.isWXAppSupportAPI()) {
            Toast.makeText(this, "当前微信版本不支持微信支付", Toast.LENGTH_SHORT).show();
            delayFinish();
            return;
        }

        // doUnifiedOrder();
        doUnifiedOrderLocal();
    }

    private void delayFinish() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, DELAY);
    }

    private void paymentFromLocal(WXPayRespInfo info) {
        String nonceStr = WXUtil.getUUID();
        String timeStamp = String.format("%s", System.currentTimeMillis() / 1000);

        PayReq req = new PayReq();
        req.appId = info.getAppid();
        req.partnerId = info.getMch_id();
        req.prepayId = info.getPrepay_id();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonceStr; // info.getNonce_str();
        req.timeStamp = timeStamp;
        req.sign = getPaySign(info, nonceStr, timeStamp);
        // Toast.makeText(WXPayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        mWXApi.sendReq(req);
    }

    public String getPaySign(WXPayRespInfo info, String nonceStr, String timeStamp) {
        String signTemp = "appid=" + info.getAppid()
                + "&noncestr=" + nonceStr
                + "&package=" + "Sign=WXPay"
                + "&partnerid=" + info.getMch_id()
                + "&prepayid=" + info.getPrepay_id()
                + "&timestamp=" + timeStamp
                + "&key=" + WXConstants.APP_KEY;
        Log.i(TAG, "getPaySign signTemp:" + signTemp);
        String sign = WXMD5.getMessageDigest(signTemp.getBytes()).toUpperCase();
        // String sign = MD5Util.string2MD5(signTemp).toUpperCase();
        Log.i(TAG, "getPaySign sign:" + sign);
        return sign;
    }

    /**
     * 本地调用统一下单接口
     */
    private void doUnifiedOrderLocal() {
        WXPayRespInfo wxPayRespInfo = new WXPayRespInfo();
        wxPayRespInfo.setPrepay_id(prepayId);
        wxPayRespInfo.setAppid(WXConstants.APP_ID);
        wxPayRespInfo.setMch_id(WXConstants.PARTNER_ID);
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", wxPayRespInfo);
        message.what = MSG_SUCCESS;
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    /**********************************************************************************************/
    /**
     * 联网调用统一下单接口
     */
    private void doUnifiedOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 统一下单获取预支付交易会话标识
                    String requestXml = buildXMLUnifiedOrder();
                    Log.i(TAG, "requestXml:" + requestXml);
                    WXPayRespInfo wxPayRespInfo = getPrepayInfo(requestXml);
                    Log.i(TAG, "wxPayRespInfo:" + wxPayRespInfo.toString());

                    if (null != wxPayRespInfo && wxPayRespInfo.getPrepay_id() != null) {
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("key", wxPayRespInfo);
                        message.what = MSG_SUCCESS;
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    } else {
                        mHandler.sendEmptyMessage(MSG_FAIL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 构造出xml文件的内容，用于发送给“统一下单”获取到prepay_id
     *
     * @return XML的String输出
     */
    private String buildXMLUnifiedOrder() {
        List<TwoTuple<String, String>> paramList = new ArrayList<TwoTuple<String, String>>();
        paramList.add(new TwoTuple<String, String>("appid", mWXPayInfo.getAppid()));
        paramList.add(new TwoTuple<String, String>("body", mWXPayInfo.getBody()));
        paramList.add(new TwoTuple<String, String>("mch_id", mWXPayInfo.getMch_id()));
        paramList.add(new TwoTuple<String, String>("nonce_str", mWXPayInfo.getNonce_str()));
        paramList.add(new TwoTuple<String, String>("notify_url", mWXPayInfo.getNotify_url()));
        paramList.add(new TwoTuple<String, String>("out_trade_no", mWXPayInfo.getOut_trade_no()));
        paramList.add(new TwoTuple<String, String>("spbill_create_ip", mWXPayInfo.getSpbill_create_ip()));
        paramList.add(new TwoTuple<String, String>("total_fee", mWXPayInfo.getTotal_fee()));
        paramList.add(new TwoTuple<String, String>("trade_type", mWXPayInfo.getTrade_type()));
        paramList.add(new TwoTuple<String, String>("sign", getPrepaySign(mWXPayInfo)));

        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<xml>");
        for (TwoTuple<String, String> paramTuple : paramList) {
            xmlBuilder.append("<").append(paramTuple.first).append(">");
            xmlBuilder.append(paramTuple.second);
            xmlBuilder.append("</").append(paramTuple.first).append(">");
        }
        xmlBuilder.append("</xml>");

        return xmlBuilder.toString();
    }

    public String getPrepaySign(WXPayReqInfo payInfo) {
        String signTemp = "appid=" + payInfo.getAppid()
                + "&body=" + payInfo.getBody()
                + "&mch_id=" + payInfo.getMch_id()
                + "&nonce_str=" + payInfo.getNonce_str()
                + "&notify_url=" + payInfo.getNotify_url()
                + "&out_trade_no=" + payInfo.getOut_trade_no()
                + "&spbill_create_ip=" + payInfo.getSpbill_create_ip()
                + "&total_fee=" + payInfo.getTotal_fee()
                + "&trade_type=" + payInfo.getTrade_type()
                + "&key=" + WXConstants.APP_KEY;
        Log.i(TAG, "signTemp:" + signTemp);
        String sign = WXMD5.getMessageDigest(signTemp.getBytes()).toUpperCase();
        // String sign = MD5Util.string2MD5(signTemp).toUpperCase();
        Log.i(TAG, "sign:" + sign);
        return sign;
    }

    private WXPayRespInfo getPrepayInfo(String soap) throws Exception {
        String url = BuildConfig.WX_PREPAY_URL;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        // String soap = readSoap();
        byte[] entity = soap.getBytes("UTF-8");
        conn.setDoInput(true);
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        conn.setRequestProperty("Content-Length", entity.length + "");
        conn.getOutputStream().write(entity);
        if (conn.getResponseCode() == 200) {
            // String responseXml = inputStreamToString(conn.getInputStream());
            // Log.i(TAG, "responseXml:" + responseXml);
            return parseSOAP(conn.getInputStream());
        }
        return null;
    }

    private WXPayRespInfo parseSOAP(InputStream inputStream) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();
        WXPayRespInfo info = new WXPayRespInfo();
        while (event != parser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_TAG:
                    if ("appid".equals(parser.getName())) {
                        info.setAppid(parser.nextText());
                    } else if ("mch_id".equals(parser.getName())) {
                        info.setMch_id(parser.nextText());
                    } else if ("nonce_str".equals(parser.getName())) {
                        info.setNonce_str(parser.nextText());
                    } else if ("sign".equals(parser.getName())) {
                        info.setSign(parser.nextText());
                    } else if ("result_code".equals(parser.getName())) {
                        info.setResult_code(parser.nextText());
                    } else if ("trade_type".equals(parser.getName())) {
                        info.setTrade_type(parser.nextText());
                    } else if ("prepay_id".equals(parser.getName())) {
                        info.setPrepay_id(parser.nextText());
                    }
                    break;
            }
            event = parser.next();
        }
        if ("SUCCESS".equals(info.getResult_code())) {
            return info;
        }
        return null;
    }

    private String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }
}
