package com.rwz.mvvmsdk.ui.activity;

import android.os.Bundle;

import com.rwz.basemodule.base.BaseFragment;
import com.rwz.basemodule.base.BaseTabVpActivity;
import com.rwz.basemodule.databinding.ActivityTabVpBinding;
import com.rwz.basemodule.entity.TabEntity;
import com.rwz.basemodule.utils.FragmentUtil;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.mvvmsdk.R;
import com.rwz.mvvmsdk.ui.fragment.MainListFragment;
import com.rwz.mvvmsdk.ui.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTabVpActivity<ActivityTabVpBinding> {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void requestTabData() {
        List<TabEntity> list = new ArrayList<>();
        list.add(new TabEntity(ResourceUtil.getString(R.string.main)));
        list.add(new TabEntity(ResourceUtil.getString(R.string.recommend)));
        list.add(new TabEntity(ResourceUtil.getString(R.string.mine)));
        setupContentViewPager(list);
    }

    @Override
    protected BaseFragment initFragment(TabEntity tab, int position) {
        if(position == 2)
            return new MineFragment();
        return FragmentUtil.newInstance(MainListFragment.class, position);
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        super.setSwipeBackEnable(false);
    }
}
