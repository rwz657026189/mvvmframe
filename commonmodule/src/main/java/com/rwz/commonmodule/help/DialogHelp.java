package com.rwz.commonmodule.help;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rwz.commonmodule.utils.show.LogUtil;


/**
 * Created by rwz on 2017/4/13.
 */

public class DialogHelp {

    /**
     * 显示dialog 避免异常java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     * @param manager
     * @param tag
     * @param dialog
     */
    public static boolean show(FragmentManager manager, Fragment dialog , String tag) {
        if (manager == null) {
            LogUtil.e("manager == null");
            return false;
        }
        Fragment dialogTag = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = manager.beginTransaction();
        if (dialogTag == null) {
            if (dialog == null) {
                LogUtil.e("dialog == null");
                return false;
            }
            dialogTag = dialog;
            transaction.add(dialogTag, tag);
            transaction.commitAllowingStateLoss();
        }else{
            transaction.show(dialogTag);
        }
        return true;
    }


    public static boolean show(FragmentManager manager , @IdRes int containerRes, Fragment dialog , String tag) {
        if (manager == null) {
            LogUtil.e("manager == null");
            return false;
        }
        Fragment dialogTag = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = manager.beginTransaction();

        LogUtil.d("DialogHelp_Fragment_show", "dialogTag = " + dialogTag);

        if (dialogTag == null) {
            if (dialog == null) {
                LogUtil.e("dialog == null");
                return false;
            }
            dialogTag = dialog;
            transaction.add(containerRes, dialogTag, tag);
        }else{
            transaction.show(dialogTag);
        }
        transaction.commitAllowingStateLoss();
        return true;
    }

    public static boolean show(Context context, Fragment dialog , String tag) {
        AppCompatActivity activity = ContextHelp.getAppCompActivity(context);
        if (activity == null) {
            return false;
        } else {
            show(activity.getSupportFragmentManager(), dialog, tag);
            return true;
        }
    }

    public static boolean show(Fragment fragment, Fragment dialog , String tag) {
        if (fragment == null) {
            return false;
        } else {
            show(fragment.getChildFragmentManager(), dialog, tag);
            return true;
        }
    }

    /**
     * Get activity from context object
     *
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

}
