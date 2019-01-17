package com.rwz.basemodule.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.download.DownloadConfig;
import com.rwz.basemodule.download.IFileDownloadListener;
import com.rwz.basemodule.download.downloadmanager.DownloadChangeObserver;
import com.rwz.basemodule.download.downloadmanager.SystemDownload;
import com.rwz.basemodule.receiver.DownLoadCompleteReceiver;
import com.rwz.commonmodule.utils.show.LogUtil;

/**
 *  {@link DownloadService}
 */
@Deprecated
public class DownloadService2 extends Service {

    public static final String TAG = "DownloadService2";
    public static final int HANDLE_DOWNLOAD = 0x001;

    //下载任务ID(开始下载才能获取到)
    private long downloadId;
    private DownloadBinder binder;
//    private IFileDownloadListener onProgressListener;
    private DownloadChangeObserver downloadObserver;
    private DownLoadCompleteReceiver completeReceiver;
    private WrapFileDownloadListener listener;

    public Handler downLoadHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (HANDLE_DOWNLOAD == msg.what && listener != null) {
                LogUtil.d(TAG, "downLoadHandler：" + msg.arg1, msg.arg2);
                //被除数可以为0，除数必须大于0
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    listener.onProgress(msg.arg1 / (msg.arg2 / 100));
//                    SystemDownload.getInstance().onProgress(downloadId, msg.arg1 / (msg.arg2 / 100));
                }
            }
        }
    };

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
        }
    };

    public void setOnProgressListener(IFileDownloadListener onProgressListener) {
//        this.onProgressListener = onProgressListener;
        if (listener == null) {
            listener = new WrapFileDownloadListener(onProgressListener);
        } else {
            listener.onProgressListener = onProgressListener;
        }
        if (downloadId == SystemDownload.DOWNLOAD_ERROR_ID) {
            listener.onError();
        }
    }

    /**
     * 发送Handler消息更新进度和状态
     */
    private void updateProgress() {
        int[] bytesAndStatus = SystemDownload.getInstance().getBytesAndStatus(downloadId);
        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new DownloadBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            LogUtil.d(TAG, "onStartCommand", "listener = " + listener);
            if (listener == null) {
                listener = new WrapFileDownloadListener(null);
            }
            DownloadConfig config = intent.getParcelableExtra(BaseKey.PARCELABLE_ENTITY);
            LogUtil.d(TAG, "onStartCommand, DownloadConfig", "config = " + config);
            downloadApk(config);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * 开启一个下载
     */
    private void downloadApk(DownloadConfig config) {
        if (config == null) {
            //关闭服务
            stopSelf();
            return;
        }
        downloadObserver = new DownloadChangeObserver(downLoadHandler, progressRunnable);
        registerContentObserver();
        registerReceiver();
        if (config != null && listener != null) {
            config.setListener(listener);
        }
        SystemDownload.getInstance().startDownload(config);
        if (config != null) {
            downloadId = config.getId();
        }
        /*if (downloadId == SystemDownload.DOWNLOAD_ERROR_ID && listener != null) {
            listener.onError();
        }*/
        LogUtil.d(TAG, "Apk下载DownloadConfig：" + config, "downloadId = " + downloadId);
    }


    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        /** observer download change **/
        if (downloadObserver != null) {
            getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), false, downloadObserver);
            LogUtil.d(TAG, "registerContentObserver over");
        }
    }

    /**
     * 注册下载完成的广播
     */
    private void registerReceiver() {
        if (completeReceiver == null) {
            /**注册service 广播 1.任务完成时 2.进行中的任务被点击*/
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
            completeReceiver = new DownLoadCompleteReceiver();
            registerReceiver(completeReceiver, intentFilter);
            LogUtil.d(TAG, "registerReceiver over");
        }
    }

    /**
     * 注销ContentObserver
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    /**
     * 注销广播
     */
    private void unregisterReceiver() {
        if (completeReceiver != null) {
            unregisterReceiver(completeReceiver);
            completeReceiver = null;
        }
    }

    public class DownloadBinder extends Binder {
        /**
         * 返回当前服务的实例
         * @return
         */
        public DownloadService2 getService() {
            return DownloadService2.this;
        }
    }

    private void close() {
        if (downLoadHandler != null) {
            downLoadHandler.removeCallbacksAndMessages(null);
        }
    }

    private void stopTask() {
        close();
        if (downloadObserver != null) {
            downloadObserver.close();
        }
    }

    @Override
    public void onDestroy() {
        stopTask();
        unregisterContentObserver();
        unregisterReceiver();
        LogUtil.d(TAG, "下载任务服务销毁");
        super.onDestroy();
    }


    private  class WrapFileDownloadListener implements IFileDownloadListener {

        private IFileDownloadListener onProgressListener;

        public WrapFileDownloadListener(IFileDownloadListener onProgressListener) {
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
            //关闭服务
            stopSelf();
            if (onProgressListener != null) {
                onProgressListener.onCompleted();
            }
        }

        @Override
        public void onError() {
            LogUtil.d(TAG, "WrapFileDownloadListener, onError ； ", onProgressListener);
            //关闭服务
            stopSelf();
            if (onProgressListener != null) {
                onProgressListener.onError();
            }
        }
    }

}
