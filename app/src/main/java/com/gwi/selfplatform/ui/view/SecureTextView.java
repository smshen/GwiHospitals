package com.gwi.selfplatform.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.validator.CardValidator;
import com.gwi.selfplatform.common.utils.validator.Validator;

/**
 * @author 彭毅
 * @date 2015/4/21.
 */
public class SecureTextView extends TextView {

    CardValidator mCardValidator;

    public SecureTextView(Context context) {
        super(context);
        init();
    }

    public SecureTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SecureTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SecureTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mCardValidator = new CardValidator();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Logger.d("EncryptTextView","onTextChanged");
        if (mCardValidator!=null&&!TextUtils.isEmpty(text)&& !text.toString().contains("*")&&mCardValidator.validate(text.toString(), CardValidator.CARD_ID) == Validator.SUCCESS) {
//            setText(text.subSequence(0, 6) + "********" + text.subSequence(14, 17) + "*");
            setText(text.subSequence(0, 1) + "****************" + text.subSequence(17, 18));
        }
    }

}
