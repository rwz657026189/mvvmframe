package com.rwz.basemodule.download;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.rwz.basemodule.R;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.download.downloadmanager.SystemDownload;
import com.rwz.basemodule.help.PermissionHelp;
import com.rwz.basemodule.service.DownloadService2;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.network.CommonObserver;

import java.lang.ref.SoftReference;

/**
 * @function: 下载管理者 入口
 * @author: rwz
 * @date: 2017-08-19 18:16
 */

public class FileDownloadManager{

    BaseFileDownloadProxy mFileDownloadProxy;
    private static FileDownloadManager instance;

    public static FileDownloadManager getInstance() {
        if (instance == null) {
            synchronized (FileDownloadManager.class) {
                if (instance == null) {
                    instance = new FileDownloadManager();
                }
            }
        }
        return instance;
    }

    private FileDownloadManager() {
        mFileDownloadProxy = SystemDownload.getInstance();
    }

    /**
     * 开始一个下载任务
     * @param config
     */
    public void startDownload(FragmentActivity aty, final DownloadConfig config) {
        if (aty != null && config != null) {
            final SoftReference<FragmentActivity> srAty = new SoftReference<>(aty);
            final SoftReference<DownloadConfig> srConfig = new SoftReference<>(config);
            LogUtil.d("startDownload, DownloadConfig", "config = " + config);
            PermissionHelp.requestWrite(aty)
                    .subscribe(new CommonObserver<Boolean>(){
                        @Override
                        public void onNext(Boolean result) {
                            Activity activity = srAty.get();
                            DownloadConfig downloadConfig = srConfig.get();
                            if (result) {
                                boolean downloadResult = SystemDownload.getInstance().startDownload(downloadConfig);
                                if (downloadResult) {

                                }
                                SystemDownload.getInstance().addConfig(config);
                                //开始下载
                                if (activity != null && downloadConfig != null) {
                                    Intent intent = new Intent(activity, DownloadService2.class);
                                    intent.putExtra(BaseKey.PARCELABLE_ENTITY, downloadConfig);
                                    activity.startService(intent);
                                }

                            } else {
                                ToastUtil.getInstance().showShortSingle(R.string.no_write_permission);
                            }
                        }
                    });
        }
    }

    public void startDownload(FragmentActivity aty, String url, String savePath,IFileDownloadListener listener) {
        startDownload(aty, new DownloadConfig(null, url, savePath, null, listener));
    }



}
