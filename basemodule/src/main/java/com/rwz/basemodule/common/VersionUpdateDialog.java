package com.rwz.basemodule.common;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.rwz.basemodule.R;
import com.rwz.basemodule.base.BaseDialog;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.databinding.DialogUpdateVersionBinding;
import com.rwz.basemodule.entity.NotificationEntity;
import com.rwz.basemodule.entity.UpdateVersionEntity;
import com.rwz.basemodule.help.PermissionHelp;
import com.rwz.basemodule.receiver.ActivityResultReceiver;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.help.DialogHelp;
import com.rwz.commonmodule.inf.IPostEvent3;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.network.CommonObserver;

/**
 * Created by rwz on 2017/7/18.
 * 更新版本dialog
 */

public class VersionUpdateDialog extends BaseDialog<DialogUpdateVersionBinding> implements View.OnClickListener {

    private UpdateVersionEntity mEntity;//版本跟新
    private NotificationEntity notificationEntity; //更新过程通知栏实体类
    private ActivityResultReceiver mReceiver;
    public static final int INSTALL_APP_REQUEST_CODE = 6999;

    public static VersionUpdateDialog newInstance(UpdateVersionEntity entity, NotificationEntity notificationEntity) {
        VersionUpdateDialog dialog = new VersionUpdateDialog();
        if (entity != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(BaseKey.PARCELABLE_ENTITY, entity);
            bundle.putParcelable(BaseKey.PARCELABLE_ENTITY2, notificationEntity);
            dialog.setArguments(bundle);
        }
        return dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mEntity = bundle.getParcelable(BaseKey.PARCELABLE_ENTITY);
            notificationEntity = bundle.getParcelable(BaseKey.PARCELABLE_ENTITY2);
        }
        TextView cancel = mBind.cancel;
        cancel.setText(ResourceUtil.getString(R.string.wait_a_moment));
        TextView enter = mBind.enter;
        enter.setText(ResourceUtil.getString(R.string.update_immediately));
        if (mEntity == null) {
            return;
        }
        setCancelable(!mEntity.getTips());
        mBind.setEntity(mEntity);
        cancel.setOnClickListener(this);
        enter.setOnClickListener(this);
        mReceiver = new ActivityResultReceiver(postEvent);
        ActivityResultReceiver.register(getContext(), mReceiver);
    }

    IPostEvent3<Integer, Integer, Bundle> postEvent = new IPostEvent3<Integer, Integer, Bundle>() {
        @Override
        public void onEvent(Integer integer, Integer integer2, Bundle bundle) {
            if (integer == INSTALL_APP_REQUEST_CODE) {
                if (PermissionHelp.hasInstallApp()) {
                    assertWritePermission();
                } else {
                    showNoInstallAppPermissionDialog();
                }
            }
        }
    };

    @Override
    public void onDismiss(DialogInterface dialog) {
        ActivityResultReceiver.unregister(getContext(), mReceiver);
        LogUtil.d(TAG, "onDismiss", "context = " + getContext());
        super.onDismiss(dialog);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_update_version;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            if (mEntity != null && mEntity.getTips()) {
                ToastUtil.showShort(ResourceUtil.getString(R.string.update_force));
            }else{
                dismissAllowingStateLoss();
            }
        } else if (v.getId() == R.id.enter) {
            assertInstallAppPermission();
        }
    }

    private void assertInstallAppPermission() {
        if (PermissionHelp.hasInstallApp()) {
            assertWritePermission();
        } else {
            installApp(getActivity());
        }
    }

    /**
     * 安装app
     */
    public void installApp(Activity aty) {
        if (aty != null) {
            //设置包名，可直接跳转当前软件的设置页面
            Uri packageURI = Uri.parse("package:"+ aty.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
            aty.startActivityForResult(intent, INSTALL_APP_REQUEST_CODE);
        }
    }

    private void assertWritePermission() {
        // 判断有无文件读写权限, android 8.0以上需要附加安装未知来源权限
        CommonObserver<Boolean> observer = new CommonObserver<Boolean>() {
            @Override
            public void onNext(Boolean entity) {
                if (entity) {
                    startDownload();
                    dismissAllowingStateLoss();
                } else {
                    showNoWritePermissionDialog();
                }
            }
        };
        PermissionHelp.requestWrite(getActivity()).subscribe(observer);
    }

    /**
     * 设置无相关权限的提示对话框
     */
    private void showNoWritePermissionDialog() {
        mBind.title.setText(ResourceUtil.getString(R.string.dialog_def_title));
        mBind.msg.setText(ResourceUtil.getString(R.string.no_permission_to_update_new_version));
        mBind.cancel.setText(ResourceUtil.getString(R.string.cancel));
        mBind.enter.setText(ResourceUtil.getString(R.string.enter));
    }

    private void showNoInstallAppPermissionDialog() {
        mBind.title.setText(ResourceUtil.getString(R.string.dialog_def_title));
        mBind.msg.setText(ResourceUtil.getString(R.string.no_install_app_permission_to_update_new_version));
        mBind.cancel.setText(ResourceUtil.getString(R.string.cancel));
        mBind.enter.setText(ResourceUtil.getString(R.string.enter));
    }

    private void startDownload() {
        if (mEntity != null) {
            if (notificationEntity != null) {
                String appName = AndroidUtils.getAppName(BaseApplication.getInstance());
                notificationEntity.setTitle(appName);
                notificationEntity.setMsg(ResourceUtil.getString(R.string.download_new_version));
            }
            DownloadDialog dialog = DownloadDialog.getInstance(notificationEntity, mEntity.getDownloadUrl(), mEntity.getTips());
            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            LogUtil.d("fragmentManager = " + fragmentManager);
            DialogHelp.show(fragmentManager, dialog, "DownloadAppDialog");
        }
    }

}
