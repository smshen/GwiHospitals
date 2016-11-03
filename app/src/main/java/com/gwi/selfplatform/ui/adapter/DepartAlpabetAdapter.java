package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.response.G1211;
import com.gwi.selfplatform.ui.view.PingYinUtil;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 按字母表排列的科室adapter.
 * @author 彭毅
 *
 */
public class DepartAlpabetAdapter extends BaseExpandableListAdapter implements SectionIndexer {
    
    private List<G1211> mDeptList;
    private Context mContext;
    private ExpandableListView mParent;
    private List<String> mAllGroupData;
    private List<String> mRealGroupData;
    private List<List<G1211>> mChildData;
    
    private static final Comparator<G1211> ALPHACOMPARATOR = new Comparator<G1211>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(G1211 lhs, G1211 rhs) {
            return sCollator.compare(lhs.getDeptName(), rhs.getDeptName());
        }
        
    };
    
    public void clear() {
        mDeptList.clear();
    }
    
    public void addAll(List<G1211> list) {
        if(list!=null&&!list.isEmpty()) {
            mDeptList.addAll(list);
        }
        notifyDataSetChanged();
    }
    
    
    public DepartAlpabetAdapter(Context context, List<G1211> deptList, ExpandableListView parent) {
        mContext = context;
        mDeptList = deptList;
        mParent = parent;
        mRealGroupData = new ArrayList<String>();
        mChildData = new ArrayList<List<G1211>>();
        generateGroupData();
        groupBy();
    }
    
    private void groupBy() {
        mRealGroupData.clear();
        Collections.sort(mDeptList, ALPHACOMPARATOR);
        for(int i=0,n=mAllGroupData.size();i<n;i++) {
            List<G1211> child = new ArrayList<G1211>();
            String a = mAllGroupData.get(i);
            for(int j=0,dn=mDeptList.size();j<dn;j++) {
                G1211 dept = mDeptList.get(j);
                String pinyin;
                if(!TextUtils.isEmpty(dept.getPinYinCode())) {
                    pinyin = dept.getPinYinCode();
                }else {
                    //Alternative.
                    pinyin = PingYinUtil.getPingYin(dept.getDeptName());
                }
                if(pinyin.substring(0, 1).equalsIgnoreCase(a)) {
                    child.add(dept);
                }
            }
            if(!child.isEmpty()) {
                mChildData.add(child);
                mDeptList.removeAll(child);
                mRealGroupData.add(a);
            }
        }
    }
    
    private void generateGroupData() {
        String[] alpha = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
                "T", "U", "V", "W", "X", "Y", "Z" };
        mAllGroupData = Arrays.asList(alpha);
    }
    
    @Override
    public void notifyDataSetChanged() {
        groupBy();
        super.notifyDataSetChanged();
    }
    

    @Override
    public int getGroupCount() {
        return mRealGroupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mRealGroupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildData.get(groupPosition).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_disease_alpha_group, parent,false);
        }
        TextView title  = (TextView) convertView.findViewById(R.id.alpha_title);
        title.setText((String) getGroup(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_baike, parent,false);
        }
        TextView title = (TextView) convertView.findViewById(android.R.id.text1);
        G1211 dept = (G1211)getChild(groupPosition, childPosition);
        title.setText(dept.getDeptName());
        title.setCompoundDrawables(null, null, null, null);
        title.setTag(dept);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        for(int i=0;i<getGroupCount();i++) {
            String title = mRealGroupData.get(i);
            String pinyin = PingYinUtil.getPingYin(title);
            char first = pinyin.toUpperCase().charAt(0);
            if(first==sectionIndex) {
                return mParent.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(i));
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

}
