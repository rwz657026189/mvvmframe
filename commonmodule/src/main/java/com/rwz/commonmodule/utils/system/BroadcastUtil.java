package com.rwz.commonmodule.utils.system;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.File;

/**
 * Created by rwz on 2017/4/20.
 */

public class BroadcastUtil {

    /**
     * 最后通知媒体库更新
     */
    public static void notifySystemMediaUpdate(String filePath) {
        notifySystemMediaUpdate2(BaseApplication.getInstance(), new File(filePath));
    }

    public static void notifySystemMediaUpdate(Context context, File file) {
        LogUtil.d("BroadcastUtil", "notifySystemMediaUpdate" , "context = " + context, "file = " + file);
        if (context == null || file == null || !file.exists()) {
            return;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("notifySystemMediaUpdate fail");
        }
        Uri uri;
        Intent intent;
        if(Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            //file:///storage/emulated/0/fjz/update/fanjianzhi.apk
            String packageName = AndroidUtils.getPackageName(context);
            uri = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
            intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }else{
            uri = Uri.parse("file://" + file);
            intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        }
        LogUtil.d("BroadcastUtil", "uri = " + uri);
        context.sendBroadcast(intent);
    }

    /**
     * 通知更新， 必须在子线程调用  连接有延迟1s
     * @param context
     * @param file
     */
    public static void notifySystemMediaUpdate2(Context context, File file) {
        LogUtil.d("BroadcastUtil", "notifySystemMediaUpdate2" , "context = " + context, "file = " + file);
        try {
            if (context == null || file == null || !file.exists()) {
                return;
            }
            MediaScannerConnection msc = new MediaScannerConnection(context, new MediaScannerConnection.MediaScannerConnectionClient(){
                @Override
                public void onMediaScannerConnected() {
                }
                @Override
                public void onScanCompleted(String path, Uri uri) {
                }
            });
            msc.connect();
            Thread.sleep(1000);
//            URL url = file.toURL();
//            MimeTypeMap mtm = MimeTypeMap.getSingleton();
//            LogUtil.d("notifySystemMediaUpdate2", url.toString(), mtm.getFileExtensionFromUrl(url.toString()));
            if (msc.isConnected()) {
//                msc.scanFile(file.getAbsolutePath(), mtm.getMimeTypeFromExtension(mtm.getFileExtensionFromUrl(url.toString())));
                //此句上面的一句可以，下面的一句也可以，都适合这种方法（已用颜色标示）。扫描文件夹方式在部分定制rom中无效，极力建议扫描具体文件
                msc.scanFile(file.getAbsolutePath(), null);
                msc.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
