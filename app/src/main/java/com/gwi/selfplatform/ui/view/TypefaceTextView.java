package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefaceTextView extends TextView {

    public TypefaceTextView(Context context) {
        super(context);
        init();
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init() {
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "fonts/Clockopia.ttf");
        this.setTypeface(face);
    }

}
