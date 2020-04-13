package com.tenakatasupervisor.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
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
import com.tenakatasupervisor.Dialog.ProgressDialog;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.GPSTracker;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.FragmentBusinessVisitBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class FragmentBusinessVisit extends BaseFragment implements  View.OnClickListener{
    FragmentBusinessVisitBinding binding;
    private Context context;
    private String shopId;
    String address;
    private int mYear, mDay, mMonth;
    private  String selectedDate= "";
    ProgressDialog progressDialog;
    String latitude,longitude;
    public FragmentBusinessVisit(String shopId){
        this.shopId = shopId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),R.layout.fragment_business_visit, container, false);
        binding.saveButton.setOnClickListener(this);
        context = getActivity();
        binding.markerLocation.setOnClickListener(this);
        binding.etLayoutCurrentdate.setOnClickListener(this);
        progressDialog =new ProgressDialog(context);
        getLocation();
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

            case R.id.markerLocation:
               // Toast.makeText(getActivity(), "Not able to get location enter manually", Toast.LENGTH_SHORT).show();
                GPSTracker gpsTracker = new GPSTracker(getActivity());
                Double lat = gpsTracker.getLatitude();
                Double lng = gpsTracker.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(lng);
                if (lat != 0.0 && lng != 0.0) {
                    getAddressOfLocation(lat, lng);
                } else {
                    Toast.makeText(getActivity(), "Not able to get location enter manually", Toast.LENGTH_SHORT).show();
                }

                break;

        }


    }


    public void getAddressOfLocation(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && addresses.size() > 0) {
                String address1 = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String zipCode = addresses.get(0).getPostalCode();
                address = addresses.get(0).getAddressLine(0);
                binding.etLocation.setText(address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocation() {
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        Double lat = gpsTracker.getLatitude();
        Double lng = gpsTracker.getLongitude();
        latitude=String.valueOf(lat);
        longitude=String.valueOf(lng);
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


    private void calender() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        mYear = year;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
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
                        binding.etLayoutCurrentdate.setText(DateFormat.format("dd-MM-yyyy", c.getTimeInMillis()).toString());


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void hitApi() {
       String comments  = binding.etComments.getText().toString();
       String location=binding.etLocation.getText().toString();



        JSONObject jsonObject=new JSONObject();
        try {
            if (!((Activity)context).isFinishing() && !progressDialog.isShowing()){
                progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
            }
            jsonObject.put("shop_id",shopId);
            jsonObject.put("current_date",selectedDate);
            jsonObject.put("comment",comments);
            jsonObject.put("bussiness_location",location);
            jsonObject.put("stock",binding.ratingBar1.getRating());
            jsonObject.put("busy_shop",binding.ratingBar2.getRating());
            jsonObject.put("latitude",latitude);
            jsonObject.put("longitude",longitude);
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