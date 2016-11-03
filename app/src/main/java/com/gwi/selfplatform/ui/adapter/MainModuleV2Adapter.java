package com.gwi.selfplatform.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwi.phr.hospital.BuildConfig;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.IMethodCallback;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.module.pay.common.PayMode;
import com.gwi.selfplatform.ui.activity.encyclopedia.HealthEncyclopediaActivity;
import com.gwi.selfplatform.ui.activity.expand.HealthEduActivity;
import com.gwi.selfplatform.ui.activity.expand.IndoorNavActivity;
import com.gwi.selfplatform.ui.activity.expand.MapNavActivity;
import com.gwi.selfplatform.ui.activity.expand.ParkingCarActivity;
import com.gwi.selfplatform.ui.activity.nav.SmartTreatActivity;
import com.gwi.selfplatform.ui.activity.pay.MedicalCardChargeActivity;
import com.gwi.selfplatform.ui.activity.pay.OutpatientChargesActivity;
import com.gwi.selfplatform.ui.activity.query.AssayQueryActivity;
import com.gwi.selfplatform.ui.activity.query.CostsQueryActivity;
import com.gwi.selfplatform.ui.activity.query.GuideQueryActivity;
import com.gwi.selfplatform.ui.activity.query.HospInfoQueryActivity;
import com.gwi.selfplatform.ui.activity.query.HospitalIntroductionV2Activity;
import com.gwi.selfplatform.ui.activity.query.InspectionQueryActivity;
import com.gwi.selfplatform.ui.activity.query.PayRecordActivity;
import com.gwi.selfplatform.ui.activity.query.PriceQueryV2Activity;
import com.gwi.selfplatform.ui.activity.query.QueueQueryActivity;
import com.gwi.selfplatform.ui.activity.query.RegistQueryActivity;
import com.gwi.selfplatform.ui.activity.registration.RegistrationSelectV2Activity;
import com.gwi.selfplatform.ui.activity.start.HomeActivity;
import com.gwi.selfplatform.ui.activity.user.PersonalCenterV2Activity;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.base.WebActivity;
import com.gwi.selfplatform.ui.beans.MainModuleBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/12 0012.
 */
