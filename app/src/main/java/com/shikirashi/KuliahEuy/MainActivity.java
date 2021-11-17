package com.shikirashi.KuliahEuy;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirestoreRecyclerAdapter alarmAdapter;
    RecyclerView seninList, selasaList;
    ImageButton addnew;
    String items[], desc[];

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ItemsRef = db.collection("Note").document("items").collection("content");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = getResources().getStringArray(R.array.items);
        desc = getResources().getStringArray(R.array.desc);

        addnew = findViewById(R.id.addnew);
        seninList = findViewById(R.id.seninAlarm);
        selasaList = findViewById(R.id.selasaAlarm);

        Query query = db.collection("Note").document("items").collection("content");
        FirestoreRecyclerOptions<JadwalModel> options = new FirestoreRecyclerOptions.Builder<JadwalModel>().setQuery(query, JadwalModel.class).build();

        alarmAdapter = new FirestoreRecyclerAdapter<JadwalModel, JadwalViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull JadwalViewHolder holder, int position, @NonNull JadwalModel model) {
                holder.title.setText(model.getTitle());
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

                }else {
                    Toast.makeText(MainActivity.this, "Collection does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Save failed!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });
        seninList.setHasFixedSize(true);
        seninList.setAdapter(alarmAdapter);
        seninList.setLayoutManager(new LinearLayoutManager(this));
        //alarmAdapter = new AlarmAdapter(this, desc, items);
        selasaList.setHasFixedSize(true);
        selasaList.setAdapter(alarmAdapter);
        selasaList.setLayoutManager(new LinearLayoutManager(this));

//        AlarmAdapter alarmAdapter = new AlarmAdapter(this, items, desc);
//        seninList.setAdapter(alarmAdapter);
//        seninList.setLayoutManager(new LinearLayoutManager(this));
//        alarmAdapter = new AlarmAdapter(this, desc, items);
//        selasaList.setAdapter(alarmAdapter);
//        selasaList.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNavigationView = findViewById(R.id.botNav);
        bottomNavigationView.setSelectedItemId(R.id.alarm);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.alarm:
                        return true;

                    case R.id.tugasku:
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(),FirebaseExample.class));
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

        private TextView title, desc;

        public JadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.alarmRecycleTitle);
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