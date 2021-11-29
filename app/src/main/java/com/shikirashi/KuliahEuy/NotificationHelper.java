package com.shikirashi.KuliahEuy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID1 = "channel1ID";
    public static final String channel1Name = "channel1";
    public static final String channelID2 = "channel2";

    public int id;

    private NotificationManager nManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel channel1 = new NotificationChannel(channelID1, channel1Name, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.design_default_color_on_primary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager() {
        if (nManager == null) {
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nManager;
    }

    public NotificationCompat.Builder getChannel1Notif(String title, String message) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("notifID", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Builder(getApplicationContext(), channelID1)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_priority_high_24)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }

}
