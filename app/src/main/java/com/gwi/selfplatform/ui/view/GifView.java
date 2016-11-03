package com.gwi.selfplatform.ui.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.gwi.phr.hospital.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class GifView extends View {
    private Movie mMovie;
    private long mMovieStart;
    private int mGifId = 0;

    //Set to false to use decodeByteArray
    private static final boolean DECODE_STREAM = true;

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }

    public GifView(Context context) {
        super(context);
        init();
    }
    public GifView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        TypedArray a = getContext().obtainStyledAttributes(attrs,R.styleable.GifView);
        String gifSource = a.getString(R.styleable.GifView_src);
        String sourceName = Uri.parse(gifSource).getLastPathSegment().replace(".gif", "");
        mGifId = getResources().getIdentifier(sourceName, "raw", getContext().getPackageName());
        a.recycle();
        
        init();
    }
    private void init() {
        setFocusable(true);
        if(mGifId!=0) {
            
        }
        java.io.InputStream is;
        is = getContext().getResources().openRawResource(mGifId);
        if (DECODE_STREAM) {
            mMovie = Movie.decodeStream(is);
        } else {
            byte[] array = streamToBytes(is);
            mMovie = Movie.decodeByteArray(array, 0, array.length);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(0xFFCCCCCC);

        Paint p = new Paint();
        p.setAntiAlias(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, p);
        }

        long now = SystemClock.uptimeMillis();
        if (mMovieStart == 0) {   // first time
            mMovieStart = now;
        }
        if (mMovie != null) {
            int dur = mMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }
            int relTime = (int)((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, getWidth() - mMovie.width(),
                        getHeight() - mMovie.height());
            invalidate();
        }
        super.draw(canvas);
    }
    
    

}
