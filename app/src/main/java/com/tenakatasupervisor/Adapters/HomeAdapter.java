package com.tenakatasupervisor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tenakatasupervisor.Activity.ActivityShopDetails;
import com.tenakatasupervisor.Models.ModelHome;
import com.tenakatasupervisor.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {
    Context context;
    RowClick callBack;
    ArrayList<ModelHome.ResultBean> list;
    LayoutInflater layoutInflater;
    public HomeAdapter(Context context, ArrayList<ModelHome.ResultBean> list,RowClick callBack){
        this.context=context;
        this.list=list;
        this.callBack=callBack;
        layoutInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.homerecyclerviewitems, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
        String name= (String) list.get(position).getOwner_name();
        String mob=list.get(position).getPhone();
        String shopname=list.get(position).getBusiness_name();
        holder.tv_shopname.setText(shopname);
        holder.tv_name.setText(name);
        holder.tv_mob.setText(mob);
       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               callBack.onRowClick(position);
           }
       });
    }

    private void goToShopDetailsActivity() {
        Intent intent=new Intent(context, ActivityShopDetails.class);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        TextView tv_shopname,tv_name,tv_mob;
        View view;
        public Holder(@NonNull View itemView) {
            super(itemView);
             view=itemView;
            tv_mob=(TextView)itemView.findViewById(R.id.tv_mob);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            tv_shopname=(TextView)itemView.findViewById(R.id.tv_shopname);
        }
    }

    public interface RowClick {
        void onRowClick(int id);

    }
}
