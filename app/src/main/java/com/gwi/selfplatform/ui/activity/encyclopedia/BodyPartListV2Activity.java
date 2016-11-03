package com.gwi.selfplatform.ui.activity.encyclopedia;

import android.os.Bundle;
import android.widget.ListView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.module.net.beans.KBBodyPart;
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.adapter.BodyPartListV2Adapter;
import com.gwi.selfplatform.ui.adapter.DiseaseCommonAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by Administrator on 2016/2/26 0026.
 */
public class BodyPartListV2Activity extends HospBaseActivity {
    private static final String TAG = BodyPartListV2Activity.class.getSimpleName();

    /**
     * 一级列表
     */
    private BodyPartListV2Adapter mFristAdapter = null;
    private List<KBBodyPart> mFristListData = null;

    /**
     * 二级列表
     */
    private DiseaseCommonAdapter mSecondAdapter = null;
    private List<KBDiseaseDetails> mSecondListData = null;

    @Bind(R.id.lv_frist_level)
    ListView mLvFristLevel;
    @Bind(R.id.lv_second_level)
    ListView mLvSecondLevel;

    @OnItemClick(R.id.lv_frist_level)
    void onFirstLevelClick(int position) {
        if (null != mFristListData && mFristListData.size() > 0) {
            setTitle(mFristListData.get(position).getName());
            String bodyPartCode = mFristListData.get(position).getCode();
            requestSecondLevel(bodyPartCode, null);
        }
    }

    @OnItemClick(R.id.lv_second_level)
    void onSecondLevelClick(int position) {
        if (null != mSecondListData && mSecondListData.size() > 0) {
            KBDiseaseDetails disease = mSecondListData.get(position);
            Bundle b = new Bundle();
            b.putString("DiseaseId", disease.getDiseaseId());
            b.putString("DiseaseName", disease.getDiseaseName());
            openActivity(DiseaseDetailsActivity.class, b);
        }
    }

    @Override
    protected void initViews() {
        setTitle("按部位查询");

        mFristListData = new ArrayList<>();
        mFristAdapter = new BodyPartListV2Adapter(this, mFristListData);
        mLvFristLevel.setAdapter(mFristAdapter);
        mLvFristLevel.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // mLvFristLevel.setClickable(true);

        mSecondListData = new ArrayList<>();
        mSecondAdapter = new DiseaseCommonAdapter(this, mSecondListData);
        mLvSecondLevel.setAdapter(mSecondAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);
        ButterKnife.bind(this);
        addHomeButton();
        initViews();

        requestFirstLevel();
    }

    private void requestFirstLevel() {
        doCancellableAsyncTask(this, getString(R.string.dialog_content_loading), new AsyncCallback<List<KBBodyPart>>() {
            @Override
            public List<KBBodyPart> callAsync() throws Exception {
                try {
                    return ApiCodeTemplate.getBodyPartsAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onCallFailed(Exception exception) {
                CommonUtils.showError(BodyPartListV2Activity.this, exception);
            }

            @Override
            public void onPostCall(List<KBBodyPart> kbBodyParts) {
                if (null != kbBodyParts && kbBodyParts.size() > 0) {
                    mFristListData.addAll(kbBodyParts);
                    mFristAdapter.notifyDataSetChanged();

                    mLvFristLevel.setItemChecked(0, true);
                    mLvFristLevel.performItemClick(mLvFristLevel.getChildAt(0), 0, mLvFristLevel.getItemIdAtPosition(0));
                }
            }
        });
    }

    private void requestSecondLevel(final String bodyPartCode, final String deptCode) {
        doCancellableAsyncTask(this, getString(R.string.dialog_content_loading), new AsyncCallback<List<KBDiseaseDetails>>() {
            @Override
            public List<KBDiseaseDetails> callAsync() throws Exception {
                try {
                    return ApiCodeTemplate.getDiseaseList(bodyPartCode, deptCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onCallFailed(Exception exception) {
                CommonUtils.showError(BodyPartListV2Activity.this, exception);
            }

            @Override
            public void onPostCall(List<KBDiseaseDetails> kbDiseaseDetailses) {
                if (null != kbDiseaseDetailses && kbDiseaseDetailses.size() > 0) {
                    mSecondListData.clear();
                    mSecondListData.addAll(kbDiseaseDetailses);
                    mSecondAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
