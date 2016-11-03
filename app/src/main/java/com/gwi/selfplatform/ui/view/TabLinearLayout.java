package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class TabLinearLayout extends LinearLayout {
    
    private static final int MARGIN_MIN = 4;
    private static int mChildMargin = MARGIN_MIN;

    public TabLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TabLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabLinearLayout(Context context) {
        super(context);
        init();
    }
    
    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
    }
    
  @Override
  protected void measureChildWithMargins(View child,
          int parentWidthMeasureSpec, int widthUsed,
          int parentHeightMeasureSpec, int heightUsed) {
      final int index = indexOfChild(child);
      final LayoutParams params = (LayoutParams) child.getLayoutParams();

      if (isHasChildBefore(index)) {
          params.leftMargin = mChildMargin;
      }
      super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed,
              parentHeightMeasureSpec, heightUsed);
  }
    
  /**
   * 判断tab前面是否有tab
   * 
   * @param childIndex
   * @return
   */
  private boolean isHasChildBefore(int childIndex) {
      if (childIndex == 0 || childIndex == getChildCount()) {
          return false;
      } else {
          return true;
      }
  }

}
