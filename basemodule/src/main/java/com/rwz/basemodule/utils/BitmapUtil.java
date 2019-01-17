package com.rwz.basemodule.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            /**   final int halfHeight = height / 2;
             final int halfWidth = width / 2;

             // Calculate the largest inSampleSize value that is a power of 2 and keeps both
             // height and width larger than the requested height and width.
             while ((halfHeight / inSampleSize) > reqHeight
             && (halfWidth / inSampleSize) > reqWidth) {
             inSampleSize *= 2;
             } */
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都不会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        }

        return inSampleSize;
    }


    /**
     * view创建一个bitmap
     * @return
     */
    public static Bitmap createBitmapFromView(View view) {
        if(view == null)
            return null;
        Bitmap bmp = null;
        try {
            bmp = Bitmap.createBitmap(view.getWidth(), 1, Bitmap.Config.RGB_565);
            int rowBytes = bmp.getRowBytes();
            bmp = null;
            long availMemory = getAvailMemory();
            Log.e(TAG, "createBitmapFromView: " + "rowBytes = " + rowBytes + ", " + rowBytes * view.getHeight() + ", " + availMemory);
            if (rowBytes * view.getHeight() >= availMemory) {
                return null;
            }
            bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bmp);
            view.draw(canvas);
        } catch (Exception e) {
            if (e != null)
                e.printStackTrace();
        } catch (Error error) {
            if (error != null) {
                error.printStackTrace();
            }
        }
        return bmp;
    }

    public static long getAvailMemory() {// 获取android当前可用内存大小
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory();
    }

}
