package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.beans.KBTestCheckKind;

import java.util.List;

public class TestKindsAdapter extends ArrayAdapter<KBTestCheckKind> {

    public TestKindsAdapter(Context context, List<KBTestCheckKind> objects) {
        super(context, 0, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_baike, parent,false);
        }
        TextView item = (TextView) convertView.findViewById(android.R.id.text1);
        KBTestCheckKind kind = getItem(position);
        item.setText(kind.getName());
        return convertView;
    }

}
