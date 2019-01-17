package com.rwz.ui.float_view;

import android.content.Context;
import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.system.ScreenUtil;
import com.rwz.ui.R;

public class FloatView extends FrameLayout{

    private static final String TAG = "FloatView";
    public static final int MENU_MIN_WIDTH = ResourceUtil.getDimen(R.dimen.h_48);

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    private boolean isAnchoring = false;
    private boolean isTouching = false;
    private WindowManager.LayoutParams mParams = null;
    private WindowManager windowManager;
    private int scaledTouchSlop;
    private int screenWidth;
    private int screenHeight;

    public FloatView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        initScreenSize();
        scaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private void initScreenSize() {
        screenWidth = ScreenUtil.getInstance().getScreenWidth(BaseApplication.getInstance());
        screenHeight = ScreenUtil.getInstance().getScreenHeight(BaseApplication.getInstance());
    }

    public void initParams(WindowManager windowManager, WindowManager.LayoutParams params) {
        this.windowManager = windowManager;
        this.mParams = params;
    }

    private boolean isClickView;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAnchoring) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                initScreenSize();
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                isClickView = true;
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY();
                if (Math.abs(xDownInScreen - xInScreen) > scaledTouchSlop
                        || Math.abs(yDownInScreen - yInScreen) > scaledTouchSlop) {
                    isClickView = false;
                    // 手指移动的时候更新小悬浮窗的位置
                    updateViewPosition((int) (xInScreen - xInView), (int) (yInScreen - yInView));
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouching = false;
                if (isClickView) { //点击按钮
                    onClick();
                } else {
                    //吸附效果
                    anchorToSide();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouching = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void onClick() {

    }

    private int getLeftMargin() {
        return mParams.x;
    }

    private int getTopMargin() {
        return mParams.y;
    }

    private void anchorToSide() {
        isAnchoring = true;
        initScreenSize();
        int middleX = getLeftMargin() + getWidth() / 2;


        int animTime;
        int xDistance;
        int yDistance = 0;

        int dp_25 = 0;
        int endX;
        int endY;
        //1
        if (middleX <= dp_25 + getWidth() / 2) {
            endX = dp_25;
            xDistance = endX - getLeftMargin();
        }
        //2
        else if (middleX <= screenWidth / 2) {
            endX = dp_25;
            xDistance = endX - getLeftMargin();
        }
        //3
        else if (middleX >= screenWidth - getWidth() / 2 - dp_25) {
            endX = screenWidth - getWidth() - dp_25;
            xDistance = endX - getLeftMargin();
        }
        //4
        else {
            endX = screenWidth - getWidth() - dp_25;
            xDistance = endX - getLeftMargin();
        }

        //1
        if (getTopMargin() < 0) {
            endY = 0;
            yDistance = endY - getTopMargin();
        }
        //2
        else if (getTopMargin() + getHeight()  >= screenHeight) {
            endY = screenHeight - getHeight();
            yDistance = endY - getTopMargin();
        }
        LogUtil.d(TAG, "xDistance = " + xDistance + "   yDistance = " + yDistance);

        animTime = Math.abs(xDistance) > Math.abs(yDistance) ? (int) (((float) xDistance / (float) screenWidth) * 600f)
                : (int) (((float) yDistance / (float) screenHeight) * 900f);
        this.post(new AnchorAnimRunnable(Math.abs(animTime), xDistance, yDistance, System.currentTimeMillis()));
    }

    private class AnchorAnimRunnable implements Runnable {

        private int animTime;
        private long currentStartTime;
        private Interpolator interpolator;
        private int xDistance;
        private int yDistance;
        private int startX;
        private int startY;

        private AnchorAnimRunnable(int animTime, int xDistance, int yDistance, long currentStartTime) {
            this.animTime = animTime;
            this.currentStartTime = currentStartTime;
            interpolator = new AccelerateDecelerateInterpolator();
            this.xDistance = xDistance;
            this.yDistance = yDistance;
            startX = getLeftMargin();
            startY = getTopMargin();
        }

        @Override
        public void run() {
            if (System.currentTimeMillis() >= currentStartTime + animTime) {
                isAnchoring = false;
                boolean isLeft = getLeftMargin() < screenWidth / 2;
                LogUtil.d(TAG, "AnchorAnimRunnable", "isLeft = " + isLeft);
                updateViewPosition(isLeft ? 0 : screenWidth - MENU_MIN_WIDTH, getTopMargin());
                return;
            }
            float delta = interpolator.getInterpolation((System.currentTimeMillis() - currentStartTime) / (float) animTime);
            int xMoveDistance = (int) (xDistance * delta);
            int yMoveDistance = (int) (yDistance * delta);
            /*LogUtil.d(TAG, "delta:  " + delta , "xMoveDistance = " + xMoveDistance , "startX = " + startX,
                    "yMoveDistance = " + yMoveDistance, "startY = " + startY);*/
            updateViewPosition(startX + xMoveDistance, startY + yMoveDistance);
            FloatView.this.postDelayed(this, 16);
        }
    }

    void updateViewPosition(int x, int y) {
        if(mParams == null)
            return;
        if(x < 0)
            x = 0;
        if(x > screenWidth - MENU_MIN_WIDTH)
            x = screenWidth - MENU_MIN_WIDTH;
        if(y < 0)
            y = 0;
        if(y > screenHeight - MENU_MIN_WIDTH)
            y = screenHeight - MENU_MIN_WIDTH;
        //增加移动误差
        this.mParams.x = x;
        this.mParams.y = y;
//        LogUtil.d(TAG, "updateViewPosition","x = " + getLeftMargin() , "   y = " + getTopMargin());
        windowManager.updateViewLayout(this, mParams);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        LogUtil.d(TAG, "onConfigurationChanged");
        anchorToSide();
    }

}

