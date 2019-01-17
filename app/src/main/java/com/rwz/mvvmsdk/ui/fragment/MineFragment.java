package com.rwz.mvvmsdk.ui.fragment;

import android.os.Bundle;

import com.rwz.basemodule.base.BaseFragment;
import com.rwz.mvvmsdk.R;
import com.rwz.mvvmsdk.databinding.FragmentMineBinding;
import com.rwz.mvvmsdk.view_module.MineViewModule;

public class MineFragment extends BaseFragment<FragmentMineBinding, MineViewModule>{

    @Override
    protected MineViewModule setViewModule() {
        return new MineViewModule();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setPageTitle(R.string.mine);
    }
}
