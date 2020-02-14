package com.example.commutingrecord.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commutingrecord.R;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    public TextView name, startTime, endTime, listLine;


    public RecordViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.recycle_list_name);
        startTime = itemView.findViewById(R.id.recycle_list_time);
        endTime = itemView.findViewById(R.id.recycle_list_time2);
        listLine = itemView.findViewById(R.id.recycle_list_line);
    }
}
