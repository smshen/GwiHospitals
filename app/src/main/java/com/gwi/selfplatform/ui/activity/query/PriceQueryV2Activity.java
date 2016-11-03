package com.gwi.selfplatform.ui.activity.query;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncTasks;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.THeader;
import com.gwi.ccly.android.commonlibrary.common.net.connector.implement.request.TRequest;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.JsonUtil;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.common.utils.WebUtil;
import com.gwi.selfplatform.config.Constants;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.module.net.request.T2010;
import com.gwi.selfplatform.module.net.request.T2011;
import com.gwi.selfplatform.module.net.response.G2010;
import com.gwi.selfplatform.module.net.response.G2011;
import com.gwi.selfplatform.ui.base.HospBaseActivity;
import com.gwi.selfplatform.ui.view.andbase.AbViewUtil;
import com.gwi.selfplatform.ui.view.listview.EndlessScrollListener;
import com.gwi.selfplatform.ui.view.listview.GenericAdapter;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * 物价查询
 */
public class PriceQueryV2Activity extends HospBaseActivity {

    @Bind(R.id.loading_indicator)
    View mLoadingView;

    @Bind(R.id.price_query_v2_type_select)
    Spinner mTypeSelectSpinner;

    @Bind(R.id.price_query_input)
    EditText mEtSearchContent;

    @Bind(R.id.price_query_v2_result_list)
    ListView mLvResultList;

    @Bind(R.id.price_query_input_submit)
    ImageButton mBtnInputSubmit;

    PriceQueryAdapter mAdapter;

    private int mPageNum;
    /**
     * 一页显示的页数
     */
    private int mReturnNum;
    /**
     * 查询的价格类型
     */
    private int mType=1;

    private int mTotal;

