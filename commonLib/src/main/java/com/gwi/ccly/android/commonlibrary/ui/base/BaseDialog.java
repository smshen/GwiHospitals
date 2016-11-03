package com.gwi.ccly.android.commonlibrary.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.R;


public class BaseDialog extends Dialog implements
        View.OnClickListener {

    private LinearLayout mLlHeader;
    private TextView mEtTitle;
    private LinearLayout mLlContent;
    private TextView mTvContent;
    private LinearLayout mLlFooter;
    private View mFooterBtnDivider;
    private Button mBtnLeft;
    private Button mBtnRight;

    private OnClickListener mOnClickListenerLeft;
    private OnClickListener mOnClickListenerRight;

    public BaseDialog(Context context) {
        this(context, R.style.FullScreenDialog);
        setContentView(R.layout.layout_dialog_base);
        initViews();
        initEvents();
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable,
                         OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initViews() {
        mLlHeader = (LinearLayout) findViewById(R.id.dialog_layout_header);
        mLlContent = (LinearLayout) findViewById(R.id.dialog_layout_content);
        mTvContent = (TextView) findViewById(R.id.dialog_tv_content);
        mLlFooter = (LinearLayout) findViewById(R.id.dialog_layout_footer);
        mFooterBtnDivider = findViewById(R.id.dialog_footer_btn_divider);

        mEtTitle = (TextView) findViewById(R.id.dialog_tv_title);
        mBtnLeft = (Button) findViewById(R.id.dialog_btn_left);
        mBtnRight = (Button) findViewById(R.id.dialog_btn_right);
    }

    private void initEvents() {
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
    }

    /**
     * 加载对话框内容视图。
     * @param resId 相应的对话框内容资源
     * @return 返回得到的View
     */
    public View setDialogContentView(int resId) {
        View v = LayoutInflater.from(getContext()).inflate(resId, null);
        if(mLlContent.getChildCount()>=1) {
            mLlContent.removeAllViews();
        }
        mLlContent.addView(v);
        return v;
    }

    /**
     * 是否显示对话框底部布局。
     * @param bShow
     */
    public void showFooter(boolean bShow) {
        if(bShow) mLlFooter.setVisibility(View.VISIBLE);
        else mLlFooter.setVisibility(View.GONE);
    }


    public void showHeader(boolean bShow) {
        if(bShow) mLlHeader.setVisibility(View.VISIBLE);
        else mLlHeader.setVisibility(View.GONE);
    }

    /**
     * 设置对话框标题
     * @param title
     */
    public void setTitle(CharSequence title) {
        mEtTitle.setText(title);
    }

    public void setContent(CharSequence text) {
        if(mTvContent!=null) {
            mTvContent.setText(text);
        }
    }

    /**
     * 显示Left button.
     * @param text
     * @param listener
     */
    public void setLeftButton(String text,
            OnClickListener listener) {
        if (text == null) {
            mBtnLeft.setVisibility(View.GONE);
            return;
        }
        mBtnLeft.setText(text);
        mOnClickListenerLeft = listener;
        mBtnLeft.setVisibility(View.VISIBLE);
    }

    /**
     * 显示Right button.
     * @param text
     * @param listener
     */
    public void setRightButton(String text,OnClickListener listener) {
        if (text == null) {
            mBtnRight.setVisibility(View.GONE);
            return;
        }
        mBtnRight.setText(text);
        mBtnRight.setVisibility(View.VISIBLE);
        mFooterBtnDivider.setVisibility(View.VISIBLE);
        mOnClickListenerRight = listener;
    }

    public void setTwoButton(String leftText,
            OnClickListener leftListener,String rightText,OnClickListener rightListener) {
        mBtnLeft.setText(leftText);
        mBtnRight.setText(rightText);
        mOnClickListenerLeft = leftListener;
        mOnClickListenerRight = rightListener;
        mBtnLeft.setVisibility(View.VISIBLE);
        mFooterBtnDivider.setVisibility(View.VISIBLE);
        mBtnRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
		if (id == R.id.dialog_btn_left) {
			if(mOnClickListenerLeft!=null) {
                mOnClickListenerLeft.onClick(this, 0);
                this.dismiss();
            }
		} else if (id == R.id.dialog_btn_right) {
			if(mOnClickListenerRight!=null) {
                mOnClickListenerRight.onClick(this, 1);
                this.dismiss();
            }
		}

    }

}
