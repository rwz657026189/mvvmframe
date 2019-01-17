package com.rwz.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.inf.IPostEvent2;
import com.rwz.commonmodule.utils.show.LogUtil;


/**
 * Created by Administrator on 2017/1/2.
 * 如果子控件不足ScrollView高度时向下拉不调用overScrollBy(),导致页面不能向下滑动
 */

public class SpecialScrollView extends ScrollView implements ViewTreeObserver.OnPreDrawListener {
    private static final String TAG = "SpecialScrollView";
    private int mOriginalHeight;
    //    private int drawableHeight;
    private View mAnimView;
    private float mLastY;
    private boolean isMeasured =false;
    private float mScale = 359f/750; //图片原始比例
    private final int MAX_DELTAY = 400;//透明度变化的最大距离


    //监听滑动动画
    public IPostEvent2<Float,Float> listener1;
    public void setIPostEvent2(IPostEvent2<Float,Float> listener1) {
        this.listener1 = listener1;
    }

    public SpecialScrollView(Context context) {
        super(context);
    }

    public SpecialScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpecialScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置ImageView图片, 拿到引用
     * @param view
     */
    public void setParallaxImage(View view) {
        this.mAnimView = view;
        if (view != null) {
            view.getViewTreeObserver().addOnPreDrawListener(this);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        LogUtil.d("overScrollBy" , "isTouchEvent = " + isTouchEvent , " deltaY = " + deltaY);
        // 手指拉动 并且 是下拉
        if(isTouchEvent && deltaY < 0 && mAnimView !=null ){
            // 把拉动的瞬时变化量的绝对值交给mIamge, 就可以实现放大效果
            float scrollValueY = Math.abs(deltaY / 3.0f);
            int newHeight = (int) (mAnimView.getHeight() + scrollValueY);
            // 高度不超出图片最大高度时,才让其生效
            mAnimView.getLayoutParams().height = newHeight;
            if (mScale > 0) {
                mAnimView.getLayoutParams().width = (int) (newHeight / mScale);
            }

            mAnimView.requestLayout();
            if (listener1 != null) {
                listener1.onEvent(scrollValueY,1 - (newHeight - mOriginalHeight) * 1f / MAX_DELTAY);
            }
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                // 执行回弹动画,  属性动画\值动画
                // 从当前高度mImage.getHeight(), 执行动画到原始高度mOriginalHeight
                if (mAnimView !=null){
                    final int startHeight = mAnimView.getHeight();
                    final int endHeight = mOriginalHeight;
                    // 486, 200, 200, 0.4
                    LogUtil.d("onTouchEvent", "startHeight = " + startHeight, "endHeight = " + endHeight, "mOriginalHeight = " + mOriginalHeight, "mScale = " + mScale);
                    if (startHeight > endHeight) {
                        valueAnimator(startHeight, endHeight);
                    }else if (startHeight != endHeight){
                        mAnimView.getLayoutParams().height = mOriginalHeight;
                        mAnimView.getLayoutParams().width = (int) (mOriginalHeight * mScale);
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void valueAnimator(final int startHeight, final int endHeight) {
        ValueAnimator mValueAnim = ValueAnimator.ofInt(1);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator mAnim) {
                float fraction = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                    fraction = mAnim.getAnimatedFraction();
                } else {
                    LogUtil.e("SpecialScrollView can't support when api < 12");
                    return;
                }
                Integer newHeight = evaluate(fraction, startHeight, endHeight);
                newHeight = newHeight > startHeight ? startHeight : newHeight < endHeight ? endHeight : newHeight;
                if (listener1 != null) {
                    listener1.onEvent((float) (newHeight - mAnimView.getLayoutParams().height),
                            1 - (newHeight - mOriginalHeight) * 1f / MAX_DELTAY);
                }
                mAnimView.getLayoutParams().height = newHeight;
                if (mScale > 0) {
                    mAnimView.getLayoutParams().width = (int) (newHeight / mScale) + 1;
                }
                mAnimView.requestLayout();
            }
        });
        mValueAnim.setDuration(500);
        mValueAnim.start();
    }

    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }


    @Override
    public boolean onPreDraw() {
        if (!isMeasured && mAnimView != null) {
            isMeasured = true;
            int height = mAnimView.getMeasuredHeight();
            int width = mAnimView.getMeasuredWidth();
            if (height > 200 && width > 400) {
                mScale = height / width;
            }
            width = width < 400 ? getScreenWidth(BaseApplication.getInstance()) : width;
            mOriginalHeight = height < 200 ? (int) (width * mScale) : height;
            LogUtil.d("onTouchEvent", "height = " + height, "width = " + width, "mOriginalHeight = " + mOriginalHeight, "mScale = " + mScale);
        }
        return true;
    }

    /**
     * 获得屏幕高度
     */
    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if(wm == null)
            return 0;
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

}