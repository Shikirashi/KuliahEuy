package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class UploadTugas extends AppCompatActivity {
    private EditText editTitle, editDesc;
    private Calendar date, cal;
    private List<String> titles;
    private List<Date> dates;
    private List<Long> ids;
    private Integer delay;

    private static final String TAG = "UploadTugasLog";
    private static final String KEY_TITLE = "Judul";
    private static final String KEY_DESC = "Deskripsi";
    private static final String KEY_DONE = "ifDone";
    private static final String KEY_DEAD = "Deadline";
    private static final String KEY_ID = "NotifID";

    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_tugas);

        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);
        ImageButton backBtn = findViewById(R.id.backBtn);
        Button saveBtn = findViewById(R.id.saveBtn);
        Button pickTime = findViewById(R.id.pickTime);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    editTitle.setError("Mohon masukkan judul mata kuliah");
                    editTitle.requestFocus();
                    return;
                } else if (date == null) {
                    Toast.makeText(UploadTugas.this, "Mohon masukkan jam mata kuliah", Toast.LENGTH_SHORT).show();
                    pickTime.setError("Mohon masukkan jam mata kuliah");
                    pickTime.requestFocus();
                    return;
                } else {
                    saveNote();
                }
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadTugas.this, Tugasku.class));
                finish();
            }
        });
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(UploadTugas.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(UploadTugas.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        Log.d(TAG, "Time chosen:  " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        Date deadline = date.getTime();
        cal = Calendar.getInstance();
        Integer id = Long.valueOf(cal.getTimeInMillis()).intValue();

        Map<String, Object> jadwal = new HashMap<>();
        jadwal.put(KEY_TITLE, title);
        jadwal.put(KEY_DESC, desc);
        jadwal.put(KEY_DONE, false);
        jadwal.put(KEY_DEAD, deadline);
        jadwal.put(KEY_ID, id);

        db.collection("Users").document(mUser.getUid()).collection("tugas").document().set(jadwal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UploadTugas.this, "Save successful!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Upload successful");
                        scheduleNotifications();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadTugas.this, "Save failed!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scheduleNotifications() {
        db.collection("Users").document(mUser.getUid()).collection("tugas").whereEqualTo("ifDone", false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            titles = new ArrayList<>();
                            dates = new ArrayList<>();
                            ids = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                titles.add(document.getString("Judul"));
                                dates.add(document.getDate("Deadline"));
                                ids.add(document.getLong("NotifID"));

                                db.collection("Users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            delay = document.getLong("delayTugas").intValue();
                                        }
                                        DocumentReference users = db.collection("Users").document(mUser.getUid());
                                        Map<String, Object> userData = new HashMap<>();
                                        delay = 1200000;
                                        userData.put("delayTugas", delay);
                                        users.set(userData);
                                    }
                                });

                            }
                            Log.d(TAG, "Retrieved: " + titles.toString());
                            Log.d(TAG, "Retrieved: " + dates.toString());
                            Log.d(TAG, "Scheduling notifications...");
                            Log.d(TAG, "titles size is: " + titles.size());

                            for (int i = 0; i < titles.size(); i++) {
                                Log.d(TAG, "Canceling previous notifications...");

                                Intent notificationIntent = new Intent(UploadTugas.this, AlertReceiver.class);
                                notificationIntent.putExtra("ID", ids.get(i).intValue());
                                notificationIntent.putExtra("title", "KuliahEuy");
                                notificationIntent.putExtra("message", titles.get(i));
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(UploadTugas.this, ids.get(i).intValue(), notificationIntent, 0);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                alarmManager.cancel(pendingIntent);
                                Log.d("UploadTugasLog", "Alarm " + titles.get(i) + " canceled");
                            }

                            for (int i = 0; i < titles.size(); i++) {
                                Log.d(TAG, "Starting...");
                                SimpleDateFormat format = new SimpleDateFormat("MMMM dd, yyyy HH:mm:ss z");
                                Date dated = dates.get(i);


                                Intent notificationIntent = new Intent(UploadTugas.this, AlertReceiver.class);
                                notificationIntent.putExtra("ID", ids.get(i).intValue());
                                notificationIntent.putExtra("title", "KuliahEuy");
                                notificationIntent.putExtra("message", titles.get(i));
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(UploadTugas.this, ids.get(i).intValue(), notificationIntent, 0);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                //Debug. Schedule at 2, 4, 6 minutes.

                                if(dated.before(cal.getTime())){
                                    dated = new Date(dated.getTime() + (1000 * 3600 * 24));
                                }

                                dated = new Date(dated.getTime() - delay);
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, dated.getTime(), pendingIntent);
                                Log.d("UploadTugasLog", "Date set is: " + dated);
                                startActivity(new Intent(UploadTugas.this, Tugasku.class));
                                finish();
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                }
        });
    }


//    public void sendNotifChannel1(String title, String message) {
//        NotificationCompat.Builder nb = helper.getChannel1Notif(title, message);
//        helper.getManager().notify(1, nb.build());
//    }
//
//    private void startAlarm(Calendar c) {
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pending = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        if (c.before(Calendar.getInstance())) {
//            c.add(Calendar.DATE, 1);
//        }
//
//        alarm.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pending);
//    }
//
//    private void cancelAlarm() {
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlertReceiver.class);
//        PendingIntent pending = PendingIntent.getBroadcast(this, 1, intent, 0);
//
//        alarm.cancel(pending);
//    }


//    private Notification getNotification(String rocketName, String padName) {
//        Notification.Builder builder = new Notification.Builder(this);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
//        builder.setContentIntent(pendingIntent);
//        builder.setContentTitle("Upcoming Launch");
//        builder.setContentText("A launch of a " + rocketName + " is about to occur at " + padName + ". Click for more info.");
//        builder.setSmallIcon(R.drawable.rocket_icon);
//        return builder.build();
//    }
}