package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.beans.KBDrugProperty;

import java.util.List;

public class DrugPropertyAdapter extends ArrayAdapter<KBDrugProperty> {

    public DrugPropertyAdapter(Context context, List<KBDrugProperty> objects) {
        super(context, 0, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_baike, parent,false);
        }
        TextView bodyPartName = (TextView) convertView.findViewById(android.R.id.text1);
        KBDrugProperty part = getItem(position);
        bodyPartName.setText(part.getName());
        return convertView;
    }
    
    

}
