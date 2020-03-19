package com.tenakatasupervisor.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tenakatasupervisor.Activity.ActivityRegisterNewBusiness_1;
import com.tenakatasupervisor.Activity.ActivityShopDetails;
import com.tenakatasupervisor.Adapters.HomeAdapter;
import com.tenakatasupervisor.Base.BaseFragment;
import com.tenakatasupervisor.Models.ModelHome;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;

import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRPrefManager;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentHome extends BaseFragment implements HomeAdapter.RowClick {
    private Context context;
    private   RecyclerView.LayoutManager layoutManager;
    private FragmentHomeBinding binding;
    List<ModelHome.ResultBean> list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_home, container, false);
        binding.newbusinessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ActivityRegisterNewBusiness_1.class);
                startActivity(intent);
            }
        });




        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();

        hitApi();

    }

    private void hitApi() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("supervisor_id", HRPrefManager.getInstance(getActivity()).getUserDetail().getResult().getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Authentication.object(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_HOME),
                this, jsonObject);
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        dismissLoader();
        if ((responseObj instanceof ModelHome)) {
            ModelHome model = (ModelHome) responseObj;
            list.addAll(model.getResult());

            HomeAdapter homeAdapter=new HomeAdapter(context, (ArrayList<ModelHome.ResultBean>) list,this);
            layoutManager = new LinearLayoutManager(getActivity());

            binding.recyclerviewmypanel.setLayoutManager(layoutManager);
            DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
            divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.thin_line));
            binding.recyclerviewmypanel.addItemDecoration(divider);
            binding.recyclerviewmypanel.setAdapter(homeAdapter);

        }



    }

    @Override
    public void onRowClick(int rowid) {
        Intent intent=new Intent(getActivity(), ActivityShopDetails.class);
       intent.putExtra("business_name",list.get(rowid).getBusiness_name());
       intent.putExtra("owner_name",(String) list.get(rowid).getOwner_name());
       intent.putExtra("phone",list.get(rowid).getPhone());
       intent.putExtra("id",list.get(rowid).getId());
       intent.putExtra("supervisor_id",list.get(rowid).getSupervisor_id());
       startActivity(intent);
       // startActivity(IntentHelper.getShopDetails(getActivity()));
    }
}
