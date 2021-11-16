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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    RecyclerView seninList, selasaList;
    ImageButton addnew;
    String items[], desc[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = getResources().getStringArray(R.array.items);
        desc = getResources().getStringArray(R.array.desc);

        addnew = findViewById(R.id.addnew);
        seninList = findViewById(R.id.seninAlarm);
        selasaList = findViewById(R.id.selasaAlarm);

        AlarmAdapter alarmAdapter = new AlarmAdapter(this, items, desc);
        seninList.setAdapter(alarmAdapter);
        seninList.setLayoutManager(new LinearLayoutManager(this));
        alarmAdapter = new AlarmAdapter(this, desc, items);
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
}