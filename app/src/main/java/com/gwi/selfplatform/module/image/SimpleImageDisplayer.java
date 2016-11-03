package com.gwi.selfplatform.module.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * 简单的Bitmap显示类，它调整bitmap显示的宽度。
 *
 * @author Administrator
 */
public class SimpleImageDisplayer implements BitmapDisplayer {

    private int targetWidth;

    public SimpleImageDisplayer(int targetWidth) {
        this.targetWidth = targetWidth;
    }

    public Bitmap display(Bitmap bitmap, ImageView imageView,
                          LoadedFrom loadedFrom) {
        if (bitmap != null) {
            bitmap = ImageUtils.resizeImageByWidth(bitmap, targetWidth);
        }
        imageView.setImageBitmap(bitmap);
        return bitmap;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware,
                        LoadedFrom loadedFrom) {

    }

}
