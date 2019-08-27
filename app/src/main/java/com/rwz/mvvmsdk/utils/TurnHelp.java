package com.rwz.mvvmsdk.utils;

import android.content.Context;
import android.content.Intent;

import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.entity.WebEntity;
import com.rwz.commonmodule.help.CheckHelp;
import com.rwz.mvvmsdk.ui.activity.CommWebActivity;

/**
 * date： 2019/7/5 10:15
 * author： rwz
 * description：
 **/
public class TurnHelp {

    private static boolean checkParams(Context context) {
        return context != null && CheckHelp.checkTurnTime();
    }

    public static void h5(Context context, String url) {
        if(!checkParams(context))
            return;
        Intent intent = new Intent(context, CommWebActivity.class);
        WebEntity entity = new WebEntity(url);
        intent.putExtra(BaseKey.PARCELABLE_ENTITY, entity);
        context.startActivity(intent);
    }

}
