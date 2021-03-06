package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.IntentHelper;
import com.tenakatasupervisor.databinding.ActivityCreateMpinBinding;


public class ActivityCreateMpin extends AppCompatActivity  {
ActivityCreateMpinBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_mpin);

        binding.firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length()==4){
                    goTONextActivity();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void goTONextActivity() {
        String x=binding.firstPinView.getText().toString();
        Intent intent=new Intent(ActivityCreateMpin.this,ActivityMpinRetype.class);
        intent.putExtra("pin",x);
        startActivity(intent);
        finish();
    }
}
