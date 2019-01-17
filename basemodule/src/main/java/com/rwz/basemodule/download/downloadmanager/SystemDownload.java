package com.rwz.basemodule.download.downloadmanager;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.rwz.basemodule.R;
import com.rwz.basemodule.download.BaseFileDownloadProxy;
import com.rwz.basemodule.download.DownloadConfig;
import com.rwz.basemodule.download.IFileDownloadListener;
import com.rwz.basemodule.entity.NotificationEntity;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.config.SystemConfig;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @function: 采用系统下载（单例）
 * @author: rwz
 * @date: 2017-08-19 18:18
 *
 * http://www.jianshu.com/p/bb4cde6e88c6
 *
 */

public class SystemDownload extends BaseFileDownloadProxy {

    //下载失败id
    public static final long DOWNLOAD_ERROR_ID = -1;

    private static SystemDownload instance;
    private DownloadManager downManager;

    public static SystemDownload getInstance() {
        if (instance == null) {
            synchronized (SystemDownload.class) {
                if (instance == null) {
                    instance = new SystemDownload();
                }
            }
        }
        return instance;
    }

    private SystemDownload() {
        downManager = (DownloadManager)BaseApplication
                .getInstance()
                .getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 开始一个下载
     * @param config
     */
    public boolean startDownload(DownloadConfig config) {
        boolean isResult = super.startDownload(config);
        if (isResult && downManager != null&& !TextUtils.isEmpty(config.getUrl())) {
            String url = config.getUrl();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));//设置需要下载目标的Uri，可以是http、ftp等等了。
            //设置在什么网络情况下进行下载
//            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            NotificationEntity entity = config.getEntity();
            if (entity != null) {
                /**
                 * 设置notification显示状态
                 * Request.VISIBILITY_VISIBLE：在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数值。
                 * Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED：在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该
                 * Notification或者消除该Notification。
                 * Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION：只有在下载完成后该Notification才会被显示。
                 * Request.VISIBILITY_HIDDEN：不显示该下载请求的Notification。如果要使用这个参数，需要在应用的清单文件中加上android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
                 */
                //设置通知栏标题
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle(entity.getTitle());
                request.setDescription(entity.getMsg());
            } else  {
                /**设置通知栏是否可见*/
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            }
            //下载的文件能被其他应用扫描到
            request.allowScanningByMediaScanner();
            //设置被系统的Downloads应用扫描到并管理,默认true
            request.setVisibleInDownloadsUi(true);
            //对于下载，考虑到流量费用，这里是否允许使用漫游。
            request.setAllowedOverRoaming(false);
            //设置mime类型，这里看服务器配置，一般国家化的都为utf-8编码。
            String mimeType = getMimeType(url);
            LogUtil.d(TAG, "startDownload, mimeType = " + mimeType);
            if (!TextUtils.isEmpty(mimeType)) {
                request.setMimeType(mimeType);
            }
            //设置文件存放目录
            Context appContext = BaseApplication.getInstance();
            String savePath = config.getSavePath();
            String fileName = getFileName(config);
            //setDestinationInExternalFilesDir
            request.setDestinationInExternalFilesDir(appContext, Environment.DIRECTORY_DOWNLOADS, fileName);
//            request.setDestinationInExternalFilesDir(appContext, savePath, fileName);
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            long id = downManager.enqueue(request);//此方法返回一个编号用于标示此下载任务：
            showLog("savePath = " + savePath, "fileName = " + fileName, "id = " + id);
            config.setId(id);
            IFileDownloadListener listener = config.getListener();
            if (listener != null) {
                if (id == DOWNLOAD_ERROR_ID) {
                    listener.onError();
                } else {
                    listener.onStart();
                    addConfig(config);
                }
            }
            return true;
        } else if (config != null) {
            IFileDownloadListener listener = config.getListener();
            if (listener != null) {
                listener.onError();
                removeConfig(config);
            }
        }
        return false;
    }

    private String getMimeType(String url) {
        if (!TextUtils.isEmpty(url)) {
            //apk
            if (url.endsWith(ResourceUtil.getString(R.string.apk))) {
                return SystemConfig.MimeType.INSTALL_APP;
            } else if (url.endsWith(ResourceUtil.getString(R.string.jpg))
                    ||url.endsWith(ResourceUtil.getString(R.string.jpeg))
                    || url.endsWith(ResourceUtil.getString(R.string.png))
                    || url.endsWith(ResourceUtil.getString(R.string.gif))) {
                return SystemConfig.MimeType.IMAGE;
            }
        }
        return null;
    }

    /**
     * 查询下载任务
     * @param downManager
     * @param status
     */
    private void queryDownTask(DownloadManager downManager,int status) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterByStatus(status);
        Cursor cursor= downManager.query(query);
        while(cursor.moveToNext()){
            String downId= cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
            String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//            String statuss = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String size= cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            String sizeTotal = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Map<String, String> map = new HashMap<>();
            map.put("downID", downId);
            map.put("title", title);
            map.put("address", address);
            map.put("status", sizeTotal+":"+size);
        }
        cursor.close();
    }

    /**
     * 查询下载任务路径
     * @param downId
     * @return
     */
    public String queryDownFilePath(long downId) {
        if (downManager == null) {
            return "";
        }
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downId);
        Cursor cursor= downManager.query(query);
        String address = "";
        if (cursor.moveToFirst()) {
            address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            showLog("queryDownFilePath",  "address = " + address);
        }
        cursor.close();
        return address;
    }

    /**
     * 完成下载回调
     * @param id
     */
    public void onCompleted(long id) {
        LogUtil.d(TAG, "onCompleted", "id = " + id);
        DownloadConfig config = getConfig(id);
        if (config != null) {
            removeConfig(config);
            IFileDownloadListener listener = config.getListener();
            if (listener != null) {
                listener.onCompleted();
            }
        }
    }

    public Uri getUriForDownloadedFile(long id) {
        if (downManager != null) {
            return downManager.getUriForDownloadedFile(id);
        }
        return null;
    }

    /**
     * 设置进度
     * @param id
     * @param progress
     */
    public void onProgress(long id, int progress) {
        DownloadConfig config = getConfig(id);
        LogUtil.d(TAG, "onProgress", "id = " + id, "progress = " + progress, "config = " + config);
        if (config != null) {
            IFileDownloadListener listener = config.getListener();
            if (listener != null) {
                listener.onProgress(progress);
            }
        }
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     * @param downloadId
     * @return
     */
    public int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{-1, -1, 0};
        if (downManager == null) {
            return bytesAndStatus;
        }
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = downManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //            String downId= cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

}
