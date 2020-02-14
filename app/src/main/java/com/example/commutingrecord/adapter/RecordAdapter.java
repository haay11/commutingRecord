package com.example.commutingrecord.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commutingrecord.R;
import com.example.commutingrecord.holder.RecordViewHolder;
import com.example.commutingrecord.vo.UserTimeRecord;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {

    private Context context;
    private List<UserTimeRecord> data;

    public RecordAdapter (List<UserTimeRecord> data, Context context){
        this.context = context;
        this.data = data;
    }
    public void clear(){
        if (data!=null) {
            data.clear();
        }
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        ));
        RecordViewHolder viewHolder = new RecordViewHolder(view);
        return viewHolder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordViewHolder viewHolder = holder;
        viewHolder.name.setText(data.get(position).getName());
        if (data.get(position).getStartTime()==null && data.get(position).getEndTime()==null){
            viewHolder.startTime.setText("-");
            viewHolder.endTime.setText("-");
        } else if (data.get(position).getStartTime()!=null && data.get(position).getEndTime()==null){
            viewHolder.startTime.setText(data.get(position).getStartTime());
            viewHolder.endTime.setText("근무중");
            viewHolder.endTime.setTextColor(R.color.dark_beige);
        } else if (data.get(position).getStartTime()!=null && data.get(position).getEndTime()!=null){
            viewHolder.startTime.setText(data.get(position).getStartTime());
            viewHolder.endTime.setText(data.get(position).getEndTime());
        }

    }

    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }


}
