package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.gwi.phr.hospital.R;

public class ShakableEditText extends EditText{
    
    private Drawable mClear = null;

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(getCompoundDrawables()[2]==null) {
                return false;
            }
            if(event.getAction()!=MotionEvent.ACTION_UP) {
                return false;
            }
            if (event.getX() > getWidth() - getPaddingRight() - mClear.getIntrinsicWidth()) {
                setText("");
                setCompoundDrawables(null, null, null, null);
            }
            return false;
        }
    };
    
    public ShakableEditText(Context context) {
        super(context);
        init();
    }
    
    public ShakableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public ShakableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }



    private void init() {
        setOnTouchListener(mOnTouchListener);
        mClear = getResources().getDrawable(R.drawable.ic_action_remove);
        mClear.setBounds(0, 0, mClear.getIntrinsicWidth(), mClear.getIntrinsicHeight());

    }
    
    private void showRemove() {
        setCompoundDrawables(null, null, getText().toString().equals("")?null:mClear, null);
    }
    
    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        if(focused&&isEnabled()) {
            showRemove();
        }else {
            setCompoundDrawables(null, null, null, null);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    public void shakeText() {
        Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        startAnimation(shake);
        requestFocus();
    }


    @Override
    protected void onTextChanged(CharSequence text, int start,
            int lengthBefore, int lengthAfter) {
        if (isEnabled()) {
            showRemove();
        } else {
            setCompoundDrawables(null, null, null, null);
        }
    }


}
