package com.gwi.selfplatform.ui.dialog;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;
import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.module.net.response.G1417;

/**
 * 预约挂号时，选择预约医生选择“上午”或“下午”后弹出的详细时间点的对话框
 * 
 * @author 彭毅
 *
 */
public class DoctorAppointTimeSelectDialog extends BaseDialog {

    private List<G1417> mTimePoints;
    private GridView mGrdView;
    private TimePointAdapter mAdapter;
    
    private G1417 mSelectedTime;

    public G1417 getSelectedTime() {
        return mSelectedTime;
    }

    public DoctorAppointTimeSelectDialog(Context context, List<G1417> timePoints) {
        super(context);
        mTimePoints = timePoints;
        Logger.d("tag", mTimePoints.size()+"==");
//        if(mTimePoints.size()%2!=0) {
//            mTimePoints.add(new G1417());
//        }
        init();
    }
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        if(listener!=null) {
            mGrdView.setOnItemClickListener(listener);
        }
    }

    private void init() {
        setDialogContentView(R.layout.dialog_grid_view);
        mGrdView = (GridView) findViewById(R.id.dlg_grdview);
        mGrdView.setNumColumns(2);
        mAdapter = new TimePointAdapter(getContext(), mTimePoints);
        mGrdView.setAdapter(mAdapter);
        setTitle("请选择预约时间 - "+mTimePoints.get(0).getDoctName());
        showHeader(true);
        showFooter(true);
        
    }

    private class TimePointAdapter extends ArrayAdapter<G1417> {

        public TimePointAdapter(Context context, List<G1417> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView timePoint;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent,
                        false);
            }
            timePoint = (TextView) convertView.findViewById(android.R.id.text1);
            timePoint.setBackgroundResource(R.drawable.selector_btn_white);
            timePoint.setGravity(Gravity.CENTER);
            G1417 timeData = getItem(position);
            if(timeData.getStartTime()!=null) {
               timePoint.setText(timeData.getStartTime()); 
               timePoint.setTag(timeData);
               timePoint.setEnabled(true);
            }else {
                timePoint.setEnabled(false);
            }
            return convertView;
        }

    }
    
    public void show(String text,
            DialogInterface.OnClickListener listener) {
        setLeftButton(text, listener);
        show();
    }

}
