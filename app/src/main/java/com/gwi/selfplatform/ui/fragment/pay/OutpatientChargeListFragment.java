package com.gwi.selfplatform.ui.fragment.pay;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.RecipeInfo;
import com.gwi.selfplatform.module.net.response.RecipeList;
import com.gwi.selfplatform.ui.activity.pay.RecipeDetailActivity;
import com.gwi.selfplatform.ui.base.HospBaseFragment;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.kinst.jakub.view.StatefulLayout;
import de.greenrobot.event.EventBus;

/**
 * 门诊费用 - 未缴费处方单信息(STEP 1)
 * Created by 毅 on 2016/1/11.
 */
public class OutpatientChargeListFragment extends HospBaseFragment {

    @Bind(R.id.outpatient_charge_list_name)
    TextView mTvPatientName;

    @Bind(R.id.outpatient_charge_list_hospital_name)
    TextView mTvHospitalName;

    @Bind(R.id.outpatient_charge_list_card_no)
    TextView mTvCardNo;

    @Bind(R.id.outpatient_charge_list_card_balance)
    TextView mTvCardBalance;

    @Bind(R.id.outpatient_charge_recipe_list)
    ListView mLvRecipeList;

    @Bind(R.id.outpatient_charge_list_next)
    Button mBtnNext;

    @Bind(R.id.outpatient_charge_stateful_layout)
    StatefulLayout mStatefulLayout;

    @OnClick(R.id.outpatient_charge_list_next)
    void onNext() {
            EventBus.getDefault().post(this);
    }

    T_Phr_BaseInfo mMember;
    T_Phr_CardBindRec mCardInfo;
    G1011 mPatientInfo;
    private List<RecipeList> recipeList;
    OutpatientChargeListAdatpter mAdapter;

    public static OutpatientChargeListFragment newInstance(T_Phr_BaseInfo member,T_Phr_CardBindRec cardInfo,G1011 patient, List<RecipeList> recipeList) {

        Bundle args = new Bundle();
        if (patient != null) {
            args.putSerializable("patient",patient);
        }
        args.putSerializable("member",member);
        args.putSerializable("cardInfo",cardInfo);
        if (recipeList != null) {
            args.putSerializable("recipeList", (Serializable) recipeList);
        }
        OutpatientChargeListFragment fragment = new OutpatientChargeListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        recipeList = (List<RecipeList>) bundle.get("recipeList");
        mPatientInfo = (G1011) bundle.get("patient");
        mMember = (T_Phr_BaseInfo) bundle.get("member");
        mCardInfo = (T_Phr_CardBindRec) bundle.get("cardInfo");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_outpatient_charge_list, container, false);
        ButterKnife.bind(this, v);
        mBtnNext.setEnabled(!recipeList.isEmpty());

        mAdapter = new OutpatientChargeListAdatpter(getActivity(), recipeList);
        mLvRecipeList.setAdapter(mAdapter);
        mLvRecipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeList recipe = recipeList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(RecipeDetailActivity.KEY_RECIPE_LIST,recipe);
                bundle.putSerializable(RecipeDetailActivity.KEY_CARDD_INFO,mCardInfo);
                bundle.putSerializable(RecipeDetailActivity.KEY_FAMILY_INFO, mMember);
                getBaseActivity().openActivity(RecipeDetailActivity.class,bundle);
            }
        });

        mTvCardNo.setText(mCardInfo.getCardNo());
        mTvPatientName.setText(mMember.getName());
        mTvCardBalance.setText(CommonUtils.formatCash(Double.valueOf(mPatientInfo.getMoney()), "元"));
        mTvHospitalName.setText(GlobalSettings.INSTANCE.getHospitalName());

        mStatefulLayout.setEmptyView(View.inflate(getContext(), R.layout.layout_empty_item, null));
        if (recipeList==null||recipeList.isEmpty()) {
            mStatefulLayout.showEmpty();
        } else {
            mStatefulLayout.showContent();
        }

        return v;
    }

    public class OutpatientChargeListAdatpter extends ArrayAdapter<RecipeList> {

        public OutpatientChargeListAdatpter(Context context, List<RecipeList> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder viewholder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recipe_list_v2, parent, false);
                viewholder = new Viewholder(convertView);
                convertView.setTag(viewholder);
            } else viewholder = (Viewholder) convertView.getTag();

            RecipeList list = getItem(position);
            RecipeInfo info = list.getRecipeInfo();
            viewholder.recipeFee.setText(String.format("需要缴纳：%s元", CommonUtils.formatCash(Double.valueOf(info.getTotalFee()))));
            if (TextUtil.isEmpty(info.getDoctName())) {
                viewholder.doctName.setVisibility(View.INVISIBLE);
            } else {
                viewholder.doctName.setVisibility(View.VISIBLE);
                viewholder.doctName.setText(String.format("医生：%s",info.getDoctName()));
            }
            viewholder.execLocation.setText(String.format("执行科室：%s",info.getDeptName()));

            return convertView;
        }
    }

    public class Viewholder {

        @Bind(R.id.item_recipe_list_v2_fee)
        TextView recipeFee;

        @Bind(R.id.item_recipe_list_v2_loccation)
        TextView execLocation;

        @Bind(R.id.item_recipe_list_v2_doctor)
        TextView doctName;

        public Viewholder(View parentView) {
            ButterKnife.bind(this, parentView);
        }
    }

}
