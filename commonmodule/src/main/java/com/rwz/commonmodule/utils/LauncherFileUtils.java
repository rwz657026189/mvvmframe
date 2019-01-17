package com.rwz.commonmodule.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * 根据icon 创建 5个分目录的 ic_launcher文件
 *
 */
class LauncherFileUtils {

    private static final String TAG = "FileUtils";

    private static final float[] MIPMAP_SCALE = new float[]{128f/512f, 192f/512f, 256f/512f, 384f/512f, 1};
    private static final String[] MIPMAP_FILE_NAME = new String[]{"mipmap-mdpi", "mipmap-hdpi", "mipmap-xhdpi", "mipmap-xxhdpi", "mipmap-xxxhdpi"};


    /**
     * 1. 将图片存放在 main/assets/ 目录下（需要文件读写权限）
     * 2. 导出到pc上 输出命令： adb pull /sdcard/launcherDir/ D:\launcher\
     */
    public static void createLauncherIcon() {
        //源文件地址
        final String srcFilePath = "launcher.png";
        //存储目标路径
        final String targetFilePath = Environment.getExternalStorageDirectory() + "/launcherDir";
        //文件名
        final String fileName = "ic_launcher.png";
        for (int i = 0; i < MIPMAP_SCALE.length; i++) {
            Bitmap bitmap = compressBitmap(srcFilePath, MIPMAP_SCALE[i]);
            if (bitmap != null) {
                String name = targetFilePath + "/" + MIPMAP_FILE_NAME[i] + "/" + fileName;
                boolean result = saveBitmap(name, bitmap);
                LogUtil.d(TAG, "createLauncherIcon, result = " + result + ", name = " + name);
            }
        }
    }

    private static Bitmap compressBitmap(String fileName, float scale) {
        Bitmap newBitmap = null;
        Bitmap bitmap = getImageFromAssetsFile(fileName);
        if (bitmap != null) {
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            //最后一个参数是否平滑
            newBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale),
                    (int) (bitmap.getHeight() * scale), true);
            LogUtil.d(TAG, "compressBitmap, scale = " + scale, "bitmap = " + bitmap.getWidth(), bitmap.getHeight(), newBitmap.getWidth(), newBitmap.getHeight());
        }
        return newBitmap;
    }

    private static Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = BaseApplication.getInstance().getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /** 保存到文件 **/
    private static boolean saveBitmap(@NonNull String fileName, @NonNull Bitmap bitmap) {
        try {
            File file = createFile(fileName);
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(getFileFormat(fileName), 100, os);
            os.flush();
            os.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Bitmap.CompressFormat getFileFormat(String fileName) {
        if (fileName != null) {
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
                return Bitmap.CompressFormat.JPEG;
            if (fileName.endsWith(".png"))
                return Bitmap.CompressFormat.PNG;
            if (fileName.endsWith(".webp"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    return Bitmap.CompressFormat.WEBP;
                }
        }
        return Bitmap.CompressFormat.JPEG;
    }

    private static File createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        boolean result = file.createNewFile();
        return file;
    }





}
