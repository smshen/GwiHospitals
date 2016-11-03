package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.GridView;

/**
 * @author 彭毅
 * @date 2015/5/11.
 */
public class NestedGridView extends GridView {
    DisplayMetrics mDM;
    public NestedGridView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mDM = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDM);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // 重新绘制高度，保证本视图作为ExpandableListView的childView时高度不为0
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mDM.heightPixels,MeasureSpec.AT_MOST);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
