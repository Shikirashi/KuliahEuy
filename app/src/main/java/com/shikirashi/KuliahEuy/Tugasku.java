package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class Tugasku extends AppCompatActivity {


    private static final String TAG = "TugaskuActivity";
    private TugasAdapter tugasAdapter;
    private TugasBackAdapter tugasBackAdapter;
    RecyclerView tugasList, tugasBack;
    ImageButton addnew, testNotif;

    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugasku);

        tugasList = findViewById(R.id.tugasList);
        tugasBack = findViewById(R.id.tugasBack);
        addnew = findViewById(R.id.addnew);
        testNotif = findViewById(R.id.testNotif);

        String userID = mUser.getUid();
        CollectionReference ItemsRef = db.collection("Users").document(userID).collection("tugas");

        Query query = db.collection("Users").document(userID).collection("tugas").whereEqualTo("ifDone", false);
        FirestoreRecyclerOptions<Tugas> options = new FirestoreRecyclerOptions.Builder<Tugas>().setQuery(query, Tugas.class).build();
        tugasBackAdapter = new TugasBackAdapter(options);

        tugasBack.setLayoutManager(new LinearLayoutManager(this));
        tugasBack.setAdapter(tugasBackAdapter);

        tugasAdapter = new TugasAdapter(options);

        tugasList.setLayoutManager(new LinearLayoutManager(this));
        tugasList.setAdapter(tugasAdapter);

        testNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Tugasku.this, TempNew.class));
                finish();
            }
        });

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("addnew", "Clicked on addnew button");
                startActivity(new Intent(Tugasku.this, UploadTugas.class));
                finish();
            }
        });

        ItemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    //Toast.makeText(Tugasku.this, "Berhasil fetch tugas!", Toast.LENGTH_SHORT).show();
                }else {
                    //Toast.makeText(Tugasku.this, "Data tugas tidak tersedia!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Tugasku.this, "Gagal retrieve tugas!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                tugasAdapter.markAsDone(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(tugasList);

        BottomNavigationView bottomNavigationView = findViewById(R.id.botNav);
        bottomNavigationView.setSelectedItemId(R.id.tugasku);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.alarm:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.tugasku:
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        tugasAdapter.stopListening();
        tugasBackAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tugasAdapter.startListening();
        tugasBackAdapter.startListening();
    }
}