    @Override
    protected void initViews() {
        mPageNum = 0;
        mReturnNum = 10;

        int spHeight = AbViewUtil.scaleValue(this, 67);
        mTypeSelectSpinner.getLayoutParams().height = spHeight;
//        ArrayAdapter<CharSequence> typeAdatper = ArrayAdapter.createFromResource(this,R.array.price_query_type,
//                android.R.layout.simple_dropdown_item_1line);
//        typeAdatper.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        mTypeSelectSpinner.setAdapter(typeAdatper);
        String[] types = getResources().getStringArray(R.array.price_query_type);
        PriceQueryTypeAdapter adapter = new PriceQueryTypeAdapter(this, Arrays.asList(types));
        mTypeSelectSpinner.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTypeSelectSpinner.setDropDownVerticalOffset(spHeight);
        }
    }

    @Override
    protected void initEvents() {
        mLvResultList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount < mTotal) {
                    getQueryListAsync(true);
                } else {
                    showToast("已加载完毕~");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_query_v2);
        initViews();
        initEvents();

        getQuerySumAsync();
    }

    @OnClick(R.id.price_query_input_submit)
    void onClick() {
        getQuerySumAsync();
    }

    @OnItemSelected(R.id.price_query_v2_type_select)
    void onItemSelected() {
        mType = mTypeSelectSpinner.getSelectedItemPosition()+1;
    }


    /**
     * 获取查询数据的总数
     */
    private void getQuerySumAsync() {
        mTotal = 0;
        final String pinyinCode = mEtSearchContent.getText().toString();
        AsyncTasks.doSilAsyncTask(mLoadingView, new AsyncCallback<G2010>() {

            @Override
            public G2010 callAsync() throws Exception {
                TRequest<T2010> request = new TRequest<T2010>();
                request.setHeader(new THeader());
                request.getHeader().setFunCode(2010);
                request.getHeader().setAppCode(WebUtil.APP_TYPE);
                request.getHeader().setAppTypeCode(WebUtil.APP_TYPE_CODE);
                request.getHeader().setReqTime(
                        CommonUtils.phareDateFormat(Constants.FORMAT_ISO_DATE_TIME, new Date()));
                request.setBody(new T2011());
                request.getBody().setHospCode(GlobalSettings.INSTANCE.getHospCode());
                request.getBody().setTerminalNo(GlobalSettings.INSTANCE.getTerminalNO());
                request.getBody().setPinYinCode(pinyinCode);
                request.getBody().setType(String.valueOf(mType));
                request.getBody().setTerminalNo(WebUtil.TERMINAL_NO());
                JSONObject bodyData = WebUtil.httpExecute(request, false);
                return JsonUtil.toObject(bodyData, G2010.class);
            }

            @Override
            public void onPostCall(G2010 result) {
                if (result != null) {
                    mTotal = Integer.parseInt(result.getItemQuantity());
                    mEtSearchContent.setText("");
                    getQueryListAsync(false);
                } else {
                    showToast("没有查询到数据!");
                    mTotal = 0;
                }
            }

            @Override
            public void onCallFailed(Exception exception) {
                if (exception.getLocalizedMessage() != null) {
                    showToast(exception.getLocalizedMessage());
                } else {
                    showToast(R.string.msg_service_disconnected);
                }
                mTotal = 0;
            }

        });
    }

    /**
     * 获取查询的价格信息数据。
     *
     * @param isAppend true表示在当前列表末尾追加数据；否则重新刷新数据
     */
    private void getQueryListAsync(final boolean isAppend) {
        if (mAdapter != null) {
            if (!isAppend) {
                mAdapter.clear();
            }
            mAdapter.setIsOffline(false);
            mAdapter.openBottomRefresh();
            mAdapter.notifyDataSetChanged();
        }
        View loadingView = isAppend ? null : mLoadingView;
        if (!isAppend) {
            mPageNum = 1;
        }

        ApiCodeTemplate.getPriceQueryListAsync("PriceQueryFragment", loadingView,
                mType, mEtSearchContent.getText().toString(), mPageNum, mReturnNum,
                new RequestCallback<List<G2011>>() {
                    @Override
                    public void onRequestSuccess(List<G2011> result) {
                        if (mAdapter == null) {
                            mAdapter = new PriceQueryAdapter(PriceQueryV2Activity.this, result);
                            mLvResultList.setAdapter(mAdapter);
                            mAdapter.setRetryListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getQueryListAsync(true);
                                }
                            });
                        }
                        if (result != null && !result.isEmpty()) {
                            CommonUtils.removeNull(result);
                            mAdapter.addAll(result);
                            mAdapter.notifyDataSetChanged();
                            mPageNum++;
                        } else {
                            showToast("已加载完毕~");
                        }
                        mAdapter.closeBottomRefresh();
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onRequestError(RequestError error) {
                        if (mAdapter != null) {
                            mAdapter.setIsOffline(true);
                            mAdapter.closeBottomRefresh();
                            mAdapter.notifyDataSetChanged();
                        }
                        CommonUtils.showError(PriceQueryV2Activity.this, (Exception) error.getException());
                    }
                });
    }

    static class PriceQueryTypeAdapter extends ArrayAdapter<String> {

        LayoutInflater inflater;

        public PriceQueryTypeAdapter(Context context, List<String> objects) {
            super(context, 0, objects);
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setBackgroundColor(getContext().getResources().getColor(R.color.wihte_transparent_semi));
            tv.setGravity(Gravity.CENTER);
            tv.setText(getItem(position));
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setGravity(Gravity.CENTER);
            tv.setText(getItem(position));
            return convertView;
        }
    }


    public class PriceQueryAdapter extends GenericAdapter<G2011> {

        Activity activity;

        public PriceQueryAdapter(Activity activity, List<G2011> list) {
            super(activity, list);
            this.activity = activity;
        }

        @Override
        public View getDataRow(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.item_price_query_v2, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            G2011 item = getItem(position);
            holder.name.setText(String.format("名称：%s", TextUtil.isEmpty(item.getItemName())?"":item.getItemName()));
            holder.priceUnit.setText(String.format("%s元", TextUtil.isEmpty(item.getUnitPrice()) ? "" : CommonUtils.formatCash(Double.valueOf(item.getUnitPrice()))));
            holder.spec.setText(String.format("规格：%s", TextUtil.isEmpty(item.getSpecs()) ? "" : item.getSpecs()));
            holder.percentage.setText(String.format("医保比例：%s", TextUtil.isEmpty(item.getSelfPercent()) ? "" : item.getSelfPercent()));
            holder.medType.setText(TextUtil.isEmpty(item.getMedInsureType()) ? "" : item.getMedInsureType());
            return convertView;
        }
    }

    public class ViewHolder {
        @Bind(R.id.item_price_query_v2_name)
        TextView name;

        @Bind(R.id.item_price_query_v2_percentage)
        TextView percentage;

        @Bind(R.id.item_price_query_v2_unit)
        TextView unit;//单位

        @Bind(R.id.item_price_query_v2_price_unit)
        TextView priceUnit;

        @Bind(R.id.item_price_query_v2_med_type)
        TextView medType;

        @Bind(R.id.item_price_query_v2_spec)
        TextView spec;

        public ViewHolder(View parentView) {
            ButterKnife.bind(this, parentView);
        }
    }
}

