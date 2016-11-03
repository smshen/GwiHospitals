package com.gwi.selfplatform.ui.fragment.query;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_Phr_CardBindRec;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.response.OrderQueryResults;
import com.gwi.selfplatform.ui.adapter.OrderpayAdapter;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonthListFragment extends ListFragment implements LoaderCallbacks<List<OrderQueryResults.OrderQueryResult>> {
    private final static  String TAG = "OrderPayActivity";
    private PullToRefreshListView orderPay_lv;
    private TextView mEmptyText;
    private View loadingView;

    private OrderpayAdapter adapter;
    private List<OrderQueryResults.OrderQueryResult> orderList;
    private T_Phr_BaseInfo mMember;
    private ExT_Phr_CardBindRec cardInfo;

    private int mCurrentPageIndex =1;
    final int PAGE_SIZE=20;
    private boolean mIsLoadFinished = false;
    private int mTotalRecordCount;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
//        cardInfo = PayRecordActivity.getCard();
        Bundle b = getArguments();
        cardInfo = (ExT_Phr_CardBindRec) b.getSerializable(T_Phr_CardBindRec.class.getSimpleName());
//        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_order_pay,container,false);
        loadingView = view.findViewById(R.id.loadingView);
        orderPay_lv = (PullToRefreshListView) view.findViewById(R.id.payOrderLV);
        mEmptyText = (TextView)view.findViewById(R.id.text_empty);
        
        orderList = new ArrayList<>();
        adapter = new OrderpayAdapter(orderList, getActivity());
        orderPay_lv.setAdapter(adapter);
        
//        loadCardBindingAsync(mMember);
        initEvents();
        
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(cardInfo, mCurrentPageIndex, PAGE_SIZE, false);
        mIsLoadFinished = false;
    }

    protected void initEvents() {
        orderPay_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        orderPay_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!mIsLoadFinished) {
                    if (adapter.getCount() < mTotalRecordCount) {
                        mCurrentPageIndex = mCurrentPageIndex+1;
                        initData(cardInfo, mCurrentPageIndex, PAGE_SIZE, true);
                        return;
                    }else mIsLoadFinished = true;
                }
                Toast.makeText(getActivity(), "已加载完毕~", Toast.LENGTH_SHORT).show();
                orderPay_lv.onRefreshComplete();
            }
        });
    }

    /**
     *
     * @param cardInfo
     * @param pageIndex
     * @param pageSize
     * @param isAppend 是否在列表尾部添加数据
     */
    public void initData(final ExT_Phr_CardBindRec cardInfo,int pageIndex,int pageSize,final boolean isAppend) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis()+AlarmManager.INTERVAL_DAY);
        ApiCodeTemplate.loadOrderResultsAsync(TAG, isAppend ? null : loadingView,
                null, mMember, cardInfo, null, pageIndex, pageSize,
                CommonUtils.parsebeforeMonthDate(new Date()), CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE, c.getTime())
                , new RequestCallback<OrderQueryResults>() {

                    @Override
                    public void onRequestSuccess(OrderQueryResults results) {
                        mTotalRecordCount = results.getTotalCount();
                        List<OrderQueryResults.OrderQueryResult> result = results.getItems();
                        if (!isAppend) {
                            orderList.clear();
                        }
                        if (result != null && !result.isEmpty()) {
                            while (result.contains(null)) {
                                result.remove(null);
                            }
                            loadingView.setVisibility(View.GONE);
                            orderList.addAll(result);
                            mEmptyText.setVisibility(View.GONE);
                        } else {
                            loadingView.setVisibility(View.GONE);
                            mEmptyText.setText(getString(R.string.No_month_list_pay));
                            mEmptyText.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        if (isAppend) orderPay_lv.onRefreshComplete();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        if (!isAppend) {
                            orderList.clear();
                            adapter.notifyDataSetChanged();
                            mEmptyText.setText(getString(R.string.No_month_ago_pay));
                            mEmptyText.setVisibility(View.VISIBLE);
                        } else {
                            orderPay_lv.onRefreshComplete();
                        }
                        CommonUtils.showError((HospBaseActivity) getActivity(), (Exception) error.getException());
                    }
                });
    }

