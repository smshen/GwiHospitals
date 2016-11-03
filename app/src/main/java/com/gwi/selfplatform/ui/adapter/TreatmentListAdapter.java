package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.beans.KBTreatmentDetails;
import com.gwi.selfplatform.ui.view.PingYinUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TreatmentListAdapter extends ArrayAdapter<KBTreatmentDetails> {
    
    private List<KBTreatmentDetails> mTotalList;

    public TreatmentListAdapter(Context context, List<KBTreatmentDetails> objects) {
        super(context, 0, 0, objects);
        mTotalList = new ArrayList<KBTreatmentDetails>();
    }
    
    public void setTotals(List<KBTreatmentDetails> list) {
        mTotalList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_baike, parent,false);
        }
        TextView item = (TextView) convertView.findViewById(android.R.id.text1);
        KBTreatmentDetails treatment = getItem(position);
        item.setText(treatment.getTreatmentName());
        return convertView;
    }
    @Override
    public Filter getFilter() {
        return new TreatmentFilter();
    }
    
    public void search(CharSequence key) {
        getFilter().filter(key);
    }
    
    Object mLock = new Object();
    private class TreatmentFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (prefix == null || prefix.length() == 0) {
                ArrayList<KBTreatmentDetails> list;
                synchronized (mLock) {
                    list = new ArrayList<KBTreatmentDetails>(mTotalList);
                }
                results.values = list;
                results.count = list.size();
                
            }else {
                String prefixString = prefix.toString().toLowerCase(Locale.getDefault());
                ArrayList<KBTreatmentDetails> values;
                final ArrayList<KBTreatmentDetails> newValues = new ArrayList<KBTreatmentDetails>();
                synchronized (mLock) {
                    values = new ArrayList<KBTreatmentDetails>(mTotalList);
                    final int count = values.size();
                    for(int i=0;i<count;i++) {
                        String valueText = values.get(i).getTreatmentName();
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
                addAll((List<KBTreatmentDetails>) results.values);
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
        
    }
}
