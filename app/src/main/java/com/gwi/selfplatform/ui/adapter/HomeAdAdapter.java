package com.gwi.selfplatform.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import com.gwi.phr.hospital.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 显示首页广告的适配器，它维护广告图片的数组。
 *
 * @author Peng Yi
 */
public class HomeAdAdapter extends PagerAdapter {

    private String[] mImageUries = null;
    private List<ImageView> mImageList = new ArrayList<ImageView>();
    private Context mContext;

    public HomeAdAdapter(Context context, String[] uries) {
        mContext = context;
        mImageUries = uries;
        for (int i = 0; i < mImageUries.length; i++) {
//mImageUries[i] ="drawable://" + R.drawable.ad_img1; \
//mImageUries[i] = ""+R.drawable.ad_img1;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView child = new ImageView(mContext);
        child.setScaleType(ScaleType.FIT_XY);
        child.setPadding(0, 0, 0, 0);
        child.setBackgroundResource(Integer.parseInt(mImageUries[position]));
//        ImageLoader.getInstance().displayImage(mImageUries[position], child);
        mImageList.add(child);
        container.addView(child);
        return child;
    }

    @Override
    public int getCount() {
        return mImageUries.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(mImageList.get(position));
    }

}
