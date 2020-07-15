package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tenakatasupervisor.Base.BaseActivity;
import com.tenakatasupervisor.Base.BaseFragment;
import com.tenakatasupervisor.Models.ModelSuccess;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.ActivityLoginBinding;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness1Binding;
public class ActivityRegisterNewBusiness_1 extends BaseActivity implements View.OnClickListener {
RadioGroup radioGroup;
RadioButton radioButton;
int selectedradiobuttonid;
int radioflag=0;
    private SharedPreferences mPrefs;
    private static final String PREF_NAME="Prefsfile";
Boolean checkvalidation=false;
    private String countryCode;
    private ActivityRegisterNewBusiness1Binding binding;

    @Override
    public void onClick(int viewId, View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=DataBindingUtil.setContentView(this, R.layout.activity_register_new_business_1);
        binding.ccp.registerPhoneNumberTextView(binding.etPhoned);
        binding.radioButtonyes.setOnClickListener(this);
        binding.radioButtonNo.setOnClickListener(this);
        binding.buttonfirst.setOnClickListener(this);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        mPrefs = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(HRAppConstants.key_country_code,countryCode);
        intent.putExtra(HRAppConstants.key_isbusinessregistered,radioButton.getText().toString());
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonfirst:
                if (pagevalidation()){
                   checkValidation();
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

    private void checkValidation() {
        String business_name=binding.etBusinessname.getText().toString();
        String phone=binding.etPhoned.getText().toString();
        String registration_no=binding.etRegister.getText().toString();
        String business_registered=radioButton.getText().toString();
        Authentication.signUpDetailValidation(HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_CHECKVALIDATION),this,business_name,registration_no,phone,business_registered);

    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        super.onTaskSuccess(responseObj);
        if (responseObj instanceof ModelSuccess){
            ModelSuccess model=(ModelSuccess) responseObj;
           if (model.gettMessage().equals("ok")){
               goToNextActivity();

        }
        }


    }

    @Override
    public void onTaskError(String errorMsg) {
        super.onTaskError(errorMsg);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
