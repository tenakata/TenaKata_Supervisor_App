package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.ActivityLoginBinding;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness1Binding;
public class ActivityRegisterNewBusiness_1 extends AppCompatActivity implements View.OnClickListener {
RadioGroup radioGroup;
RadioButton radioButton;
int selectedradiobuttonid;
int radioflag=0;
    private String countryCode;
    private ActivityRegisterNewBusiness1Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_register_new_business_1);
        binding.ccp.registerPhoneNumberTextView(binding.etPhoned);
        binding.radioButtonyes.setOnClickListener(this);
        binding.radioButtonNo.setOnClickListener(this);
        binding.buttonfirst.setOnClickListener(this);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        binding.tvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        countryCode = binding.ccp.getSelectedCountryCode();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                countryCode = country.getPhoneCode();
            }
        });
    }

   private Boolean  pagevalidation() {

            if( HRValidationHelper.isNull(binding.etBusinessname.getText().toString())){
                HRLogger.showSneckbar(binding.constraintLayout,"Enter Business Name");
                return false;
            }
            else if (!HRValidationHelper.isNameValid(binding.etBusinessname.getText().toString())){
           HRLogger.showSneckbar(binding.constraintLayout,"Enter Valid Business Name");
           return false;
             }
             if(HRValidationHelper.isNull(binding.etNameofowner.getText().toString())){
                HRLogger.showSneckbar(binding.constraintLayout,"Enter  Owner Name");
                return  false;
            }

            else if(!HRValidationHelper.isNameValid(binding.etNameofowner.getText().toString())){
                HRLogger.showSneckbar(binding.constraintLayout,"Enter Valid Owner Name");
                return  false;
            }
                if(HRValidationHelper.isNull(binding.etPhoned.getText().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Enter Phone Number");
           return  false;
       }
            else if(!binding.ccp.isValid()){
                HRLogger.showSneckbar(binding.constraintLayout,"Enter Valid Phone Number");
                return false;
            }
            else if (HRValidationHelper.isNull(countryCode)) {
                HRLogger.showSneckbar(binding.constraintLayout, getString(R.string.txt_select_country_code));
                return false;
            }
            else if (!binding.radioButtonNo.isChecked() && !binding.radioButtonyes.isChecked() ){
                HRLogger.showSneckbar(binding.constraintLayout, "Select Yes/No");
                return false;
            }
                else if (HRValidationHelper.isNull(binding.etRegister.getText().toString()) && radioflag==0){
                    HRLogger.showSneckbar(binding.constraintLayout,"Please Enter Registration No.");
                    return false;
                }

            return true;

    }

    private void goToNextActivity() {
        Intent intent=new Intent(ActivityRegisterNewBusiness_1.this,ActivityRegisterNewBusiness_2.class);
        intent.putExtra(HRAppConstants.key_businessname,binding.etBusinessname.getText().toString());
        intent.putExtra(HRAppConstants.key_nameofowner,binding.etNameofowner.getText().toString());
        intent.putExtra(HRAppConstants.key_mobilenumber,binding.etPhoned.getText().toString());
        if (radioflag==0){
            intent.putExtra(HRAppConstants.key_registrationno,binding.etRegister.getText().toString());
        }
        intent.putExtra(HRAppConstants.key_isbusinessregistered,radioButton.getText().toString());
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonfirst:
                if (pagevalidation()){
                    goToNextActivity();
                }
                break;
            case R.id.radioButtonyes:
                selectedradiobuttonid=radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedradiobuttonid);
                binding.etLayoutRegister.setVisibility(View.VISIBLE);
                radioflag=0;
                break;
            case  R.id.radioButtonNo:
                selectedradiobuttonid=radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedradiobuttonid);
                radioflag=1;
                binding.etLayoutRegister.setVisibility(View.GONE);
                break;
        }
    }
}
