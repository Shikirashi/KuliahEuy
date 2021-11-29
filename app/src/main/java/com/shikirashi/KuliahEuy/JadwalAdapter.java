package com.shikirashi.KuliahEuy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;

public class JadwalAdapter extends FirestoreRecyclerAdapter<Jadwal, JadwalAdapter.JadwalHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnItemClickListener onItemClickListener;

    public JadwalAdapter(@NonNull FirestoreRecyclerOptions<Jadwal> options) {
        super(options);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull JadwalHolder holder, int position, @NonNull Jadwal model) {
        holder.title.setText(model.getJudul());
        SimpleDateFormat sfd = new SimpleDateFormat(" HH:mm");
        String strDate = sfd.format(model.getWaktu());
        holder.desc.setText(strDate);
    }

    @NonNull
    @Override
    public JadwalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_rows, parent, false);
        return new JadwalHolder(v);
    }

    public class JadwalHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView desc;

        public JadwalHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.alarmRecycleTitle);
            desc = itemView.findViewById(R.id.alarmRecycleDesc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && onItemClickListener != null){
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(pos),(pos));
                        Intent i = new Intent (itemView.getContext(), UploadJadwal.class);
                        i.putExtra("title", title.getText());
                        i.putExtra("desc", desc.getText());
                        //itemView.getContext().startActivity(i);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
