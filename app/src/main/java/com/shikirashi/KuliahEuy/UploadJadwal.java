package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadJadwal extends AppCompatActivity{

    ImageButton backBtn;

    private static final String TAG = "UploadJadwalLog";
    private static final String KEY_TITLE = "judul";
    private static final String KEY_DESC = "desc";
    private static final String KEY_DAY = "hari";
    private static final String KEY_TIME = "waktu";
    private static final String KEY_ID = "notifID";


    private List<String> titles;
    private List<Date> dates;
    private List<Long> ids;
    private Long delay;

    private EditText editTitle, editDesc;
    private Button saveBtn, pickTime, delBtn;
    private Spinner spinner;
    private String selectedDay, ID, hari;
    private Calendar date, cal;

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_jadwal);

        date = Calendar.getInstance();

        backBtn = findViewById(R.id.backBtn);
        delBtn = findViewById(R.id.delBtn);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);
        saveBtn = findViewById(R.id.saveBtn);
        spinner = findViewById(R.id.hariSpinner);
        pickTime = findViewById(R.id.pickTime);

        delBtn.setVisibility(View.GONE);

        if(getIntent().hasExtra("judul")){
            editTitle.setText(getIntent().getStringExtra("judul"));
            editDesc.setText(getIntent().getStringExtra("desc"));
        }

        if(getIntent().hasExtra("ID")){
            ID = getIntent().getStringExtra("ID");
            hari = getIntent().getStringExtra("hari");
            delBtn.setVisibility(View.VISIBLE);
        }

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDocument();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hari, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDay = adapterView.getItemAtPosition(i).toString();
                date.set(Calendar.DAY_OF_WEEK, i + 2);
                Log.d(TAG, "set hari is: " + date.getTime().toString() + " with index " + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedDay = "Senin";
            }
        });

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String desc = editDesc.getText().toString().trim();

                if(title.isEmpty()){
                    editTitle.setError("Mohon masukkan judul mata kuliah");
                    editTitle.requestFocus();
                    return;
                }
                else if(desc.isEmpty()){
                    editDesc.setError("Mohon masukkan jam mata kuliah");
                    editDesc.requestFocus();
                    return;
                }
                else {
                    saveNote();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadJadwal.this, MainActivity.class));
                finish();
            }
        });
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();

        new TimePickerDialog(UploadJadwal.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                Log.d(TAG, "Time chosen:  " + date.getTime());
            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

//        new DatePickerDialog(UploadJadwal.this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                date.set(year, monthOfYear, dayOfMonth);
//                new TimePickerDialog(UploadJadwal.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                        date.set(Calendar.MINUTE, minute);
//                        Log.d(TAG, "Time chosen:  " + date.getTime());
//                    }
//                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
//            }
//        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }


    private void saveNote(){
        String title = editTitle.getText().toString().trim();
        String desc = editDesc.getText().toString().trim();
        Date waktu = date.getTime();
        cal = Calendar.getInstance();
        Integer id = Long.valueOf(cal.getTimeInMillis()).intValue();

        Map<String, Object> jadwal = new HashMap<>();
        jadwal.put(KEY_TITLE, title);
        jadwal.put(KEY_DESC, desc);
        jadwal.put(KEY_DAY, selectedDay);
        jadwal.put(KEY_TIME, waktu);
        jadwal.put(KEY_ID, id);

        db.collection("Users").document(mUser.getUid()).collection(selectedDay).document().set(jadwal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UploadJadwal.this, "Save successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadJadwal.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadJadwal.this, "Save failed!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        if(getIntent().hasExtra("ID")){
            deleteDocument();
        }
    }

    private void deleteDocument(){
        DocumentReference ref = db.collection("Users").document(mUser.getUid()).collection(hari).document(ID);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        ref.delete();
                        startActivity(new Intent(UploadJadwal.this, MainActivity.class));
                        finish();
                    }
                }
                else {
                    Log.d(TAG, task.getException().toString());
                }
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
                                titles.add(document.getString("judul"));
                                dates.add(document.getDate("waktu"));
                                ids.add(document.getLong("notifID"));

                                db.collection("Users").document(mUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot docu = task.getResult();
                                            delay = docu.getLong("delayJadwal");
                                            Log.d(TAG, "Retrieved: " + titles.toString());
                                            Log.d(TAG, "Retrieved: " + dates.toString());
                                            Log.d(TAG, "Scheduling notifications...");
                                            Log.d(TAG, "titles size is: " + titles.size());

                                            for (int i = 0; i < titles.size(); i++) {
                                                Log.d(TAG, "Canceling previous notifications...");

                                                Intent notificationIntent = new Intent(UploadJadwal.this, AlertReceiver.class);
                                                notificationIntent.putExtra("notifID", ids.get(i).intValue());
                                                notificationIntent.putExtra("title", "KuliahEuy");
                                                notificationIntent.putExtra("message", titles.get(i) + " akan segera dimulai! Ketuk notifikasi ini untuk dapat EXP!");
                                                PendingIntent pendingIntent = PendingIntent.getBroadcast(UploadJadwal.this, ids.get(i).intValue(), notificationIntent, 0);
                                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                                alarmManager.cancel(pendingIntent);
                                                Log.d("UploadTugasLog", "Alarm " + titles.get(i) + " canceled");
                                            }

                                            for (int i = 0; i < titles.size(); i++) {
                                                Log.d(TAG, "Starting...");
                                                Date dated = dates.get(i);

                                                Intent notificationIntent = new Intent(UploadJadwal.this, AlertReceiver.class);
                                                notificationIntent.putExtra("notifID", ids.get(i).intValue());
                                                notificationIntent.putExtra("title", "KuliahEuy");
                                                notificationIntent.putExtra("message", titles.get(i) + " akan segera dimulai! Ketuk notifikasi ini untuk dapat EXP!");
                                                PendingIntent pendingIntent = PendingIntent.getBroadcast(UploadJadwal.this, ids.get(i).intValue(), notificationIntent, 0);
                                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                                                dated = new Date(dated.getTime() - delay);
                                                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dated.getTime(), 1000 * 3600 * 24 * 7 , pendingIntent);
                                                Log.d("UploadTugasLog", "Date set is: " + dated);
                                                startActivity(new Intent(UploadJadwal.this, Tugasku.class));
                                                finish();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}