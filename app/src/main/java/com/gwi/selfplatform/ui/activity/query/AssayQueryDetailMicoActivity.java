package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.AutoWrapView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AssayQueryDetailMicoActivity extends HospBaseActivity {
    @Bind(R.id.patient_name)
    TextView patientName;
    @Bind(R.id.send_department)
    TextView sendDepartment;
    @Bind(R.id.project_name)
    TextView projectName;
    @Bind(R.id.assay_time)
    TextView assayTime;
    @Bind(R.id.assay_departments)
    TextView assayDepartments;
    @Bind(R.id.assay_doctor)
    TextView assayDoctor;
    @Bind(R.id.sample)
    TextView sample;
    @Bind(R.id.wrap_view)
    AutoWrapView wrapView;


    @Override
    protected void initViews() {
        setTitle(R.string.label_assay_details_mico);
        //setTitle(R.string.label_hospital_costs);

        patientName.setText("张丽");
        sendDepartment.setText("妇产科");
        projectName.setText("产科二维B超");
        sample.setText("血液");
        assayTime.setText("20150112 04:30");
        assayDepartments.setText("检验科");
        assayDoctor.setText("李四");
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assay_query_detail_mico);
        ButterKnife.bind(this);

        initViews();
        initEvents();

        if (GlobalSettings.INSTANCE.MODE_LOCAL) {
            clearAutoWrapView();
            addAutoWrapView(creatAutoWrapHeader());
            final int size = 4;
            for (int i = 0; i < size; i++) {
                addAutoWrapView(creatAutoWrapItem(size - 1 == i, null));
            }
        }
    }


    protected void removeAutoWrapItem(View view) {
        wrapView.removeView(view);
    }

    protected void clearAutoWrapView() {
        wrapView.removeAllViews();
    }

    protected void addAutoWrapView(View view) {
        wrapView.addView(view);
    }

    private View creatAutoWrapHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_assay_detail_mico_header, null);
        view.findViewById(R.id.ly_root).setBackgroundColor(getResources().getColor(R.color.box_title_color));
        view.findViewById(R.id.line_bottom).setVisibility(View.GONE);
        return view;
    }

    protected View creatAutoWrapItem(boolean isLast, Object obj) {
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_assay_detail_mico_header, null);
        view.findViewById(R.id.line_bottom).setVisibility(isLast ? View.VISIBLE : View.GONE);

        ((TextView) view.findViewById(R.id.txt_bacterial_name)).setText("金色葡萄糖球菌");
        ((TextView) view.findViewById(R.id.txt_antibiotics_name)).setText("青霉素");
        ((TextView) view.findViewById(R.id.txt_susceptibility_result)).setText("耐药");
        ((TextView) view.findViewById(R.id.txt_num_value)).setText("12");
        ((TextView) view.findViewById(R.id.txt_bacterial_num)).setText("12(12%)");
        return view;
    }
}
