package com.gwi.ccly.android.commonlibrary.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Pattern;

public class TextUtil {

    /**
     * 添加下划线
     * 
     * @param context
     *            上下文
     * @param textView
     *            添加下划线的TextView
     * @param start
     *            添加下划线开始的位置
     * @param end
     *            添加下划线结束的位置
     */
    public static void addUnderlineText(final Context context,
            final TextView textView, final int start, final int end,
            final IHyperlinkText callback) {
        textView.setFocusable(true);
        textView.setClickable(true);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
                textView.getText().toString().trim());
        spannableStringBuilder.setSpan(new UnderlineSpan(), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                callback.hyperlinkClick();
            }
        }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setTextColor(Color.GRAY);
    }

    public static boolean matchPhone(String text) {
        if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
            return true;
        }
        return false;
    }

    /**
     * <b>55.23</b>元
     * 
     * @param text
     * @return
     */
    public static SpannableStringBuilder colorText(String text,int color,int start,
            int end) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new ForegroundColorSpan(color), start, end,
                SpannableStringBuilder.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }
    
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str)||"null".equalsIgnoreCase(str);
    }

    /**
     * 超链接回调
     * @author 彭毅
     *
     */
    public interface IHyperlinkText {
        public void hyperlinkClick();
    }

}
