package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.GPSTracker;
import com.tenakatasupervisor.Utilities.HRAppConstants;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_new_business_2);
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
                double lat = gpsTracker.getLatitude();
                double lng = gpsTracker.getLongitude();
                if (lat != 0.0 && lng != 0.0) {
                    getAddressOfLocation(lat, lng);
                } else {
                    Toast.makeText(this, "No able to get location enter manually", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private boolean isValidate(){
        if (HRValidationHelper.isNull(binding.spinnergender.getSelectedItem().toString())){
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }else if (HRValidationHelper.isNull(binding.etShopLocation.getText().toString())){
            Toast.makeText(this, "Enter LOCation", Toast.LENGTH_SHORT).show();
            return false;
        }else if (HRValidationHelper.isNull(binding.spinnercorebusiness.getSelectedItem().toString())){
            Toast.makeText(this, "Select Core business", Toast.LENGTH_SHORT).show();
            return false;
        }else if (HRValidationHelper.isNull(binding.etComments.getText().toString())){
            Toast.makeText(this, "Enter Comments", Toast.LENGTH_SHORT).show();
            return false;
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
        startActivity(intent);
    }
}
