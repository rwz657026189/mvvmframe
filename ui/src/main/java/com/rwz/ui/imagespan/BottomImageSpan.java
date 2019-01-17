package com.rwz.ui.imagespan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * Created by rwz on 2017/2/14.
 * 图片文字底对齐，ImageSpan有时不对齐，无语
 */

public class BottomImageSpan extends ImageSpan {

    public BottomImageSpan(Context context, final int drawableRes) {
        super(context, drawableRes);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // image to draw
        Drawable b = getDrawable();
        // font metrics of text to be replaced
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
//        int transY = (y + fm.descent + y + fm.ascent) / 2  - b.getBounds().bottom / 2;
        int transY = (y + fm.descent )  - b.getBounds().bottom;
        canvas.save();
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}