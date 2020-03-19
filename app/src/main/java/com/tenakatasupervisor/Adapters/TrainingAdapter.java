package com.tenakatasupervisor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.tenakatasupervisor.Activity.ActivityTrainingDetails;
import com.tenakatasupervisor.Models.TrainingDetailModel;
import com.tenakatasupervisor.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class TrainingAdapter extends RecyclerView.Adapter <TrainingAdapter.Holder>{
    Context context;
    ArrayList <TrainingDetailModel>list;
    public TrainingAdapter(Context context,ArrayList<TrainingDetailModel> list){
        this.list=list;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);

    }
    LayoutInflater layoutInflater;
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.training_recyclerview_items, parent, false);
        return new Holder(view);
    }

    @NonNull

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String title=list.get(position).getTitle();
        String subtitle=list.get(position).getSubtitle();
        String readmore=list.get(position).getReadmore();
        holder.readmore.setText(readmore);
        holder.subtitle.setText(subtitle);
        holder.title.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ActivityTrainingDetails.class);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView title,subtitle,readmore;
        ImageView videoimage;
        public Holder(@NonNull View itemView) {
            super(itemView);
            videoimage=(ImageView)itemView.findViewById(R.id.iv_video);

            title=(TextView)itemView.findViewById(R.id.tv_video_title);
            subtitle=(TextView)itemView.findViewById(R.id.tv_sub_title);
            readmore=(TextView)itemView.findViewById(R.id.tv_read_more);
        }
    }
}
