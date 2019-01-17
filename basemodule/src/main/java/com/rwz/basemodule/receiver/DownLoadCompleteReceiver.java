package com.rwz.basemodule.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.download.IFileDownloadListener;
import com.rwz.basemodule.download.downloadmanager.SystemDownload;
import com.rwz.basemodule.help.InstallHelp;
import com.rwz.basemodule.utils.SharePreUtil;
import com.rwz.commonmodule.utils.app.CommUtils;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.File;
import java.net.URI;

/**
 * @function:
 * @author: rwz
 * @date: 2017-08-19 19:02
 */

public class DownLoadCompleteReceiver extends BroadcastReceiver {

    public DownLoadCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(IFileDownloadListener.TAG, "DownLoadCompleteReceiver" , "action = " + intent.getAction());
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
//            ToastUtil.showShort("下载完成, 安装app");
            //下载完成
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //回调
            SystemDownload.getInstance().onCompleted(id);
            //安装app
            Uri uri = SystemDownload.getInstance().getUriForDownloadedFile(id);
            if (uri != null) {
                //保存新版本路径
                SharePreUtil.putString(BaseKey.NEW_VERSION_PATH, uri.getPath());
            }

            String filePath = SystemDownload.getInstance().queryDownFilePath(id);
            File file = new File(URI.create(filePath));
            InstallHelp.installApk(context, file);
            //apkPath = file:///storage/emulated/0/Android/data/com.touchrom.yuliao/files/storage/emulated/0/yuliao/temp/yuliao(1).apk
        }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
            //点击通知栏
            //通过隐式意图打开系统下载界面
            if (context != null) {
                Intent newIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if(CommUtils.canTurn(context, newIntent))
                    context.startActivity(newIntent);
            }
        }
    }
}
