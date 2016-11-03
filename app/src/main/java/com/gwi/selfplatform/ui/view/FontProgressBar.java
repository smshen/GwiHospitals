package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.GlobalSettings;


/**
 * @author 彭毅
 * @date 2015/3/31.
 */
public class FontProgressBar extends View{
    Paint mBackLinePaint;
    Paint mFontLinePaint;
    Paint mCirclePiant;
    PointF mCurrntPos;

    /** 单独使用 */
    private static final int MODE_NORMAL = 1;
    /** 作为popWindow的内容 */
    private static final int MODE_POP_WINDOW = 2;

    private static final float RADIUS_NORMAL = 10f;
    private static final float RADIUS_PRESS = 20f;
    private static final int FONT_MIN = 16;
    private static final int FONT_MAX = 30;
    private int mCurrentVal = 16;
    private int mPopPosY = 0;

    private boolean mIsPressed = false;
    private TextView mTvFont;

    private int mCurrentMode = MODE_NORMAL;

    private Integer mInitialFontSize;

    private onValueChangeListener mOnValueChangeListner;

    public void setOnValueChangeListner(onValueChangeListener listner) {
        mOnValueChangeListner = listner;
    }

    PopupWindow mFontSizeHintPopWindow;
    public FontProgressBar(Context context) {
        super(context);
        init();
    }

    public FontProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttrs(attrs);
        init();
    }



    private void handleAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontProgressBar);
        mCurrentMode = a.getInt(R.styleable.FontProgressBar_Mode,MODE_NORMAL);
    }

    private void init() {

        mCirclePiant = new Paint();
        mCirclePiant.setColor(Color.GREEN);
        mCirclePiant.setAntiAlias(true);
        mCirclePiant.setStyle(Paint.Style.FILL);


        mBackLinePaint = new Paint();
        mBackLinePaint.setColor(Color.GRAY);
        mBackLinePaint.setAntiAlias(true);

        mFontLinePaint = new Paint();
        mFontLinePaint.setColor(Color.GREEN);
        mFontLinePaint.setAntiAlias(true);
        mFontLinePaint.setStyle(Paint.Style.FILL);
        mFontLinePaint.setStrokeWidth(4);
        mCurrntPos = new PointF(0,0);

        mInitialFontSize = GlobalSettings.INSTANCE.getFontSize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int center_vertical = height/2;
        mCurrntPos.y = center_vertical;
        float curRadius = mIsPressed?RADIUS_PRESS:RADIUS_NORMAL;
        int maxXOfCircle = (int) (width-RADIUS_NORMAL/2);
        int minXofCircle = -(int) (RADIUS_NORMAL/2);
        int x;
       if(mInitialFontSize!=null){
           x = (mInitialFontSize-FONT_MIN)*getWidth()/(FONT_MAX-FONT_MIN);
           mInitialFontSize = null;
       }else if(mCurrntPos.x<minXofCircle) {
            x = minXofCircle;
        }else if(mCurrntPos.x>maxXOfCircle) {
            x = maxXOfCircle;
        }else {
            x = (int) mCurrntPos.x;
        }
        canvas.drawCircle(x,mCurrntPos.y,curRadius,mCirclePiant);
        canvas.drawLine(0,center_vertical,x+curRadius/2,center_vertical,mFontLinePaint);
        canvas.drawLine(x+curRadius/2,center_vertical,width,center_vertical,mBackLinePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_UP:
                mIsPressed = false;
                updatePopWindow(event.getX(),event.getY(),mCurrentVal);
                //更新数据
                if(mOnValueChangeListner!=null) {
                    mOnValueChangeListner.onValueChanged(FONT_MIN+mCurrentVal);
                }
                dismissFontPopWindow();
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                createPopWindow();
                mIsPressed = true;
                mCurrntPos.x = event.getX();
                //获取当前的值
                mCurrentVal = (int) (mCurrntPos.x*(FONT_MAX-FONT_MIN)/getWidth());
                updatePopWindow(event.getX(),mPopPosY,mCurrentVal);
                invalidate();
                //return true for trigger action_up.
                return true;
            case MotionEvent.ACTION_OUTSIDE:
                Toast.makeText(getContext(),"out",Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_MOVE:
                mIsPressed = true;
                mCurrntPos.x = event.getX();
                mCurrentVal = (int) (mCurrntPos.x*(FONT_MAX-FONT_MIN)/getWidth());
                updatePopWindow(event.getX()-RADIUS_PRESS/2,mPopPosY,mCurrentVal);
                invalidate();
                return true;

        }

        return super.onTouchEvent(event);
    }

    private void createPopWindow() {
        if(mFontSizeHintPopWindow==null) {
            int[] loc = new int[2];
            this.getLocationOnScreen(loc);
//            mPopPosY = getBottom()+getPaddingTop()+getHeight()/2
//                        +(mCurrentMode==MODE_NORMAL?0:getActionBarHeight());
//            mPopPosY = loc[1];
            mTvFont = (TextView) View.inflate(getContext(),R.layout.layout_font_change,null);
            mTvFont.setText("");
            mFontSizeHintPopWindow= new PopupWindow(mTvFont, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,false);
            mFontSizeHintPopWindow.setAnimationStyle(R.style.custom_popwindow_anim_style);
            mFontSizeHintPopWindow.showAsDropDown(this);
        }
    }

    private void updatePopWindow(float x, float y,int value) {
        if(mTvFont!=null) {
            mTvFont.setText(String.valueOf(FONT_MIN+value));
        }
        int curX;
     if(x>getRight()){
            curX = getRight()-getRightPaddingOffset();
        }else {
            curX = (int)x+ getLeft()+getLeftPaddingOffset();
        }
        if(mFontSizeHintPopWindow.isShowing()) {
            mFontSizeHintPopWindow.update(curX,(int)y, -1, -1,true);
        }else {
            mFontSizeHintPopWindow.showAsDropDown(this,curX,(int)y);
        }
    }

    private void dismissFontPopWindow() {
        mFontSizeHintPopWindow.dismiss();
        mFontSizeHintPopWindow = null;
    }

    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        Log.d("DI",actionBarHeight+"");
        return actionBarHeight;
    }

    public interface onValueChangeListener {
        void onValueChanged(int value);
    }
}
