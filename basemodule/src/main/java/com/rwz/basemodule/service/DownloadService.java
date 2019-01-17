package com.rwz.basemodule.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.rwz.basemodule.R;
import com.rwz.basemodule.base.InstallTempActivity;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.config.Path;
import com.rwz.basemodule.download.DownloadConfig;
import com.rwz.basemodule.download.IFileDownloadListener;
import com.rwz.basemodule.entity.NotificationEntity;
import com.rwz.basemodule.utils.DownloadUtil;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;


/**
 * Created by rwz on 2018/4/12.
 */

public class DownloadService extends Service{
    public static final int VERSION_APK_ID = 0x01001; //版本升级APK
    private static final String TAG = "DownloadService";
    private DownloadBinder binder;
    private NotificationManager mNm;
    private NotificationCompat.Builder mBuilder;
    private WrapFileDownloadListener listener;
    private int mCurrProgress = -1; //当前进度
    private static final String apkPath = Path.External.BASE_DIR + Path.External.NEW_APK_NAME;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
    }

    public class DownloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    private NotificationCompat.Builder createNotification(Context context, NotificationManager nm, NotificationEntity entity) {
        if (entity == null) {
            return null;
        }
        String channelId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel(nm);
        }
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(entity.getIcon())
                .setContentTitle(entity.getTitle())
                .setProgress(100, 0, false)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager nm){
        String channelId = "M_CH_ID";
        String channelName = "Background Service";
        //中等级别（没有通知声音，但是通知栏有通知）
        NotificationChannel chan = new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_LOW);
//        NotificationChannel chan = new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setLightColor(Color.DKGRAY);
        chan.setSound(null,null); //<---- ignore sound
        //必须卸载app才能生效（参考：http://www.jsc0.com/post/153.html）
        chan.enableLights(false);//关闭指示灯，如果设备有的话。
        chan.enableVibration(false);//关闭震动
        chan.setImportance(NotificationManager.IMPORTANCE_LOW);
        // 设置是否应在锁定屏幕上显示此频道的通知
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        nm.createNotificationChannel(chan);
        return channelId;
    }

    public void setOnProgressListener(IFileDownloadListener listener) {
        if (this.listener == null) {
            this.listener = new WrapFileDownloadListener(listener);
        } else {
            this.listener.setOnProgressListener(listener);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (listener == null) {
                listener = new WrapFileDownloadListener(null);
            }
            DownloadConfig config = intent.getParcelableExtra(BaseKey.PARCELABLE_ENTITY);
            LogUtil.d(TAG, "onStartCommand, DownloadConfig", "config = " + config);
            mNm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = createNotification(this, mNm, new NotificationEntity(ResourceUtil.getString(R.string.app_name),
                    "", R.mipmap.ic_launcher));
            mNm.notify(VERSION_APK_ID, mBuilder.build());
            downloadApk(config);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadApk(DownloadConfig config) {
        if (config != null) {
//            createFile(apkPath);
            LogUtil.d(TAG, "downloadApk", "apkPath = " + apkPath);
            DownloadUtil.getInstance().download(config.getUrl(), apkPath, new DownloadUtil.DownloadListener() {
                @Override
                public void onCompleted() {
                    LogUtil.d("onComplete, 新版下载完成");
                    if (listener != null) {
                        listener.onCompleted();
                    }
                    mBuilder.setContentText(ResourceUtil.getString(R.string.download_complete_and_install))
                            .setProgress(0, 0, false);
                    PendingIntent intent = createIntent(DownloadService.this);
                    LogUtil.d("onComplete, onComplete", intent == null);
                    if (intent != null) {
                        mBuilder.setContentIntent(intent);
                        mBuilder.setContentInfo(ResourceUtil.getString(R.string.download_complete));
                        mNm.notify(VERSION_APK_ID, mBuilder.build());
                    } else {
                        mNm.cancel(VERSION_APK_ID);
                    }
                    InstallTempActivity.turn(apkPath);
                    stopSelf();
                }

                @Override
                public void onProgress(int currProgress) {
                    if(currProgress != mCurrProgress && mBuilder != null){
                        LogUtil.d(TAG, "onProgress", "currProgress = " + currProgress);
                        if(listener != null)
                            listener.onProgress(currProgress);
                        mBuilder.setProgress(100, currProgress, false);
                        mNm.notify(VERSION_APK_ID, mBuilder.build());
                    }
                    mCurrProgress = currProgress;
                }

                @Override
                public void onFailed() {
                    LogUtil.d(TAG, "onFail");
                    if(listener != null)
                        listener.onError();
                }
            });
        }
    }

    public static PendingIntent createIntent(Context context) {
        if (context == null) {
            return null;
        }
        Intent intent = new Intent(context, InstallTempActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BaseKey.STRING, apkPath);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private  class WrapFileDownloadListener implements IFileDownloadListener {

        private IFileDownloadListener onProgressListener;

        public WrapFileDownloadListener(IFileDownloadListener onProgressListener) {
            this.onProgressListener = onProgressListener;
        }

        public void setOnProgressListener(IFileDownloadListener onProgressListener) {
            this.onProgressListener = onProgressListener;
        }

        @Override
        public void onStart() {
            LogUtil.d(TAG, "WrapFileDownloadListener, onStart ； " + onProgressListener);
            if (onProgressListener != null) {
                onProgressListener.onStart();
            }
        }

        @Override
        public void onProgress(int progress) {
            if (onProgressListener != null) {
                onProgressListener.onProgress(progress);
            }
        }

        @Override
        public void onCompleted() {
            LogUtil.d(TAG, "WrapFileDownloadListener, onCompleted ； " + onProgressListener);
            if (onProgressListener != null) {
                onProgressListener.onCompleted();
            }
            //关闭服务
            stopSelf();
        }

        @Override
        public void onError() {
            LogUtil.d(TAG, "WrapFileDownloadListener, onError ； ", onProgressListener);
            //关闭服务
            if (onProgressListener != null) {
                onProgressListener.onError();
            }
            stopSelf();
        }
    }

}
