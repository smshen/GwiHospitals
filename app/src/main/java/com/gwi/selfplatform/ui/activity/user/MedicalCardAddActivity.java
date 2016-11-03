package com.gwi.selfplatform.ui.activity.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.validator.CardValidator;
import com.gwi.selfplatform.common.utils.validator.Validator;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;

/**
 * 添加诊疗卡
 */
public class MedicalCardAddActivity extends HospBaseActivity {

    private static final String TAG = MedicalCardAddActivity.class.getSimpleName();
    @Bind(R.id.m_card_patient_name)
    EditText mEtPatientName;

    @Bind(R.id.m_card_id_card)
    EditText mEtIdCard;

    @Bind(R.id.m_card_medaical_card)
    EditText mEtMedicalCard;

    @Bind(R.id.m_card_mobile)
    EditText mEtMobile;

    @Bind(R.id.btn_add_medical_card)
    Button mBtnAddMedicalCard;

    private T_Phr_BaseInfo mNewMember;

    private T_Phr_BaseInfo mMainFamilyMember;
    G1011 mPatientInfo;

    @OnTextChanged({R.id.m_card_patient_name, R.id.m_card_id_card, R.id.m_card_medaical_card, R.id.m_card_mobile})
    void onTextChanged(CharSequence s) {
        if (!TextUtils.isEmpty(mEtIdCard.getText())
                && !TextUtils.isEmpty(mEtMedicalCard.getText())
                && !TextUtils.isEmpty(mEtMobile.getText())
                && !TextUtils.isEmpty(mEtPatientName.getText())) {
            mBtnAddMedicalCard.setEnabled(true);
        } else mBtnAddMedicalCard.setEnabled(false);
    }

    CardValidator cardValidator = new CardValidator();

    @OnClick(R.id.btn_add_medical_card)
    void addMedicalCard() {
        if (cardValidator.validate(mEtIdCard.getText().toString(), CardValidator.CARD_ID) != Validator.SUCCESS) {
            mEtIdCard.setError("身份号码格式不正确");
            mEtIdCard.requestFocus();
            return;
        }
        getPatientInfoAsync();
    }


    @Override
    protected void initViews() {
        if (getIntent() != null) {
            mMainFamilyMember = (T_Phr_BaseInfo) getIntent().getSerializableExtra(MyMedicalCardActivity.FAMILY_INFO_MAIN);
        }

        //因为注册时，不再完善个人信息，因此会出现主用户IDcard为空的情况，
        //当主用户已经绑过卡时，才会有身份证信息，且暂时不提供修改的功能。
        if (mMainFamilyMember != null && !TextUtil.isEmpty(mMainFamilyMember.getIDCard())) {
            setTitle("替换诊疗卡");
            mEtPatientName.setText(mMainFamilyMember.getName());
            mEtIdCard.setText(mMainFamilyMember.getIDCard());
            mEtMobile.setText(mMainFamilyMember.getSelfPhone());

            mEtPatientName.setEnabled(false);
            mEtIdCard.setEnabled(false);
            mEtMobile.setEnabled(false);
        }

        mBtnAddMedicalCard.setEnabled(false);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_card_add);
        addHomeButton();
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getPatientInfoAsync() {
        ExT_Phr_CardBindRec cardBindRec = new ExT_Phr_CardBindRec();
        cardBindRec.setCardNo(mEtMedicalCard.getText().toString());
        cardBindRec.setCardType(Constants.CARD_TYPE_MEDICAL);
        ApiCodeTemplate.getPatientInfoAsync(this, TAG, null, cardBindRec, new RequestCallback<G1011>() {
            @Override
            public void onRequestSuccess(G1011 result) {
                if (result != null) {
                    if (!G1011.STATUS_OK.equalsIgnoreCase(result.getCardStatus())) {
                        showLongToast("您的诊疗卡不存在或已被注销,请确认卡号是否输入正确~");
                        return;
                    } else if (!result.getName().equalsIgnoreCase(mEtPatientName.getText().toString())
                            || !result.getName().contains(mEtPatientName.getText().toString())) {
                        showLongToast("诊疗卡与姓名不匹配");
                        return;
                    }
                    if (!TextUtil.isEmpty(result.getIDCardNo()) && !result.getIDCardNo().equalsIgnoreCase(mEtIdCard.getText().toString())) {
                        showLongToast("诊疗卡与身份证不匹配");
                        return;
                    }
                    if (CommonUtils.validateIDCard(mEtIdCard.getText().toString())) {
                        mEtIdCard.setTag(mEtIdCard.getText().toString());
                    } else mEtMedicalCard.setTag(result.getIDCardNo());
                    mPatientInfo = result;
                    if (mMainFamilyMember == null) {
                        addNewMemberNewAsync();
                    } else {
                        bindCardAsync();
                    }
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MedicalCardAddActivity.this, (Exception) error.getException());
            }
        });
    }

