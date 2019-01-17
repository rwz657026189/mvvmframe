package com.rwz.basemodule.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Rect;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.rwz.commonmodule.utils.app.StringUtil;
import com.rwz.commonmodule.utils.show.LogUtil;


/**
 * 抽象的PopupWindow悬浮框
 */
public abstract class BasePopupWindow<VB extends ViewDataBinding> extends PopupWindow {

    protected      String             TAG;
    protected      Context            mContext;
    protected      View               mRootView;
    protected      VB mBind;

    public BasePopupWindow(Context context) {
        this(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public BasePopupWindow(Context context, int width, int height) {
        mContext = context;
        initPopupWindow(width, height);
        init();
    }

    protected void init() {
    }

    private void initPopupWindow(int width, int height) {
        mBind = DataBindingUtil.inflate(LayoutInflater.from(mContext), setLayoutId(), null, false);
        mRootView = mBind.getRoot();
        setContentView(mRootView);
        TAG = StringUtil.getClassName(this);
        //设置弹出窗体的宽
        setWidth(width);
        //设置弹出窗体的高
        setHeight(height);
        setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        setAnimationStyle(R.style.wisdom_anim_style);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(null);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24 && anchor != null) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
            LogUtil.d(TAG, "showAsDropDown", "height = " + height);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }
}
