package com.gwi.selfplatform.ui.activity.expand;

import android.os.Bundle;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.TextUtil;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class ParkingCarActivity extends HospBaseActivity {
    private static final String TAG = ParkingCarActivity.class.getSimpleName();

    @Override
    protected void initViews() {
        setTitle(R.string.label_parking_car);

        // for test
        TextView leftNum = (TextView) findViewById(R.id.txt_left_num);
        TextView leftNum1 = (TextView) findViewById(R.id.txt_left_num1);
        TextView leftNum2 = (TextView) findViewById(R.id.txt_left_num2);

        String leftStr = "剩余数量：25";
        leftNum.setText(TextUtil.colorText(leftStr, getResources().getColor(R.color.colorPrimary), 5, leftStr.length()));
        leftNum1.setText(TextUtil.colorText(leftStr, getResources().getColor(R.color.colorPrimary), 5, leftStr.length()));
        leftNum2.setText(TextUtil.colorText(leftStr, getResources().getColor(R.color.colorPrimary), 5, leftStr.length()));
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_car);
        addBackListener();
        initViews();
        initEvents();
    }
}
