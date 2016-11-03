package com.gwi.selfplatform.ui.activity.nav;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.common.utils.Logger;
import com.gwi.selfplatform.common.utils.Version;
import com.gwi.selfplatform.ui.base.HospBaseActivity;

/**
 * 智能导诊页面
 * 
 * @author 彭毅
 *
 */
public class SmartTreatActivity extends HospBaseActivity {

    private static final String TAG = SmartTreatActivity.class.getSimpleName();

    private ImageView mFront;
    private ImageView mBack;
    private ImageView mColor;
    private Bitmap mBmColor;

    private CheckedTextView mCtvSex;
    private CheckedTextView mCtvOrientation;
    private ImageButton mIbList;

    private ImageView mTap;

    private GestureDetector mSingleTouchDetector;
    private ScaleGestureDetector mScaleDetector;

    private Interpolator mAccelerator = new AccelerateInterpolator();
    private Interpolator mDecelerator = new DecelerateInterpolator();

    private int mOriginalTop = 0;
    private int mOriginalLeft = 0;

    private float mScaleFactor = 1;
    private boolean bIsScaled = false;
    /* 点击时的坐标 */
    private float mTapX = 0;
    private float mTapY = 0;

    private float mRawX = 0;
    private float mRawY = 0;
    /* 身体图，显示给用户看的,0:male:1:female;[0][0]:男性正面，[0][1]:男性背面 */
    private int[][] drawable_user = new int[][] {
            { R.drawable.man_body_up, R.drawable.man_body_down },
            { R.drawable.women_body_up, R.drawable.women_body_down } };
    /* 身体图，颜色区分 */
    private int[][] drawable_color = new int[][] {
            { R.drawable.man_body_up_1, R.drawable.man_body_down_1 },
            { R.drawable.women_body_up_1, R.drawable.women_body_down_1 } };

    private WindowManager mWManager;

