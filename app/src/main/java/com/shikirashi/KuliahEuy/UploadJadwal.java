package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UploadJadwal extends AppCompatActivity{

    ImageButton backBtn;

    private static final String TAG = "UploadJadwalLog";
    private static final String KEY_TITLE = "judul";
    private static final String KEY_DESC = "desc";
    private static final String KEY_DAY = "hari";

    private EditText editTitle, editDesc;
    private Button saveBtn;
    private Spinner spinner;
    private String selectedDay;

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_jadwal);

        backBtn = findViewById(R.id.backBtn);
        editTitle = findViewById(R.id.editTitle);
        editDesc = findViewById(R.id.editDesc);
        saveBtn = findViewById(R.id.saveBtn);
        spinner = findViewById(R.id.hariSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hari, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDay = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedDay = "Senin";
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
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

    private void saveNote(){
        String title = editTitle.getText().toString();
        String desc = editDesc.getText().toString();

        Map<String, Object> jadwal = new HashMap<>();
        jadwal.put(KEY_TITLE, title);
        jadwal.put(KEY_DESC, desc);
        jadwal.put(KEY_DAY, selectedDay);

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
    }
}