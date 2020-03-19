package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness2Binding;

public class ActivityRegisterNewBusiness_2 extends AppCompatActivity implements View.OnClickListener{
Button nextbtn;
Intent previntent;
ActivityRegisterNewBusiness2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register_new_business_2);
        binding.tvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.secondbutton.setOnClickListener(this);
        binding.spinnergender.getSelectedItem();
         previntent=getIntent();
     //   Toast.makeText(this,x.getStringExtra(HRAppConstants.key_isbusinessregistered),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.secondbutton:


            goToNextActivity();
            break;

        }

    }

    private void goToNextActivity() {
        Intent intent=new Intent(ActivityRegisterNewBusiness_2.this,ActivityRegisterNewBusiness_3.class);
        intent.putExtra(HRAppConstants.key_businessname,previntent.getStringExtra(HRAppConstants.key_businessname));
        intent.putExtra(HRAppConstants.key_nameofowner,previntent.getStringExtra(HRAppConstants.key_nameofowner));
        intent.putExtra(HRAppConstants.key_mobilenumber,previntent.getStringExtra(HRAppConstants.key_mobilenumber));
        intent.putExtra(HRAppConstants.key_isbusinessregistered,previntent.getStringExtra(HRAppConstants.key_isbusinessregistered));
        intent.putExtra(HRAppConstants.key_registrationno,previntent.getStringExtra(HRAppConstants.key_registrationno));
        intent.putExtra(HRAppConstants.key_gender,binding.spinnergender.getSelectedItem().toString());
        intent.putExtra(HRAppConstants.key_spinnershoplocation,binding.etShopLocation.getText().toString());
        intent.putExtra(HRAppConstants.key_corebusiness,binding.spinnercorebusiness.getSelectedItem().toString());
        intent.putExtra(HRAppConstants.key_activities,binding.etComments.getText().toString());
        startActivity(intent);
    }
}
