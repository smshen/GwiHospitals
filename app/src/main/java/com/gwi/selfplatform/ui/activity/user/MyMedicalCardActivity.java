package com.gwi.selfplatform.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.gwi.ccly.android.commonlibrary.common.net.AsyncCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestCallback;
import com.gwi.ccly.android.commonlibrary.common.net.connector.RequestError;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;
import com.gwi.selfplatform.common.utils.CommonUtils;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.db.gen.ExT_Phr_CardBindRec;
import com.gwi.selfplatform.db.gen.T_Phr_BaseInfo;
import com.gwi.selfplatform.db.gen.T_UserInfo;
import com.gwi.selfplatform.module.net.connector.implement.ApiCodeTemplate;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 我的诊疗卡页面
 */
public class MyMedicalCardActivity extends HospBaseActivity {

    private static final String TAG = MyMedicalCardActivity.class.getSimpleName();
    public static final String FAMILY_INFO_MAIN = "family_info";


    @Bind(R.id.my_m_card_name)
    TextView mTvPatientName;

    @Bind(R.id.my_m_card_idcard)
    TextView mTvIdCard;

    @Bind(R.id.my_m_card_mobile)
    TextView mTvMobile;

    @Bind(R.id.my_m_card_card_no)
    TextView mTvCardNo;

    @Bind(R.id.my_m_card_list)
    ListView mLvCardList;

    T_Phr_BaseInfo mCurrentFamilyMember;

    T_Phr_BaseInfo mMainFamilyMember;
    T_UserInfo mUserInfo;

    List<T_Phr_BaseInfo> mFamilyMembers;

    FamilyCardsChangeSwipeAdapter mFamilyCardsChangeAdapter;

    // 与memberList一一对应
    List<ExT_Phr_CardBindRec> mCardList;
    SparseArray<ExT_Phr_CardBindRec> mCardListHelperArray;

    @OnClick(R.id.btn_add_medical_card)
    void addMedicalCard() {
        //openActivityForResult(MedicalCardAddActivity.class, 1);
        bindMainUserInfo();
    }

    @Override
    protected void initViews() {
        mCurrentFamilyMember = GlobalSettings.INSTANCE.getCurrentFamilyAccount();
        mUserInfo = GlobalSettings.INSTANCE.getCurrentUser();
        mFamilyMembers = new ArrayList<>();
        mCardList = new ArrayList<>();
        mFamilyCardsChangeAdapter = new FamilyCardsChangeSwipeAdapter(this, mFamilyMembers);
        mFamilyCardsChangeAdapter.setSelectedMember(mCurrentFamilyMember.getEhrID());
        mLvCardList.setAdapter(mFamilyCardsChangeAdapter);
        setValue(mCurrentFamilyMember, null);
    }

    private void setValue(T_Phr_BaseInfo member, ExT_Phr_CardBindRec cardBindRec) {
        if (member != null) {
            mTvPatientName.setText(member.getName());
            mTvIdCard.setText(member.getIDCard());
            mTvMobile.setText(member.getSelfPhone());
        }
        if (cardBindRec != null) {
            mTvCardNo.setText(cardBindRec.getCardNo());
        }
    }

