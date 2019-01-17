package com.rwz.basemodule.common;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.rwz.basemodule.R;
import com.rwz.basemodule.base.BaseDialog;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.config.Path;
import com.rwz.basemodule.databinding.DialogDownloadBinding;
import com.rwz.basemodule.download.DownloadConfig;
import com.rwz.basemodule.download.IFileDownloadListener;
import com.rwz.basemodule.entity.NotificationEntity;
import com.rwz.basemodule.help.NotificationHelp;
import com.rwz.basemodule.help.PermissionHelp;
import com.rwz.basemodule.service.DownloadService2;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.help.DialogHelp;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.ui.count_down_btn.HorizontalProgressBarWithNumber;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by rwz on 2017/4/14.
 * 如需要采用，请在AndroidManifest 加入权限
 *     <uses-permission android:name="android.permission.ACCESS_DOWNLOAD_MANAGER"/>
 * 采用的是系统下载， 部分设备下载间断性失败, 故{@link DownloadDialog}
 */
@Deprecated
public class DownloadDialog2 extends BaseDialog<DialogDownloadBinding> {

    private NotificationCompat.Builder mNotificationBuilder;
    private NotificationManager mNm;
    private HorizontalProgressBarWithNumber mProgress;
    private NotificationEntity notificationEntity;

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_download;
    }

    public static DownloadDialog2 getInstance(NotificationEntity notificationEntity , String downLoadUrl , boolean isForce) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseKey.STRING,downLoadUrl);
        bundle.putBoolean(BaseKey.BOOLEAN, isForce);
        bundle.putParcelable(BaseKey.PARCELABLE_ENTITY, notificationEntity);
        DownloadDialog2 dialog = new DownloadDialog2();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (!PermissionHelp.hasWritePermission()) {
            //无权限操作
            ToastUtil.getInstance().showShortSingle(R.string.no_write_permission);
            return;
        }
        mProgress = mBind.progress;
        Bundle bundle = getArguments();
        String downUrl = bundle.getString(BaseKey.STRING);
        boolean isForce = bundle.getBoolean(BaseKey.BOOLEAN);
        notificationEntity = bundle.getParcelable(BaseKey.PARCELABLE_ENTITY);
        setCancelable(!isForce);
//        downUrl = "http://rs.0.gaoshouvr.com/d/7b/38/e37b219cac38f3f81aa50c647d6dd606.apk";
        LogUtil.d("版本更新" , "downUrl = " + downUrl, "getTips = " + isForce);
//        createNotification();
        download(downUrl);
    }

    //开始一个下载任务
    private void download(String downUrl) {
        //    public DownloadConfig(String fileName, String url, String savePath, NotificationEntity entity, IFileDownloadListener listener) {
//        FragmentActivity activity = getActivity();
        Activity activity = DialogHelp.scanForActivity(getContext());
        LogUtil.d("download", "activity = " + activity, "getActivity() = " + getActivity());
        //.setDestinationInExternalPublicDir(
        bindService(new DownloadConfig(Path.External.NEW_APK_NAME, downUrl, Path.External.TEMP_DIR, notificationEntity, null));
    }

    boolean isBindService = false;
    private void bindService(DownloadConfig config) {
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, DownloadService2.class);
            intent.putExtra(BaseKey.PARCELABLE_ENTITY, config);
            isBindService = context.bindService(intent, conn, BIND_AUTO_CREATE);
            context.startService(intent);
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service == null) {
                dismissAllowingStateLoss();
            }
            DownloadService2.DownloadBinder binder = (DownloadService2.DownloadBinder) service;
            if (binder == null) {
                dismissAllowingStateLoss();
            }
            DownloadService2 downloadService = binder.getService();
            if (downloadService == null) {
                dismissAllowingStateLoss();
                return;
            }
            LogUtil.d("ServiceConnection","onServiceConnected", service, binder, downloadService);
            downloadService
                    .setOnProgressListener(new IFileDownloadListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onProgress(int progress) {
                            LogUtil.d("DownloadDialog","onProgress", "progress = " + progress);
                            setProgress(progress);
                        }

                        @Override
                        public void onCompleted() {
                            unbindService();
                            dismiss();
                        }

                        @Override
                        public void onError() {
                            ToastUtil.showShort(R.string.download_error_str);
                            unbindService();
                            dismiss();
                        }
                    });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("ServiceConnection","onServiceDisconnected", "name = " + name);
            dismiss();
        }
    };

    private void unbindService() {
        Context context = getContext();
        if (context != null && isBindService) {
            context.unbindService(conn);
            isBindService = false;
        }
    }

    @Override
    public void onDestroy() {
        unbindService();
        super.onDestroy();
    }

    private void setProgress(int progress) {
        if (mProgress != null) {
            if (progress < 0) {
                progress = 0;
            }
            if (progress > mProgress.getMax()) {
                progress = mProgress.getMax();
            }
            mProgress.setProgress(progress);
        }
    }

    /**
     * 创建一个通知
     */
    public void createNotification() {
        if (mNotificationBuilder == null) {
            String appName = AndroidUtils.getAppName(BaseApplication.getInstance());
            if (notificationEntity == null) {
                notificationEntity = new NotificationEntity(appName, ResourceUtil.getString(R.string.version_update), R.mipmap.ic_launcher);
            }
            mNotificationBuilder = NotificationHelp.addNotification(getContext(), notificationEntity);
        }
        if (mNotificationBuilder != null) {
            mNm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNm.notify(NotificationHelp.VERSION_APK_ID, mNotificationBuilder.build());
        }
    }

    /**
     * 更新通知栏
     * @param progress
     */
    public void updateNotification(int progress) {
        if (mNotificationBuilder != null && mNm != null) {
            mNotificationBuilder.setProgress(100, progress, false);
            mNm.notify(NotificationHelp.VERSION_APK_ID, mNotificationBuilder.build());
        }
    }

}
