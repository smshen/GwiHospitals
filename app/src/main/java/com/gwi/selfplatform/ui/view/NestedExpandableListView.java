package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ExpandableListView;

/**
 * 可以<b>嵌套</b>的ExpandableListView:可以作为三级列表中的二级列表
 * @author 彭毅
 *
 */
public class NestedExpandableListView extends ExpandableListView {
    DisplayMetrics mDM;

    public NestedExpandableListView(Context context) {
        super(context);
        init();
    }
    
    public NestedExpandableListView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NestedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDM = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDM);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 重新绘制高度，保证本视图作为ExpandableListView的childView时高度不为0
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mDM.heightPixels,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}