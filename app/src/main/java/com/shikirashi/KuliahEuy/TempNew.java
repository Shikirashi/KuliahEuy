package com.shikirashi.KuliahEuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.AlarmManagerCompat;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Calendar;

public class TempNew extends AppCompatActivity {

    ImageButton backBtn;
    Button saveBtn;

    private NotificationHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_new);

        backBtn = findViewById(R.id.backBtn);
        saveBtn = findViewById(R.id.saveBtn);
        helper = new NotificationHelper(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotifChannel1("Kuliah Euy", "Tes notif");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TempNew.this, MainActivity.class));
                finish();
            }
        });
    }

    public void sendNotifChannel1(String title, String message){
        NotificationCompat.Builder nb = helper.getChannel1Notif(title, message);
        helper.getManager().notify(1, nb.build());
    }

    private void startAlarm(Calendar c){
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 1, intent, 0);

        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        alarm.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pending);
    }

    private void cancelAlarm(){
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarm.cancel(pending);
    }
}