package com.rwz.basemodule.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.rwz.basemodule.R;
import com.rwz.basemodule.base.BaseDialog;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.databinding.DialogMsgBinding;
import com.rwz.basemodule.entity.turnentity.MsgDialogTurnEntity;
import com.rwz.basemodule.inf.CommBiConsumer;

/**
 * 消息对话框
 */
public class MsgDialog extends BaseDialog<DialogMsgBinding> implements View.OnClickListener {

    private MsgDialogTurnEntity mEntity;

    public static MsgDialog newInstance(MsgDialogTurnEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BaseKey.PARCELABLE_ENTITY,entity);
        MsgDialog dialog = new MsgDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setRequestCode(int requestCode) {
        if (mEntity != null) {
            mEntity.setRequestCode(requestCode);
        }
    }

    public void setEntity(MsgDialogTurnEntity mEntity) {
        this.mEntity = mEntity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEntity = bundle.getParcelable(BaseKey.PARCELABLE_ENTITY);
        }
        TextView cancel = mBind.cancel;
        TextView enter = mBind.enter;
        if (mEntity != null) {
            mBind.setEntity(mEntity);
            CommBiConsumer<MsgDialogTurnEntity, Boolean> listener = mEntity.getListener();
            if (listener != null && this.listener == null) {
                this.listener = listener;
            }
            mBind.setIsSingleBtn(TextUtils.isEmpty(mEntity.getCancelText()));
            setCancelable(mEntity.isCancelable());
        }
        cancel.setOnClickListener(this);
        enter.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_msg;
    }

    private CommBiConsumer<MsgDialogTurnEntity, Boolean> listener;
    public MsgDialog setListener(CommBiConsumer<MsgDialogTurnEntity, Boolean> listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            onClickDialog(false);
        } else if (v.getId() == R.id.enter) {
            onClickDialog(true);
        }
        dismiss();
    }

    private void onClickDialog(boolean isOk) {
        if (listener != null) {
            try {
                listener.accept(mEntity, isOk);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
