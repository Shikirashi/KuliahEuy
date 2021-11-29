package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivityLog";
    private JadwalAdapter seninAdapter, selasaAdapter, rabuAdapter, kamisAdapter, jumatAdapter, sabtuAdapter, mingguAdapter;
    RecyclerView seninList, selasaList, rabuList, kamisList, jumatList, sabtuList, mingguList;
    ImageButton addnew;

    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userID = mUser.getUid();
        DocumentReference ItemsRef = db.collection("Users").document(userID);

        addnew = findViewById(R.id.addnew);
        seninList = findViewById(R.id.seninAlarm);
        selasaList = findViewById(R.id.selasaAlarm);
        rabuList = findViewById(R.id.rabuAlarm);
        kamisList = findViewById(R.id.kamisAlarm);
        jumatList = findViewById(R.id.jumatAlarm);
        sabtuList = findViewById(R.id.sabtuAlarm);
        mingguList = findViewById(R.id.mingguAlarm);

        db.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snap = task.getResult();
                    int currExp = snap.getLong("currExp").intValue();
                    int nextExp = snap.getLong("nextExp").intValue();
                    if(getIntent().hasExtra("EXP")){
                        Log.d(TAG, "Current EXP is: " + currExp);
                        int exp = getIntent().getIntExtra("EXP", 1);
                        db.collection("Users").document(userID).update("currExp", currExp + exp);
                        Log.d(TAG, "EXP is now: " + snap.getLong("currExp").intValue());
                    }

                    if(currExp >= nextExp){
                        db.collection("Users").document(userID).update("nextExp", nextExp + 10);
                    }
                }
            }
        });

        Query query = db.collection("Users").document(userID).collection("Senin").orderBy("waktu");
        FirestoreRecyclerOptions<Jadwal> options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        seninAdapter = new JadwalAdapter(options);
        seninAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        seninList.setLayoutManager(new LinearLayoutManager(this));
        seninList.setAdapter(seninAdapter);

        query = db.collection("Users").document(userID).collection("Selasa").orderBy("waktu");
        options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        selasaAdapter = new JadwalAdapter(options);
        selasaAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        selasaList.setLayoutManager(new LinearLayoutManager(this));
        selasaList.setAdapter(selasaAdapter);

        query = db.collection("Users").document(userID).collection("Rabu").orderBy("waktu");
        options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        rabuAdapter = new JadwalAdapter(options);
        rabuAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        rabuList.setLayoutManager(new LinearLayoutManager(this));
        rabuList.setAdapter(rabuAdapter);


        query = db.collection("Users").document(userID).collection("Kamis").orderBy("waktu");
        options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        kamisAdapter = new JadwalAdapter(options);
        kamisAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        kamisList.setLayoutManager(new LinearLayoutManager(this));
        kamisList.setAdapter(kamisAdapter);


        query = db.collection("Users").document(userID).collection("Jumat").orderBy("waktu");
        options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        jumatAdapter = new JadwalAdapter(options);
        jumatAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        jumatList.setLayoutManager(new LinearLayoutManager(this));
        jumatList.setAdapter(jumatAdapter);


        query = db.collection("Users").document(userID).collection("Sabtu").orderBy("waktu");
        options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        sabtuAdapter = new JadwalAdapter(options);
        sabtuAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        sabtuList.setLayoutManager(new LinearLayoutManager(this));
        sabtuList.setAdapter(sabtuAdapter);


        query = db.collection("Users").document(userID).collection("Minggu").orderBy("waktu");
        options = new FirestoreRecyclerOptions.Builder<Jadwal>().setQuery(query, Jadwal.class).build();

        mingguAdapter = new JadwalAdapter(options);
        mingguAdapter.setOnItemClickListener(new JadwalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                Log.d("RecycleClick", "Clicking recyclerview");
                Intent intent = new Intent(MainActivity.this, UploadJadwal.class);
                intent.putExtra("judul", snapshot.getString("judul"));
                intent.putExtra("desc", snapshot.getString("desc"));
                intent.putExtra("hari", snapshot.getString("hari"));
                intent.putExtra("ID", snapshot.getId());
                Log.d("RecycleClick", "Document title is: " + snapshot.getString("judul"));
                Log.d("RecycleClick", "Document desc is: " + snapshot.getString("desc"));
                Log.d("RecycleClick", "Document id is: " + snapshot.getId());
                startActivity(intent);
                finish();
            }
        });
        mingguList.setLayoutManager(new LinearLayoutManager(this));
        mingguList.setAdapter(mingguAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.botNav);
        bottomNavigationView.setSelectedItemId(R.id.alarm);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.alarm:
                        return true;

                    case R.id.tugasku:
                        startActivity(new Intent(getApplicationContext(), Tugasku.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        //finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        addnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("addnew", "Clicked on addnew button");
                startActivity(new Intent(MainActivity.this, UploadJadwal.class));
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        seninAdapter.stopListening();
        selasaAdapter.stopListening();
        rabuAdapter.stopListening();
        kamisAdapter.stopListening();
        jumatAdapter.stopListening();
        sabtuAdapter.stopListening();
        mingguAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        seninAdapter.startListening();
        selasaAdapter.startListening();
        rabuAdapter.startListening();
        kamisAdapter.startListening();
        jumatAdapter.startListening();
        sabtuAdapter.startListening();
        mingguAdapter.startListening();
    }
}