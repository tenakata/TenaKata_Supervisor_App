package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tenakatasupervisor.CallBacks.AuthenticationCallBacks;
import com.tenakatasupervisor.Dialog.ErrorDialog;
import com.tenakatasupervisor.Dialog.ProgressDialog;
import com.tenakatasupervisor.Models.ModelSuccess;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRLogger;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.ActivityForgotPasswordBinding;

import org.json.JSONObject;

public class ActivityForgotPassword extends AppCompatActivity implements View.OnClickListener, AuthenticationCallBacks {
    private String countryCode, contact;
    private Context context;
    private ActivityForgotPasswordBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        context = this;
        progressDialog = new ProgressDialog(context);
        binding.viewSubmitBtn.setOnClickListener(this);

        if (getIntent() != null) {
            countryCode = getIntent().getStringExtra("countryCode");
            contact = getIntent().getStringExtra("contact");

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.viewSubmitBtn:
                if (validation(view)){
                    apiForgetPassword();
                }
                break;
        }
    }

    private boolean validation(View view) {

        if (HRValidationHelper.isNull(binding.viewPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_enter_password));
            return false;
        }else if (!HRValidationHelper.isValidPassword(binding.viewPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_password_lenth_short));
            return false;
        } else if (HRValidationHelper.isNull(binding.viewConfirmPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_enter_confirm_password));
            return false;
        } else if (!binding.viewPassword.getText().toString().equals(binding.viewConfirmPassword.getText().toString())) {
            HRLogger.showSneckbar(view, getString(R.string.txt_confirm_password_not_matching));
            return false;
        }
        return true;
    }

    private void apiForgetPassword(){

        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject params = new JSONObject();
        try {
            params.put("phone", contact);
            params.put("new_password", binding.viewPassword.getText().toString());
            params.put("country_code", countryCode);
            params.put("role", "supervisor");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Authentication.apiOfLogin(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_FORGOT_PASSWORD),
                this, params, HRUrlFactory.getDefaultHeaders());
    }


    @Override
    public void onSuccessCallback(Object response) {
        if (!isFinishing()) progressDialog.dismiss();
        if (response instanceof ModelSuccess) {
            ModelSuccess modelSuccess =(ModelSuccess)response;
            Toast.makeText(this,"New password set successfully",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSuccessCallback(boolean isSuccess) {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onErrorCallBack(String error) {
        if (!isFinishing()) progressDialog.dismiss();
        ErrorDialog.errorDialog(context, getString(R.string.app_name), error);
    }

    @Override
    public void onInternetNotFound() {
        if (!isFinishing()) progressDialog.dismiss();
    }

    @Override
    public void onSessionExpire(String message) {
        if (!isFinishing()) progressDialog.dismiss();
    }
}
