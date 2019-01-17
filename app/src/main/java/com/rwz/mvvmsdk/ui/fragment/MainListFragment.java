package com.rwz.mvvmsdk.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.rwz.basemodule.base.BaseListFragment;
import com.rwz.basemodule.common.CommBottomDialog;
import com.rwz.basemodule.common.DeveloperModeDialog;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.databinding.LayoutRecyclerviewBinding;
import com.rwz.basemodule.entity.params.CommBottomEntity;
import com.rwz.commonmodule.help.DialogHelp;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.mvvmsdk.R;
import com.rwz.mvvmsdk.view_module.MainListViewModule;

import java.util.List;

public class MainListFragment extends BaseListFragment<LayoutRecyclerviewBinding, MainListViewModule>{

    private int mType;

    @Override
    protected void config() {
        super.config();
        mType = getArguments().getInt(BaseKey.INT);
        //推荐两列
        SPAN_COUNT = mType == 1 ? 2 : 1;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_toolbar_recyclerview;
    }

    @Override
    protected MainListViewModule setViewModule() {
        return new MainListViewModule(mType);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        switch (mType) {
            case 0:
                setPageTitle(R.string.main);
                setLeftDrawable(0);
                setRightDrawable(R.mipmap.ic_more);
                break;
            case 1:
                setPageTitle(R.string.recommend);
                setLeftDrawable(0);
                mRightView.setText("连续点击3次有惊喜");
                break;
        }
    }

    @Override
    public void onRightClick() {
        super.onRightClick();
        switch (mType) {
            case 0:
                onClickMain();
                break;
            case 1:
                onClickRecommend();
                break;
        }
    }

    private void onClickMain() {
        CommBottomDialog dialog = new CommBottomDialog.Build()
                .setTitle("首页更多")
                .addItem("版本升级")
                .create();
        dialog.setOnClickItemListener(new CommBottomDialog.OnClickItemListener() {
            @Override
            public void onClickItem(Context context, int position, List<CommBottomEntity> data, Bundle args) {
                ToastUtil.getInstance().showShortSingle("暂时没有新版本");
            }
        });
        DialogHelp.show(getContext(), dialog, "CommBottomDialog");
    }

    private void onClickRecommend() {
        DeveloperModeDialog dialog = DeveloperModeDialog.openDialog(getContext());
        if (dialog != null) {
            dialog.addEvent("点我", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.getInstance().showShortSingle("click me");
                }
            });
        }
    }

}
