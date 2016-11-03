package com.gwi.selfplatform.ui.activity.query;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.exception.BaseException;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.JsonUtil;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.request.T2111;
import com.gwi.selfplatform.module.net.response.BasicInfo;
import com.gwi.selfplatform.module.net.response.G2110;
import com.gwi.selfplatform.module.net.response.G2111;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.AutoWrapView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AssayQueryDetailCommActivity extends HospBaseActivity {
    private static final String TAG = AssayQueryDetailCommActivity.class.getSimpleName();

    // private View mLoadingView;
    private List<G2111> mG2111List;
    private BasicInfo mReportBasicInfo;
    private G2110 mG2110Info;

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
        setTitle(R.string.label_assay_details_comm);
    }

    private void refreshUpBox(BasicInfo info) {
        if (null == info) {
            return;
        }

        if (null != GlobalSettings.INSTANCE.getCurrentFamilyAccount()) {
            patientName.setText(GlobalSettings.INSTANCE.getCurrentFamilyAccount().getName());
        }
        sendDepartment.setText(info.getSendDeptName());
        projectName.setText(info.getRepName());
        sample.setText(null == mG2110Info ? "" : mG2110Info.getSampName().trim());
        assayTime.setText(info.getRepTime());
        // assayDepartments.setText(info.getRepDeptName());
        assayDepartments.setText(null == mG2110Info ? "" : mG2110Info.getRepDeptName());
        assayDoctor.setText(info.getRepDoctName());
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assay_query_detail_comm);
        ButterKnife.bind(this);

        initData();
        initViews();
        initEvents();

        ceshi();
    }

    private void initData() {
        mG2111List = new ArrayList<>();
        mG2110Info = (G2110) getIntent().getSerializableExtra(Constants.KEY_BUNDLE);
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
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_assay_detail_comm_header, null);
        view.findViewById(R.id.ly_root).setBackgroundColor(getResources().getColor(R.color.box_title_color));
        view.findViewById(R.id.line_bottom).setVisibility(View.GONE);
        return view;
    }

    protected View creatAutoWrapItem(boolean isLast, Object obj) {
        if (null == obj) {
            return null;
        }
        G2111 item = (G2111) obj;
        View view = LayoutInflater.from(this).inflate(R.layout.listitem_assay_detail_comm_header, null);
        view.findViewById(R.id.line_bottom).setVisibility(isLast ? View.VISIBLE : View.GONE);

        ((TextView) view.findViewById(R.id.txt_project_name)).setText(item.getItemName());
        ((TextView) view.findViewById(R.id.txt_reference)).setText(item.getReference());
        // ((TextView) view.findViewById(R.id.txt_result_value)).setText(item.getResult());
        ((TextView) view.findViewById(R.id.txt_unit)).setText(item.getItemUnit());

        // item.setHighLowFlag("BBB");
        if (item.getHighLowFlag() != null && !item.getHighLowFlag().isEmpty()) {
            int length = item.getHighLowFlag().length();
            String tmp = item.getResult() + item.getHighLowFlag();
            ((TextView) view.findViewById(R.id.txt_result_value)).setText(TextUtil.colorText(tmp, Color.RED, tmp.length() - length, tmp.length()));
        } else {
            ((TextView) view.findViewById(R.id.txt_result_value)).setText(item.getResult());
        }

        return view;
    }

    public void ceshi() {
        showLoadingDialog(getString(R.string.common_loading));

        AsyncTasks.doSilAsyncTask(null, new AsyncCallback<List<G2111>>() {
            @Override
            public List<G2111> callAsync() throws Exception {
                Request<T2111> request = new Request<>();
                Request.commonHeader(request, 2111, false);
                request.setBody(new T2111());
                request.getBody().setHospCode(WebUtil.HOSP_CODE);
                request.getBody().setTerminalNo(WebUtil.TERMINAL_NO());
                if (mG2110Info != null) {
                    request.getBody().setRepNo(mG2110Info.getRepNo());
                }
                request.getBody().setNote("123");
                final List<G2111> result = new ArrayList<G2111>();
                WebUtil.httpExecute(request, new WebUtil.JSONResponseHandler() {

                    @Override
                    public void handleJsonResponse(JSONObject obj) {
                        try {
                            JSONObject responseObj = obj.getJSONObject("Response");
                            JSONObject headerObj = responseObj.getJSONObject("Header");
                            int status = headerObj.getInt("Status");
                            if (status != 1) {
                                String resultMsg = headerObj.getString("ResultMsg");
                                throw new BaseException(status, resultMsg);
                            }
                            if (!responseObj.getJSONObject("Body").isNull("Items")) {
                                JSONObject bodyData = responseObj.getJSONObject("Body").getJSONObject("Items");
                                Type lt = new TypeToken<List<G2111>>() {
                                }.getType();
                                result.addAll(JsonUtil.toListObject(bodyData, "Item", G2111.class, lt));
                            }
                            if (!responseObj.getJSONObject("Body").isNull("BasicInfo")) {
                                JSONObject basicInfo = responseObj.getJSONObject("Body").getJSONObject("BasicInfo");
                                mReportBasicInfo = JsonUtil.toObject(basicInfo, BasicInfo.class);
                            }
                        } catch (Exception e) {
                            Logger.e(TAG, "handleJsonResponse", e);
                        }
                    }
                });
                return result;
            }

            @Override
            public void onPostCall(List<G2111> result) {
                dismissLoadingDialog();

                refreshUpBox(mReportBasicInfo);

                CommonUtils.removeNull(result);
                if (result != null && result.size() > 0) {
                    clearAutoWrapView();
                    addAutoWrapView(creatAutoWrapHeader());
                    final int size = result.size();
                    for (int i = 0; i < size; i++) {
                        View view = creatAutoWrapItem(size - 1 == i, result.get(i));
                        if (null != view) {
                            addAutoWrapView(view);
                        }
                    }
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e(TAG, exception.getLocalizedMessage() + "");
                dismissLoadingDialog();
            }
        });
    }
}
