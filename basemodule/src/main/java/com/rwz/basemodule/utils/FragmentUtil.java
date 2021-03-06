package com.rwz.basemodule.utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.rwz.basemodule.base.BaseDialog;
import com.rwz.basemodule.base.BaseFragment;
import com.rwz.basemodule.config.BaseKey;

/**
 * Created by rwz on 2017/7/20.
 */

public class FragmentUtil{


    public static <T extends BaseFragment>T newInstance(Class<T> cl, Parcelable params) {
        T baseFragment = newInstance(cl);
        putParcelable(baseFragment, params);
        return baseFragment;
    }

    public static <T extends BaseFragment>T newInstance(Class<T> cl, String title) {
        T baseFragment = newInstance(cl);
        putString(title, baseFragment);
        return baseFragment;
    }

    public static <T extends BaseFragment>T newInstance(Class<T> cl, int id) {
        T baseFragment = newInstance(cl);
        putInt(id, baseFragment);
        return baseFragment;
    }

    public static <T extends BaseFragment>T newInstance(Class<T> cl, boolean bool) {
        T baseFragment = newInstance(cl);
        putBoolean(bool, baseFragment);
        return baseFragment;
    }


    /** 获取dialog实例 **/
    public static <T extends BaseDialog>T newDialog(Class<T> cl, Parcelable params) {
        T dialog = newDialog(cl);
        putParcelable(dialog, params);
        return dialog;
    }



    private static void putParcelable(Fragment fragment, Parcelable params) {
        if (fragment != null && params != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(BaseKey.PARCELABLE_ENTITY,params);
            fragment.setArguments(bundle);
            fragment.setArguments(bundle);
        }
    }

    private static void putInt(int id, BaseFragment baseFragment) {
        if (baseFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(BaseKey.INT, id);
            baseFragment.setArguments(bundle);
        }
    }

    private static void putString(String title, BaseFragment baseFragment) {
        if (baseFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(BaseKey.STRING, title);
            baseFragment.setArguments(bundle);
        }
    }

    private static void putBoolean(boolean value, BaseFragment baseFragment) {
        if (baseFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(BaseKey.BOOLEAN, value);
            baseFragment.setArguments(bundle);
        }
    }

    private static <T extends BaseFragment>T newInstance(Class<T> cl) {
        if (cl != null) {
            try {
                return cl.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static <T extends BaseDialog>T  newDialog(Class<T> cl) {
        if (cl != null) {
            try {
                return cl.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
