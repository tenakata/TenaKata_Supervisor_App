package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.tenakatasupervisor.Application.App;
import com.tenakatasupervisor.Base.BaseActivity;
import com.tenakatasupervisor.Dialog.ErrorDialog;
import com.tenakatasupervisor.Dialog.ProgressDialog;
import com.tenakatasupervisor.Models.LoginModel;
import com.tenakatasupervisor.Models.ModelSuccess;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRPrefManager;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness5Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityRegisterNewBusiness_5 extends BaseActivity {
    ActivityRegisterNewBusiness5Binding binding;
    private ProgressDialog progressDialog;
     JSONObject parameters;
    String business_name,name,phone,country_code,location,role,image,owner_name,business_registered,registration_no,gender,
            activities,business_date,no_of_employees,branches,financial_institution,any_loan,loan_amount,loan_purpose;
    Context context;
    Intent intent;
    StringBuilder mcq1result=new StringBuilder();
    StringBuilder mcq2result=new StringBuilder();
    StringBuilder mcq3result=new StringBuilder();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register_new_business_5);
        progressDialog = new ProgressDialog(context);
        context=this;
        intent=getIntent();
        binding.tvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.submitbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(int viewId, View view) {
        switch (view.getId()){
            case R.id.submitbutton :

                try {
                    if(pageValidation()){
                        apiHit2();
                        break;
                    }
                    else
                    {
                        HRLogger.showSneckbar(binding.constraintLayout,"Check atlease one box from all MCQs");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

        }
    }

    private void apiHit2() {

        String business_name=intent.getStringExtra(HRAppConstants.key_businessname);
        String name=intent.getStringExtra(HRAppConstants.key_name);
        String phone=intent.getStringExtra(HRAppConstants.key_mobilenumber);
        String location=intent.getStringExtra(HRAppConstants.key_spinnershoplocation);
        String country_code="91";
        String name_of_owner=intent.getStringExtra(HRAppConstants.key_nameofowner);
        String is_registered=intent.getStringExtra(HRAppConstants.key_isbusinessregistered);
        String registration_no=intent.getStringExtra(HRAppConstants.key_registrationno);
        String gender=intent.getStringExtra(HRAppConstants.key_gender);
        String core_business=intent.getStringExtra(HRAppConstants.key_corebusiness);
        String activities=intent.getStringExtra(HRAppConstants.key_activities);
        String business_start_date=intent.getStringExtra(HRAppConstants.key_businessstartdate);
        String branches=intent.getStringExtra(HRAppConstants.key_radiobranches);
        String no_of_employees=intent.getStringExtra(HRAppConstants.key_noofemployees);
        String anyloan=intent.getStringExtra(HRAppConstants.key_takenanyloan);
        String financial_institution=intent.getStringExtra(HRAppConstants.key_financial_institution);
        String receive_payments=mcq1result.toString();
        String make_payments=mcq2result.toString();
        String business_funding=mcq3result.toString();
        String loan_purpose=intent.getStringExtra(HRAppConstants.key_loanpurpose);
        String loan_amount=intent.getStringExtra(HRAppConstants.key_loanamount);
         String path=intent.getStringExtra(HRAppConstants.key_imagepath);
       String id= HRPrefManager.getInstance(context).getUserDetail().getResult().getId();


        Authentication.signUpApi(HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_SIGN_UP),this,id,business_name,name,phone,location,
                country_code,name_of_owner,is_registered,registration_no,gender,core_business,activities,business_start_date,branches,no_of_employees,
                anyloan,financial_institution,receive_payments,make_payments,business_funding,loan_purpose,loan_amount,path);

    }














    private boolean pageValidation() {
        int countfor1=0, countfor2=0,countfor3=0;
        Boolean value=false;
        if (binding.op1a.isChecked()){
            mcq1result.append(binding.op1a.getText().toString());
            countfor1++;
        }
        if (binding.op1b.isChecked()){
            mcq1result.append(binding.op1b.getText().toString());
            countfor1++;
        }
        if (binding.op1c.isChecked()){
            mcq1result.append(binding.op1c.getText().toString());
            countfor1++;
        }
        if (binding.op1d.isChecked()){
            mcq1result.append(binding.op1d.getText().toString());
            countfor1++;
        }


        if (binding.op2a.isChecked()){
            mcq2result.append(binding.op2a.getText().toString());
            countfor2++;
        }
        if (binding.op2b.isChecked()){
            mcq2result.append(binding.op2b.getText().toString());
            countfor2++;
        }
        if (binding.op2c.isChecked()){
            mcq2result.append(binding.op2c.getText().toString());
            countfor2++;
        }
        if (binding.op2d.isChecked()){
            mcq2result.append(binding.op2d.getText().toString());
            countfor2++;
        }



        if (binding.op1a.isChecked()){
            mcq3result.append(binding.op3a.getText().toString());
            countfor3++;
        }
        if (binding.op1b.isChecked()){
            mcq3result.append(binding.op3b.getText().toString());
            countfor3++;
        }

       if (countfor1>=1 && countfor2>=1 && countfor3>=1){

            value=true;
            return value;
       }

        return value;

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);

        if (responseObj instanceof ModelSuccess) {

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

            Intent intent=new Intent(this,ActivityDashboard.class);
            startActivity(intent);

            finish();

        }



    }


    @Override
    public void onTaskError(String errorMsg) {
       super.onTaskError(errorMsg);
       // ErrorDialog.errorDialog(this,getString(R.string.app_name), errorMsg);

    }

}

