package com.gwi.selfplatform.ui.activity.query;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.module.net.response.G1217;
import com.gwi.selfplatform.ui.adapter.WaitingQueueDepartAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 科室排队叫号页面
 * @author 彭毅
 *
 */
public class WaitingQueueDepartActivity extends HospBaseActivity {

    private static final String TAG = WaitingQueueDepartActivity.class.getSimpleName();

    private TextView mTvRefreshTime;
    private ListView mLvDepart;
    private TextView mTvEmptyText;
    private View mLoadingView;
    private G1211 mDepart;
    private List<G1217> mQueueList;
    private WaitingQueueDepartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_queue_depart);
        addHomeButton();
        Bundle b = getIntent().getExtras();
        if(b!=null&&b.containsKey(Constants.KEY_DEPT)) {
            mDepart = (G1211) b.get(Constants.KEY_DEPT);
            setTitle(mDepart.getDeptName());
        }else {
            showToast(R.string.msg_error_common);
            finish();
        }
        
        initViews();
        initEvents();
        getDepartQueueList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    protected void initViews() {
        mTvRefreshTime = (TextView) findViewById(R.id.waiting_queue_dept_refresh_time);
        mLvDepart = (ListView) findViewById(R.id.waiting_queue_dept_list);
        mTvEmptyText = (TextView) findViewById(R.id.empty_text);
        mLoadingView = findViewById(R.id.loadingview);
        
        mQueueList = new ArrayList<>();
        mAdapter = new WaitingQueueDepartAdapter(this, mQueueList);
        mLvDepart.setAdapter(mAdapter);
        mLvDepart.setEmptyView(mTvEmptyText);
        
        mTvRefreshTime.setText("--:--");
    }

    @Override
    protected void initEvents() {

    }
    
    public void onRefreshClick(View v) {
        getDepartQueueList();
    }
    
    private void getDepartQueueList() {
        ApiCodeTemplate.loadDepartQueueListAsync(TAG, mLoadingView, mDepart.getDeptID(), new RequestCallback<List<G1217>>() {
            @Override
            public void onRequestSuccess(List<G1217> result) {
                mQueueList.clear();
                if (result != null && !result.isEmpty()) {
                    CommonUtils.removeNull(result);
                    mQueueList.addAll(result);
                    //特定排序
                    final Collator collator = ((RuleBasedCollator) Collator.getInstance(Locale.CHINA));
                    Collections.sort(mQueueList, new Comparator<G1217>() {

                        @Override
                        public int compare(G1217 lhs, G1217 rhs) {
                            try {
                                //二次排序：优先级是诊室>序号
                                int firstVal = collator.compare(lhs.getExecLocation(), rhs.getExecLocation());
                                if (firstVal == 0) {
                                    return Integer.parseInt(lhs.getCurrentNo())- Integer.parseInt(rhs.getCurrentNo());
                                }
                                return firstVal;
                            } catch (Exception e) {

                            }
                            return 0;
                        }
                    });
                }
                mAdapter.notifyDataSetChanged();
                updateTime();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(WaitingQueueDepartActivity.this, (Exception) error.getException());
                mQueueList.clear();
                mAdapter.notifyDataSetChanged();
                updateTime();
            }
        });

//        doProgressAsyncTask(mLoadingView, new AsyncCallback<List<G1217>>() {
//
//            @Override
//            public List<G1217> callAsync() throws Exception {
//                Request<T1217> request = new Request<T1217>();
//                request.setHeader(new THeader());
//                request.getHeader().setFunCode(1217);
//                request.getHeader().setAppTypeCode(WebUtil.APP_TYPE_CODE);
//                request.getHeader().setAppCode(WebUtil.APPCODE);
//                request.getHeader().setReqTime(
//                        CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date()));
//                request.setBody(new T1217());
//                request.getBody().setHospCode(WebUtil.HOSP_CODE);
//                request.getBody().setDeptID(mDepart.getDeptID());
//                request.getBody().setTerminalNo(WebUtil.TERMINAL_NO());
//                JSONObject bodyData = WebUtil.execute(request, true);
//                Type lt = new TypeToken<List<G1217>>() {
//                }.getType();
//                List<G1217> result = new ArrayList<G1217>();
//                result = JsonUtil.toListObject(bodyData, "Item", G1217.class, lt);
//                return result;
//            }
//
//            @Override
//            public void onPostCall(List<G1217> result) {
//                mQueueList.clear();
//                if (result != null && !result.isEmpty()) {
//                    CommonUtils.removeNull(result);
//                    mQueueList.addAll(result);
//                    //特定排序
//                    final Collator collator = ((RuleBasedCollator) Collator.getInstance(Locale.CHINA));
//                    Collections.sort(mQueueList, new Comparator<G1217>() {
//
//                        @Override
//                        public int compare(G1217 lhs, G1217 rhs) {
//                            try {
//                                //二次排序：优先级是诊室>序号
//                                int firstVal = collator.compare(lhs.getExecLocation(), rhs.getExecLocation());
//                                if (firstVal == 0) {
//                                    return Integer.parseInt(lhs.getCurrentNo()).compareTo(Integer.parseInt(rhs.getCurrentNo()));
//                                }
//                                return firstVal;
//                            } catch (Exception e) {
//
//                            }
//                            return 0;
//                        }
//                    });
//                }
//                mAdapter.notifyDataSetChanged();
//                updateTime();
//            }
//
//            @Override
//            public void onCallFailed(Exception exception) {
//                if (exception.getLocalizedMessage() != null) {
//                    showToast(exception.getLocalizedMessage());
//                    mQueueList.clear();
//                    mAdapter.notifyDataSetChanged();
//                    updateTime();
//                } else {
//                    showToast(R.string.msg_service_disconnected);
//                }
//            }
//        });
    }
    
    private void updateTime() {
        mTvRefreshTime.setText(CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date()));
    }

}
