package com.gwi.selfplatform.ui.view;

import com.gwi.selfplatform.common.utils.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ExSideBar extends View {
    private char[] l;  
    private SectionIndexer sectionIndexter = null;  
    private ExpandableListView list;  
    private TextView mDialogText;
    private Paint paint = new Paint(); 
    private Handler mHandler;
    public ExSideBar(Context context) {  
        super(context);  
        init();  
    }  
    public ExSideBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }  
    private void init() {  
        l = new char[] {'#','A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',  
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };  
        mHandler = new Handler();
    }  
    public ExSideBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle); 
        init();  
    }  
    public void setExpandableListView(ExpandableListView _list,ExpandableListAdapter adapter) {  
        list = _list;  
        sectionIndexter = (SectionIndexer) adapter;  
    }  
    public void setTextView(TextView mDialogText) {  
        this.mDialogText = mDialogText;  
    }  
   
    private boolean mTouched = false;
    int mIdx = -1;
    public boolean onTouchEvent(MotionEvent event) {  
        super.onTouchEvent(event);  
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
            mTouched = true;
            float y = (int) event.getY();  
            int idx = (int)(y / getHeight() * l.length);  
            if (idx >= 0 && idx < l.length) {  
                mDialogText.setVisibility(View.VISIBLE);
                mDialogText.setText(""+l[idx]);
                if (sectionIndexter == null) {  
                    sectionIndexter = (SectionIndexer) list.getAdapter();  
                }  
                mIdx = idx;
                int position=-1;
                if(idx==0) {
                    position = 0;
                }else {
                    position = sectionIndexter.getPositionForSection(l[idx]);  
                }
                if (position == -1) {  
                    invalidate();
                    return true;  
                }  
                list.setSelection(position);  
            }else {
                mIdx = -1;
            }
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            mHandler.postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    mDialogText.setVisibility(View.INVISIBLE);
                }
            }, 200);
            Logger.d("ACTION_UP", mIdx+","+mTouched);
            mTouched = false;
            mIdx = -1;
            invalidate();
            break;
        }
        return true;  
    }  
    
    protected void onDraw(Canvas canvas) { 
        float widthCenter = getMeasuredWidth()/2;  
        float m_nItemHeight = getHeight() / l.length;
        if(mTouched) {
            canvas.drawColor(0x7F313131); 
        }
        for (int i = 0; i < l.length; i++) {  
            if(mTouched&&mIdx==i) {
                paint.setColor(Color.BLUE);
                paint.setTextSize(42);
            }else {
                paint.setColor(0xff595c61); 
                paint.setTextSize(30);
            }
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.CENTER); 
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight * i + m_nItemHeight, paint); 
            paint.reset();
        }  
        super.onDraw(canvas);  
    }
}
