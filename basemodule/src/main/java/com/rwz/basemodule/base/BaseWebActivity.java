package com.rwz.basemodule.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import com.rwz.basemodule.BR;
import com.rwz.basemodule.R;
import com.rwz.basemodule.abs.IViewModule;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.entity.TempEntity;
import com.rwz.basemodule.entity.WebEntity;
import com.rwz.basemodule.temp.ITempView;
import com.rwz.commonmodule.inf.IPostEvent1;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.web.AbsWebViewProxy;
import com.rwz.web.InfWebView;


/**
 * Created by rwz on 2018/5/14.
 */

public abstract class BaseWebActivity<VB extends ViewDataBinding,
        VM extends IViewModule> extends BaseActivity<VB, VM> {

    protected int mType;
    protected WebEntity mWebEntity;
    protected AbsWebViewProxy mProxy;
    protected TempEntity mTempEntity;

    @NonNull
    protected abstract AbsWebViewProxy setWebViewProxy();

    @Override
    protected void init(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.init(savedInstanceState);
        setSwipeBackEnable(false);
        mWebEntity = getIntent().getParcelableExtra(BaseKey.PARCELABLE_ENTITY);
        String postParams = getIntent().getStringExtra(BaseKey.STRING);
        if (mWebEntity != null) {
            mType = mWebEntity.getType();
            setPageTitle(mWebEntity.getTitle());
        }
        mProxy = setWebViewProxy();
        mProxy.setInfWebView(mInfWebView);
        mProxy.setOnLoadProgress(onPageLoadProgress);
        if(mWebEntity != null)
            mProxy.loadUrl(mWebEntity.getUrl(), postParams, true);
        mTempEntity = new TempEntity();
        LogUtil.d(TAG, "init", mWebEntity);
        mBind.setVariable(BR.entity, mTempEntity);
        View reload = findViewById(R.id.reload);
        if(reload != null)
            reload.setOnClickListener(this);
    }

    InfWebView mInfWebView = new InfWebView() {
        @Override
        public void onPageStarted(String url) {
        }

        @Override
        public void onPageFinished(String url) {
            setTempType(ITempView.STATUS_DISMISS);
        }

        @Override
        public void onReceivedError(String url) {
            setTempType(ITempView.STATUS_ERROR);
        }

        @Override
        public boolean shouldOverrideUrlLoading(String url) {
            return BaseWebActivity.this.shouldOverrideUrlLoading(url);
        }

        @Override
        public void onReceivedTitle(String title) {
            setPageTitle(title);
        }

        @Override
        public void onFullScreenChanged(boolean isFullScreen, View view) {
            View webView = mProxy.getWebView();
            if(webView == null)
                return;
            if (isFullScreen) { //全屏播放视频
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                webView.setVisibility(View.INVISIBLE);
                FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                decor.addView(view);
            } else {//正常播放视频
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                decor.removeView(view);
                webView.setVisibility(View.VISIBLE);
            }
        }
    };

    protected boolean shouldOverrideUrlLoading(String url) {
        return false;
    }

    private IPostEvent1<Integer> onPageLoadProgress = new IPostEvent1<Integer>() {
        @Override
        public void onEvent(Integer progress) {
            onLoadProgress(progress);
            if (progress >= 100) {
                setTempType(ITempView.STATUS_DISMISS);
            }
        }
    };

    protected void onLoadProgress(int progress) {

    }

    protected void setTempType(int type) {
        if(mTempEntity != null)
            mTempEntity.setType(type);
    }

    @Override
    protected void onLeftClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mProxy != null && !mProxy.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.reload && mProxy != null) {
            setTempType(ITempView.STATUS_DISMISS);
            mProxy.reload();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mProxy != null)
            mProxy.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if(mProxy != null)
            mProxy.onDestroy();
        super.onDestroy();
    }


}
