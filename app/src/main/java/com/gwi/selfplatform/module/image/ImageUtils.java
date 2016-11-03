package com.gwi.selfplatform.module.image;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Bitmap处理工具类
 *
 * @author Peng Yi
 */
public class ImageUtils {
    /**
     * 根据宽度等比例缩放图片
     *
     * @param defaultBitmap
     * @param width
     * @return
     */
    public static Bitmap resizeImageByWidth(Bitmap defaultBitmap,
                                            int targetWidth) {
        int rawWidth = defaultBitmap.getWidth();
        int rawHeight = defaultBitmap.getHeight();
        float targetHeight = targetWidth * (float) rawHeight / rawWidth;
        float scaleWidth = targetWidth / (float) rawWidth;
        float scaleHeight = targetHeight / rawHeight;
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(scaleHeight, scaleWidth);
        return Bitmap.createBitmap(defaultBitmap, 0, 0, rawWidth, rawHeight,
                localMatrix, true);
    }
}
