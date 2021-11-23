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
    //private FirestoreRecyclerAdapter alarmAdapter;
    private FirestoreRecyclerAdapter seninAdapter, selasaAdapter, rabuAdapter, kamisAdapter, jumatAdapter, sabtuAdapter, mingguAdapter;
    RecyclerView seninList, selasaList, rabuList, kamisList, jumatList, sabtuList, mingguList;
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
        rabuList = findViewById(R.id.rabuAlarm);
        kamisList = findViewById(R.id.kamisAlarm);
        jumatList = findViewById(R.id.jumatAlarm);
        sabtuList = findViewById(R.id.sabtuAlarm);
        mingguList = findViewById(R.id.mingguAlarm);

        Query senin = db.collection("Users").document(userID).collection("Senin");
        FirestoreRecyclerOptions<JadwalModel> seninOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(senin, JadwalModel.class).build();

        seninAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(seninOptions) {
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

        seninList.setAdapter(seninAdapter);
        seninList.setLayoutManager(new LinearLayoutManager(this));

        Query selasa = db.collection("Users").document(userID).collection("Selasa");
        FirestoreRecyclerOptions<JadwalModel> selasaOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(selasa, JadwalModel.class).build();

        selasaAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(selasaOptions) {
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

        selasaList.setAdapter(selasaAdapter);
        selasaList.setLayoutManager(new LinearLayoutManager(this));

        Query rabu = db.collection("Users").document(userID).collection("Rabu");
        FirestoreRecyclerOptions<JadwalModel> rabuOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(rabu, JadwalModel.class).build();

        rabuAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(rabuOptions) {
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

        rabuList.setAdapter(rabuAdapter);
        rabuList.setLayoutManager(new LinearLayoutManager(this));

        Query kamis = db.collection("Users").document(userID).collection("Kamis");
        FirestoreRecyclerOptions<JadwalModel> kamisOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(kamis, JadwalModel.class).build();

        kamisAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(kamisOptions) {
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

        kamisList.setAdapter(kamisAdapter);
        kamisList.setLayoutManager(new LinearLayoutManager(this));

        Query jumat = db.collection("Users").document(userID).collection("Jumat");
        FirestoreRecyclerOptions<JadwalModel> jumatOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(jumat, JadwalModel.class).build();

        jumatAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(jumatOptions) {
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

        jumatList.setAdapter(jumatAdapter);
        jumatList.setLayoutManager(new LinearLayoutManager(this));

        Query sabtu = db.collection("Users").document(userID).collection("Sabtu");
        FirestoreRecyclerOptions<JadwalModel> sabtuOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(sabtu, JadwalModel.class).build();

        sabtuAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(sabtuOptions) {
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

        sabtuList.setAdapter(sabtuAdapter);
        sabtuList.setLayoutManager(new LinearLayoutManager(this));

        Query minggu = db.collection("Users").document(userID).collection("Minggu");
        FirestoreRecyclerOptions<JadwalModel> mingguOptions = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(minggu, JadwalModel.class).build();

        mingguAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(mingguOptions) {
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

        mingguList.setAdapter(mingguAdapter);
        mingguList.setLayoutManager(new LinearLayoutManager(this));

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