//    public void initData(final ExT_Phr_CardBindRec cardInfo) {
//        AsyncTasks.doSilAsyncTask(loadingView,
//                new AsyncCallback<List<OrderQueryResult>>() {
//
//                    @Override
//                    public List<OrderQueryResult> callAsync() throws Exception {
//                        Request<OrderQuery> request = new Request<OrderQuery>();
//                        request.setHeader(new THeader());
//                        request.getHeader().setFunCode(1016);
//                        request.getHeader().setAppTypeCode("5");
//                        request.getHeader().setAppCode("2");
//                        request.getHeader().setReqTime(
//                                CommonUtils.phareDateFormat(
//                                        Constants.FORMAT_ISO_DATE_TIME,
//                                        new Date()));
//                        request.setBody(new OrderQuery());
//                        request.getBody().setHospCode(WebUtil.HOSP_CODE);
//                        request.getBody().setOrderNo(null);
//                        request.getBody().setIDCardNo(mMember.getIDCard());
//                       // request.getBody().setCardNo(cardInfo.getCardNoFormat());
//                        request.getBody().setBeginDate(CommonUtils.parsebeforeMonthDate(new Date()));
//                       // request.getBody().setEndDate(endDate);
//                        if (cardInfo != null && cardInfo.getCardNo() != null) {
//                            request.getBody().setCardNo(cardInfo.getCardNo());
//                        }
//
//                        JSONObject bodyData = WebUtil
//                                .httpExecute(request, true);
//                        Type lt = new TypeToken<List<OrderQueryResult>>() {}.getType();
//                        List<OrderQueryResult> result = new ArrayList<OrderQueryResult>();
//                        result = JsonUtil.toListObject(bodyData, "Item",
//                                OrderQueryResult.class, lt);
//                        return result;
//                    }
//
//                    @Override
//                    public void onPostCall(List<OrderQueryResult> result) {
//                        orderList.clear();
//                        if (result != null ) {
//                            for (OrderQueryResult order : result) {
//                                if (order != null) {
//                                    orderList.add(order);
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//                            mEmptyText.setVisibility(View.GONE);
//                        } else {
//                            mEmptyText.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onCallFailed(Exception exception) {
//                        Logger.e(TAG, exception.getLocalizedMessage() + "");
//                        if(exception.getLocalizedMessage()!=null) {
//                            ((BaseActivity)getActivity()).showToast(exception.getLocalizedMessage());
//                        }else {
//                            ((BaseActivity)getActivity()).showToast(R.string.msg_service_disconnected);
//                        }
//                    }
//            });
//    }

        @Override
    public Loader<List<OrderQueryResults.OrderQueryResult>> onCreateLoader(int arg0, Bundle arg1) {
        return new MonthPayLoader(getActivity(),cardInfo,mMember);
    }

    @Override
    public void onLoadFinished(Loader<List<OrderQueryResults.OrderQueryResult>> loader,
            List<OrderQueryResults.OrderQueryResult> data) {
         orderList.clear();
         if(data!=null&&!data.isEmpty()) {
             while(data.contains(null)) {
                 data.remove(null);
             }
             loadingView.setVisibility(View.GONE);
             orderList.addAll(data);
             mEmptyText.setVisibility(View.GONE);
         }else{
             loadingView.setVisibility(View.GONE);
             mEmptyText.setText(getString(R.string.No_month_list_pay));
             mEmptyText.setVisibility(View.VISIBLE);
         }
         adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<OrderQueryResults.OrderQueryResult>> arg0) {
        orderList.clear();
        loadingView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }
    
 private static class MonthPayLoader extends AsyncTaskLoader<List<OrderQueryResults.OrderQueryResult>> {
    private List<OrderQueryResults.OrderQueryResult> orderList;
    private T_Phr_BaseInfo mMember;
    private ExT_Phr_CardBindRec mCardInfo;
    public MonthPayLoader(Context context,ExT_Phr_CardBindRec cardInfo,T_Phr_BaseInfo member) {
        super(context);
        mCardInfo = cardInfo;
        mMember = member;
    }
    
    @Override
    public List<OrderQueryResults.OrderQueryResult> loadInBackground() {
//        Request<OrderQuery> request = new Request<OrderQuery>();
//        request.setHeader(new THeader());
//        request.getHeader().setFunCode(1016);
//        request.getHeader().setAppTypeCode("5");
//        request.getHeader().setAppCode("2");
//        request.getHeader().setReqTime(
//                CommonUtils.phareDateFormat(
//                        Constants.FORMAT_ISO_DATE_TIME,
//                        new Date()));
//        request.setBody(new OrderQuery());
//        request.getBody().setHospCode(WebUtil.HOSP_CODE);
//        request.getBody().setOrderNo(null);
//        request.getBody().setIDCardNo(mMember.getIDCard());
//        //request.getBody().setCardNo(mCardInfo.getCardNoFormat());
//        request.getBody().setBeginDate(CommonUtils.parsebeforeMonthDate(new Date()));
//        if (mCardInfo != null && mCardInfo.getCardNo() != null) {
//            request.getBody().setCardNo(mCardInfo.getCardNo());
//        }
//
//        try {
//            JSONObject    bodyData = WebUtil.httpExecute(request, true);
//            Type lt = new TypeToken<List<OrderQueryResult>>() {}.getType();
//            List<OrderQueryResult> result = new ArrayList<OrderQueryResult>();
//            result = JsonUtil.toListObject(bodyData, "Item",
//                    OrderQueryResult.class, lt);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
//            return ApiCodeTemplate.getOrderResults(null,mMember,mCardInfo);
        } catch (Exception e) {
            Logger.e(TAG, "MonthPayLoader#loadInBackground", e);
        }

        return null;
    }
    
     @Override
     public void deliverResult(List<OrderQueryResults.OrderQueryResult> data) {
         orderList = data;
         if (isStarted()) {
             super.deliverResult(data);
         }
     }

     @Override
     protected void onReset() {
         super.onReset();
         onStopLoading();
         orderList = null;
     }

     @Override
     protected void onStartLoading() {
         super.onStartLoading();
         if (orderList != null) {
             deliverResult(orderList);
         }

         if (orderList == null || takeContentChanged()) {
             forceLoad();
         }
     }

     @Override
     protected void onStopLoading() {
         super.onStopLoading();
         cancelLoad();
     }
    }
}
