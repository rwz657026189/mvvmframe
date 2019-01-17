package com.rwz.ui.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.rwz.ui.R;


/**
 * Created by rwz on 2017/2/15.
 * 带圆形背景TextView
 */

public class CircleTextView extends AppCompatTextView {

    private Paint mPaint;
    private int mCircleColor;

    public CircleTextView(Context context) {
        super(context);
        init(context,null);
    }

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleTextView);
            mCircleColor = array.getColor(R.styleable.CircleTextView_circleColor, 0);
            array.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCircleColor);
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        mPaint.setColor(circleColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCircleColor != 0) {
            int x = getWidth()/2;
            int y = getHeight()/2;
            canvas.drawCircle(x , y , x , mPaint);
        }
        super.onDraw(canvas);
    }
}
