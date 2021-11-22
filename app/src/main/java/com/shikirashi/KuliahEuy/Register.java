package com.shikirashi.KuliahEuy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private TextView loginBtn;
    private FirebaseAuth mAuth;
    private EditText inputUsername, inputEmail, inputPassword;
    private Button registerUser;
    private ProgressBar progressBar;
    private String userID;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.login);
        registerUser = findViewById(R.id.registerButton);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        progressBar = findViewById(R.id.progressBar);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (username.isEmpty()){
                    inputUsername.setError("Mohon masukkan username");
                    inputUsername.requestFocus();
                    return;
                }
                else if (email.isEmpty()){
                    inputEmail.setError("Mohon masukkan email");
                    inputEmail.requestFocus();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    inputEmail.setError("Alamat email tidak valid!");
                    inputEmail.requestFocus();
                    return;
                }
                else if (password.isEmpty()){
                    inputPassword.setError("Mohon masukkan password");
                    inputPassword.requestFocus();
                    return;
                }
                else if (password.length() < 8){
                    inputPassword.setError("Password minimal 8 karakter!");
                    inputPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(username, email);
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference users = db.collection("Users").document(userID);
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("username", username);
                            userData.put("email", email);
                            userData.put("user ID", userID);
                            users.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user created for " + username + " with user ID " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            currentUser.sendEmailVerification();
                            Toast.makeText(Register.this, "Silakan cek email Anda untuk verifikasi", Toast.LENGTH_SHORT).show();

                            progressBar.setVisibility(View.GONE);
                        }
                        else {
                            Toast.makeText(Register.this, "User gagal dibuat", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

    }
}