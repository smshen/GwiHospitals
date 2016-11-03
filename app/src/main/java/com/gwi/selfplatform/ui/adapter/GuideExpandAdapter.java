package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.response.G1615;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/28 0028.
 */
public class GuideExpandAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<G1615> mDepts;
    private List<G1615> mGrpData;
    private List<List<G1615>> mChildDataArray;
    private LayoutInflater inflater;

    public GuideExpandAdapter(Context context, List<G1615> depts) {
        this.mContext = context;
        this.mDepts = depts;
        mDepts = depts;
        mGrpData = new ArrayList<>();
        mChildDataArray = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        groupBy(depts);
    }

    private void groupBy(List<G1615> list) {
        mGrpData.clear();
        mChildDataArray.clear();

        // String deptId = null;
        List<G1615> childData = null;
        List<G1615> tmplist = new ArrayList<>();

        for (G1615 item : list) {
            if (mGrpData.size() > 0) {
                tmplist = new ArrayList<>();
                tmplist.addAll(mGrpData);
                boolean flag = false;
                for (int i = 0; i < tmplist.size(); i++) {
                    G1615 flg = tmplist.get(i);
                    if (flg.getExecDeptID().equals(item.getExecDeptID())) {
                        mChildDataArray.get(i).add(item);
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    ;
                } else {
                    mGrpData.add(item);
                    childData = new ArrayList<G1615>();
                    mChildDataArray.add(childData);
                }

            } else {
                mGrpData.add(item);
                childData = new ArrayList<G1615>();
                mChildDataArray.add(childData);
                childData.add(item);
            }
        }
    }

    @Override
    public int getGroupCount() {
        return mGrpData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildDataArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGrpData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildDataArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_guide_expandable, parent, false);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        holder.mTitle.setText(((G1615) getGroup(groupPosition)).getExecDeptName());
        holder.mTitle.setChecked(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChilHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_guide_expandable_child, parent, false);
            holder = new ChilHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChilHolder) convertView.getTag();
        }

        int status = ((G1615) getChild(groupPosition, childPosition)).getStatus();
        String statusMsg = null;
        // 1：未执行；2：已执行
        if (2 == status) {
            statusMsg = mContext.getResources().getTextArray(R.array.recipe_status)[1].toString();
            holder.txtExecuted.setTextColor(mContext.getResources().getColor(R.color.text_color_primary));
        } else {
            statusMsg = mContext.getResources().getTextArray(R.array.recipe_status)[0].toString();
            holder.txtExecuted.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        holder.txtExecuted.setText(String.format("%s", statusMsg));

        //holder.ivIcon.setImageResource((0 == childPosition) ? R.drawable.selector_medicine1 : R.drawable.selector_tcm1);
        holder.ivIcon.setImageResource( R.drawable.selector_tcm1);
        holder.txtRegisterNum.setText(((G1615) getChild(groupPosition, childPosition)).getRegNo());
        // holder.txtExecuted.setText(R.string.label_executed);
        holder.txtReceiptNum.setText(((G1615) getChild(groupPosition, childPosition)).getRecipeNo());
        holder.txtTotalCost.setText(((G1615) getChild(groupPosition, childPosition)).getTotalFee());
        holder.txtPaymentTime.setText(((G1615) getChild(groupPosition, childPosition)).getPaymentTime());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void resetQuery() {
        groupBy(mDepts);
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_guide_expandable_child.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ChilHolder {
        @Bind(R.id.txt_register_num)
        TextView txtRegisterNum;
        @Bind(R.id.txt_executed)
        TextView txtExecuted;
        @Bind(R.id.txt_receipt_num)
        TextView txtReceiptNum;
        @Bind(R.id.txt_total_cost)
        TextView txtTotalCost;
        @Bind(R.id.txt_payment_time)
        TextView txtPaymentTime;
        @Bind(R.id.iv_icon)
        ImageView ivIcon;

        ChilHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class GroupHolder {
        @Bind(R.id.expand_group_title)
        CheckedTextView mTitle;

        public GroupHolder(View parent) {
            ButterKnife.bind(this, parent);
        }
    }
}
