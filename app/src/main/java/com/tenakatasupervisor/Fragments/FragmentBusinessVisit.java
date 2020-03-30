package com.tenakatasupervisor.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tenakatasupervisor.Base.BaseFragment;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.FragmentBusinessVisitBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class FragmentBusinessVisit extends BaseFragment implements  View.OnClickListener{
    FragmentBusinessVisitBinding binding;
    private Context context;
    private String shopId;
    public FragmentBusinessVisit(String shopId){
        this.shopId = shopId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_business_visit, container, false);
        binding.saveButton.setOnClickListener(this);
        context = getActivity();
        binding.etLayoutCurrentdate.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_button : if (pagevalidation()){
                hitApi();
            }
            break;
            case R.id.et_layout_currentdate : calender();
            break;

        }

    }

    private boolean pagevalidation() {
        if((binding.etLayoutCurrentdate.getText().toString().equals("Current Date"))){
            HRLogger.showSneckbar(binding.constraintLayout,"Enter Current Date");
            return false;
        }
        else if (HRValidationHelper.isNull(binding.etComments.getText().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Comment");
            return false;
        }
        else if(binding.ratingBar1.getRating()<.5){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Fill Stock Rating Bar");
            return false;
        }
        else if (binding.ratingBar2.getRating()<.5){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Fill Busy Shop Rating Bar");
            return false;
        }

        else if (HRValidationHelper.isNull(binding.etLocation.getText().toString()) ){
            HRLogger.showSneckbar(binding.constraintLayout, "Enter Location");
            return false;
        }
        return true;
    }


    private void calender() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int date=calendar.get(Calendar.DATE);
        final DatePickerDialog datePicker= new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                binding.etLayoutCurrentdate.setText(dayOfMonth+"-"+month+"-"+year);
            }
        },year,month,date);
        datePicker.show();
    }

    private void hitApi() {
       String comments  = binding.etComments.getText().toString();
       String location=binding.etLocation.getText().toString();




        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("shop_id",shopId);
            jsonObject.put("current_date",binding.etLayoutCurrentdate.getText().toString());
            jsonObject.put("comment",comments);
            jsonObject.put("bussiness_location",location);
            jsonObject.put("stock",binding.ratingBar1.getRating());
            jsonObject.put("busy_shop",binding.ratingBar2.getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Authentication.object(getActivity(), HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_BUSINESSVISIT),this,jsonObject);
    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);
        Toast.makeText(getActivity(),"data successfully submitted ",Toast.LENGTH_LONG).show();
        getActivity().finish();
    }



    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
    }
}