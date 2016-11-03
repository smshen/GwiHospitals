package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.beans.KBDrugUseKind;
import com.gwi.selfplatform.ui.view.PingYinUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 疾病列表adapter
 * @author 彭毅
 *
 */
public class DrugCommonAdapter extends ArrayAdapter<KBDrugUseKind>{
    
    private List<KBDrugUseKind> mDrug;

    public DrugCommonAdapter(Context context, List<KBDrugUseKind> objects) {
        super(context, 0, 0, objects);
        mDrug = new ArrayList<KBDrugUseKind>();
    }
    
    /**
     * 获取所有数据，用于数据搜索
     * @param list
     */
    public void setAllSearchResults(List<KBDrugUseKind> list) {
        if(list!=null&&!list.isEmpty()) {
            mDrug.addAll(list);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_baike, parent,false);
        }
        TextView diseaseName = (TextView) convertView.findViewById(android.R.id.text1);
        diseaseName.setText(getItem(position).getName());
        return convertView;
    }
    
    public void search(String key){
        getFilter().filter(key);
    }

    @Override
    public Filter getFilter() {
        return new DiseaseFilter();
    }
    
    private Object mLock = new Object();
    private class DiseaseFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            clear();
            if (prefix == null || prefix.length() == 0) {
                ArrayList<KBDrugUseKind> list;
                synchronized (mLock) {
                    list = new ArrayList<KBDrugUseKind>(mDrug);
                }
                results.values = list;
                results.count = list.size();
                
            }else {
                String prefixString = prefix.toString().toLowerCase(Locale.getDefault());
                ArrayList<KBDrugUseKind> values;
                final ArrayList<KBDrugUseKind> newValues = new ArrayList<KBDrugUseKind>();
                synchronized (mLock) {
                    values = new ArrayList<KBDrugUseKind>(mDrug);
                    final int count = values.size();
                    for(int i=0;i<count;i++) {
                        String valueText = values.get(i).getName();
                        if(valueText.startsWith(prefixString)||valueText.contains(prefixString)) {
                            newValues.add(values.get(i));
                        }else if(PingYinUtil.getAllPinYin(valueText).startsWith(prefixString)){
                            //按拼音查询
                            newValues.add(values.get(i));
                        }else {
                            final String[] words = valueText.split(" ");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)||words[k].contains(prefixString)) {
                                    newValues.add(values.get(i));
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                FilterResults results) {
            if(results.values!=null) {
                addAll((List<KBDrugUseKind>) results.values);
            }
            Logger.d("publishResults ==>", getCount() + "");
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
        
    }

}
