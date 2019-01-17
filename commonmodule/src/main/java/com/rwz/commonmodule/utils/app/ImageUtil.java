package com.rwz.commonmodule.utils.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.DrawableRes;

/**
 * Created by rwz on 2017/3/13.
 */

public class ImageUtil {
    /**
     * android系统的模糊方法
     * @param bitmap 要模糊的图片
     * @param radius 模糊等级（0 ~ 25）
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, int radius) {
        if(context == null) return bitmap;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            //Let's create an empty bitmap with the same size of the bitmap we want to blur
            Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            //Instantiate a new Renderscript
            RenderScript rs = RenderScript.create(context);
            //Create an Intrinsic Blur Script using the Renderscript
            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
            //Set the radius of the blur
            blurScript.setRadius(radius);
            //Perform the Renderscript
            blurScript.setInput(allIn);
            blurScript.forEach(allOut);
            //Copy the final bitmap created by the out Allocation to the outBitmap
            allOut.copyTo(outBitmap);
            //recycle the original bitmap
            bitmap.recycle();
            //After finishing everything, we destroy the Renderscript.
            rs.destroy();
            return outBitmap;
        }else{
            return bitmap;
        }
    }

    /**
     * 对资源图片进行模糊
     * @param context 上下文
     * @param imgRes    图片id
     * @param inSampleSize 图片采样 ： 2的平方数， 值越大效率越高最终效果越模糊， 建议给 8
     * @param radius 模糊值（0 ~ 25） 值越大最终效果越模糊
     * @return
     */
    public static Bitmap blurBitmap(Context context, @DrawableRes int imgRes, int inSampleSize, int radius) {
        if(context == null || imgRes == 0)
            return null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        if(inSampleSize > 0)
            opts.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes, opts);
        if (radius <= 0) {
            return bitmap;
        } else if (radius > 25) {
            radius = 25;
        }
      return blurBitmap(context, bitmap, radius);
    }

}
