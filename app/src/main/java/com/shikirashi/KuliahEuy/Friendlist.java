package com.shikirashi.KuliahEuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class Friendlist extends AppCompatActivity {

    private static final String TAG = "FriendlistLog";
    private ImageButton backBtn;
    private RecyclerView friend_list;

    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);

        backBtn = findViewById(R.id.backBtn);
        friend_list = findViewById(R.id.friendRecyclerView);

        db.collection("Users").document(mUser.getUid()).collection("friends").document("friendlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            List<String> friends = (List<String>) document.get("friends_uid");
                            AlarmAdapter adapter = new AlarmAdapter(Friendlist.this, friends);
                            friend_list.setAdapter(adapter);
                            friend_list.setLayoutManager(new LinearLayoutManager(Friendlist.this));
                        }
                    }
                });



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Friendlist.this, Profile.class));
                finish();
            }
        });

    }

    public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

        List<String> data1;
        Context contextGlobal;

        public  AlarmAdapter(Context context, List<String> items){
            contextGlobal = context;
            data1 = items;
        }

        @NonNull
        @Override
        public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(contextGlobal);
            View view = inflater.inflate(R.layout.friend_rows, parent, false);
            return new AlarmViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
            db.collection("Users").document(data1.get(position))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot snapshot = task.getResult();
                                Log.d(TAG, "username is " + snapshot.getString("username"));
                                holder.title.setText(snapshot.getString("username"));
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return data1.size();
        }

        public class AlarmViewHolder extends RecyclerView.ViewHolder{

            TextView title;

            public AlarmViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.namaTeman);
            }
        }
    }

}