package com.rwz.commonmodule.utils.app;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.EditText;

import com.rwz.commonmodule.R;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by rwz on 2018/6/12.
 */

public class CommUtils {

    /**
     * 判断intent是否可以安全跳转(主要是隐式跳转)
     */
    public static boolean canTurn(Context context, Intent intent) {
        return context != null && intent != null && intent.resolveActivity(context.getPackageManager()) != null;
    }

    /**
     * 复制文字到剪切板
     */
    public static void copyText(CharSequence text) {
        ClipboardManager cmb = (ClipboardManager) BaseApplication.getInstance().getSystemService(CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            String label = ResourceUtil.getString(R.string.copy);
            cmb.setPrimaryClip(ClipData.newPlainText(label, text));
            ToastUtil.getInstance().showShort(R.string.copy_text_completed);
        } else {
            ToastUtil.getInstance().showShort(R.string.not_support_copy);
        }
    }

    public static String subString(String text, int num){
        String content = "";
        if (text.length() > num){
            content = text.substring(0, num -1) + "...";
        }else{
            content = text;
        }
        return content;
    }

    /**
     * @return 判断服务是否开启
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        if (TextUtils.isEmpty(serviceName) || context == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //android 26之后只返回本应用的服务，之前会返回所有正在运行的服务，故值需要足够大
        int maxNum = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 10 : 200;
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) (myManager != null ? myManager
                .getRunningServices(maxNum) : null);
        for (int i = 0; i < runningService.size(); i++) {
            if (TextUtils.equals(runningService.get(i).service.getClassName(), serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return 判断某个进程是否开启
     */
    public static boolean isProcessRunning(Context context, String processName) {
        if(context == null || processName == null)
            return false;
        boolean isRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info : lists){
            if(info.processName.equals(processName)){
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * @param isMute 值为true时为关闭背景音乐
     * @return
     */
    public static boolean muteBackgroundAudio(boolean isMute) {
        boolean bool;
        AudioManager am = (AudioManager)BaseApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        if(isMute){
            int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }else{
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return bool;
    }

    /**
     * 从文件获取uri
     */
    public static Uri getUriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= 24) {
            String packageName = AndroidUtils.getPackageName(context);
            return FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
        } else
            return Uri.fromFile(file);
    }

    /**
     * 设置editText 不可编辑(同TextView)
     */
    public static void setNotEditable(EditText editText) {
        if(editText == null) return;
        editText.setCursorVisible(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }


}
