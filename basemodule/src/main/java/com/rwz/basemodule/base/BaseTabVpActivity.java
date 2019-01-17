package com.rwz.basemodule.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.BR;
import com.rwz.basemodule.R;
import com.rwz.basemodule.adapter.SimpleVPAdapter;
import com.rwz.basemodule.entity.TabEntity;
import com.rwz.basemodule.entity.TempEntity;
import com.rwz.basemodule.temp.ITempView;
import com.rwz.basemodule.viewmodule.TabVpViewModule;
import com.rwz.basemodule.weidgt.CommTabLayout;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.network.CommonObserver;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/7/19.
 * TabLayout + ViewPager
 */

public abstract class BaseTabVpActivity<VB extends ViewDataBinding> extends BaseActivity<VB, TabVpViewModule>implements ViewPager.OnPageChangeListener {

    protected ViewPager mVp;
    protected CommTabLayout mTab;
    protected SimpleVPAdapter<BaseFragment> mAdapter;
    protected int mCurrPos;
    private ViewStub mViewStub;
    boolean isInflate;//是否加载过空视图
    private TempEntity mTempEntity;
    protected boolean isAllowSwipeBack = true;//是否允许滑动返回

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mVp = (ViewPager) findViewById(R.id.vp);
        mTab = (CommTabLayout) findViewById(R.id.tab);
        mViewStub = (ViewStub) findViewById(R.id.view_stub);
        mAdapter = new SimpleVPAdapter(getSupportFragmentManager());
        mVp.addOnPageChangeListener(this);
        setSwipeBackEnable(isAllowSwipeBack);
    }

    @Override
    protected TabVpViewModule setViewModule() {
        return new TabVpViewModule(onClickEventCommand);
    }

    public Consumer<CommandEntity<IBaseMulInterface>> onClickEventCommand =
            new Consumer<CommandEntity<IBaseMulInterface>>() {
                @Override
                public void accept(CommandEntity<IBaseMulInterface> commandEntity) throws Exception {
                    if (commandEntity != null) {
                        int id = commandEntity.getId();
                        IBaseMulInterface iEntity = commandEntity.getT();
                        if (id == R.id.reload && mTempEntity != null) {
                            mTempEntity.type.set(ITempView.STATUS_LOADING);
                            requestData();
                        } else if(mViewModule != null){
                            mViewModule.onClickView(id, iEntity);
                        }
                    }
                }
            };

    @Override
    protected void requestData() {
        requestTabData();//获取tab
    }

    protected abstract void requestTabData();
    protected abstract BaseFragment initFragment(TabEntity tab,int position);

    /**
     * 初始化内容Viewpager
     */
    protected void setupContentViewPager(List<TabEntity> tabs) {
        if (tabs == null) {
            onRequestFail();
            return;
        }

        if (mViewStub != null && mTempEntity != null) {
            mTempEntity.type.set(ITempView.STATUS_DISMISS);
            mViewStub.setVisibility(View.GONE);
        }

        for (int i = 0; i < tabs.size(); i++) {
            TabEntity tab = tabs.get(i);
            tab.setPosition(i);
//            mFragments.add(initFragment());
            mAdapter.addFrag(initFragment(tab,i), tab.getTitle());
        }
        mVp.setAdapter(mAdapter);
        if (mTab != null) {
            mTab.setCanScroll(tabs.size() > 5);
            mTab.setupWithViewPager(mVp);
        }
    }

    protected CommonObserver<List<TabEntity>> getObserver() {
        return new CommonObserver<List<TabEntity>>(){
            @Override
            public void onNext(List<TabEntity> value) {
                setupContentViewPager(value);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                onRequestFail();
            }
        };
    }

    /**
     * 刷新数据
     */
    public void notifyDataSetChanged() {
        if (mTab != null) {
            mTab.notifyDataSetChanged();
        }
    }

    public BaseFragment getCurrFragment() {
        if (mAdapter != null) {
            return mAdapter.getItem(mCurrPos);
        }
        return null;
    }

    public void onRequestFail() {
        inflateViewStub();
    }

    /**
     * 显示ViewStub--在需要的地方调用
     */
    public void inflateViewStub() {
        if (mViewStub != null && !isInflate) {
            isInflate = true;
            mViewStub.setOnInflateListener(getInflateListener());
            mViewStub.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mCurrPos = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //只允许在第一页滑动返回
        setSwipeBackEnable(isAllowSwipeBack && state == ViewPager.SCROLL_STATE_IDLE && mVp.getCurrentItem() == 0);
    }

    public ViewStub.OnInflateListener getInflateListener() {
        return new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub stub, View inflated) {
                ViewDataBinding bind = DataBindingUtil.bind(inflated);
                Drawable nullImg = ResourceUtil.getDrawable(R.mipmap.no_data);
                Drawable errorImg = ResourceUtil.getDrawable(R.mipmap.no_nerwork);
                mTempEntity = new TempEntity(nullImg, errorImg);
                mTempEntity.type.set(ITempView.STATUS_ERROR);
                bind.setVariable(BR.entity, mTempEntity);
                bind.setVariable(BR.viewModule, mViewModule);
            }
        };
    }

    @Override
    public void scrollToTop() {
        BaseFragment fragment = getCurrFragment();
        if (fragment != null) {
            fragment.scrollToTop();
        }
    }

}
