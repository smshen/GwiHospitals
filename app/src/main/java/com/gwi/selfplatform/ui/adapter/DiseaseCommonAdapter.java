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
import com.gwi.selfplatform.module.net.beans.KBDiseaseDetails;
import com.gwi.selfplatform.ui.view.PingYinUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 疾病列表adapter
 * @author 彭毅
 *
 */
public class DiseaseCommonAdapter extends ArrayAdapter<KBDiseaseDetails>{
    
    private List<KBDiseaseDetails> mDiseases;

    public DiseaseCommonAdapter(Context context, List<KBDiseaseDetails> objects) {
        super(context, 0, 0, objects);
        mDiseases = new ArrayList<KBDiseaseDetails>();
    }
    
    /**
     * 获取所有数据，用于数据搜索
     * @param list
     */
    public void setAllSearchResults(List<KBDiseaseDetails> list) {
        if(list!=null&&!list.isEmpty()) {
            mDiseases.addAll(list);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_baike, parent,false);
        }
        TextView diseaseName = (TextView) convertView.findViewById(android.R.id.text1);
        diseaseName.setText(getItem(position).getDiseaseName());
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
            if (prefix == null || prefix.length() == 0) {
                ArrayList<KBDiseaseDetails> list;
                synchronized (mLock) {
                    list = new ArrayList<KBDiseaseDetails>(mDiseases);
                }
                results.values = list;
                results.count = list.size();
                
            }else {
                String prefixString = prefix.toString().toLowerCase(Locale.getDefault());
                ArrayList<KBDiseaseDetails> values;
                final ArrayList<KBDiseaseDetails> newValues = new ArrayList<KBDiseaseDetails>();
                synchronized (mLock) {
                    values = new ArrayList<KBDiseaseDetails>(mDiseases);
                    final int count = values.size();
                    for(int i=0;i<count;i++) {
                        String valueText = values.get(i).getDiseaseName();
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
            clear();
            if(results.values!=null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                    addAll((List<KBDiseaseDetails>) results.values);
                } else {
                    List<KBDiseaseDetails> detals = (List<KBDiseaseDetails>)  results.values;
                    for (KBDiseaseDetails detail :detals) {
                        add(detail);
                    }
                }
            }
            Logger.d("publishResults ==>", getCount() + "");
//            updateChildren((List<KBDiseaseDetails>) results.values,true);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
        
    }

}
