package com.rwz.basemodule.help;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import com.rwz.basemodule.R;
import com.rwz.basemodule.config.Path;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.CommUtils;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.network.CommonObserver;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rwz on 2017/7/25.
 */

public class PhotoHelp {

    public static final int PHOTO_CODE = 0;
    public static final int CROP_CODE = 1;
    public static final int PICK_CODE = 2;

    /**
     * 拍照
     * @param aty
     */
    public static void takePhoto(FragmentActivity aty) {
        requestCamera(aty);
    }

    private static void handleTakePhoto(Activity aty) {
        LogUtil.d("PhotoHelp", "takePhoto", aty);
        if(aty == null) return;
        File file = new File(Path.External.TEMP_TAKE_PHOTO_AVATAR);
        if (Build.VERSION.SDK_INT >= 24) {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            LogUtil.d("缓存头像文件目录 ；",file.getAbsoluteFile());
            //通过FileProvider创建一个content类型的Uri
            Context context = BaseApplication.getInstance();
            String packageName = AndroidUtils.getPackageName(context);
            Uri imageUri = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
//            Uri imageUri = FileProvider.getUriForFile(aty, ResourceUtil.getString(R.string.fileprovider_pick_photo), file);
            LogUtil.d("imageUri = "+ imageUri);
            Intent intent = new Intent();
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
            if(CommUtils.canTurn(aty, intent))
                aty.startActivityForResult(intent, PHOTO_CODE);
        }else{
            if (setImageFile()) return;
            Uri imgUri = Uri.fromFile(file);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);//照片保存目录
            if(CommUtils.canTurn(aty, intent))
                aty.startActivityForResult(intent, PHOTO_CODE);
        }
    }

    /**  * 转换 content:// uri  *   * @param imageFile  * @return  */
    public static Uri getImageContentUri(File imageFile) {
        if (imageFile == null) {
            return null;
        }
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = null;
        try {
            cursor = BaseApplication.getInstance().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Media._ID},
                    MediaStore.Images.Media.DATA + "=? ",
                    new String[]{filePath}, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                Uri baseUri = Uri.parse("content://media/external/images/media");
                return Uri.withAppendedPath(baseUri, "" + id);
            } else {
                if (imageFile.exists()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return BaseApplication.getInstance().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                } else {
                    return null;
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    private static void requestCamera(FragmentActivity aty) {
        final SoftReference<FragmentActivity> srAty = new SoftReference<>(aty);
        PermissionHelp.requestCameraAndWrite(aty)
                .subscribe(new CommonObserver<Boolean>(){
                    @Override
                    public void onNext(Boolean entity) {
                        if (PermissionHelp.hasWritePermission() && PermissionHelp.hasCameraPermission()) {
                            handleTakePhoto(srAty.get());
                        } else {
                            ToastUtil.showShort(R.string.not_camera_write_permission);
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        ToastUtil.showShort(R.string.handle_fail);
                    }
                });
    }

    private static boolean setImageFile() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        String imgFileStr = Path.External.TEMP_HEAD_IMG_PATH;
        File imgFile = new File(imgFileStr);
        if(!imgFile.getParentFile().exists()){
            imgFile.getParentFile().mkdirs();
        }
        try {
            if(imgFile.exists()){
                imgFile.delete();
            } else {
                LogUtil.e("PhotoHelp",  "setImageFile error");
            }
            imgFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 选择照片
     * @param aty
     */
    public static void pickPhoto(Activity aty) {
        LogUtil.d("PhotoHelp", "pickPhoto", aty);
        if (aty == null) {
            return;
        }
        try {
            if (setImageFile()) return;
            Intent intent = new Intent("android.intent.action.PICK");
            intent.setType("image/*");//设置选择的是图片类型
            if(CommUtils.canTurn(aty, intent))
                aty.startActivityForResult(intent, PICK_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**启动照片剪切程序**/
    public static void startCropActivity(Activity aty , Uri saveUri, Uri cropUri) {
        LogUtil.d("PhotoHelp", "startCropActivity", aty, "saveUri = " + saveUri, "cropUri = " + cropUri);
        if (aty != null) {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(cropUri, "image/*");//设置Data，并且类型是一种图片
            intent.putExtra("scale", true);//是否允许缩放
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            int size = ResourceUtil.getDimen(R.dimen.h_120);
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);//剪切后照片保存目录
            if(Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            if(CommUtils.canTurn(aty, intent))
                aty.startActivityForResult(intent, CROP_CODE);
        }
    }

    public static boolean onActivityResult(Activity aty, int requestCode, int resultCode, Intent data) {
        if(requestCode == PhotoHelp.CROP_CODE && resultCode == RESULT_OK){//剪切
            LogUtil.d("onActivityResult, 返回剪切结果");
            return true;
        }else if(requestCode == PhotoHelp.PICK_CODE && resultCode == RESULT_OK){//选择
            Uri saveUri = getImageContentUri(new File(Path.External.TEMP_HEAD_IMG_PATH));
            LogUtil.d("onActivityResult, 返回选择结果");
            Uri cropUri = data.getData();
            PhotoHelp.startCropActivity(aty, saveUri, cropUri);
        } else if (requestCode == PhotoHelp.PHOTO_CODE && resultCode == RESULT_OK) { //拍照
            //必须要创建一个新文件！！！
            File file = new File(Path.External.TEMP_HEAD_IMG_PATH);
            boolean result;
            if (file.exists()) {
                file.delete();
            }
            try {
                result = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
            LogUtil.d("onActivityResult, 返回拍照结果", "result = " + result);
            if (result) {
                Uri saveUri = getImageContentUri(file);
                Uri cropUri = getImageContentUri(new File(Path.External.TEMP_TAKE_PHOTO_AVATAR));
                //7.0以上可能造成“无法保持剪切后的图片”
                LogUtil.d("onActivityResult, 返回拍照结果");
                PhotoHelp.startCropActivity(aty, saveUri, cropUri);
            }

        }
        return false;
    }

}
