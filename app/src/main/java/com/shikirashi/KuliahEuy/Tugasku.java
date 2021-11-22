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

public class Tugasku extends AppCompatActivity {

    private static final String TAG = "TugaskuActivity";
    private FirestoreRecyclerAdapter tugasAdapter;
    RecyclerView tugasList;
    ImageButton addnew;

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ItemsRef = db.collection("Note").document("items").collection("tugas");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tugasku);

        tugasList = findViewById(R.id.tugasList);
        addnew = findViewById(R.id.addnew);

        String userID = mUser.getUid();

        Query query = db.collection("Users").document(userID).collection("tugas");
        FirestoreRecyclerOptions<TugasModel> options = new FirestoreRecyclerOptions.Builder<TugasModel>().setQuery(query, TugasModel.class).build();

        tugasAdapter = new FirestoreRecyclerAdapter<TugasModel, TugasViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TugasViewHolder holder, int position, @NonNull TugasModel model) {
                holder.judul.setText(model.getJudul());
                holder.deadline.setText(model.getDeadline());
            }

            @NonNull
            @Override
            public TugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tugas_rows, parent, false);
                return new TugasViewHolder(view);
            }
        };

        ItemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                }else {
                    Toast.makeText(Tugasku.this, "Data tugas tidak tersedia!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Tugasku.this, "Gagal retrieve tugas!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        tugasList.setAdapter(tugasAdapter);
        tugasList.setLayoutManager(new LinearLayoutManager(this));

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

    private class TugasViewHolder extends RecyclerView.ViewHolder {

        private TextView judul, deadline;

        public TugasViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.tugasRecycleTitle);
            deadline = itemView.findViewById(R.id.tugasRecycleDesc);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        tugasAdapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        tugasAdapter.startListening();
    }
}