public class MainModuleV2Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MainModuleBean> mList;
    private LayoutInflater mInflater;

    /**
     *
     * @param context context
     */
    public MainModuleV2Adapter(Context context) {
        mContext = context;
        mList = getDataList();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_module, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MainModuleBean item = mList.get(position); //getItem(position);

        holder.ivIcon.setImageResource(item.getResIcon());
        holder.ivTips.setImageResource(item.getResTips());
        holder.txtDesp.setText(item.getResDescription());
        holder.txtDesp.setTextColor(mContext.getResources().getColor(item.getResDespColor()));
        holder.lyRoot.setBackgroundColor(mContext.getResources().getColor(item.getResBgColor()));

        holder.lyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getCallback() != null) {
                    item.getCallback().callback(null);
                }
            }
        });
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_module.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.txt_desp)
        TextView txtDesp;
        @Bind(R.id.iv_tips)
        ImageView ivTips;
        @Bind(R.id.ly_root)
        LinearLayout lyRoot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public boolean isShowHomeItem(int resId) {
        return HospitalParams.isShowHomeItem(resId, getFieldKeys());
    }

    private List<String> getFieldKeys() {
        String value = GlobalSettings.INSTANCE.getHospitalParams().get(HospitalParams.CODE_MOBILE_FUNCTION_LIST);
        return HospitalParams.getFields(value);
    }

    private void openModuleActivity(Class<?> pclass) {
        openModuleActivity(pclass, null);
    }

    private void openModuleActivity(Class<?> pclass, Bundle pBundle) {
        if (GlobalSettings.INSTANCE.isIsLogined()) {
            ((HospBaseActivity) mContext).openActivity(pclass, pBundle);
        } else {
            if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
                return;
            }
            ((HomeActivity) mContext).toLogin();
        }
    }

    private ArrayList<MainModuleBean> getDataList() {
        ArrayList<MainModuleBean> list = new ArrayList<>();

        if (isShowHomeItem(R.string.label_smart_leading_examining)) {
            list.add(new MainModuleBean(R.color.bg_smart_leading_examining, R.string.label_smart_leading_examining,
                    R.color.desp_smart_leading_examining, R.drawable.smart_leading_examining, R.drawable.smart_leading_examining2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(SmartTreatActivity.class);
                        }
                    }
            ));
        }

        if (isShowHomeItem(R.string.label_registered)) {
            list.add(new MainModuleBean(R.color.bg_registered, R.string.label_registered, R.color.desp_registered,
                    R.drawable.registered, R.drawable.registered2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(RegistrationSelectV2Activity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_pay_cost)) {
            list.add(new MainModuleBean(R.color.bg_pay_cost, R.string.label_pay_cost, R.color.desp_pay_cost,
                    R.drawable.pay_cost, R.drawable.pay_cost2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(OutpatientChargesActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_top_up)) {
            list.add(new MainModuleBean(R.color.bg_top_up, R.string.label_top_up, R.color.desp_top_up,
                    R.drawable.top_up, R.drawable.top_up2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(MedicalCardChargeActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_waiting_in_line)) {
            list.add(new MainModuleBean(R.color.bg_waiting_in_line, R.string.label_waiting_in_line, R.color.desp_waiting_in_line,
                    R.drawable.waiting_in_line, R.drawable.waiting_in_line2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(QueueQueryActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_electronic_leading_examining)) {
            list.add(new MainModuleBean(R.color.bg_electronic_leading_examining, R.string.label_electronic_leading_examining, R.color.desp_electronic_leading_examining,
                    R.drawable.electronic_leading_examining, R.drawable.electronic_leading_examining2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(GuideQueryActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_registration_record)) {
            list.add(new MainModuleBean(R.color.bg_registration_record, R.string.label_registration_record, R.color.desp_registration_record,
                    R.drawable.registration_record, R.drawable.registration_record2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(RegistQueryActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_outpatient_service_charge)) {
            list.add(new MainModuleBean(R.color.bg_outpatient_service_charge, R.string.label_outpatient_service_charge, R.color.desp_outpatient_service_charge,
                    R.drawable.outpatient_service_charge, R.drawable.outpatient_service_charge2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(CostsQueryActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_payment_order)) {
            list.add(new MainModuleBean(R.color.bg_payment_order, R.string.label_payment_order, R.color.desp_payment_order,
                    R.drawable.payment_order, R.drawable.payment_order2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(PayRecordActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_among)) {
            list.add(new MainModuleBean(R.color.bg_among, R.string.label_among, R.color.desp_among,
                    R.drawable.among, R.drawable.among2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(AssayQueryActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_inspection_report)) {
            list.add(new MainModuleBean(R.color.bg_inspection_report, R.string.label_inspection_report, R.color.desp_inspection_report,
                    R.drawable.inspection_report, R.drawable.inspection_report2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(InspectionQueryActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_price_inquiry)) {
            list.add(new MainModuleBean(R.color.bg_price_inquiry, R.string.label_price_inquiry, R.color.desp_price_inquiry,
                    R.drawable.price_inquiry, R.drawable.price_inquiry2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(PriceQueryV2Activity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_health_information)) {
            list.add(new MainModuleBean(R.color.bg_health_information, R.string.label_health_information, R.color.desp_health_information,
                    R.drawable.health_information, R.drawable.health_information2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(HealthEduActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_medical_encyclopedia)) {
            list.add(new MainModuleBean(R.color.bg_medical_encyclopedia, R.string.label_medical_encyclopedia, R.color.desp_medical_encyclopedia,
                    R.drawable.medical_encyclopedia, R.drawable.medical_encyclopedia2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(HealthEncyclopediaActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_map_navigation)) {
            list.add(new MainModuleBean(R.color.bg_map_navigation, R.string.label_map_navigation, R.color.desp_map_navigation,
                    R.drawable.map_navigation, R.drawable.map_navigation2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(MapNavActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_indoor_navigation)) {
            list.add(new MainModuleBean(R.color.bg_map_navigation, R.string.label_indoor_navigation, R.color.desp_map_navigation,
                    R.drawable.map_navigation, R.drawable.map_navigation2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(IndoorNavActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_hospital_news)) {
            list.add(new MainModuleBean(R.color.bg_map_navigation, R.string.label_hospital_news, R.color.desp_map_navigation,
                    R.drawable.map_navigation, R.drawable.map_navigation2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.KEY_BUNDLE, Constants.HospInfoQuery.HOSPITAL_NEWS);
                            openModuleActivity(HospInfoQueryActivity.class, bundle);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_treatment_guidelines)) {
            list.add(new MainModuleBean(R.color.bg_map_navigation, R.string.label_treatment_guidelines, R.color.desp_map_navigation,
                    R.drawable.map_navigation, R.drawable.map_navigation2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.KEY_BUNDLE, Constants.HospInfoQuery.TREATMENT_GUIDELINES);
                            openModuleActivity(HospInfoQueryActivity.class, bundle);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_pharmacy)) {
            list.add(new MainModuleBean(R.color.bg_pharmacy, R.string.label_pharmacy, R.color.desp_pharmacy,
                    R.drawable.pharmacy, R.drawable.pharmacy2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            // openModuleActivity(MapNavActivity.class);
                            Bundle b = new Bundle();
                            b.putString(WebActivity.KEY_TITLE, "线上药房");
                            b.putString(WebActivity.KEY_URL, "http://wei.yunzhi120.com/medicine?hospital_id=52");
                            // openActivity(OnlinePharmacyActivity.class, b);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_admitted_to_hospital)) {
            list.add(new MainModuleBean(R.color.bg_admitted_to_hospital, R.string.label_admitted_to_hospital, R.color.desp_admitted_to_hospital,
                    R.drawable.admitted_to_hospital, R.drawable.admitted_to_hospital2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
//                ActivityInfo activityInfo = new ActivityInfo();
//                activityInfo.packageName = "com.gwi.bedsideterminal";
//                activityInfo.targetActivity = "com.gwi.bedsideterminal.entities.activity.DemoMainActivity";
//                CommonUtils.startNewActivity(this, activityInfo, false);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_parking)) {
            list.add(new MainModuleBean(R.color.bg_parking, R.string.label_parking, R.color.desp_parking,
                    R.drawable.parking, R.drawable.parking2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
                            openModuleActivity(ParkingCarActivity.class);
                        }
                    }));
        }

        if (isShowHomeItem(R.string.label_hospital_is_introduced)) {
            list.add(new MainModuleBean(R.color.bg_hospital_is_introduced, R.string.label_hospital_is_introduced, R.color.desp_hospital_is_introduced,
                    R.drawable.hospital_is_introduced, R.drawable.hospital_is_introduced2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {

                            openModuleActivity(HospitalIntroductionV2Activity.class);
                        }
                    }));
        }


        if (isShowHomeItem(R.string.label_user_center)) {
            list.add(new MainModuleBean(R.color.bg_user_center, R.string.label_user_center, R.color.desp_user_center,
                    R.drawable.user_center, R.drawable.user_center2,
                    new IMethodCallback<Void>() {
                        @Override
                        public void callback(Void callback) {
//                            if (PayMode.getInstance().isCCBPaySupport() && BuildConfig.DEBUG) {
//                                PayMode.getInstance().createFakeCCBPay(mContext);
//                            } else {
                                openModuleActivity(PersonalCenterV2Activity.class);
//                            }
                        }
                    }));
        }

        return list;
    }
}
