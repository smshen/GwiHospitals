package com.gwi.selfplatform.ui.fragment.registration;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.ccly.android.commonlibrary.common.net.connector.GWINet;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.interfaces.INoCardCallback;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.config.HospitalParams;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.connector.implement.tRequest.Request;
import com.gwi.selfplatform.module.net.request.T1211;
import com.gwi.selfplatform.module.net.request.T1411;
import com.gwi.selfplatform.module.net.response.G1011;
import com.gwi.selfplatform.module.net.response.G1017;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.ui.activity.registration.DoctorSelectActivity;
import com.gwi.selfplatform.ui.activity.user.MedicalCardAddActivity;
import com.gwi.selfplatform.ui.activity.user.MyMedicalCardActivity;
import com.gwi.selfplatform.ui.adapter.DepartAlpabetAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.ExSideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 按字母表显示的一级科室列表
 * @author 彭毅
 *
 */
public class DepartAlphabetFragment extends Fragment {
    
    private static final String TAG = DepartAlphabetFragment.class.getSimpleName();
    
    private View mLoadingView;
    private ExpandableListView mExList;
    private TextView mEmptyText;
    private DepartAlpabetAdapter mAdapter;
    
    // 指示域
    private TextView mDialogText;
    WindowManager mManager;
    private ExSideBar mSideBar;
    private List<G1211> mDeptList;
    //input params from calling activity.
    private Class<?> mNextClz = null;
    private boolean mIsTypeRegist = true;
    private String mDateOrderStr = null;
    boolean mIsDataFromPhr=false;
    G1017 mSelectedSubHos = null;
    private String mTypeID;

    public void setSelectedSubHos(G1017 selectedSubHos) {
        mSelectedSubHos = selectedSubHos;
    }

    public void setTypeID(String typeID) {
        mTypeID = typeID;
    }

