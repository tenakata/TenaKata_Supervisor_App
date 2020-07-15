package com.tenakatasupervisor.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RadioButton;

import com.google.gson.Gson;
import com.tenakatasupervisor.Models.LoginModel;

public class BusinessRegisterPref {

    private static BusinessRegisterPref instance;
    private static final String PREF_NAME = "TenakataBusinessRegisterPref";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Context context;
    int PRIVATE_MODE = 0;

    public BusinessRegisterPref(Context context) {

        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    public static BusinessRegisterPref getInstance(Context context) {
        if (instance == null) {
            instance = new BusinessRegisterPref(context);
        }
        return instance;
    }




    public boolean getBooleanValue(String key) {
        return this.preferences.getBoolean(key, false);
    }

    public void setBooleanValue(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public String getStringValue(String key) {
        return this.preferences.getString(key, "");
    }

    public void setStringValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }


    private int getIntValue(String key) {
        return this.preferences.getInt(key, 0);
    }

    private void setIntValue(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    private long getLongValue(String key) {
        return this.preferences.getLong(key, 0);
    }

    private void setLongValue(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.commit();
    }

    public void setRegisterBusiness2( int gender,String etLocation,int corebusiness,String activity) {
        this.editor.putInt(HRAppConstants.key_gender, gender);
        this.editor.putString(HRAppConstants.key_spinnershoplocation, etLocation);
        this.editor.putInt(HRAppConstants.key_corebusiness, corebusiness);
        this.editor.putString(HRAppConstants.key_activities, activity);
        this.editor.commit();
    }

    public Integer getGender() {
            return this.preferences.getInt(HRAppConstants.key_gender,0);
    }
    public String getetLocation() {
        return this.preferences.getString(HRAppConstants.key_spinnershoplocation,"");
    }
    public Integer getCoreBusiness() {
        return this.preferences.getInt(HRAppConstants.key_corebusiness,0);
    }
    public String getComment() {
        return this.preferences.getString(HRAppConstants.key_activities,"");
    }
    public void setRegisterBusiness3(String captureDocument,String businessDate,String selectedDate,String no_of_emoployes,int radiobutton,int financial,String name, String path) {
        this.editor.putString(HRAppConstants.key_capturedocuments, captureDocument);
        this.editor.putString(HRAppConstants.key_businessstartdate, businessDate);
        this.editor.putString("selectedDate", selectedDate);
        this.editor.putString(HRAppConstants.key_noofemployees, no_of_emoployes);
        this.editor.putInt(HRAppConstants.key_radiobranches, radiobutton);
        this.editor.putInt(HRAppConstants.key_financial_institution, financial);
        this.editor.putString(HRAppConstants.key_name, name);
        this.editor.putString(HRAppConstants.key_imagepath, path);
        this.editor.commit();
    }


    public String getCaptureDocument() {
        return this.preferences.getString(HRAppConstants.key_capturedocuments,"");
    }
    public String getSelectedDate() {
        return this.preferences.getString("selectedDate","");
    }
    public String getDate() {
        return this.preferences.getString(HRAppConstants.key_businessstartdate,"");
    }
    public String getEmployee() {
        return this.preferences.getString(HRAppConstants.key_noofemployees,"");
    }
    public Integer getFinancial() {
        return this.preferences.getInt(HRAppConstants.key_financial_institution,0);
    }
    public String getNameee() {
        return this.preferences.getString(HRAppConstants.key_name,"");
    }
    public String getPath() {
        return this.preferences.getString(HRAppConstants.key_imagepath,"");
    }
    public Integer radioBranches() {
        return this.preferences.getInt(HRAppConstants.key_radiobranches,0);
    }


    public void setRegisterBusiness4( int loanradiobutton,String loanamount,int loanPurpose) {
        this.editor.putInt(HRAppConstants.key_takenanyloan, loanradiobutton);
        this.editor.putString(HRAppConstants.key_loanamount, loanamount);
        this.editor.putInt(HRAppConstants.key_loanpurpose, loanPurpose);
        this.editor.commit();
    }
    public Integer getTakenAnyLoan() {
        return this.preferences.getInt(HRAppConstants.key_takenanyloan,0);
    }
    public String  getLoanAmount() {
        return this.preferences.getString(HRAppConstants.key_loanamount,"");
    }
    public Integer getLoanPurpose() {
        return this.preferences.getInt(HRAppConstants.key_loanpurpose,0);
    }



    public void clearPrefs() {
        this.editor.clear();
        BusinessRegisterPref.getInstance(context);
        this.editor.commit();
    }

    public void removeFromPreference(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }



}
