package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.BusinessRegisterPref;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.ActivityRegisterNewBusiness3Binding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import id.zelory.compressor.Compressor;

public class ActivityRegisterNewBusiness_3 extends AppCompatActivity implements View.OnClickListener {
private ActivityRegisterNewBusiness3Binding binding;
private Intent previntent;
    private String path = null;
private int radioflag=0;
    private  String selectedDate= "";
    private int mYear, mDay, mMonth;
    Boolean value=false;
    Context context;
    BusinessRegisterPref businessRegisterPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        businessRegisterPref=new BusinessRegisterPref(this);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register_new_business_3);
        if (!HRValidationHelper.isNull(businessRegisterPref.getNameee())){
            binding.etNamee.setText(businessRegisterPref.getNameee());
        }
        if (!HRValidationHelper.isNull(businessRegisterPref.getDate())){
            binding.tvBusinessStartDate.setText(businessRegisterPref.getDate());
        }
        if (!HRValidationHelper.isNull(businessRegisterPref.getCaptureDocument())){
            binding.tvCaptureDocuments.setText(businessRegisterPref.getCaptureDocument());
        }
        if (!HRValidationHelper.isNull(businessRegisterPref.getPath())){
            path=businessRegisterPref.getPath();
        }
        if (!HRValidationHelper.isNull(businessRegisterPref.getEmployee())){
            binding.etEmployees.setText(businessRegisterPref.getEmployee());
        } if (!HRValidationHelper.isNull(String.valueOf(businessRegisterPref.radioBranches()))){
            binding.radioGrouponthird.check(businessRegisterPref.radioBranches());;
        }
        if (!HRValidationHelper.isNull(String.valueOf(businessRegisterPref.getFinancial()))){
            binding.spinner.setSelection(businessRegisterPref.getFinancial());
        }
        if (!HRValidationHelper.isNull(String.valueOf(businessRegisterPref.getSelectedDate()))){
            selectedDate =businessRegisterPref.getSelectedDate();
        }


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
            /*case R.id.radioButtonyes:
               *//* binding.spinner.setVisibility(View.VISIBLE);
                binding.etLayoutName.setVisibility(View.VISIBLE);*//*
               // radioflag=0;
                break;
            case  R.id.radioButtonNo:
                    radioflag=1;
               *//* binding.spinner.setVisibility(View.GONE);
                binding.etLayoutName.setVisibility(View.GONE);*//*
                break;*/

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
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        mYear = year;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        c.set(year, monthOfYear, dayOfMonth);

                        Calendar userAge = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        Calendar minAdultAge = new GregorianCalendar();
                        minAdultAge.add(Calendar.YEAR, 0);
                        /*if (minAdultAge.before(userAge)) {
                            selectedDob = null;
                            binding.tvDob.setText("");
                            Toast.makeText(context, context.getString(R.string.txt_age_message), Toast.LENGTH_SHORT).show();
                        } else {
                            selectedDob = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                            binding.tvDob.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());
                        }*/

                        //  selectedDob = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                        selectedDate = DateFormat.format("yyyy-MM-dd", c.getTimeInMillis()).toString();
                        binding.tvBusinessStartDate.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
        else if(HRValidationHelper.isNull(binding.etNamee.getText().toString())){
            HRLogger.showSneckbar(binding.constraintLayout,"Please Enter Name");
            return false;
        }
        else if (!HRValidationHelper.isNameValid(binding.etNamee.getText().toString())){
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
        intent.putExtra(HRAppConstants.key_businessstartdate,selectedDate);
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
        intent.putExtra(HRAppConstants.key_latitude,previntent.getStringExtra(HRAppConstants.key_latitude));
        intent.putExtra(HRAppConstants.key_longitude,previntent.getStringExtra(HRAppConstants.key_longitude));
        businessRegisterPref.setRegisterBusiness3(binding.tvCaptureDocuments.getText().toString(),binding.tvBusinessStartDate.getText().toString(),selectedDate
                ,binding.etEmployees.getText().toString(),binding.radioGrouponthird.getCheckedRadioButtonId(),binding.spinner.getSelectedItemPosition(),binding.etNamee.getText().toString(),path);;
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
           // path = result.getUri().getEncodedPath();

          /* File compressedImageFile = Compressor.getDefault(this).compressToFile(new File(result.getUri().getEncodedPath()));
            Uri x=Uri.fromFile(new File(compressedImageFile.getPath()));
            path=String.valueOf(x.getPath());*/
            try {
                path=   String.valueOf(new Compressor(context).compressToFile(
                        new File(result.getUri().getPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
