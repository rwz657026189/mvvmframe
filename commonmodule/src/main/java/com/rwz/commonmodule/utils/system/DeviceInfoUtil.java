package com.rwz.commonmodule.utils.system;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.safety.EncryptionUtil;
import com.rwz.commonmodule.utils.show.LogUtil;


/**
 * Created by rwz on 2017/3/13.
 * 获取设备信息
 */

public class DeviceInfoUtil {

    private static final String TAG = "DeviceInfoUtil";
    private String uuid = "";//设备id
    private static DeviceInfoUtil mInstance;
    //随机数
    private static final String RANDOM_CODE_KEY = "RANDOM_CODE_KEY";
    //唯一code
    private static final String UNIQUE_CODE_KEY = "UNIQUE_CODE_KEY";

    public static DeviceInfoUtil getInstance() {
        if (mInstance == null) {
            synchronized (DeviceInfoUtil.class) {
                if (mInstance == null) {
                    mInstance = new DeviceInfoUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取设备唯一ID 【首选】
     * 如果有ACCESS_WIFI_STATE权限，则会获取imsi地址作为唯一id。否则根据硬件信息生成唯一ID
     * 保存在sp中是避免开始没有权限，以后又给予权限，造成ID值不一致的问题。
     */
    public String getUUID(Context context){
        String flag = "";
        if (TextUtils.isEmpty(uuid)) {
            uuid = getString(AndroidUtils.getAppName(context), context, UNIQUE_CODE_KEY);
            flag = "cache uuid = ";
            if (TextUtils.isEmpty(uuid)) {
                String imsi = getIMSI(context);
                if (!TextUtils.isEmpty(imsi)) {
                    uuid = EncryptionUtil.encodeMD5ToString(imsi);
                    flag = "imsi uuid = ";
                } else {
                    //如果获取失败(没有权限)， 就根据硬件设备生成一个
                    uuid = getUniqueID();
                    flag = "unique uuid = ";
                }
                putString(AndroidUtils.getAppName(context), context, UNIQUE_CODE_KEY, uuid);
            }
            LogUtil.d(TAG, flag + uuid);
        }
        return uuid;
    }

    public String getLocalMacAddress(Context context) {
        if(context == null) return "";
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        if (wifi != null) {
            info = wifi.getConnectionInfo();
        }
        if (info != null) {
            return info.getMacAddress();
        }
        return "";
    }

    public String getIMSI(Context context) {
        if(context == null) return "";
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            boolean hasPermission = Build.VERSION.SDK_INT < 23 || permissionCheck == PackageManager.PERMISSION_GRANTED;
            LogUtil.d(TAG, "getIMSI", "hasPermission = " + hasPermission);
            if (!hasPermission) {
                return "";
            }
            TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyMgr != null) {
                return telephonyMgr.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getString(String preName, Context context, String key) {
        if (context != null) {
            SharedPreferences pre = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
            return pre.getString(key, "");
        }
        return "";
    }

    /** 随机生产一个字符串 **/
    private String newUniqueCode() {
        Context context = BaseApplication.getInstance();
        String value = getString(AndroidUtils.getAppName(context), context, RANDOM_CODE_KEY);
        if (TextUtils.isEmpty(value)) {
            value = System.currentTimeMillis() + AndroidUtils.getPackageName(context);
            putString(AndroidUtils.getAppName(context), context, RANDOM_CODE_KEY, value);
        }
        return value;
    }


    private static Boolean putString(String preName, Context context, String key, String value) {
        if(context == null) return false;
        SharedPreferences pre    = context.getSharedPreferences(preName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString(key, value);
        return editor.commit();
    }


    /**
     * @return  根据硬件信息获取设备唯一标识
     * 参考：https://jingyan.baidu.com/article/a681b0de7dde313b19434664.html
     */
    public String getUniqueID() {
        //区分同款机型的不同手机(已测试过同款小米8SE)
        String serial = Build.SERIAL;
        //若没有获取到随机生成一个
        if(TextUtils.isEmpty(serial))
            serial = newUniqueCode();
        String hardwareInfo = Build.BOARD +
                "&" + Build.BRAND +
                "&" + Build.CPU_ABI +
                "&" + Build.DEVICE +
                "&" + Build.DISPLAY +
                "&" + Build.HOST +
                "&" + Build.ID +
                "&" + Build.MANUFACTURER +
                "&" + Build.MODEL +
                "&" + Build.PRODUCT +
                "&" + Build.TAGS +
                "&" + Build.TYPE +
                "&" + Build.USER +
                "&" + serial;
        String mDevIDShort = EncryptionUtil.encodeMD5ToString(hardwareInfo);
        LogUtil.d("hardwareInfo= " + hardwareInfo + " mDevIDShort = " + mDevIDShort);
        return mDevIDShort;
    }


}
