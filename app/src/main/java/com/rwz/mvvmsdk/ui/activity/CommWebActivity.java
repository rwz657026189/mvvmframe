package com.rwz.mvvmsdk.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rwz.basemodule.abs.IViewModule;
import com.rwz.basemodule.base.BaseWebActivity;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.mvvmsdk.R;
import com.rwz.mvvmsdk.databinding.ActivityTencentWebBinding;
import com.rwz.web.AbsWebViewProxy;
import com.rwz.web.sys.SysWebViewProxy;

/**
 * Created by rwz on 2018/8/7.
 * 通用web加载 , 采用的是系统WebView (因腾讯x5内核不能播放youtube视频,
 * 此外至少需要安装微信、qq、qq浏览器其中一个， 海外环境几率较低，故采用系统内置webView)
 *
 */

public class CommWebActivity extends BaseWebActivity<ActivityTencentWebBinding, IViewModule> {

    private ProgressBar mProgressBar;

    @NonNull
    @Override
    protected AbsWebViewProxy setWebViewProxy() {
        return new SysWebViewProxy(this, (WebView) findViewById(R.id.webView));
    }

    @Override
    protected IViewModule setViewModule() {
        return null;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_tencent_web;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mProgressBar = mBind.progressBar;
    }

    @Override
    public void themeColorSetting(int color) {
        super.themeColorSetting(ResourceUtil.getColor(R.color.white));
        setDarkStatusBar(false, true);
    }

    @Override
    protected void onLoadProgress(int progress) {
        if (progress >= 100) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setProgress(progress);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

}