    private OnTouchListener onImageTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 事件传递到color时处理
            if (v.getId() == R.id.smart_treat_color) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    bIsScaled = false;
                    mTapX = event.getX();
                    mTapY = event.getY();
                    mRawX = event.getRawX();
                    mRawY = event.getRawY();
                    Logger.d("onTouch", event.getRawX() + "," + event.getRawY()
                            + "=>" + event.getX() + "," + event.getY());
                    break;
                }
            }
            if (MotionEvent.ACTION_UP == event.getAction()) {
                System.out
                        .println(event.getX() + "-onTouch up-" + event.getY());
            }
            return false;
        }
    };
    
    /**
     * 修正翻转后坐标变化的bug
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void fixXY() {
        if(mFront.getX()!=mBack.getX()||mFront.getY()!=mBack.getY()) {
            mBack.setX(mFront.getX());
            mBack.setY(mFront.getY());
        }
//        if(mColor.getX()!=mFront.getX()||mColor.getY()!=mColor.getY()) {
//            
//        }
    }

    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if(v.getId()== R.id.smart_treat_list) {
                Bundle b = new Bundle();
                b.putString(KEY_HEX_COLOR, "##ffe8e8");
                b.putBoolean(KEY_IS_MALE, !mCtvSex.isChecked());
                openActivity(BodyAndSymptomActivity.class, b);
                return;
            }else if (v.getId() == R.id.smart_treat_sex) {
                mCtvSex.setChecked(!mCtvSex.isChecked());
            } else if (v.getId() == R.id.smart_treat_orentation) {
                mCtvOrientation.setChecked(!mCtvOrientation.isChecked());
            }
            if (mCtvSex.isChecked()) {
                // 女性，背面
                if (mCtvOrientation.isChecked()) {
                    mBack.setBackgroundResource(drawable_user[1][1]);
                    mColor.setBackgroundResource(drawable_color[1][1]);
                    mCtvOrientation.setText("反面");
                } else {
                    // 女性，正面
                    mFront.setBackgroundResource(drawable_user[1][0]);
                    mColor.setBackgroundResource(drawable_color[1][0]);
                    mCtvOrientation.setText("正面");
                }
            } else {
                // 男性，背面
                if (mCtvOrientation.isChecked()) {
                    mBack.setBackgroundResource(drawable_user[0][1]);
                    mColor.setBackgroundResource(drawable_color[0][1]);
                    mCtvOrientation.setText("反面");
                } else {
                    // 男性，正面
                    mFront.setBackgroundResource(drawable_user[0][0]);
                    mColor.setBackgroundResource(drawable_color[0][0]);
                    mCtvOrientation.setText("正面");
                }
            }
            mBmColor = ((BitmapDrawable) mColor.getBackground()).getBitmap();
            if (v.getId() == R.id.smart_treat_orentation) {
                // 最后旋转
                flipit();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_treat);
        addBackListener();
        initViews();
        initEvents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void initViews() {
        mIbList = (ImageButton) findViewById(R.id.smart_treat_list);
        mCtvOrientation = (CheckedTextView) findViewById(R.id.smart_treat_orentation);
        mCtvSex = (CheckedTextView) findViewById(R.id.smart_treat_sex);
        mFront = (ImageView) findViewById(R.id.smart_treat_front);
        mBack = (ImageView) findViewById(R.id.smart_treat_back);
        mColor = (ImageView) findViewById(R.id.smart_treat_color);
        mTap = (ImageView) findViewById(R.id.smart_tap);
        mBmColor = ((BitmapDrawable) mColor.getBackground()).getBitmap();

        mHandler = new Handler();
        mTap.setVisibility(View.GONE);
        //measure first.
        mTap.measure(mTap.getLayoutParams().width, mTap.getLayoutParams().height);
        mWManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mWManager.getDefaultDisplay().getMetrics(dm);
        mColor.measure(mColor.getLayoutParams().width, mColor.getLayoutParams().height);
        mOriginalLeft = dm.widthPixels / 2 - mColor.getMeasuredWidth() / 2;
        mOriginalTop = dm.heightPixels / 2 - mColor.getMeasuredHeight() / 2;
        mSingleTouchDetector = new GestureDetector(this,
                new SigleTouchListener(mWManager));
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        
        Logger.d(TAG, new PointF(mBack.getScaleX(), mBack.getScaleY()).toString());
    }

    @Override
    protected void initEvents() {
        mIbList.setOnClickListener(mClickListener);
        mCtvOrientation.setOnClickListener(mClickListener);
        mCtvSex.setOnClickListener(mClickListener);
        mColor.setOnTouchListener(onImageTouchListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointCount = event.getPointerCount();
        if (pointCount == 1) {
            return mSingleTouchDetector.onTouchEvent(event);
        } else {
            return mScaleDetector.onTouchEvent(event);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateTapPosition(float x, float y) {
        mTap.setVisibility(View.VISIBLE);
        // What the fuck!!
        // LayoutParams params = (LayoutParams) mTap.getLayoutParams();
        // params.leftMargin = (int) x-mTap.getMeasuredWidth();
        // params.topMargin = (int)
        // y-mTap.getMeasuredHeight()-getSupportActionBar().getHeight()-4;
        // mTap.setLayoutParams(params);
        // //The same result...

        Rect rect = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top; // 状态栏高度

        //version 2.0
        mTap.setX(x - mTap.getMeasuredWidth() / 2);
        mTap.setY(y - mTap.getMeasuredHeight() / 2
                /*- getSupportActionBar().getHeight()*/ - statusBarHeight);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mTap.setVisibility(View.GONE);
            }
        }, 500);
    }

    /**
     * 图片翻转:默认先显示正面
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void flipit() {
        final ImageView visibleList;
        final ImageView invisibleList;
        if (mBack.getVisibility() == View.GONE) {
            visibleList = mFront;
            invisibleList = mBack;
        } else {
            invisibleList = mFront;
            visibleList = mBack;
        }
        if (Version.isGeHONEYCOMB()) {
            ObjectAnimator visToInvis = ObjectAnimator.ofFloat(visibleList,
                    "rotationY", 0f, 90f);
            visToInvis.setDuration(500);
            visToInvis.setInterpolator(mAccelerator);
            final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(
                    invisibleList, "rotationY", -90f, 0f);
            invisToVis.setDuration(500);
            invisToVis.setInterpolator(mDecelerator);
            visToInvis.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator anim) {
                    visibleList.setVisibility(View.GONE);
                    invisToVis.start();
                    invisibleList.setVisibility(View.VISIBLE);
                    Logger.d(TAG, new PointF(visibleList.getX(), visibleList.getY()).toString());
                }
            });
            invisToVis.addListener(new AnimatorListenerAdapter(){

                @Override
                public void onAnimationEnd(Animator animation) {
                    fixXY();
                }
                
            });
            visToInvis.start();
        } else {
            visibleList.setVisibility(View.GONE);
            invisibleList.setVisibility(View.VISIBLE);
        }
    }

    public static final String KEY_HEX_COLOR = "key_hex_color";
    public static final String KEY_IS_MALE = "key_is_male";

    /**
     * 转换颜色值，传入部位列表页面
     * @param x
     * @param y
     */
    private void transferToColorCode(float x, float y) {
        int pixel;

        DisplayMetrics dm = new DisplayMetrics();
        mWManager.getDefaultDisplay().getMetrics(dm);

        // int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        // int height = wm.getDefaultDisplay().getHeight();//屏幕高度
        // Rect rect= new Rect();
        // this.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // int statusBarHeight = rect.top; //状态栏高度
        //
        // x=x- mTap.getMeasuredWidth()/2;
        // y=y -
        // mTap.getMeasuredHeight()/2-getSupportActionBar().getHeight()-statusBarHeight;
        try {
            pixel = mBmColor.getPixel((int) x, (int) y);
            String hexString = Integer.toHexString(pixel);
            // Toast.makeText(this, hexString.substring(2), Toast.LENGTH_SHORT)
            // .show();
            Bundle b = new Bundle();
            b.putString(KEY_HEX_COLOR, "#" + hexString.substring(2));
            b.putBoolean(KEY_IS_MALE, !mCtvSex.isChecked());
            openActivity(BodyAndSymptomActivity.class, b);
        } catch (Exception e) {
        }
    }

    /**
     * 单点触摸
     *
     */
    private class SigleTouchListener extends
            GestureDetector.SimpleOnGestureListener {

        DisplayMetrics dm;

        public SigleTouchListener(WindowManager manager) {
            dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            transferToColorCode(mTapX, mTapY);
            updateTapPosition(mRawX, mRawY);
            PointF point = new PointF(mRawX, mRawY);
            PointF point2 = new PointF(e.getX(), e.getY());
            Logger.i(TAG, point.toString() + "," + point2.toString());
            return super.onSingleTapUp(e);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
//            if (!bIsScaled) {
            if(mScaleFactor>1.0f) {
                if (Version.isGeHONEYCOMB()) {
                    float x = 0;
                    float y = 0;
                    
//                    if (mFront.getRight() <= 0 || mFront.getBottom() <= 0
//                            || mFront.getTop() >= dm.heightPixels
//                            || mFront.getLeft() >= dm.widthPixels) {
                    Rect r = new Rect();
                    mFront.getGlobalVisibleRect(r);
                    Logger.d(TAG,"onScroll | "+r.toShortString());
                    if((r.left|r.top)==0) {
                        x = mOriginalLeft;
                        y = mOriginalTop;
                    }else {
                        x = mFront.getX() - distanceX;
                        y = mFront.getY() - distanceY;
                    }
                    mFront.setX(x);
                    mFront.setY(y);
                    mBack.setX(x);
                    mBack.setY(y);
                    mColor.setX(x);
                    mColor.setY(y);
//                    mFront.setX(mFront.getX() - distanceX);
//                    mFront.setY(mFront.getY() - distanceY);
//                    mBack.setX(mBack.getX() - distanceX);
//                    mBack.setY(mBack.getY() - distanceY);
//                    mColor.setX(mColor.getX() - distanceX);
//                    mColor.setY(mColor.getY() - distanceY);
                } else {
                    mFront.setLeft((int) (mFront.getLeft() - distanceX));
                    mFront.setTop((int) (mFront.getTop() - distanceY));
                    mBack.setLeft((int) (mBack.getLeft() - distanceX));
                    mBack.setTop((int) (mBack.getTop() - distanceY));
                    mColor.setLeft((int) (mColor.getLeft() - distanceX));
                    mColor.setTop((int) (mColor.getTop() - distanceY));
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    }

    /**
     * 放大缩小
     *
     */
    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor()/1.2;
            // 限定范围
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 4.0f));
            mFront.setScaleX(mScaleFactor);
            mFront.setScaleY(mScaleFactor);
            mBack.setScaleX(mScaleFactor);
            mBack.setScaleY(mScaleFactor);
            mColor.setScaleX(mScaleFactor);
            mColor.setScaleY(mScaleFactor);

            bIsScaled = true;
            return super.onScale(detector);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
            DisplayMetrics dm = new DisplayMetrics();
            mWManager.getDefaultDisplay().getMetrics(dm);
            mOriginalLeft = dm.widthPixels / 2 - mColor.getMeasuredWidth() / 2;
            mOriginalTop = dm.heightPixels / 2 - mColor.getMeasuredHeight() / 2-getSupportActionBar().getHeight();
            // if(mScaleFactor<=1) {
            mFront.setX(mOriginalLeft);
            mFront.setY(mOriginalTop);

            mBack.setX(mOriginalLeft);
            mBack.setY(mOriginalTop);

            mColor.setX(mOriginalLeft);
            mColor.setY(mOriginalTop);
            
            Logger.d(TAG, new PointF(mOriginalLeft, mOriginalTop).toString());
            // }
        }

    }

}
