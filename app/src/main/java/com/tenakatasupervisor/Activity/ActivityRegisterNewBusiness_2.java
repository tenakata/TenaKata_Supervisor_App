package com.tenakatasupervisor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.BusinessRegisterPref;
import com.tenakatasupervisor.Utilities.GPSTracker;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness2Binding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ActivityRegisterNewBusiness_2 extends AppCompatActivity implements View.OnClickListener {
    Button nextbtn;
    Intent previntent;
    String address;
    ActivityRegisterNewBusiness2Binding binding;
    String latitude,longitude;
    Double lat,lng;
    Double getLat;
    BusinessRegisterPref businessRegisterPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessRegisterPref=new BusinessRegisterPref(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_new_business_2);
        if (!HRValidationHelper.isNull(businessRegisterPref.getComment())){
            binding.etComments.setText(businessRegisterPref.getComment());
        }
        if (!HRValidationHelper.isNull(String.valueOf(businessRegisterPref.getCoreBusiness()))){
            binding.spinnercorebusiness.setSelection(businessRegisterPref.getCoreBusiness());
        }
        if (!HRValidationHelper.isNull(businessRegisterPref.getetLocation())){
            binding.etShopLocation.setText(businessRegisterPref.getetLocation());
        }
        if (!HRValidationHelper.isNull(String.valueOf(businessRegisterPref.getGender()))){
            binding.spinnergender.setSelection(businessRegisterPref.getGender());
        }


        binding.tvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
        binding.secondbutton.setOnClickListener(this);
        binding.imageView11.setOnClickListener(this);
        binding.spinnergender.getSelectedItem();
        binding.etShopLocation.setOnClickListener(this);
        previntent = getIntent();
        getLocation();
    }

    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(this);
        Double lat = gpsTracker.getLatitude();
        Double lng = gpsTracker.getLongitude();
        latitude=String.valueOf(lat);
        longitude=String.valueOf(lng);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.secondbutton:
                if (isValidate())
                goToNextActivity();
                break;

            case R.id.imageView11:

                GPSTracker gpsTracker = new GPSTracker(this);
                Double lat = gpsTracker.getLatitude();
                Double lng = gpsTracker.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(lng);
                if (lat != 0.0 && lng != 0.0) {
                    getAddressOfLocation(lat, lng);
                } else {
                    Toast.makeText(this, "Not able to get location enter manually", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private boolean isValidate(){
        if (HRValidationHelper.isNull(binding.spinnergender.getSelectedItem().toString())){
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            HRLogger.showSneckbar(binding.constraintLayout,"Select Gender");
            return false;
        }else if (HRValidationHelper.isNull(binding.etShopLocation.getText().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Enter Location");
            return false;
        }else if (HRValidationHelper.isNull(binding.spinnercorebusiness.getSelectedItem().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Select Core Business");
            return false;
        }else if (HRValidationHelper.isNull(binding.etComments.getText().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Enter Comments");
            return false;
        }
        else if (HRValidationHelper.isNull(longitude) || HRValidationHelper.isNull(latitude)){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Tap on Location Marker to get Current Location");
            return false;
        }
         else if (!HRValidationHelper.isNull(longitude) || !HRValidationHelper.isNull(latitude)){
            if(latitude.equals("0.0") || longitude.equals("0.0") ){
                HRLogger.showSneckbar(binding.constraintLayout,"Please Tap on Location Marker to get Current Location");
                return false;
            }
        }
        return true;
    }



    public void getAddressOfLocation(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                String address1 = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String zipCode = addresses.get(0).getPostalCode();
                address = addresses.get(0).getAddressLine(0);
                binding.etShopLocation.setText(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToNextActivity() {
        BusinessRegisterPref businessRegisterPref=new BusinessRegisterPref(this);
       Intent intent = new Intent(ActivityRegisterNewBusiness_2.this, ActivityRegisterNewBusiness_3.class);
        intent.putExtra(HRAppConstants.key_country_code, previntent.getStringExtra(HRAppConstants.key_country_code));
        intent.putExtra(HRAppConstants.key_businessname, previntent.getStringExtra(HRAppConstants.key_businessname));
        intent.putExtra(HRAppConstants.key_nameofowner, previntent.getStringExtra(HRAppConstants.key_nameofowner));
        intent.putExtra(HRAppConstants.key_mobilenumber, previntent.getStringExtra(HRAppConstants.key_mobilenumber));
        intent.putExtra(HRAppConstants.key_isbusinessregistered, previntent.getStringExtra(HRAppConstants.key_isbusinessregistered));
        intent.putExtra(HRAppConstants.key_registrationno, previntent.getStringExtra(HRAppConstants.key_registrationno));
        intent.putExtra(HRAppConstants.key_gender, binding.spinnergender.getSelectedItem().toString());
        intent.putExtra(HRAppConstants.key_spinnershoplocation, binding.etShopLocation.getText().toString());
        intent.putExtra(HRAppConstants.key_corebusiness, binding.spinnercorebusiness.getSelectedItem().toString());
        intent.putExtra(HRAppConstants.key_activities, binding.etComments.getText().toString());
        intent.putExtra(HRAppConstants.key_latitude, latitude);
        intent.putExtra(HRAppConstants.key_longitude, longitude);
        businessRegisterPref.setRegisterBusiness2(binding.spinnergender.getSelectedItemPosition(),binding.etShopLocation.getText().toString(),binding.spinnercorebusiness.getSelectedItemPosition(),binding.etComments.getText().toString());

        startActivity(intent);
    }


}
