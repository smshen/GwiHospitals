package com.gwi.selfplatform.ui.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;

/**
 * 显示自定义选项卡标题的ViewPager Indicator
 * 
 * @author user
 * 
 */
public class ExTabPageIndicator extends HorizontalScrollView implements PageIndicator {
    public static final String TAG = ExTabPageIndicator.class.getSimpleName();
    /**
     * 空标题
     */
    private static final CharSequence EMPTY_TITLE = "";

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
    
    private int mMaxTabWidth;
    private int mSelectedTabIndex;

    private Runnable mTabSelector;
    
    private TabLinearLayout mTabLayout = null;


    private OnClickListener mTabOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            TabView tabView = (TabView) v;
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
        }
    };

    public ExTabPageIndicator(Context context) {
        this(context, null);
    }

    public ExTabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabLayout = new TabLinearLayout(getContext()); 
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        Logger.d(TAG, "mTabLayout:"+childCount); 
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }
    
    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            @Override
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if(mListener!=null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }


    public ViewPager.OnPageChangeListener getListener() {
        return mListener;
    }

    public void setListener(ViewPager.OnPageChangeListener listener) {
        this.mListener = listener;
    }

    private void addTab(int index, CharSequence text) {
        Logger.d(TAG, "addTab"+mTabLayout.getMeasuredHeight());
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setText(text);
        tabView.setOnClickListener(mTabOnClickListener);
        
        mTabLayout.addView(tabView, index, new LinearLayout.LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1));
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    /**
     * 初始化 viewPager，并设置相应的margin，更新UI
     */
    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("View Pager没有初始化adapter.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);

        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("没有设置viewPager!");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();

        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        // 创建tab，并设置标题
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            addTab(i, title == null ? EMPTY_TITLE : title);
        }
        // 修正index
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        // 更新状态
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    /**
     * tab 标题视图
     * 
     * @author 彭毅
     * 
     */
    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.ExTabPageTitleStyle);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }

}
