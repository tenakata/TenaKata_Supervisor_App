package com.tenakatasupervisor.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness3Binding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityRegisterNewBusiness_3 extends AppCompatActivity implements View.OnClickListener {
private ActivityRegisterNewBusiness3Binding binding;
private Intent previntent;
    private String path = null;
private int radioflag=0;
    Boolean value=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register_new_business_3);
        previntent=getIntent();
        binding.tvHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.thirdbutton.setOnClickListener(this);
        binding.radioButtonyes.setOnClickListener(this);
        binding.radioButtonNo.setOnClickListener(this);
        binding.tvBusinessStartDate.setOnClickListener(this);

        binding.tvCaptureDocuments.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_capture_documents:
                    captureDocumentswithPermission();

                            break;
            case R.id.tv_business_start_date:
                            calender();
                            break;

            case R.id.thirdbutton:
                            if (pagevalidation()){
                                goToNextActivity();
                            }
                            break;
            case R.id.radioButtonyes:
                binding.spinner.setVisibility(View.VISIBLE);
                binding.etLayoutName.setVisibility(View.VISIBLE);
                radioflag=0;
                break;
            case  R.id.radioButtonNo:
                    radioflag=1;
                binding.spinner.setVisibility(View.GONE);
                binding.etLayoutName.setVisibility(View.GONE);
                break;

        }



    }

    private void captureDocumentswithPermission() {

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        captureDocuments();

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();

                    }

                })
                .setDeniedMessage("Please Give Permissions")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                .check();
    }


    private void calender() {
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int date=calendar.get(Calendar.DATE);
        final DatePickerDialog datePicker= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                binding.tvBusinessStartDate.setText(dayOfMonth+"/"+month+"/"+year);
            }
        },year,month,date);
        datePicker.show();
    }

    private void captureDocuments(){
        TedPermission.with(ActivityRegisterNewBusiness_3.this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        CropImage.activity()
                                //.setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.OVAL)
                                .start(ActivityRegisterNewBusiness_3.this);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        finish();
                    }
                })
                .setDeniedMessage("error")
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private boolean pagevalidation() {
       if((binding.tvCaptureDocuments.getText().toString().equals("Capture Documents"))){
           HRLogger.showSneckbar(binding.constraintLayout,"Please Capture Document");
            return false;
        }
         if ((binding.tvBusinessStartDate.getText().toString().equals("Business Start Date"))){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Enter Date");
            return false;
        }
        else if (HRValidationHelper.isNull(binding.etEmployees.getText().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Enter no. of Employees");
            return false;
        }
        else if(HRValidationHelper.isNull(binding.etNamee.getText().toString()) && radioflag==0){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Enter Name");
            return false;
        }
        else if (!HRValidationHelper.isNameValid(binding.etNamee.getText().toString()) && radioflag==0){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Enter Valid Name");
            return false;
        }

        else if (!binding.radioButtonNo.isChecked() && !binding.radioButtonyes.isChecked() ){
            HRLogger.showSneckbar(binding.constraintLayout, "Select Yes/No");
            return false;
        }
        return true;
    }

    private void goToNextActivity() {
        Intent intent = new Intent(ActivityRegisterNewBusiness_3.this, ActivityRegisterNewBusiness_4.class);
        intent.putExtra(HRAppConstants.key_capturedocuments,binding.tvCaptureDocuments.getText().toString());
        intent.putExtra(HRAppConstants.key_businessstartdate,binding.tvBusinessStartDate.getText().toString());
        intent.putExtra(HRAppConstants.key_noofemployees,binding.etEmployees.getText().toString());
        RadioButton radioButton=(RadioButton)findViewById(binding.radioGrouponthird.getCheckedRadioButtonId());
        intent.putExtra(HRAppConstants.key_radiobranches,radioButton.getText().toString());
        intent.putExtra(HRAppConstants.key_financial_institution,binding.spinner.getSelectedItem().toString());
        intent.putExtra(HRAppConstants.key_name,binding.etNamee.getText().toString());
        intent.putExtra(HRAppConstants.key_imagepath,path);


        intent.putExtra(HRAppConstants.key_country_code,previntent.getStringExtra(HRAppConstants.key_country_code));
        intent.putExtra(HRAppConstants.key_activities,previntent.getStringExtra(HRAppConstants.key_activities));
        intent.putExtra(HRAppConstants.key_businessname,previntent.getStringExtra(HRAppConstants.key_businessname));
        intent.putExtra(HRAppConstants.key_nameofowner,previntent.getStringExtra(HRAppConstants.key_nameofowner));
        intent.putExtra(HRAppConstants.key_mobilenumber,previntent.getStringExtra(HRAppConstants.key_mobilenumber));
        intent.putExtra(HRAppConstants.key_isbusinessregistered,previntent.getStringExtra(HRAppConstants.key_isbusinessregistered));
        intent.putExtra(HRAppConstants.key_registrationno,previntent.getStringExtra(HRAppConstants.key_registrationno));
        intent.putExtra(HRAppConstants.key_gender,previntent.getStringExtra(HRAppConstants.key_gender));
        intent.putExtra(HRAppConstants.key_spinnershoplocation,previntent.getStringExtra(HRAppConstants.key_spinnershoplocation));
        intent.putExtra(HRAppConstants.key_corebusiness,previntent.getStringExtra(HRAppConstants.key_corebusiness));

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            Uri tempUri = data.getData();
            CropImage.activity(tempUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
          //  String imageUri = result.getUri().toString();

            path = result.getUri().getEncodedPath();

            binding.tvCaptureDocuments.setText(HRValidationHelper.optional(getRealPathFromURI(result.getUri())));

        }
    }
    public String getRealPathFromURI(Uri contentUri){
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e){
            return contentUri.getPath();
        }
    }





}
