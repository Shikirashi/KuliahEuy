package com.shikirashi.KuliahEuy;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    String data1[], data2[];
    Context contextGlobal;

    public  AlarmAdapter(Context context, String items[], String desc[]){
        contextGlobal = context;
        data1 = items;
        data2 = desc;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contextGlobal);
        View view = inflater.inflate(R.layout.alarm_rows, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        holder.title.setText(data1[position]);
        holder.desc.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder{

        TextView title, desc;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.alarmRecycleTitle);
            desc = itemView.findViewById(R.id.alarmRecycleDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //itemView.getContext()
                    Log.d("cardClick", "clicked on a card");
                    Toast.makeText(contextGlobal.getApplicationContext(), "Clicked on a card", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
