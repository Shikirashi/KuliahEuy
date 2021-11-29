package com.shikirashi.KuliahEuy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;

public class TugasBackAdapter extends FirestoreRecyclerAdapter<Tugas, TugasBackAdapter.TugasHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public TugasBackAdapter(@NonNull FirestoreRecyclerOptions<Tugas> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TugasHolder holder, int position, @NonNull Tugas model) {
        holder.judul.setText(model.getJudul());
        holder.desc.setText(model.getDeskripsi());
        SimpleDateFormat sfd = new SimpleDateFormat(" HH:mm dd-MM-yyyy");
        String strDate = sfd.format(model.getDeadline());
        holder.deadline.setText(strDate);
    }

    @NonNull
    @Override
    public TugasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tugas_back_rows, parent, false);
        return new TugasHolder(v);
    }

    class TugasHolder extends RecyclerView.ViewHolder {

        private TextView judul, desc, deadline;

        public TugasHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.tugasRecycleTitle);
            desc = itemView.findViewById(R.id.tugasRecycleDesc);
            deadline = itemView.findViewById(R.id.tugasRecycleTime);
        }
    }
}
