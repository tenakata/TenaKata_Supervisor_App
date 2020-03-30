package com.tenakatasupervisor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tenakatasupervisor.Activity.ActivityTrainingDetails;
import com.tenakatasupervisor.Models.TrainingDetailModel;
import com.tenakatasupervisor.Models.TrainingListModel;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.databinding.TrainingbaseadapterBinding;

import java.util.ArrayList;
import java.util.List;

public class TrainingBaseAdapter extends BaseAdapter {

    private Context context;
    private List<TrainingListModel.ResultBean> list;
    private RowClick callBack;

    public TrainingBaseAdapter(Context context, List<TrainingListModel.ResultBean> list, RowClick callBack) {
        this.list = list;
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public int getCount() {
        if (list.size()>0){
            return list.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            TrainingbaseadapterBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                    , R.layout.trainingbaseadapter, parent, false);
            holder = new ViewHolder(itemBinding);
            holder.binding.tvSubTitle.setText(list.get(position).getTitle());
            holder.view.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(R.drawable.placeholder)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .transform(new RoundedCorners(10)))
                .into(holder.binding.ivVideo);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onRowClick(position,list.get(position).getId());
            }
        });
        return holder.view;
    }

    public interface RowClick {
        void onRowClick(int position,String id);

    }

    private class ViewHolder {
        View view;
        TrainingbaseadapterBinding binding;

        ViewHolder(TrainingbaseadapterBinding binding) {
            this.view = binding.getRoot();
            this.binding = binding;
        }
    }
}

