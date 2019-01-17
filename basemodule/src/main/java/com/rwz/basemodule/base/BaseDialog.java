package com.rwz.basemodule.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.rwz.basemodule.R;
import com.rwz.commonmodule.inf.IPostEvent;
import com.rwz.commonmodule.utils.app.StringUtil;

public abstract class BaseDialog<VB extends ViewDataBinding> extends DialogFragment {
    protected String TAG = "";
    protected   VB                 mBind;
    protected View mRootView;
    protected BaseActivity mActivity;
    private IPostEvent dismissListener;

    public BaseDialog() {
        super();
        setStyle(STYLE_NO_TITLE, R.style.CommonDialog);
    }

    public void setOnDismissListener(IPostEvent postEvent) {
        this.dismissListener = postEvent;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dismissListener != null)
            dismissListener.onEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBind = DataBindingUtil.inflate(inflater, setLayoutId(), container, false);
        mRootView = mBind.getRoot();
        initFragment();
        return mRootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            mActivity = (BaseActivity) activity;
        }
    }

    private void initFragment() {
        TAG = StringUtil.getClassName(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState){
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 设置资源布局
     *
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 获取binding对象
     */
    protected VB getBinding() {
        return mBind;
    }

}
