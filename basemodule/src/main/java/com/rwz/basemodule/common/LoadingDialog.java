package com.rwz.basemodule.common;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.rwz.basemodule.R;
import com.rwz.basemodule.base.BaseDialog;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.databinding.DialogLoadingBinding;
import com.rwz.basemodule.entity.turnentity.LoadingDialogTurnEntity;
import com.rwz.commonmodule.utils.app.ResourceUtil;

/**
 *  加载中对话框
 */
public class LoadingDialog extends BaseDialog<DialogLoadingBinding> implements View.OnClickListener {

    LoadingDialogTurnEntity mEntity;

    private TextView mTipsView;

    public static LoadingDialog newInstance(LoadingDialogTurnEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BaseKey.PARCELABLE_ENTITY, entity);
        LoadingDialog dialog = new LoadingDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setCancelable(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEntity = bundle.getParcelable(BaseKey.PARCELABLE_ENTITY);
        }
        mTipsView = mBind.tips;
        if (mEntity != null) {
            setTips(mEntity.getTips());
        }
        mRootView.setOnClickListener(this);
        setCancelable(false);
    }

    /**
     * 设置提示语
     * @param tips
     */
    public void setTips(String tips) {
        if (mTipsView != null) {
            mTipsView.setText(tips);
        }
    }

    public void setEntity(LoadingDialogTurnEntity mEntity) {
        this.mEntity = mEntity;
        if (mEntity != null) {
            setTips(mEntity.getTips());
        }
    }

    public void setTips(@StringRes int tips) {
        setTips(ResourceUtil.getString(tips));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    public void onClick(View v) {
        if (v == mRootView && mEntity != null && mEntity.isCanDismissOutSide()) {
            dismissAllowingStateLoss();
        }
    }
}
