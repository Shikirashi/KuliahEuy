package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile extends AppCompatActivity {

    private static final String TAG = "About";

    private Button logout;
    private TextView displayUsername, displayEmail;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID;
    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        logout = findViewById(R.id.logoutButton);
        displayUsername = findViewById(R.id.displayUsername);
        displayEmail = findViewById(R.id.displayUserEmail);

        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
            DocumentReference reference = db.collection("Users").document(userID);
//            reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                            isLoggedIn = true;
//                        } else {
//                            Log.d(TAG, "No such document");
//                            isLoggedIn = false;
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                        isLoggedIn = false;
//                    }
//                    Log.d(TAG, "isLoggedIn: " + isLoggedIn);
//                }
//            });
            reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    Log.d("cekLogout", "user ID: " + userID);
                    Log.d("cekLogout", "display Username: " + displayUsername);
                    if (value != null && value.exists()){
                        Log.d("cekLogout", "document exists");
                        displayUsername.setText(value.getString("username"));
                        displayEmail.setText(value.getString("email"));
                    }
                }
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cekLogout", "user ID 2: " + userID);
                mAuth.signOut();
                Log.d("cekLogout", "user ID 3: " + userID);
                startActivity(new Intent(Profile.this, Login.class));
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.botNav);
        bottomNavigationView.setSelectedItemId(R.id.about);
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
                        startActivity(new Intent(getApplicationContext(), Tugasku.class));
                        finish();
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        return true;
                }
                return false;
            }
        });
    }
}