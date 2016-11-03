package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.ui.view.PingYinUtil;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 疾病字母表listAdapter
 * @author fengleioh
 *
 */
public class DiseaseAlphaExpandableAdapter extends BaseExpandableListAdapter implements SectionIndexer{
    
    Context mContext;
    List<KBDiseaseDetails> mData;
    List<List<KBDiseaseDetails>> mChildData;
    List<String> mAllGroupData;
    List<String> mRealGroupData;
    ExpandableListView mParent;
    
    public DiseaseAlphaExpandableAdapter(Context context, List<KBDiseaseDetails> data, ExpandableListView parent) {
        mContext = context;
        mData = data;
        mChildData = new ArrayList<List<KBDiseaseDetails>>();
        mRealGroupData =new ArrayList<String>();
        mParent = parent;
        generateGroupData();
        groupBy();
    }
    
    
    private static final Comparator<KBDiseaseDetails> ALPHACOMPARATOR = new Comparator<KBDiseaseDetails>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(KBDiseaseDetails lhs, KBDiseaseDetails rhs) {
            return sCollator.compare(lhs.getDiseaseName(), rhs.getDiseaseName());
        }
    };
    
    private void generateGroupData() {
        String[] alpha = new String []{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",  
                "T", "U", "V", "W", "X", "Y", "Z" };
        mAllGroupData = Arrays.asList(alpha);
    }
    
    
    
    @Override
    public void notifyDataSetChanged() {
        groupBy();
        super.notifyDataSetChanged();
    }



    private void groupBy() {
        //Sort first.
        
        Collections.sort(mData, ALPHACOMPARATOR);
        for(int i=0,n=mAllGroupData.size();i<n;i++) {
            List<KBDiseaseDetails> child = new ArrayList<KBDiseaseDetails>();
            String a = mAllGroupData.get(i);
            for(int j=0,dn=mData.size();j<dn;j++) {
                KBDiseaseDetails details = mData.get(j);
//                String pinyin = PingYinUtil.getPingYin(details.getDiseaseName());
                String pinyin = details.getPyCode();
                if(pinyin.substring(0, 1).equalsIgnoreCase(a)) {
                    child.add(details);
                }
            }
            if(!child.isEmpty()) {
                mChildData.add(child);
                mData.removeAll(child);
                mRealGroupData.add(a);
            }
        }
        Logger.d("fsdf", mChildData.size() + "," + mRealGroupData.size());
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
        title.setText(((KBDiseaseDetails)getChild(groupPosition, childPosition)).getDiseaseName());
        title.setCompoundDrawables(null, null, null, null);
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
        Logger.d("getSectionForPosition", "position: " + position);
        return mRealGroupData.get(position).toUpperCase().charAt(0);
    }

    
    

}
