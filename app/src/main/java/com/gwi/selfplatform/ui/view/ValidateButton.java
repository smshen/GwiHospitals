package com.gwi.selfplatform.ui.view;


import com.gwi.phr.hospital.R;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 验证码获取按钮
 * @author 彭毅
 *
 */
public class ValidateButton extends Button {
    
    private final static long INTERNAL_MILLIS = 1000;
    //60==>120
    private static final int MAX = 120;
    
    private final static int MSG_VALIDATE_BLOCK_START = 0x0001;
    private final static int MSG_VALIDATE_BLOCK_CYCLE = 0x0002;
    private final static int MSG_VALIDATE_BLOCK_END = 0X0003;
    
    private static String mTextGetCode = null;
    private static String mTextBlock = null;
    private boolean mIsBlocked = false;
    
    private Handler mHandler = new Handler(new Handler.Callback() {
        private int count = 0;
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
            case MSG_VALIDATE_BLOCK_START:
                setText(String.format(mTextBlock, MAX-count));
                setDisabled(false);
                mHandler.sendEmptyMessageDelayed(MSG_VALIDATE_BLOCK_CYCLE, INTERNAL_MILLIS);
                mIsBlocked = true;
                break;
            case MSG_VALIDATE_BLOCK_CYCLE:
                setText(String.format(mTextBlock, MAX-count));
                if(count>=MAX){
                    mHandler.sendEmptyMessage(MSG_VALIDATE_BLOCK_END);
                }else {
                    mHandler.sendEmptyMessageDelayed(MSG_VALIDATE_BLOCK_CYCLE, INTERNAL_MILLIS);
                    count++;
                }
                break;
            case MSG_VALIDATE_BLOCK_END:
                setDisabled(true);
                setText(mTextGetCode);
                count=0;
                mIsBlocked = false;
                break;
            }
            return false;
        }
    });
    
    private void setDisabled(boolean isDisabed) {
        setClickable(isDisabed);
        setEnabled(isDisabed);
        setFocusable(isDisabed);
    }

    public ValidateButton(Context context) {
        super(context);
        init();
    }

    public ValidateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValidateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init() {
        mTextGetCode = getContext().getString(R.string.btn_get_code);
        mTextBlock = getContext().getString(R.string.btn_get_block);
    }

    public void setInitMessage(String message) {
        mTextGetCode = message;
    }

    public void sendBlockMessage() {
        mHandler.sendEmptyMessage(MSG_VALIDATE_BLOCK_START);
    }
    
    public boolean isBlocked() {
        return mIsBlocked;
    }
    
}
