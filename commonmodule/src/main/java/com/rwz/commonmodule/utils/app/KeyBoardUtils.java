package com.rwz.commonmodule.utils.app;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 *
 * @author zhy
 */
public class KeyBoardUtils {
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        if (mEditText == null || mContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm == null)
            return;
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext  上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        if (mEditText == null || mContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm == null)
            return;
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }


    /**
     * 广点通爬下了的
     * 隐藏软键盘，这只是个简单的隐藏软键盘示例实现，与广告sdk功能无关
     * @param context
     */
    public static void hideSoftInput(Activity context) {
        if(context == null)
            return;
        View currentFocus = context.getCurrentFocus();
        if (currentFocus == null || currentFocus.getWindowToken() == null) {
            return;
        }
        InputMethodManager systemService = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(systemService == null)
            return;
        systemService.hideSoftInputFromWindow(
                currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}  