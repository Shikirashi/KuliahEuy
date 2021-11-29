package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    private static final String TAG = "LoginLog";
    private TextView forgotPass, registerBtn;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private EditText inputEmail, inputPassword;
    private Button loginBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        forgotPass = findViewById(R.id.forgotPassword);
        registerBtn = findViewById(R.id.register);
        loginBtn = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        Intent intent = new Intent(Login.this, MainActivity.class);



        if(mUser != null){
            if(getIntent().hasExtra("notifID")){
                Log.d(TAG, "dapat id " + getIntent().getIntExtra("notifID", 0));
                intent.putExtra("EXP", 1);
            }
            else {
                Log.d(TAG, "tidak dapat ID");
            }
            startActivity(intent);
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String pass = inputPassword.getText().toString().trim();
                if (email.isEmpty()){
                    inputEmail.setError("Mohon masukkan email");
                    inputEmail.requestFocus();
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    inputEmail.setError("Alamat email tidak valid!");
                    inputEmail.requestFocus();
                    return;
                }
                else if (pass.isEmpty()){
                    inputPassword.setError("Mohon masukkan password");
                    inputPassword.requestFocus();
                    return;
                }
                else if (pass.length() < 8){
                    inputPassword.setError("Password minimal 8 karakter!");
                    inputPassword.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()){
                                Toast.makeText(Login.this, "Selamat datang", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(Login.this, MainActivity.class));
                                finish();
                            }
                            else {
                                user.sendEmailVerification();

                                new AlertDialog.Builder(Login.this)
                                        .setTitle("Verifikasi email")
                                        .setMessage("Silakan cek email Anda untuk verifikasi akun")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setIcon(R.drawable.ic_baseline_email_24)
                                        .show();
                            }
                        }
                        else {
                            Toast.makeText(Login.this, "Gagal login cek email/password Anda", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}