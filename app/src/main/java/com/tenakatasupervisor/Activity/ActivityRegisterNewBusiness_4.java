package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.BusinessRegisterPref;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness4Binding;

public class ActivityRegisterNewBusiness_4 extends AppCompatActivity implements View.OnClickListener {
private  Button nextbtn;
private Intent previntent;
private int radioflag=0;
BusinessRegisterPref businessRegisterPref;
private ActivityRegisterNewBusiness4Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register_new_business_4);
        previntent=getIntent();
        businessRegisterPref=new BusinessRegisterPref(this);
        binding.tvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       binding.fourthbutton.setOnClickListener(this);
       binding.radioButtonyes.setOnClickListener(this);
       binding.radioButtonNo.setOnClickListener(this);
        if (!HRValidationHelper.isNull(businessRegisterPref.getLoanAmount())){
            binding.etLoanamount.setText(businessRegisterPref.getLoanAmount());
        }
        if (!HRValidationHelper.isNull(String.valueOf(businessRegisterPref.getLoanPurpose()))){
            binding.loanpurposeSpinner.setSelection(businessRegisterPref.getLoanPurpose());
        }
        if (!HRValidationHelper.isNull(businessRegisterPref.getNameee())){
            binding.radioGroupfourth.check(businessRegisterPref.getTakenAnyLoan());
            switch (businessRegisterPref.getTakenAnyLoan()){
                case R.id.radioButtonyes:
                    binding.etLayoutLoanamount.setVisibility(View.VISIBLE);
                    binding.loanpurposeSpinner.setVisibility(View.VISIBLE);
                    radioflag=0;
                    break;
                case R.id.radioButtonNo:
                    binding.etLayoutLoanamount.setVisibility(View.GONE);
                    binding.loanpurposeSpinner.setVisibility(View.GONE);
                    radioflag=1;
                    break;
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fourthbutton:
                if (pagevalidation()){
                    goToNextActivity();
                }
                break;
            case R.id.radioButtonyes:
                binding.etLayoutLoanamount.setVisibility(View.VISIBLE);
                binding.loanpurposeSpinner.setVisibility(View.VISIBLE);
                radioflag=0;
                break;
            case R.id.radioButtonNo:
                binding.etLayoutLoanamount.setVisibility(View.GONE);
                binding.loanpurposeSpinner.setVisibility(View.GONE);
                radioflag=1;
                break;

        }
    }

    private boolean pagevalidation() {
        if (HRValidationHelper.isNull(binding.etLoanamount.getText().toString()) && radioflag==0){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Enter Loan Amount");
            return false;
        }
        else if (!binding.radioButtonNo.isChecked() && !binding.radioButtonyes.isChecked() ){
            HRLogger.showSneckbar(binding.constraintLayout, "Select Yes/No");
            return false;
        }
        return true;
    }

    private void goToNextActivity(){
        Intent intent=new Intent(ActivityRegisterNewBusiness_4.this, ActivityRegisterNewBusiness_5.class);
        RadioButton radioButton=findViewById(binding.radioGroupfourth.getCheckedRadioButtonId());
        intent.putExtra(HRAppConstants.key_takenanyloan,radioButton.getText().toString());
        intent.putExtra(HRAppConstants.key_loanamount,binding.etLoanamount.getText().toString());
        intent.putExtra(HRAppConstants.key_loanpurpose,binding.loanpurposeSpinner.getSelectedItem().toString());




        intent.putExtra(HRAppConstants.key_businessname,previntent.getStringExtra(HRAppConstants.key_businessname));
        intent.putExtra(HRAppConstants.key_nameofowner,previntent.getStringExtra(HRAppConstants.key_nameofowner));
        intent.putExtra(HRAppConstants.key_mobilenumber,previntent.getStringExtra(HRAppConstants.key_mobilenumber));
        intent.putExtra(HRAppConstants.key_isbusinessregistered,previntent.getStringExtra(HRAppConstants.key_isbusinessregistered));
        intent.putExtra(HRAppConstants.key_registrationno,previntent.getStringExtra(HRAppConstants.key_registrationno));
        intent.putExtra(HRAppConstants.key_gender,previntent.getStringExtra(HRAppConstants.key_gender));
        intent.putExtra(HRAppConstants.key_spinnershoplocation,previntent.getStringExtra(HRAppConstants.key_spinnershoplocation));
        intent.putExtra(HRAppConstants.key_corebusiness,previntent.getStringExtra(HRAppConstants.key_corebusiness));


        intent.putExtra(HRAppConstants.key_country_code,previntent.getStringExtra(HRAppConstants.key_country_code));
        intent.putExtra(HRAppConstants.key_activities,previntent.getStringExtra(HRAppConstants.key_activities));
        intent.putExtra(HRAppConstants.key_capturedocuments,previntent.getStringExtra(HRAppConstants.key_capturedocuments));
        intent.putExtra(HRAppConstants.key_businessstartdate,previntent.getStringExtra(HRAppConstants.key_businessstartdate));
        intent.putExtra(HRAppConstants.key_noofemployees,previntent.getStringExtra(HRAppConstants.key_noofemployees));
        intent.putExtra(HRAppConstants.key_radiobranches,previntent.getStringExtra(HRAppConstants.key_radiobranches));
        intent.putExtra(HRAppConstants.key_financial_institution,previntent.getStringExtra(HRAppConstants.key_financial_institution));
        intent.putExtra(HRAppConstants.key_name,previntent.getStringExtra(HRAppConstants.key_name));
        intent.putExtra(HRAppConstants.key_imagepath,previntent.getStringExtra(HRAppConstants.key_imagepath));
        intent.putExtra(HRAppConstants.key_latitude,previntent.getStringExtra(HRAppConstants.key_latitude));
        intent.putExtra(HRAppConstants.key_longitude,previntent.getStringExtra(HRAppConstants.key_longitude));

        businessRegisterPref.setRegisterBusiness4(binding.radioGroupfourth.getCheckedRadioButtonId(),binding.etLoanamount.getText().toString(),binding.loanpurposeSpinner.getSelectedItemPosition());




        startActivity(intent);
    }
}
