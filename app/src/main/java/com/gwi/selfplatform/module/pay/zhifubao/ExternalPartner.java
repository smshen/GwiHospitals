package com.gwi.selfplatform.module.pay.zhifubao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.gwi.ccly.android.commonlibrary.AppManager;
import com.gwi.phr.hospital.BuildConfig;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.BankUtil;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1417;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class ExternalPartner extends HospBaseActivity {
    public static final String TAG = "alipay-sdk";
    private static final int RQF_PAY = 1;
    private static final int RQF_LOGIN = 2;
    //测试回调地址
    //private static final String pay_url = "http://phr.gwi.com.cn:7783/ali/NotifyUrl";
    //正式回调地址
    private  String pay_url = BuildConfig.ALI_PAY_URL;
    private Product product;

    private TextView payResult;
    private String orderNo;

    private TextView cardNum;
    private TextView nameTV;
    private TextView chargeNum;

    private TextView recipeCardNum;
    private TextView recipeName;
    private TextView recipeChargeNum;

    private Button checkBtn;
    private String type;
    private String cardValue;
    private String chargeValue;
    private String name;

    private T_Phr_BaseInfo personInfo;
    private G1417 dct;
    private ExT_Phr_CardBindRec cardInfo;
    private String orderDate;
    private G1211 mFromIntentDept;

    private TextView personDctTV;
    private TextView sexTV;
    private TextView mobileTV;
    private TextView departmentTV;
    private TextView IDNumberTV;
    private TextView hospitalTV;
    private TextView doctorTV;
    private TextView rgtTimeTV;
    private TextView amountTV;
    //private TextView tv_card;
    private TextView doctorRank;
    private Button mBtnBack;


    private View orderLayout;
    private View registLayout;
    private View chargeLayout;
    private View recipe_layout;

    private boolean mIsSuccess = false;

    private String mDiscouontMode = HospitalParams.VALUE_ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_partner);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        if (!"NA".equalsIgnoreCase(BuildConfig.ALIPAY_URL_FLAVOR)) {
            pay_url = BuildConfig.ALIPAY_URL_FLAVOR;
        }

        recipe_layout = findViewById(R.id.recipe_layout);
        registLayout = findViewById(R.id.regist_layout);
        orderLayout = findViewById(R.id.orderLayout);
        chargeLayout = findViewById(R.id.charge_layout);
        orderLayout.setVisibility(View.VISIBLE);

        payResult = (TextView) findViewById(R.id.pay_result);
        cardNum = (TextView) findViewById(R.id.cardNum);
        nameTV = (TextView) findViewById(R.id.name);
        chargeNum = (TextView) findViewById(R.id.chargeNum);

        recipeCardNum = (TextView) findViewById(R.id.recipeCardNum);
        recipeName = (TextView) findViewById(R.id.recipeName);
        recipeChargeNum = (TextView) findViewById(R.id.recipeChargeNum);

        mBtnBack  = (Button) findViewById(R.id.pay_result_back);

        mDiscouontMode = HospitalParams.getValue(GlobalSettings.INSTANCE.getHospitalParams(),
                HospitalParams.CODE_REG_DISCOUNT_MODE);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        orderNo = intent.getStringExtra("OrderNo");
        product = (Product) intent.getSerializableExtra("Product");
        if (type != null && (type.equals(BankUtil.BusinessType_Rgt) || type.equals(BankUtil.BusinessType_Rgt_Order))) {
            registLayout.setVisibility(View.VISIBLE);
            chargeLayout.setVisibility(View.GONE);
            recipe_layout.setVisibility(View.GONE);
            dct = (G1417) intent.getSerializableExtra("Dct");
            personInfo = (T_Phr_BaseInfo) intent.getSerializableExtra("PersonInfo");
            cardInfo = (ExT_Phr_CardBindRec) intent.getSerializableExtra("CardInfo");
            orderDate = intent.getStringExtra("OrderDate");
            mFromIntentDept = (G1211) intent.getSerializableExtra(Constants.KEY_DEPT);
            try {
                personDctTV.setText(personInfo.getName());
                if (personInfo.getSex() == 1) {
                    sexTV.setText("男");
                } else {
                    sexTV.setText("女");
                }
                mobileTV.setText(personInfo.getSelfPhone());
                IDNumberTV.setText(personInfo.getIDCard());

                departmentTV.setText(TextUtil.isEmpty(dct.getDeptName()) ? mFromIntentDept.getDeptName() : dct.getDeptName());
                hospitalTV.setText(R.string.app_name);
                doctorTV.setText(dct.getRegSourceName());
                rgtTimeTV.setText(orderDate);
                //设置折扣显示模式
                if (HospitalParams.VALUE_ONE.equalsIgnoreCase(mDiscouontMode)) {
                    amountTV.setText(CommonUtils.cashGreenText(
                            String.format("%s元(已减免%s元)",
                                    CommonUtils.formatCash(dct.getTotalRegFee()),
                                    CommonUtils.formatCash(dct.getDiscountFee())
                            ), "免"));
                } else {
                    amountTV.setText(CommonUtils.cashText(
                            CommonUtils.formatCash(dct.getRegFee() == 0 ? dct.getTotalRegFee() : dct.getRegFee())
                                    + "元"));
                }

                doctorRank.setText(dct.getRankName());
            } catch (Exception e) {
                showToast(R.string.msg_error_regist_data);
            }
        } else if (type != null && type.equals(BankUtil.BusinessType_CARD_CHARGE)) {
            registLayout.setVisibility(View.GONE);
            recipe_layout.setVisibility(View.GONE);
            chargeLayout.setVisibility(View.VISIBLE);
            cardValue = intent.getStringExtra("CardValue");
            name = intent.getStringExtra("Name");
            cardNum.setText(cardValue);
            chargeNum.setText("¥" + product.getPrice());
            nameTV.setText(name);
        } else if (type != null && type.equals(BankUtil.BusinessType_Recipe)) {
            registLayout.setVisibility(View.GONE);
            recipe_layout.setVisibility(View.VISIBLE);
            chargeLayout.setVisibility(View.GONE);
            name = intent.getStringExtra("Name");
            cardValue = intent.getStringExtra("CardValue");
            recipeCardNum.setText(cardValue);
            recipeChargeNum.setText("¥" + product.getPrice());
            recipeName.setText(name);
        }
        aliPay();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("OrderNo", orderNo);
        outState.putString("CardValue", cardValue);
        outState.putSerializable("Product", product);
        outState.putString("Name", name);
    }

    @Override
    public void onBackPressed() {
        if (mIsSuccess) {
            AppManager.getInstance().killAllActivity();
            openActivity(HomeActivity.class);
        }
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        orderLayout.setVisibility(View.VISIBLE);
        product = (Product) savedInstanceState.getSerializable("Product");
        orderNo = savedInstanceState.getString("OrderNo");
        cardValue = savedInstanceState.getString("CardValue");
        name = savedInstanceState.getString("Name");
        recipeCardNum.setText(cardValue);
        chargeNum.setText("¥" + product.getPrice());
        nameTV.setText(name);
    }

    @Override
    protected void initEvents() {
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (type.equals(BankUtil.BusinessType_Recipe)) {
//                    AppManager.getInstance().killAllActivityExcept(HomeActivity.class);
//                    gotoENav();
//                } else {
                AppManager.getInstance().killAllActivity();
                openActivity(HomeActivity.class);
//                }
            }
        });
    }

    public void aliPay() {
        String info = getNewOrderInfo(product);
        String sign = Rsa.sign(info, Keys.PRIVATE);
        sign = URLEncoder.encode(sign);
        info += "&sign=\"" + sign + "\"&" + getSignType();
        Log.i(TAG, "info = " + info);

        final String orderInfo = info;
        new Thread() {
            public void run() {
//				AliPay alipay = new AliPay(ExternalPartner.this, handler);
                PayTask alipay = new PayTask(ExternalPartner.this);
                //设置为沙箱模式，不设置默认为线上环境
                //alipay.setSandBox(true);

//				String result = alipay.pay(orderInfo);
                String result = alipay.pay(orderInfo,true);

                Log.i(TAG, "result = " + result);
                Message msg = new Message();
                msg.what = RQF_PAY;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        }.start();
    }

    private String getNewOrderInfo(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=\"");
        sb.append(Keys.DEFAULT_PARTNER);
        sb.append("\"&out_trade_no=\"");
        //sb.append(getOutTradeNo());
        sb.append(orderNo);
        sb.append("\"&subject=\"");
        sb.append(product.subject);
        sb.append("\"&body=\"");
        sb.append(product.body);
        sb.append("\"&total_fee=\"");
        sb.append(product.price);
        sb.append("\"&notify_url=\"");

        // 网址需要做URL编码
        sb.append(URLEncoder.encode(pay_url));
        sb.append("\"&service=\"mobile.securitypay.pay");
        sb.append("\"&_input_charset=\"UTF-8");
        sb.append("\"&return_url=\"");
        sb.append(URLEncoder.encode("http://m.alipay.com"));
        sb.append("\"&payment_type=\"1");
        sb.append("\"&seller_id=\"");
        sb.append(Keys.DEFAULT_SELLER);

        // 如果show_url值为空，可不传
        //sb.append("\"&show_url=\"");
        sb.append("\"&it_b_pay=\"1m");
        sb.append("\"");

        return new String(sb);
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        java.util.Random r = new java.util.Random();
        key += r.nextInt();
        key = key.substring(0, 15);
        Log.d(TAG, "outTradeNo: " + key);
        return key;
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    private String getUserInfo() {
        String userId = null;
        return trustLogin(Keys.DEFAULT_PARTNER, userId);
    }

    private String trustLogin(String partnerId, String appUserId) {
        StringBuilder sb = new StringBuilder();
        sb.append("app_name=\"mc\"&biz_type=\"trust_login\"&partner=\"");
        sb.append(partnerId);
        Log.d("TAG", "UserID = " + appUserId);
        if (!TextUtils.isEmpty(appUserId)) {
            appUserId = appUserId.replace("\"", "");
            sb.append("\"&app_id=\"");
            sb.append(appUserId);
        }
        sb.append("\"");

        String info = sb.toString();

        // 请求信息签名
        String sign = Rsa.sign(info, Keys.PRIVATE);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        info += "&sign=\"" + sign + "\"&" + getSignType();

        return info;
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Result result = new Result((String) msg.obj);

            switch (msg.what) {
                case RQF_PAY:
                case RQF_LOGIN: {
                    Toast.makeText(ExternalPartner.this, getStatusString(result.getResultStatus()), Toast.LENGTH_SHORT).show();
                    orderLayout.setVisibility(View.VISIBLE);
                    payResult.setText(getStatusString(result.getResultStatus()));
                    mIsSuccess = result.getResultStatus().equals("9000");
//				if(result.getResultStatus().equals("9000")){
//					queryOrderStatus();
//				}else{
//					orderLayout.setVisibility(View.VISIBLE);
//					payResult.setText(getStatusString(result.getResultStatus()));
//				}
                }
                break;
                case Constants.MSG_SHOULD_FINISH: {
                    finish(R.anim.push_right_in, R.anim.push_right_out);
                    break;
                }

                default:
                    break;
            }
        }
    };

    public void initLayout(String status) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if ((type.equals("1") || type.equals("4")) && !status.equals("9000")) {
            registLayout.setVisibility(View.GONE);
            payResult.setWidth(params.width);
            payResult.setHeight(params.height);
        }
        if (type.equals("2") && !status.equals("9000")) {
            chargeLayout.setVisibility(View.GONE);
            payResult.setWidth(params.width);
            payResult.setHeight(params.height);
        }
        if (type.equals("2") && !status.equals("9000")) {
            recipe_layout.setVisibility(View.GONE);
            payResult.setWidth(params.width);
            payResult.setHeight(params.height);
        }
        payResult.setText(getStatusString(status));
    }

    public String getStatusString(String status) {
        if (status.equals("9000")) {
            return "支付成功";
        } else if (status.equals("4000")) {
            return "订单支付失败";
        } else if (status.equals("4001")) {
            return "订单参数错误";
        } else if (status.equals("6001")) {
            return "用户取消支付";
        } else if (status.equals("6002")) {
            return "网络连接异常";
        } else if (status.equals("8000")) {
            return "正在处理中";
        }
        return "系统异常";
    }

    public String getStatusString2(String status) {
        if (status.equals("1")) {
            return "创建订单";
        } else if (status.equals("2")) {
            return "支付成功";
        } else if (status.equals("3")) {
            return "支付失败";
        } else if (status.equals("4")) {
            return "支付异常";
        } else if (status.equals("5")) {
            return "业务成功";
        } else if (status.equals("6")) {
            return "业务失败";
        } else if (status.equals("7")) {
            return "业务异常";
        } else if (status.equals("8")) {
            return "退款成功";
        }
        return "业务正在处理中...";
    }

}