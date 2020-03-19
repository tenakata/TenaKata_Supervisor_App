package com.tenakatasupervisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.tenakatasupervisor.Adapters.ShopBusinessDetailAdapter;
import com.tenakatasupervisor.R;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.tenakatasupervisor.databinding.ActivityShopDetailsBinding;

import java.util.ArrayList;

public class ActivityShopDetails extends AppCompatActivity {
    ActivityShopDetailsBinding bindind;
    Intent intent;
    ViewPager viewPager;
    ArrayList list;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindind= DataBindingUtil.setContentView(this,R.layout.activity_shop_details);
        intent=getIntent();
        String business_name=intent.getStringExtra("business_name");
        String shopID=intent.getStringExtra("id");
        ShopBusinessDetailAdapter tabAdapterMama=new ShopBusinessDetailAdapter(getSupportFragmentManager(),this,shopID);
        bindind.textView4.setText(business_name);
        bindind.viewPager.setAdapter(tabAdapterMama);
        bindind.tabLayoutmama.setupWithViewPager(bindind.viewPager);

    }
}