    private OnChildClickListener mOnChildClickListener = new OnChildClickListener() {
        
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                int groupPosition, int childPosition, long id) {
            G1211 depart = (G1211) v.getTag();
            Bundle b = new Bundle();
            b.putBoolean(Constants.KEY_IS_TYPE_REGIST, mIsTypeRegist);
            b.putSerializable(Constants.KEY_DEPT, depart);
            // TODO:默认type id 为2:普通挂号.
            if (!TextUtil.isEmpty(mTypeID)) {
                b.putString(Constants.KEY_TYPE_ID, mTypeID);
            }
            b.putString(Constants.KEY_ORDER_DATE, mDateOrderStr);
            if(mSelectedSubHos!=null) {
                b.putParcelable(Constants.KEY_SUB_HOSPITAL, mSelectedSubHos);
            }
            Map<String, String> list = GlobalSettings.INSTANCE.getHospitalParams();
            String value = list.get(HospitalParams.CODE_ORDER_DATE_SELECT_POSITION);
//            if (HospitalParams.VALUE_TWO.equals(value)&&!mIsTypeRegist) {
//                getBaseActivity().openActivity(mNextClz == null ? DoctorsBeforeDateSelectActivity.class : mNextClz, b);
//            }else {
//                getBaseActivity().openActivity(mNextClz==null?DoctorActivity.class:mNextClz, b);
//            }
            getBaseActivity().openActivity(DoctorSelectActivity.class,b);

            return false;
        }
    };
    
    /**
     * 创建预约或挂号科室列表
     * @param isRegist
     * @param dateOrderStr
     * @param nextClz
     * @return
     */
    public static DepartAlphabetFragment newInstance(boolean isRegist,String dateOrderStr,Class<?> nextClz) {
        DepartAlphabetFragment f = new DepartAlphabetFragment();
        f.mIsTypeRegist = isRegist;
        f.mDateOrderStr = dateOrderStr;
        f.mNextClz = nextClz;
        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadDate();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDataFromArguments();
        //添加字母指示视图
        if (mDialogText != null) {
            mDialogText.setVisibility(View.INVISIBLE);
        }

        mDeptList = new ArrayList<G1211>();
        mAdapter = new DepartAlpabetAdapter(getActivity(), mDeptList, mExList);
        mEmptyText.setText(R.string.msg_no_record);
        mEmptyText.setVisibility(View.GONE);
        mExList.setEmptyView(mEmptyText);
        mExList.setAdapter(mAdapter);
        //屏蔽expand事件
        mExList.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
        mExList.setOnChildClickListener(mOnChildClickListener);
        mSideBar.setExpandableListView(mExList, mAdapter);

        mSideBar.setTextView(mDialogText);

        loadDate();
    }

    private void loadDate() {
        loadDataNewAsync(new RequestCallback<List<G1211>>() {
            @Override
            public void onRequestSuccess(final List<G1211> result) {
                mAdapter.clear();
                if(result!=null&&!result.isEmpty()) {
                    CommonUtils.removeNull(result);

                    Map<String,String> params = GlobalSettings.INSTANCE.getHospitalParams();
                    if (HospitalParams.VALUE_ONE.equals(HospitalParams.getValue(params, HospitalParams.CODE_IS_DEPTS_LIMITED_ENABLED))) {
                        AsyncTasks.doSilAsyncTask(mLoadingView, new AsyncCallback<List<G1211>>() {
                            @Override
                            public List<G1211> callAsync() throws Exception {
                                T_Phr_BaseInfo member = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
                                List<ExT_Phr_CardBindRec> cardBindRecList = ApiCodeTemplate.getBindedCard(member);
                                if (cardBindRecList.isEmpty()) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            CommonUtils.showNoCardDialog(getActivity(),
                                                    new INoCardCallback() {

                                                        @Override
                                                        public void isBindNow(boolean isBind) {
                                                            if (isBind) {
                                                                startActivityForResult(new Intent(getActivity(), MyMedicalCardActivity.class), 1);
                                                            } else {
                                                                mEmptyText
                                                                        .setText(R.string.msg_empty_no_card);
                                                                mEmptyText
                                                                        .setVisibility(View.VISIBLE);
                                                            }
                                                        }
                                                    });
                                        }
                                    });

                                    return Collections.emptyList();
                                } else {
                                    GlobalSettings.INSTANCE.setCurPatientId(cardBindRecList.get(0).getPatientID());
                                    G1011 patientInfo = ApiCodeTemplate.getPatientInfo(getActivity(), cardBindRecList.get(0));
                                    //验证patientID
                                    if (ApiCodeTemplate.checkMedicalCardLogout(getActivity(), patientInfo, member, null)) {
                                        return CommonUtils.filterRegistDeptsByLimits(result, patientInfo);
                                    }
                                    return new ArrayList<>();
                                }
                            }

                            @Override
                            public void onPostCall(List<G1211> g1211s) {
                                mAdapter.addAll(g1211s);
                                //Expand the list.
                                for (int i = 0, n = mAdapter.getGroupCount(); i < n; i++) {
                                    mExList.expandGroup(i);
                                }
                            }

                            @Override
                            public void onCallFailed(Exception exception) {
                                CommonUtils.showError(getBaseActivity(), exception);
                            }
                        });
                    } else {
                        mAdapter.addAll(result);
                        //Expand the list.
                        for(int i=0,n=mAdapter.getGroupCount();i<n;i++) {
                            mExList.expandGroup(i);
                        }
                    }
                }
            }

            @Override
            public void onRequestError(RequestError error) {
                Exception exception = (Exception) error.getException();
                CommonUtils.showError(getBaseActivity(), exception);
                EventBus.getDefault().post(exception);
            }
        });
    }

    private void getDataFromArguments() {
        Bundle b = getArguments();
        if(b!=null){
            if(b.containsKey(Constants.KEY_IS_FROM_PHR)) {
                mIsDataFromPhr = b.getBoolean(Constants.KEY_IS_FROM_PHR, false);
                mNextClz = (Class<?>) b.getSerializable(Constants.KEY_NEXT_ACTIVITY);
            }
            if(b.containsKey(Constants.KEY_SUB_HOSPITAL)) {
                mSelectedSubHos = b.getParcelable(Constants.KEY_SUB_HOSPITAL);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogText = (TextView) View.inflate(getActivity(), R.layout.list_position, null);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        mManager.addView(mDialogText, lp);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.layout_list_alphabet, null);
        mLoadingView = v.findViewById(R.id.progressContainer2);
        mExList= (ExpandableListView) v.findViewById(android.R.id.list);
        mSideBar = (ExSideBar) v.findViewById(R.id.sideBar);
        mExList.setGroupIndicator(null);
        mEmptyText = (TextView) v.findViewById(android.R.id.empty);
        return v;
    }

    private HospBaseActivity getBaseActivity() {
        return (HospBaseActivity) super.getActivity();
    }
    
    

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDetach() {
        if (mDialogText != null) {
            mManager.removeView(mDialogText);
            mDialogText = null;
        }
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadDataNewAsync(RequestCallback<List<G1211>> callback) {
        if(!mIsDataFromPhr) {
            if(mIsTypeRegist) {
                getRegistDeptsAsync(callback);
            }else {
                getAppointDeptsAsync(callback);
            }
        }else {
            getDeptFromPhrAsync(callback);
        }
    }
    
    private void loadDataAsync() {
        AsyncTasks.doSilAsyncTask(mLoadingView, new AsyncCallback<List<G1211>>() {

            @Override
            public List<G1211> callAsync() throws Exception {
//                if(!mIsDataFromPhr) {
//                    if(mIsTypeRegist) {
//                        return getRegistDepts();
//                    }else {
//                        return getAppointDepts();
//                    }
//                }else {
//                    return getDeptFromPhr();
//                }
                return null;
            }

            @Override
            public void onPostCall(List<G1211> result) {
                mAdapter.clear();
                if(result!=null&&!result.isEmpty()) {
                   CommonUtils.removeNull(result);
                   mAdapter.addAll(result);
                   //Expand the list.
                   for(int i=0,n=mAdapter.getGroupCount();i<n;i++) {
                       mExList.expandGroup(i);
                   }
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                Logger.e(TAG, "onCallFailed", exception);
                EventBus.getDefault().post(exception);
            }
        });
    }

    private void getRegistDeptsAsync(RequestCallback callback) {
        Request<T1211> request = new Request<>();
        Request.commonHeader(request, 1211, false);
        request.setBody(new T1211());
        if (mSelectedSubHos != null) {
            request.getBody().setSubHospCode(mSelectedSubHos.getSubHospCode());
        } else {
            request.getBody().setSubHospCode("");
        }
        if (mDateOrderStr != null) {
            request.getBody().setDate(mDateOrderStr);
        }
        request.getBody().setTypeID(mTypeID);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID("");
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null,ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mLoadingView)
                .mappingInto(new TypeToken<List<G1211>>() {})
                .execute(TAG, callback);
    }

//    private List<G1211> getRegistDepts() throws Exception {
//        Request<T1211> request = new Request<T1211>();
//        request.setHeader(new THeader());
//        request.getHeader().setFunCode(1211);
//        request.getHeader().setAppTypeCode("5");
//        request.getHeader().setAppCode("2");
//        request.getHeader().setReqTime(
//                CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME,
//                        new Date()));
//        request.setBody(new T1211());
//        if(mSelectedSubHos!=null) {
//            request.getBody().setSubHospCode(mSelectedSubHos.getSubHospCode());
//        }
//        if (mDateOrderStr != null) {
//            request.getBody().setDate(mDateOrderStr);
//        }
//        request.getBody().setHospCode(WebUtil.HOSP_CODE);
//        request.getBody().setParDeptID(null);
//        request.getBody().setPinYinCode(null);
//        request.getBody().setNote("123");
//        request.getBody().setTerminalNo(WebUtil.TERMINAL_NO());
//
//        JSONObject bodyData = WebUtil.httpExecute(request, true);
//        Type lt = new TypeToken<List<G1211>>() {}.getType();
//        List<G1211> result = new ArrayList<G1211>();
//        result = JsonUtil.toListObject(bodyData, "Item", G1211.class, lt);
//        return result;
//    }

    private void getAppointDeptsAsync(RequestCallback callback) {
        Request<T1411> request = new Request<>();
        Request.commonHeader(request, 1411, false);

        request.setBody(new T1411());
        if (mDateOrderStr != null) {
            request.getBody().setDate(mDateOrderStr);
        }
        if (mSelectedSubHos != null) {
            request.getBody().setSubHospCode(mSelectedSubHos.getSubHospCode());
        } else {
            request.getBody().setSubHospCode("");
        }
        request.getBody().setTypeID(mTypeID);
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID("");
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null,ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mLoadingView)
                .mappingInto(new TypeToken<List<G1211>>(){})
                .execute(TAG,callback);
    }

    private void getDeptFromPhrAsync(RequestCallback callback) {
        Request<T1211> request = new Request<>();
        Request.commonHeader(request,1031,true);

        request.setBody(new T1211());
        if (mDateOrderStr != null) {
            request.getBody().setDate(mDateOrderStr);
        }
        if(mSelectedSubHos!=null) {
            request.getBody().setSubHospCode(mSelectedSubHos.getSubHospCode());
        }
        request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
        request.getBody().setParDeptID(null);
        request.getBody().setPinYinCode(null);
        request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());

        GWINet.connect().createRequest().postGWI(null,ApiCodeTemplate.generateBodyRequest(request)).fromGWI()
                .setLoadingView(mLoadingView)
                .mappingInto(new TypeToken<List<G1211>>() {
                })
                .execute(TAG, callback);
    }
    
}
