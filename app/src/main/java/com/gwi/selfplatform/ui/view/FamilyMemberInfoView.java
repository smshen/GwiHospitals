package com.gwi.selfplatform.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/*
 * 家庭成员信息视图
 */
public class FamilyMemberInfoView extends LinearLayout {

    public FamilyMemberInfoView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public FamilyMemberInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FamilyMemberInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

}
