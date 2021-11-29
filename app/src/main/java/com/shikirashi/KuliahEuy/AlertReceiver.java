package com.shikirashi.KuliahEuy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    Integer id;
    String title, message;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.hasExtra("ID")){
            id = intent.getIntExtra("ID", 1);
            title = intent.getStringExtra("title");
            message = intent.getStringExtra("message");
        }


        NotificationHelper helper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = helper.getChannelNotif();
        NotificationCompat.Builder nb = helper.getChannel1Notif(title, message);
        helper.getManager().notify(id, nb.build());
    }
}
