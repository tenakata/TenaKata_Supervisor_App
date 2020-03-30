package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.tenakatasupervisor.CallBacks.AuthenticationCallBacks;
import com.tenakatasupervisor.Dialog.ErrorDialog;
import com.tenakatasupervisor.Dialog.ProgressDialog;
import com.tenakatasupervisor.Models.LoginModel;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRPrefManager;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tenakatasupervisor.Dialog.ProgressDialog.progressDialog;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener, AuthenticationCallBacks {
    private ActivityLoginBinding binding;
    private Context context;
    private BioMetricLoginSession metricLoginSession;
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = this;

        metricLoginSession =new BioMetricLoginSession(context);
        binding.ccp.registerPhoneNumberTextView(binding.viewMobile);
        countryCode = binding.ccp.getSelectedCountryCode();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                countryCode = country.getPhoneCode();
            }
        });
        progressDialog = new ProgressDialog(context);

        binding.viewLoginBtn.setOnClickListener(this);
        binding.viewForgotPassword.setOnClickListener(this);

       /* binding.viewMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    binding.ccp.registerPhoneNumberTextView(binding.viewMobile);
                    binding.textInputLayout.setHint("Mobile Number");
                }else{
                    binding.viewMobile.setHint("Mobile Number");
                }

            }
        });*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewLoginBtn:
                if (isValidate(view)){
                    apiLogin();
                }

                break;
            case R.id.viewForgotPassword:
                startActivity(IntentHelper.getVerifyMobileNumber(context));
                break;
        }
    }

    @Override
    public void onSuccessCallback(Object response) {
        if (!isFinishing()) progressDialog.dismiss();
        if (response instanceof LoginModel) {

            metricLoginSession.saveDetails(binding.viewMobile.getText().toString(),countryCode,binding.viewPassword.getText().toString(),"user");
            HRPrefManager.getInstance(context).setUserDetail((LoginModel) response);
            HRPrefManager.getInstance(context).setKeyIsLoggedIn(true);
            startActivity(IntentHelper.getDashboard(context));
            finishAffinity();
        }
    }

    @Override
    public void onSuccessCallback(boolean isSuccess) {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onErrorCallBack(String error) {
        if (!isFinishing()) progressDialog.dismiss();
        ErrorDialog.errorDialog(context, getString(R.string.app_name), "Invalid Credentials !");
    }

    @Override
    public void onInternetNotFound() {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onSessionExpire(String message) {
        if (!isFinishing()) progressDialog.dismiss();
    }

    private boolean isValidate(View view) {
        if (HRValidationHelper.isNull(binding.viewMobile.getText().toString())) {
            HRLogger.showSneckbar(binding.parent,getString(R.string.txt_enter_mobile_number));
            return false;
        }else if (!binding.ccp.isValid()) {
            HRLogger.showSneckbar(view, getString(R.string.txt_enter_valid_mobile));
            return false;
        } else if (HRValidationHelper.isNull(binding.viewPassword.getText().toString())) {
            HRLogger.showSneckbar(binding.parent, getString(R.string.txt_enter_password));
            return false;
        }else if (!HRValidationHelper.isValidPassword(binding.viewPassword.getText().toString())) {
            HRLogger.showSneckbar(binding.parent, getString(R.string.txt_password_lenth_short));
            return false;
        }else if (HRValidationHelper.isNull(countryCode)) {
            HRLogger.showSneckbar(binding.parent, getString(R.string.txt_select_country_code));
            return false;
        }
        return true;
    }

    private void apiLogin(){

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", binding.viewMobile.getText().toString());
            jsonObject.put("country_code", countryCode);
            jsonObject.put("password", binding.viewPassword.getText().toString());
            jsonObject.put("role", "supervisor");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.apiOfLogin(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_LOGIN),
                this, jsonObject, HRUrlFactory.getDefaultHeaders());
    }



}
