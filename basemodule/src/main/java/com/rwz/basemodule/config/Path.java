package com.rwz.basemodule.config;

import android.os.Environment;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.system.AndroidUtils;

import java.io.File;

/**
 * Created by rwz on 2017/7/18.
 */

public interface Path {

    String SEPARATOR = "/";

    /** 外置目录 **/
    interface External{
        //外置SD卡目录
        String BASE_DIR = Environment.getExternalStorageDirectory().getPath() + SEPARATOR + AndroidUtils.getPackageName(BaseApplication.getInstance()) + SEPARATOR;
        //外置SD卡临时目录
        String TEMP_DIR = BASE_DIR + "temp" + SEPARATOR;
        //外置SD卡缓存目录
        String CACHE_DIR = BASE_DIR + "cache" + SEPARATOR;
        //拍照的图片
        String TEMP_TAKE_PHOTO_AVATAR = TEMP_DIR + "take_photo_avatar.png";
        //头像待上传缓存文件
        String TEMP_HEAD_IMG_PATH = TEMP_DIR + "avatar.png";
        //外置SD卡下载目录
        String DOWNLOAD_DIR = BASE_DIR + "download" + SEPARATOR;
        //apk下载外置目录
        String APK_DIR = DOWNLOAD_DIR;
        //外置SD卡下载图片目录
        String IMG_DIR = DOWNLOAD_DIR + "图片" + SEPARATOR;
        //apk下载文件名
        String NEW_APK_NAME = AndroidUtils.getAppName(BaseApplication.getInstance()) + ".apk"; //版本升级apk name
        //知乎拍照路径
        String PICTURES = Environment.getExternalStorageDirectory().getPath() + SEPARATOR + "Pictures" + SEPARATOR;
    }

    /** 内置目录 **/
    interface Inner{
        //内置文件目录
        String BASE_DIR = BaseApplication.getInstance().getFilesDir().getAbsolutePath();
        //内置缓存目录
        String CACHE_DIR = BaseApplication.getInstance().getCacheDir().getAbsolutePath();
        //内置下载目录
        String DOWNLOAD_DIR = BASE_DIR + SEPARATOR + "download" + SEPARATOR;
    }

}