    @Override
    protected void initEvents() {
        mFamilyCardsChangeAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExT_Phr_CardBindRec cardBindRec = mCardListHelperArray.get(position);
//                T_Phr_BaseInfo selectedMember = getFamilyMemberFromCardbandInfo(cardBindRec);
                T_Phr_BaseInfo selectedMember = (T_Phr_BaseInfo) mFamilyCardsChangeAdapter.getItem(position);
                mFamilyCardsChangeAdapter.setSelectedMember(selectedMember.getEhrID());
                mFamilyCardsChangeAdapter.notifyDataSetChanged();
                mCurrentFamilyMember = selectedMember;
                setValue(selectedMember, cardBindRec);
                GlobalSettings.INSTANCE.setCurCardInfo(cardBindRec);
                GlobalSettings.INSTANCE.setCurrentFamilyAccount(selectedMember);

                EventBus.getDefault().post(selectedMember);
            }
        });
    }

    private void shoDeleteMemberDialog(final T_Phr_BaseInfo member, final ExT_Phr_CardBindRec cardBindRec, final int position) {
        String msg;
        if (cardBindRec != null) {
            msg = String.format("确定删除姓名为%s，卡号为%s的诊疗卡？", member.getName(), cardBindRec.getCardNo());
        } else {
            msg = String.format("确定删除姓名为%s的就诊人？", member.getName());
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.content(msg)
                .positiveText(R.string.dialog_cofirm)
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        if (cardBindRec == null) {
                            deleteFamilySecondly(member, position);
                        } else {
                            unbindCardFirstly(member, cardBindRec, position);
                        }
                        materialDialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                    }
                }).show();
    }

    private void unbindCardFirstly(final T_Phr_BaseInfo member, ExT_Phr_CardBindRec cardBindRec, final int position) {
        ApiCodeTemplate.unBindAsync(this, TAG, null, member, cardBindRec, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
            @Override
            public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                deleteFamilySecondly(member, position);
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });

    }

    private void deleteFamilySecondly(T_Phr_BaseInfo member, final int position) {
        ApiCodeTemplate.deleteFamilyInfoAsync(this, TAG, member, new RequestCallback<List<T_Phr_BaseInfo>>() {
            @Override
            public void onRequestSuccess(List<T_Phr_BaseInfo> result) {
                showToast("删除成功");
                mCardListHelperArray.remove(position);
                mFamilyMembers.remove(position);
                mFamilyCardsChangeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestError(RequestError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_medical_card);
        addHomeButton();
        initViews();
        initEvents();
        EventBus.getDefault().register(this);
        loadFamilyMembersAsync();
    }

    public void onEvent(ExT_Phr_CardBindRec rec) {
        setValue(mCurrentFamilyMember, rec);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                mFamilyCardsChangeAdapter.clear();
                if (mHandler == null) {
                    mHandler = new Handler();
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFamilyMembersAsync();
                    }
                }, 600);
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void loadFamilyMembersAsync() {
        ApiCodeTemplate.loadFamilyMembersAsync(this, TAG, null, new RequestCallback<List<T_Phr_BaseInfo>>() {
            @Override
            public void onRequestSuccess(List<T_Phr_BaseInfo> result) {
                mFamilyMembers.clear();
                if (result != null && !result.isEmpty()) {
                    mFamilyMembers.addAll(result);
                }
                if (mCardListHelperArray == null) {
                    mCardListHelperArray = new SparseArray<>();
                }

                //Set main family member.
                if (mMainFamilyMember == null) {
                    for (int i = 0; i < mFamilyMembers.size(); i++) {
                        if (mUserInfo.getEhrId().equalsIgnoreCase(mFamilyMembers.get(i).getEhrID())) {
                            mMainFamilyMember = mFamilyMembers.get(i);
                        }
                    }
                }
                getCardInfoOfEachMemberNewAsyc();
            }

            @Override
            public void onRequestError(RequestError error) {
                CommonUtils.showError(MyMedicalCardActivity.this, (Exception) error.getException());
            }
        });
    }

    private void getCardInfoOfEachMemberNewAsyc() {
        if (!mFamilyMembers.isEmpty()) {
            mCardList.clear();

            doProgressAsyncTask(null, new AsyncCallback<List<ExT_Phr_CardBindRec>>() {
                @Override
                public List<ExT_Phr_CardBindRec> callAsync() throws Exception {
                    //同步锁，当取得所有家庭成员的绑定卡数据之后,更新UI
                    final CountDownLatch countDownLatch = new CountDownLatch(mFamilyMembers.size());
                    for (int memberIndex = 0; memberIndex < mFamilyMembers.size(); memberIndex++) {
                        final T_Phr_BaseInfo member = mFamilyMembers.get(memberIndex);

                        final int finalMemberIndex = memberIndex;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ApiCodeTemplate.loadBindedCardAsync(null, TAG, member, new RequestCallback<List<ExT_Phr_CardBindRec>>() {
                                    @Override
                                    public void onRequestSuccess(List<ExT_Phr_CardBindRec> result) {
                                        ExT_Phr_CardBindRec cardInfo;
                                        if (result != null && !result.isEmpty()) {
                                            cardInfo = result.get(0);
                                            if (cardInfo.getEhrId().equalsIgnoreCase(mCurrentFamilyMember.getEhrID())) {
                                                EventBus.getDefault().post(cardInfo);
                                            }
                                            if (TextUtil.isEmpty(cardInfo.getBindMan())) {
                                                cardInfo.setBindMan(member.getName());
                                            }
                                            mCardListHelperArray.append(finalMemberIndex, cardInfo);
                                        }
                                        countDownLatch.countDown();
                                    }

                                    @Override
                                    public void onRequestError(RequestError error) {
                                        CommonUtils.showError(MyMedicalCardActivity.this, (Exception) error.getException());
                                        countDownLatch.countDown();
                                    }
                                });
                            }
                        });
                    }
                    countDownLatch.await(10000, TimeUnit.MILLISECONDS);
                    return CommonUtils.asList(mCardListHelperArray);
                }

                @Override
                public void onPostCall(List<ExT_Phr_CardBindRec> result) {
                    mCardList.clear();
                    if (result != null && !result.isEmpty()) {
                        mCardList.addAll(result);
                    }
                    mFamilyCardsChangeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCallFailed(Exception exception) {
                    CommonUtils.showError(MyMedicalCardActivity.this, exception);
                }
            });
        }
    }
    private void bindMainUserInfo() {
        boolean bindMain = true;
        for (int i = 0; i < mCardList.size(); i++) {
            if (mCardList.get(i).getEhrId().equalsIgnoreCase(mUserInfo.getEhrId())) {
                bindMain = false;
            }
        }

        if (bindMain) {
            Bundle b = new Bundle();
            b.putSerializable(FAMILY_INFO_MAIN,mMainFamilyMember);
            openActivityForResult(MedicalCardAddActivity.class, 1, b);
        }else  openActivityForResult(MedicalCardAddActivity.class, 1);


    }

    public class FamilyCardsChangeSwipeAdapter extends BaseSwipeAdapter {

        Context context;
        private List<T_Phr_BaseInfo> mFamilyInfos;
        String selectedMember = null;
        T_UserInfo mUserInfo;

        private AdapterView.OnItemClickListener mOnItemClickListener;



        public FamilyCardsChangeSwipeAdapter(Context context, List<T_Phr_BaseInfo> familyInfo) {
            this.context = context;
            this.mFamilyInfos = familyInfo;
            mUserInfo = GlobalSettings.INSTANCE.getCurrentUser();
        }

        public void setSelectedMember(String enhrId) {
            selectedMember = enhrId;
        }

        public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickLitner) {
            mOnItemClickListener = onItemClickLitner;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {

            return LayoutInflater.from(context).inflate(R.layout.item_family_cards, parent, false);
        }

        @Override
        public void fillValues(final int position, final View convertView) {
            final ViewHolder holder;
            if (convertView.getTag() == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final T_Phr_BaseInfo member = (T_Phr_BaseInfo) getItem(position);
            holder.name.setText(String.format("姓名：%s", member.getName()));
            final ExT_Phr_CardBindRec cardBindRec = mCardListHelperArray.get(position);
            if (cardBindRec != null) {
                holder.cardNo.setText(cardBindRec.getCardNo());
            }

            if (selectedMember != null) {
                holder.checkMark.setChecked(selectedMember.equalsIgnoreCase(member.getEhrID()));
            }

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.swipeLayout.close(true);
                    if (member.getEhrID().equalsIgnoreCase(selectedMember)) {
                        setSelectedMember(mUserInfo.getEhrId());
                        mCurrentFamilyMember = mMainFamilyMember;
                        setValue(mCurrentFamilyMember,mCardListHelperArray.get(position));
                    }
                    if (member.getEhrID().equalsIgnoreCase(mUserInfo.getEhrId())) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(FAMILY_INFO_MAIN, member);
                        openActivityForResult(MedicalCardAddActivity.class, 1, bundle);
                    } else {
                        shoDeleteMemberDialog(member, cardBindRec, position);
                    }
                }
            });

            holder.swipeLayout.setOnClickListener(new View.OnClickListener() {

                boolean wasClosed = true;
                @Override
                public void onClick(View v) {
                    if (SwipeLayout.Status.Close == holder.swipeLayout.getOpenStatus())
                    {
                        if (wasClosed)
                        {
                            mOnItemClickListener.onItemClick(null,convertView,position,0);
                        }
                        else
                        {
                            wasClosed = true;
                        }
                    }
                    else
                    {
                        wasClosed = false;
                    }
                }
            });
        }

        @Override
        public int getCount() {
            return mFamilyInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return mFamilyInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            mFamilyInfos.clear();
            notifyDataSetChanged();
        }
    }

    public class FamilyCardsChangeAdapter extends ArrayAdapter<T_Phr_BaseInfo> {

        public List<ExT_Phr_CardBindRec> cardInfoList;
        int selectedIndex = -1;
        T_Phr_BaseInfo curMember;

        public FamilyCardsChangeAdapter(Context context, List<T_Phr_BaseInfo> objects) {
            super(context, 0, objects);
        }

        public void setSelectedIndex(int index) {
            selectedIndex = index;
        }

        public void setCurMember(T_Phr_BaseInfo curMember) {
            this.curMember = curMember;
        }

        public void setCardList(List<ExT_Phr_CardBindRec> cardInfoList) {
            this.cardInfoList = cardInfoList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_family_cards, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            T_Phr_BaseInfo member = getItem(position);
            holder.name.setText(String.format("姓名：%s", member.getName()));
            ExT_Phr_CardBindRec cardBindRec = mCardListHelperArray.get(position);
            if (cardBindRec != null) {
                holder.cardNo.setText(cardBindRec.getCardNo());
            }

            if (selectedIndex != -1) {
                holder.checkMark.setChecked(selectedIndex == position);
            } else {
                if (curMember != null && member.getEhrID().equalsIgnoreCase(curMember.getEhrID())) {
                    holder.checkMark.setChecked(true);
                }
            }
            return convertView;
        }
    }

    public class ViewHolder {
        @Bind(R.id.swipe)
        SwipeLayout swipeLayout;
        @Bind(R.id.family_card_check_mark)
        RadioButton checkMark;
        @Bind(R.id.family_cards_name)
        TextView name;
        @Bind(R.id.family_cards_no)
        TextView cardNo;
        @Bind(R.id.family_cards_hosp_name)
        TextView hospName;

        @Bind(R.id.family_cards_remove)
        Button remove;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
