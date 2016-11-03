package com.gwi.selfplatform.ui.activity.pay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.response.G1911;
import com.gwi.selfplatform.module.net.response.RecipeDetail;
import com.gwi.selfplatform.module.net.response.RecipeList;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.AutoWrapView;

import java.util.List;

import butterknife.Bind;

/**
 * 處方單詳細接口
 * @version 2.0
 * @date 2016-03-10
 */
public class RecipeDetailActivity extends HospBaseActivity {

    public static final String KEY_CARDD_INFO = "key_cardd_info";
    public static final String KEY_FAMILY_INFO = "key_family_info";
    public static final String KEY_RECIPE_LIST = "key_recipe_list";

    @Bind(R.id.patient_name)
    TextView patientName;
    @Bind(R.id.prescribing_sections)
    TextView prescribingSections;
    /* bug2170 LiuTao 20160802 delete */
    @Bind(R.id.doctors_prescribing)
    TextView doctorsPrescribing;
    @Bind(R.id.prescribing_time)
    TextView prescribingTime;
    @Bind(R.id.medical_card)
    TextView medicalCard;
    @Bind(R.id.prescription_amount)
    TextView prescriptionAmount;
    @Bind(R.id.wrap_view)
    AutoWrapView wrapView;

    private T_Phr_CardBindRec mCardInfo;
    private RecipeList mRecipeList;
    private T_Phr_BaseInfo mCurBaseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        addHomeButton();
        addBackListener();
        handleIntent();
        initViews();
        initEvents();
    }

    private void handleIntent() {
        mCardInfo = (T_Phr_CardBindRec) getIntent().getSerializableExtra(KEY_CARDD_INFO);
        mRecipeList = (RecipeList) getIntent().getSerializableExtra(KEY_RECIPE_LIST);
        mCurBaseInfo = (T_Phr_BaseInfo) getIntent().getSerializableExtra(KEY_FAMILY_INFO);

        if (mCurBaseInfo != null) {
            patientName.setText(mCurBaseInfo.getName());
        }
        if (mRecipeList != null&&mRecipeList.getRecipeInfo()!=null) {
            prescribingSections.setText(mRecipeList.getRecipeInfo().getDeptName());
            doctorsPrescribing.setText(mRecipeList.getRecipeInfo().getDoctName());
            prescribingTime.setText(mRecipeList.getRecipeInfo().getRecipeTime());
            medicalCard.setText(mCardInfo.getCardNo());
            /* bug2170 LiuTao 20160802 change */
            String totalFee = mRecipeList.getRecipeInfo().getTotalFee() + getResources().getString(R.string.moneyUnit);
            prescriptionAmount.setText(totalFee);
        }
        creatAutoWrapList();
    }

    @Override
    protected void initViews() {
        super.initViews();
    }

    @Override
    protected void initEvents() {
        super.initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void removeAutoWrapItem(View view) {
        wrapView.removeView(view);
    }

    private void clearAutoWrapView() {
        wrapView.removeAllViews();
    }

    private void addAutoWrapView(View view) {
        wrapView.addView(view);
    }

    private View creatAutoWrapHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_costs_detail_header, null);
        view.findViewById(R.id.line_bottom).setVisibility(View.GONE);
        return view;
    }

    private View creatAutoWrapItem(boolean isLast, Object obj) {
        if (null == obj) {
            return null;
        }
        RecipeDetail info = (RecipeDetail) obj;

        View view = LayoutInflater.from(this).inflate(R.layout.listitem_costs_detail_body, null);
        view.findViewById(R.id.line_bottom).setVisibility(isLast ? View.VISIBLE : View.GONE);

        // double total = info.getItemPrice() * info.getItemQuantity();
        ((TextView) view.findViewById(R.id.txt_project_name)).setText(info.getItemName());
        ((TextView) view.findViewById(R.id.txt_specification)).setText(info.getSpecs());
        ((TextView) view.findViewById(R.id.txt_unit)).setText(info.getUnit());
        ((TextView) view.findViewById(R.id.txt_count)).setText(String.format("%s", info.getQuantity()));
        ((TextView) view.findViewById(R.id.txt_money)).setText(CommonUtils.getFormatFee(Double.valueOf(info.getUnitPrice())));
        return view;
    }

    private void creatAutoWrapList() {
        clearAutoWrapView();
        addAutoWrapView(creatAutoWrapHeader());
        if (mRecipeList != null) {
            final int size = mRecipeList.getRecipeDetailList().size();
            List<RecipeDetail> detailList = mRecipeList.getRecipeDetailList();
            for (int i = 0; i < size; i++) {
                addAutoWrapView(creatAutoWrapItem(size - 1 == i, detailList.get(i)));
            }
        }
    }
}
