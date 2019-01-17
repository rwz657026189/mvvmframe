package com.rwz.basemodule.help;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.rwz.basemodule.entity.NotificationEntity;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by rwz on 2017/4/14.
 */

public class NotificationHelp {

   private static final Set<Integer> notificationSet = new HashSet<>();

    public static final int VERSION_APK_ID = 1; //版本升级

    public static NotificationCompat.Builder addNotification(Context context, NotificationEntity entity) {
        if (context != null) {
            return new NotificationCompat.Builder(context)
                    .setSmallIcon(entity.getIcon())
                    .setContentTitle(entity.getTitle())
                    .setProgress(100, 0, false)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);
        }
        return null;
    }

    /*public static PendingIntent createIntent(BaseActivity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, InstallTempActivity.class);
            intent.putExtra(BaseKey.STRING, Path.NEW_APK_PATH);
            TaskStackBuilder stack = TaskStackBuilder.create(activity);
            stack.addNextIntent(intent);
            return stack.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return null;
    }*/

    public static void addNotifycationID(int id) {
        notificationSet.add(id);
    }

}
