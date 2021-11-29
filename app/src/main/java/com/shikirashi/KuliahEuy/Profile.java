package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ProxyFileDescriptorCallback;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private static final String TAG = "ProfileLog";

    private Button logout, addFriend, friendList;
    private ImageView userIcon;
    private TextView displayUsername, displayEmail, displayLevel, displayExp;
    private EditText searchBar;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userID;
    private boolean isLoggedIn = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference userIconRef = storageReference.child("usersIcons/" + mAuth.getCurrentUser().getUid() + "/profileImage");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        logout = findViewById(R.id.logoutButton);
        addFriend = findViewById(R.id.addFriend);
        friendList = findViewById(R.id.friendsButton);
        searchBar = findViewById(R.id.search);
        userIcon = findViewById(R.id.userIcon);
        displayUsername = findViewById(R.id.displayUsername);
        displayEmail = findViewById(R.id.displayUserEmail);
        displayLevel = findViewById(R.id.userLevel);
        displayExp = findViewById(R.id.userExp);

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
            DocumentReference reference = db.collection("Users").document(userID);
            reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    Log.d("cekLogout", "user ID: " + userID);
                    Log.d("cekLogout", "display Username: " + displayUsername);
                    if (value != null && value.exists()) {
                        Log.d("cekLogout", "document exists");
                        displayUsername.setText(value.getString("username"));
                        displayEmail.setText(value.getString("email"));
                        displayLevel.setText("AR " + value.getLong("userLevel").toString());
                        displayExp.setText("EXP " + value.getLong("currExp").toString() + "/" + value.getLong("nextExp").toString());

                        final long ONE_MEGABYTE = 1024 * 1024;
                        userIconRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                userIcon.setImageBitmap(bmp);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(getApplicationContext(), "Foto profil tidak ditemukan", Toast.LENGTH_LONG).show();
                                userIcon.setImageResource(R.drawable.ic_baseline_person_32);
                            }
                        });

                    }
                }
            });
        }

        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Friendlist.class));
                finish();
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchStr = searchBar.getText().toString().trim();
                Log.d(TAG, "Search string is: " + searchStr);

                db.collection("Users").document(mAuth.getUid()).collection("friends").document("friendlist")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    List<String> friends = (List<String>) document.get("friends_uid");

                                    db.collection("Users").whereEqualTo("username", searchStr)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if (document.getString("username").equals(searchStr)) {
                                                                Log.d(TAG, "Temen ketemu");
                                                                friends.add(document.getString("user ID"));

                                                                Map<String, Object> friendData = new HashMap<>();
                                                                friendData.put("friends_uid", friends);

                                                                db.collection("Users").document(mAuth.getUid()).collection("friends").document("friendlist")
                                                                        .set(friendData);
                                                                Toast.makeText(Profile.this, "Berhasil menambahkan teman!", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(Profile.this, "Teman tidak ditemukan!", Toast.LENGTH_SHORT).show();
                                                                Log.d(TAG, "Temen ngga ketemu, yang ketemu: " + document.getString("username"));
                                                            }
                                                        }
                                                    }

                                                }
                                            });
                                }
                            }
                        });


//                db.collection("Users").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if(task.isSuccessful()){
//                            DocumentSnapshot snap = task.getResult();
//                            if(snap.getString("username") == searchStr){
//                                Log.d(TAG, "Ketemu temen");
//                            }
//                            else {
//                                Log.d(TAG, "Temen ngga ketemu");
//                            }
//                        }
//                    }
//                });
            }
        });

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
                switch (item.getItemId()) {
                    case R.id.alarm:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.tugasku:
                        startActivity(new Intent(getApplicationContext(), Tugasku.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.about:
                        return true;
                }
                return false;
            }
        });
    }
}