    private void modifyMedicalCardAsync() {
        T_UserInfo userInfo = new T_UserInfo();
        userInfo.setUserName(mEtPatientName.getText().toString());
        userInfo.setMobilePhone(mMainFamilyMember.getSelfPhone());

        T_Phr_BaseInfo member = new T_Phr_BaseInfo();
        if (mEtIdCard.getTag() != null) {
            member.setIDCard((String) mEtIdCard.getTag());
            try {
                member.setBirthDay(CommonUtils.stringPhaseDate(
                        CommonUtils.getBirthFromIDCard((String) mEtIdCard.getTag()),
                        Constants.DATE_FORMAT));
                member.setSex(CommonUtils.getSexFromIdCard(mNewMember.getIDCard()));
            } catch (Exception e) {
                Logger.e(TAG, "modifyMedicalCardAsync", e);
                CommonUtils.showError(this, e);
                return;
            }
        }
        member.setName(mEtPatientName.getText().toString());
    }

    private void addNewMemberNewAsync() {
        mNewMember = new T_Phr_BaseInfo();
        mNewMember.setSelfPhone(mEtPatientName.getText().toString());
        mNewMember.setUserId(GlobalSettings.INSTANCE
                .getCurrentUser().getUserId());
        mNewMember.setSelfPhone(mEtMobile.getText().toString());
        mNewMember.setEhrID(UUID.randomUUID().toString());
        mNewMember.setName(mEtPatientName.getText().toString());
        if (mEtIdCard.getTag() != null) {
            mNewMember.setIDCard((String) mEtIdCard.getTag());
            try {
                mNewMember.setBirthDay(CommonUtils.stringPhaseDate(
                        CommonUtils.getBirthFromIDCard((String) mEtIdCard.getTag()),
                        Constants.DATE_FORMAT));
                mNewMember.setSex(CommonUtils.getSexFromIdCard(mNewMember.getIDCard()));
            } catch (Exception e) {
                Logger.e(TAG, "addNewMemberNewAsync", e);
                CommonUtils.showError(this, e);
                return;
            }
        }


        ApiCodeTemplate.addNewMemberNewAsync(this, TAG, mNewMember, new RequestCallback<List<T_Phr_BaseInfo>>() {
            @Override
            public void onRequestSuccess(List<T_Phr_BaseInfo> result) {
                EventBus.getDefault().postSticky(mNewMember);
                bindCardAsync();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MedicalCardAddActivity.this, (Exception) error.getException());
            }
        });
    }

    private void bindCardAsync() {
        ExT_Phr_CardBindRec cardRec = new ExT_Phr_CardBindRec();
        cardRec.setCardNo(mEtMedicalCard.getText().toString());
        cardRec.setCardType(Constants.CARD_TYPE_MEDICAL);
        T_Phr_BaseInfo baseInfo = mNewMember == null ? mMainFamilyMember : mNewMember;
        cardRec.setEhrId(baseInfo.getEhrID());
        cardRec.setBindMan(baseInfo.getName());
        cardRec.setPatientID(mPatientInfo.getPatientID());
        cardRec.setHospitalCode(GlobalSettings.INSTANCE.getHospCode());

        ApiCodeTemplate.bindCardAsync(this, TAG, null, baseInfo, cardRec, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                showToast(R.string.msg_success_added);
                setResult(RESULT_OK);
                finish(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onRequestError(RequestError error) {
                if (error != null) {
                    CommonUtils.showError(MedicalCardAddActivity.this, (Exception) error.getException());
                }
            }
        });

    }
}
