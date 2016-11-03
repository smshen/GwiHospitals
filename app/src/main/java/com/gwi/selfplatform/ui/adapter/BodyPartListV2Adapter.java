package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.beans.KBBodyPart;

import java.util.List;

public class BodyPartListV2Adapter extends ArrayAdapter<KBBodyPart> {
    public BodyPartListV2Adapter(Context context, List<KBBodyPart> objects) {
        super(context, 0, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckedTextView partText;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_smart_body_part, parent, false);
            partText = (CheckedTextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(partText);
        } else {
            partText = (CheckedTextView) convertView.getTag();
        }
        KBBodyPart bodyPart = getItem(position);
        partText.setText(bodyPart.getName());
        return convertView;
    }
}
