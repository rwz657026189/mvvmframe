package com.rwz.basemodule.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.rwz.basemodule.download.IFileDownloadListener;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.FileUtil;
import com.rwz.commonmodule.utils.safety.EncryptionUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rwz on 2017/1/12.
 * http://www.2cto.com/kf/201205/132327.html
 */

public class FileDownloadUtils {

    public static final int READ_TIME = 5000;
    public static final int CONNECT_TIME = 5000;

    private static FileDownloadUtils fileDownloadUtils;

    public static FileDownloadUtils getInstance() {
        if (fileDownloadUtils == null) {
            synchronized (FileDownloadUtils.class) {
                if (fileDownloadUtils == null) {
                    fileDownloadUtils = new FileDownloadUtils();
                }

            }
        }
        return fileDownloadUtils;
    }

    public void fun() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://gdown.baidu.com/data/wisegame/55dc62995fe9ba82/jinritoutiao_448.apk"));
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载");
        request.setDescription("今日头条正在下载");
        request.setAllowedOverRoaming(false);
        //设置文件存放目录
        request.setDestinationInExternalFilesDir(BaseApplication.getInstance(), Environment.DIRECTORY_DOWNLOADS, "mydown");
    }


    public void startDownloadImg(final String url, final String filePath, IFileDownloadListener listener) {
        final SoftReference<IFileDownloadListener> wfListener = new SoftReference(listener);
        new Thread(){
            @Override
            public void run() {
                LogUtil.d("下载url ",url);
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                IFileDownloadListener iFileDownloadListener = wfListener.get();
                File file = new File(filePath);
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    boolean isSuccess = parentFile.mkdirs();
                    if (!isSuccess) {
                        if (iFileDownloadListener != null) {
                            iFileDownloadListener.onError();
                            LogUtil.d("下载","下载完成");
                        }
                        return;
                    }
                } else {
                    if (iFileDownloadListener != null) {
                        iFileDownloadListener.onError();
                        LogUtil.d("下载","下载完成");
                    }
                }
                if (file.exists()) {
                    file.delete();
                    LogUtil.d("file是否存在 ： ",file.exists());
                }
                LogUtil.d("startDownloadImg", file.getAbsolutePath());
                InputStream is;
                FileOutputStream fos;
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    URL mUrl = new URL(url);
                    URLConnection conn = mUrl.openConnection();
                    conn.setReadTimeout(READ_TIME);
                    conn.setConnectTimeout(CONNECT_TIME);
                    int contentLength = conn.getContentLength();
                    int currLength = 0;
                    is = conn.getInputStream();
                    fos = new FileOutputStream(file);
                    bis = new BufferedInputStream(is);
                    bos = new BufferedOutputStream(fos);
                    byte[] buff = new byte[1024 * 4];
                    int length;
                    LogUtil.d("下载","开始下载: contentLength = " + contentLength);
                    while ((length = bis.read(buff)) != -1) {
                        bos.write(buff, 0, length);
                        currLength += length;
                        IFileDownloadListener downloadListener = wfListener.get();
                        if (downloadListener != null && contentLength > 0) {
                            downloadListener.onProgress(currLength / (contentLength / 100));
                        }
                    }
                    IFileDownloadListener downloadListener = wfListener.get();
                    if (downloadListener != null) {
                        downloadListener.onCompleted();
                    }
                    LogUtil.d("下载","下载完成");
                } catch (Exception e) {
                    e.printStackTrace();
                    IFileDownloadListener downloadListener = wfListener.get();
                    if (downloadListener != null) {
                        downloadListener.onError();
                    }
                    LogUtil.d("下载","下载失败");
                } finally {
                    try {
                        if (bis != null) {
                            bis.close();
                        }
                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 开始下载(不带通知栏)
     * @param url
     */
    public void startDownload(String url,String fileDir,String fileName) {
        LogUtil.d("下载url ",url);
        FileUtil.createDir(fileDir);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));//设置需要下载目标的Uri，可以是http、ftp等等了。
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置文件存放目录
        Context appContext = BaseApplication.getInstance();
        request.setDestinationInExternalFilesDir(appContext, fileDir,fileName);
        DownloadManager downManager = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        downManager.enqueue(request);//此方法返回一个编号用于标示此下载任务：
        //downManager.remove(id); 取消下载
    }

    /**
     * 开始下载(带通知栏)
     * @param url
     */
    public void startDownloadWithNotification(String url,String fileDir,String fileName) {
        LogUtil.d("下载url ",url);
        FileUtil.createDir(fileDir);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));//设置需要下载目标的Uri，可以是http、ftp等等了。
        //设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置通知栏标题
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载");
        request.setDescription("今日头条正在下载");
        request.setAllowedOverRoaming(false);//对于下载，考虑到流量费用，这里是否允许使用漫游。
        request.setMimeType("utf-8");  //设置mime类型，这里看服务器配置，一般国家化的都为utf-8编码。
        request.setVisibleInDownloadsUi(false);  //设置下载管理类在处理过程中的界面是否显示
        //设置文件存放目录
        Context appContext = BaseApplication.getInstance();
        LogUtil.d("file_path",fileDir + fileName);
        request.setDestinationInExternalFilesDir(appContext, fileDir,fileName);
        DownloadManager downManager = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        long id= downManager.enqueue(request);//此方法返回一个编号用于标示此下载任务：
        //downManager.remove(id); 取消下载
        fun();

    }

    //下载完成触发的广播
    private class DownLoadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Toast.makeText(BaseApplication.getInstance(), "编号："+id+"的下载任务已经完成！", Toast.LENGTH_SHORT).show();
            }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
                Toast.makeText(BaseApplication.getInstance(), "别瞎点！！！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //setFilterById(long… ids)：根据任务编号查询下载任务信息
    //setFilterByStatus(int flags)：根据下载状态查询下载任务

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
            //String statuss = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String size= cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
            String sizeTotal = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            Map<String, String> map = new HashMap<>();
            map.put("downid", downId);
            map.put("title", title);
            map.put("address", address);
            map.put("status", sizeTotal+":"+size);
//            this.data.add(map);
        }
        cursor.close();
    }


    /**
     * 获取文件名
     * 配置了就直接采用，没有则以MD5加密url获取
     * @return
     * extensionNameIfNull 如果未能获取到文件格式的时候，给定的文件格式（.jpg 、  .apk .....）
     */
    public static String getFileName(String url, String extensionNameIfNull) {
        if (!TextUtils.isEmpty(url)) {
            String fileExtensionName = FileUtil.getFileExtensionName(url);
            String name = EncryptionUtil.encodeMD5ToString(url);
            if (!TextUtils.isEmpty(fileExtensionName)) {
                extensionNameIfNull = "." + fileExtensionName;
            }
            return name + extensionNameIfNull;
        }
        return UUID.randomUUID().toString() + extensionNameIfNull;
    }

}
