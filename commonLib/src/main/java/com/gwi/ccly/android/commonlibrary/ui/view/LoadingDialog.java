package com.gwi.ccly.android.commonlibrary.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gwi.ccly.android.commonlibrary.R;
import com.gwi.ccly.android.commonlibrary.ui.base.BaseDialog;


/**
 * 加载对话框
 * @author Peng Yi
 *
 */
public class LoadingDialog extends BaseDialog {

    private CharSequence mText = null;
    
    public LoadingDialog(Context context, CharSequence text) {
        super(context);
        mText = text;
        init();
    }
    
    private void init() {
        setContentView(R.layout.layout_dialog_loading);
        final TextView msgText = (TextView) findViewById(R.id.dialog_loading_text);
        if(TextUtils.isEmpty(mText)){
            msgText.setVisibility(View.GONE);
        }else {
            msgText.setText(mText);
        }
    }
    
}
