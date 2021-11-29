package com.shikirashi.KuliahEuy;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    Integer id;
    String title, message;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("notifID")){
            id = intent.getIntExtra("notifID", 1);
            title = intent.getStringExtra("title");
            message = intent.getStringExtra("message");
        }


        NotificationHelper helper = new NotificationHelper(context);
        helper.id = id;
        NotificationCompat.Builder nb = helper.getChannel1Notif(title, message);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, Login.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        nb.setContent(contentIntent);
        helper.getManager().notify(id, nb.build());
    }
}
