package com.tenakatasupervisor.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tenakatasupervisor.R;
import com.tenakatasupervisor.databinding.ActivityAddDailySalesBinding;

public class ActivityAddDailySales extends AppCompatActivity {
    private Context context;
    private ActivityAddDailySalesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_add_daily_sales);
       context = this;
    }
}
