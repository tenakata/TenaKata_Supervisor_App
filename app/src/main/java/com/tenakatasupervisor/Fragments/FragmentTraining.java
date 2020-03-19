package com.tenakatasupervisor.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.os.Bundle;

import com.tenakatasupervisor.Adapters.TrainingAdapter;
import com.tenakatasupervisor.Models.TrainingDetailModel;
import com.tenakatasupervisor.R;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

public class FragmentTraining extends Fragment {
    private Context context;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<TrainingDetailModel> list = new ArrayList();
    LayoutInflater layoutInflater;
    public FragmentTraining(){
      //  layoutInflater=LayoutInflater.from(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_training,null);
        TrainingDetailModel d = new TrainingDetailModel("Video 1(Watch)", "How to improve Sales.", "Read more >>");
        list.add(d);
        list.add(d);
        list.add(d);
        list.add(d);
        list.add(d);
        list.add(d);
        list.add(d);

        TrainingAdapter adapter_training = new TrainingAdapter(getActivity(), list);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(adapter_training);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

    }

}
