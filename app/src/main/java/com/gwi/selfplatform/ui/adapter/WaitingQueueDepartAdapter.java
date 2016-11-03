package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.module.net.response.G1217;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 等待序列Adapter
 * 
 * @author 彭毅
 *
 */
public class WaitingQueueDepartAdapter extends ArrayAdapter<G1217> {

    public WaitingQueueDepartAdapter(Context context, List<G1217> objects) {
        super(context, 0, objects);
        //First.
        setupExecLocType();
    }

    /**
     * 根据比较的值来获取累加的就诊科室类型数量
     * 
     * @param val
     * @return
     */
    private int getColor(Integer val) {
        if( val == null ) {
            val = 0;
        }
        switch (val % 2) {
        case 0:
            return getContext().getResources().getColor(R.color.bg_btn_blue);
        case 1:
            return getContext().getResources().getColor(R.color.text_green);
        default:
            return Color.LTGRAY;
        }
    }

    private Map<String,Integer> mExecLocTypeList = null;

    private void setupExecLocType() {
        if (mExecLocTypeList == null) {
            mExecLocTypeList = new HashMap<String,Integer>();
        }else {
            mExecLocTypeList.clear();
        }
            String currentType = null;
            int idx = 0;
            for (int i = 0, n = getCount(); i < n; i++) {
                G1217 item = getItem(i);
                if (!item.getExecLocation().equals(currentType)) {
                    currentType = item.getExecLocation();
                    mExecLocTypeList.put(currentType, idx);
                    idx++;
                }
            }
    }

    @Override
    public void notifyDataSetChanged() {
        //Each time updating.
        setupExecLocType();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_waiting_queue_depart, parent, false);
            holder = new ViewHolder();
            holder.tvNumber = (TextView) convertView
                    .findViewById(R.id.waiting_queue_item_number);
            holder.tvPaitentName = (TextView) convertView
                    .findViewById(R.id.waiting_queue_item_paitent_name);
            holder.tvWatingNo = (TextView) convertView
                    .findViewById(R.id.waiting_queue_item_wating_no);
            holder.tvExecLocation = (TextView) convertView
                    .findViewById(R.id.waiting_queue_item_exec_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        G1217 queue = getItem(position);
        // Group by.
        int color = getColor(mExecLocTypeList.get(queue.getExecLocation()));
        holder.tvExecLocation.setTextColor(color);
        holder.tvNumber.setTextColor(color);
        holder.tvPaitentName.setTextColor(color);
        holder.tvWatingNo.setTextColor(color);
        holder.tvExecLocation.setTextColor(color);

        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.tvPaitentName.setText(queue.getPatName().substring(0, 1) + "**");
        holder.tvWatingNo.setText(queue.getCurrentNo());
        holder.tvExecLocation.setText(queue.getExecLocation());
        return convertView;
    }

    class ViewHolder {
        TextView tvPaitentName;
        TextView tvNumber;
        TextView tvWatingNo;
        TextView tvExecLocation;
    }

}
