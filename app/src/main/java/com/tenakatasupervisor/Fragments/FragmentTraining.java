package com.tenakatasupervisor.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tenakatasupervisor.Activity.ActivityTrainingDetails;
import com.tenakatasupervisor.Adapters.TrainingBaseAdapter;
import com.tenakatasupervisor.Base.BaseFragment;
import com.tenakatasupervisor.Dialog.ProgressDialog;
import com.tenakatasupervisor.Models.TrainingDetailModel;
import com.tenakatasupervisor.Models.TrainingListModel;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRPrefManager;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.databinding.FragmentTrainingBinding;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentTraining extends BaseFragment implements TrainingBaseAdapter.RowClick {
    private Context context;
    private FragmentTrainingBinding binding;
    List<TrainingListModel.ResultBean> list = new ArrayList<>();
    private TrainingBaseAdapter adapter;
    private int currentPage = 1;
    private String per_page = "10";
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_training, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        progressDialog = new ProgressDialog(context);
        hitApi();
    }

    private void hitApi() {

        final JSONObject jsonObject = new JSONObject();
        try {
            if (!((Activity)context).isFinishing() && !progressDialog.isShowing()){
                progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
            }

            jsonObject.put("page", "1");
            jsonObject.put("Perpage", "10");
            jsonObject.put("user_id", HRPrefManager.getInstance(context).getUserDetail().getResult().getId());
            jsonObject.put("role", "supervisor");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.object(getActivity(), HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_TRAINING),this,jsonObject);
    }


    @Override
    public void onTaskSuccess(Object responseObj) {

        if (!((Activity)context).isFinishing() && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if (!(responseObj instanceof TrainingListModel)) {
            return;
        }
        TrainingListModel model = (TrainingListModel) responseObj;


        if (list.size() > 0) list.clear();

        if (model.getResult().size() > 0) {
            list.addAll(model.getResult());
        }
        if (adapter == null) {
            adapter = new TrainingBaseAdapter(getActivity(),list,this);
            binding.listItem.setAdapter(adapter);
        } else {
            //  adapter.refresh(list);
        }
    }


    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
        if (!((Activity)context).isFinishing() && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRowClick(int position,String user_id) {
        // Toast.makeText(getActivity(),String.valueOf(user_id),Toast.LENGTH_LONG).show();
        Intent intent=new Intent(getActivity(), ActivityTrainingDetails.class);
        intent.putExtra("id",user_id);
        startActivity(intent);

    }

}
