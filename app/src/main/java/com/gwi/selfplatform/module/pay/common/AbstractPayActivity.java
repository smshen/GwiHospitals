package com.gwi.selfplatform.module.pay.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.AppManager;
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
import com.gwi.selfplatform.module.pay.zhifubao.Product;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;


public abstract class AbstractPayActivity extends HospBaseActivity {
    public static final String TAG = "AbstractPayActivity";
    protected Product product;

    protected TextView payResult;
    protected String orderNo;
    protected String prepayId; // 微信支付预支付ID

    private TextView cardNum;
    private TextView nameTV;
    private TextView chargeNum;

    private TextView recipeCardNum;
    private TextView recipeName;
    private TextView recipeChargeNum;

    private Button checkBtn;
    protected String type;
    protected String cardValue;
    protected String chargeValue;
    protected String name;

    protected T_Phr_BaseInfo personInfo;
    protected G1417 dct;
    protected ExT_Phr_CardBindRec cardInfo;
    protected String orderDate;
    protected G1211 mFromIntentDept;

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


    protected View orderLayout;
    private View registLayout;
    private View chargeLayout;
    private View recipe_layout;

    protected boolean mIsSuccess = false;

    private String mDiscouontMode = HospitalParams.VALUE_ZERO;

    /**
     * 统一的支付接口
     */
    public abstract void doPayAsync();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_partner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addHomeButton();
        initViews();
        initEvents();
        // doPayAsync();
    }

    @Override
    protected void initViews() {
        recipe_layout = findViewById(R.id.recipe_layout);
        orderLayout = findViewById(R.id.orderLayout);
        registLayout = findViewById(R.id.regist_layout);
        chargeLayout = findViewById(R.id.charge_layout);
        orderLayout.setVisibility(View.VISIBLE);

        payResult = (TextView) findViewById(R.id.pay_result);
        cardNum = (TextView) findViewById(R.id.cardNum);
        nameTV = (TextView) findViewById(R.id.name);
        chargeNum = (TextView) findViewById(R.id.chargeNum);

        recipeCardNum = (TextView) findViewById(R.id.recipeCardNum);
        recipeName = (TextView) findViewById(R.id.recipeName);
        recipeChargeNum = (TextView) findViewById(R.id.recipeChargeNum);

        personDctTV = (TextView) findViewById(R.id.personDct);
        sexTV = (TextView) findViewById(R.id.sex);
        mobileTV = (TextView) findViewById(R.id.mobile);
        IDNumberTV = (TextView) findViewById(R.id.IDNumber);
        departmentTV = (TextView) findViewById(R.id.departmentTV);
        hospitalTV = (TextView) findViewById(R.id.register_confirm_hospital);
        doctorTV = (TextView) findViewById(R.id.doctor);
        rgtTimeTV = (TextView) findViewById(R.id.rgtTime);
        amountTV = (TextView) findViewById(R.id.amount);
        doctorRank = (TextView) findViewById(R.id.doctorRank);
        //tv_card = (TextView) findViewById(R.id.sp_card);
        mBtnBack = (Button) findViewById(R.id.pay_result_back);

        mDiscouontMode = HospitalParams.getValue(GlobalSettings.INSTANCE.getHospitalParams(),
                HospitalParams.CODE_REG_DISCOUNT_MODE);

        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        orderNo = intent.getStringExtra("OrderNo");
        prepayId = intent.getStringExtra("PrepayId");
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
            chargeNum.setText(String.format("¥%s元", product.getPrice()));
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
}