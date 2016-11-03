package com.gwi.selfplatform.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SideBar extends View {
    private char[] l;
    private SectionIndexer sectionIndexter = null;
    private ListView list;
    private TextView mDialogText;
    private Paint paint = new Paint();

    public SideBar(Context context) {
        super(context);
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        l = new char[]{'#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListView(ListView _list) {
        list = _list;
        sectionIndexter = (SectionIndexer) _list.getAdapter();
    }

    public void setTextView(TextView mDialogText) {
        this.mDialogText = mDialogText;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                float y = (int) event.getY();
                int idx = (int) (y / getHeight() * l.length);
                if (idx >= 0 && idx < l.length) {
                    mDialogText.setVisibility(View.VISIBLE);
                    mDialogText.setText("" + l[idx]);
                    if (sectionIndexter == null) {
                        sectionIndexter = (SectionIndexer) list.getAdapter();
                    }
                    int position = sectionIndexter.getPositionForSection(l[idx]);
                    if (position == -1) {
                        return true;
                    }
                    list.setSelection(position);
                }
                break;

            case MotionEvent.ACTION_UP:
                mDialogText.setVisibility(View.INVISIBLE);
                break;
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        float widthCenter = getMeasuredWidth() / 2;
        float m_nItemHeight = getHeight() / l.length;
        for (int i = 0; i < l.length; i++) {
            paint.setColor(0xff595c61);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight * i + m_nItemHeight, paint);
            paint.reset();
        }
        super.onDraw(canvas);
    }
}
