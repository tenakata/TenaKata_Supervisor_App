package com.tenakatasupervisor.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tenakatasupervisor.Fragments.FragmentBusinessDetails;
import com.tenakatasupervisor.Fragments.FragmentBusinessVisit;

public class ShopBusinessDetailAdapter extends FragmentPagerAdapter {
    private Context context;
    private String shopId;
   public ShopBusinessDetailAdapter(FragmentManager fragmentManager, Context context,String shopId){
        super(fragmentManager);
        this.context=context;
        this.shopId = shopId;

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new FragmentBusinessDetails(shopId);
        }
        else if(position==1){
            return new FragmentBusinessVisit(shopId);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Business detail";
        }
        else if (position == 1)
        {
            title = "Business Visit";
        }
        return title;
    }
}
