package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLog";
    private FirestoreRecyclerAdapter alarmAdapter;
    RecyclerView seninList, selasaList;
    ImageButton addnew;

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ItemsRef = db.collection("Users").document(mUser.getUid()).collection("jadwal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userID = mUser.getUid();

        addnew = findViewById(R.id.addnew);
        seninList = findViewById(R.id.seninAlarm);
        selasaList = findViewById(R.id.selasaAlarm);

        Query query = db.collection("Users").document(userID).collection("jadwal");
        FirestoreRecyclerOptions<JadwalModel> options = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(query, JadwalModel.class).build();

        alarmAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JadwalViewHolder holder, int position, @NonNull JadwalModel model) {
                holder.judul.setText(model.getJudul());
                holder.desc.setText(model.getDesc());
            }

            @NonNull
            @Override
            public JadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_rows, parent, false);
                return new JadwalViewHolder(view);
            }
        };

        ItemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    //Toast.makeText(MainActivity.this, "Berhasil fetch data!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Data tidak tersedia!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Gagal retrieve data!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
        seninList.setAdapter(alarmAdapter);
        seninList.setLayoutManager(new LinearLayoutManager(this));
        selasaList.setAdapter(alarmAdapter);
        selasaList.setLayoutManager(new LinearLayoutManager(this));

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
                startActivity(new Intent(MainActivity.this, TempNew.class));
                finish();
            }
        });
    }

    private class JadwalViewHolder extends RecyclerView.ViewHolder {

        private TextView judul, desc;

        public JadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.alarmRecycleTitle);
            desc = itemView.findViewById(R.id.alarmRecycleDesc);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        alarmAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        alarmAdapter.startListening();
    }
}