package com.tenakatasupervisor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.tenakatasupervisor.R;


public class DrawerAdapter extends BaseAdapter {

    private int[] titles = {R.string.txt_home,  R.string.txt_Profile, R.string.txt_tranining,
            R.string.txt_reminder,R.string.creatempin, R.string.txt_faq, R.string.txt_contact,
            R.string.txt_logout};

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View drawerItemView, ViewGroup parent) {

        DrawerViewHolder holder;

        if(drawerItemView==null){
            drawerItemView = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.view_drawer_item, parent, false);
            holder = new DrawerViewHolder();
            holder.textView = drawerItemView.findViewById(R.id.drawerItemTV);
            holder.view = drawerItemView.findViewById(R.id.view);
            drawerItemView.setTag(holder);
        }else {
            holder = (DrawerViewHolder)drawerItemView.getTag();
        }

        holder.textView.setText(titles[position]);
        if (position == 4){
            holder.view.setVisibility(View.VISIBLE);
        }else {
            holder.view.setVisibility(View.GONE);
        }

        return drawerItemView;
    }

    private class DrawerViewHolder{
        AppCompatTextView textView;
        View view;
    }

}
