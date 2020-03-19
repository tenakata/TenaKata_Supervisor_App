package com.tenakatasupervisor.Activity;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.tenakatasupervisor.Adapters.DrawerAdapter;
import com.tenakatasupervisor.Base.BaseActivity;
import com.tenakatasupervisor.CallBacks.BaseCallBacks;
import com.tenakatasupervisor.Dialog.ProgressDialog;
import com.tenakatasupervisor.Fragments.FragmentHome;
import com.tenakatasupervisor.Fragments.FragmentProfile;
import com.tenakatasupervisor.Fragments.FragmentTraining;
import com.tenakatasupervisor.Models.ModelSuccess;
import com.tenakatasupervisor.Network.Authentication;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.DimensionHelper;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRPrefManager;
import com.tenakatasupervisor.Utilities.HRUrlFactory;
import com.tenakatasupervisor.Utilities.HRValidationHelper;
import com.tenakatasupervisor.databinding.ActivityDashboardBinding;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.transform.ElevationTransformation;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityDashboard extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener , BaseCallBacks {
    private Context context;
    private ActivityDashboardBinding binding;
    private SlidingRootNav drawerLayout;

    private boolean isHome = false;
    private boolean isSale = true;
    private boolean isPurchage = true;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_dashboard);
        context = this;

    //    binding.includedToolbar.viewUserName.setText("Hello "+HRValidationHelper.optional(HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));


        initDrawer(binding.includedToolbar.toolbarDashboard,binding.includedToolbar.toolbarMenuView);
        getSupportFragmentManager().beginTransaction().add(R.id.dashboardFrame,new FragmentHome()).commit();


    }

    public void initDrawer(View toolbar, View actionBarToggle) {

        View menuView = LayoutInflater.from(this).inflate(R.layout.view_drawer, null, false);

        drawerLayout = new SlidingRootNavBuilder(this)
                .withDragDistance(DimensionHelper.getDrawerDragDistance(this)) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.8f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .addRootTransformation(new ElevationTransformation(2))
                .withGravity(SlideGravity.LEFT)
                .withContentClickableWhenMenuOpened(false)
                .withMenuView(menuView)
                .withToolbarRootView(toolbar)
                .withActionBarToggleView(actionBarToggle)
                .inject();

        ListView drawerLV = menuView.findViewById(R.id.drawer_menu_lv);
        drawerLV.setAdapter(new DrawerAdapter());
        addDrawerHeader(drawerLV);
        drawerLV.setOnItemClickListener(this);
        binding.includedToolbar.toolbarMenuView.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        closeMenu();

        switch (position) {

            case 0:
                break;

            case 1:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.includedToolbar.viewUserName.setText("Home");
                        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrame,new FragmentHome()).commit();
                    }
                }, 225);
                break;

            case 2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.includedToolbar.viewUserName.setText("Edit Profile");
                        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrame,new FragmentProfile()).commit();

                    }
                }, 225);
                break;

            case 3:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.includedToolbar.viewUserName.setText("Training");
                        getSupportFragmentManager().beginTransaction().replace(R.id.dashboardFrame,new FragmentTraining()).commit();

                    }
                }, 225);
                break;

            case 4:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 225);
                break;

            case 5:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                                   Intent intent=new Intent(context,ActivityCreateMpin.class);
                                   startActivity(intent);
                    }
                }, 225);
                break;


            case 6:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 225);
                break;

            case 7:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 225);
                break;
            case 8:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logOutDialog(context);
                    }
                }, 225);
                break;

            default:
                Toast.makeText(context, "in development", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onClick(int viewId, View view) {

        switch (viewId) {

            case R.id.toolbarMenuView:
                if (isMenuOpened()) {
                    closeMenu();
                } else {
                    openMenu();
                }
                break;


        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {

            try {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(binding.dashboardFrame.getId(), fragment);
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openMenu() {
        drawerLayout.openMenu(true);
    }

    public void closeMenu() {
        drawerLayout.closeMenu(true);
    }

    public boolean isMenuOpened() {
        return drawerLayout.isMenuOpened();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (isMenuOpened()) closeMenu();
        } else {
            super.onBackPressed();
        }

    }

    private void addDrawerHeader(ListView drawerList){
        View headerView = ((LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.drawer_header, null, false);
        TextView userName = headerView.findViewById(R.id.viewUserName);
        userName.setText(HRValidationHelper.optional("Hello "+HRPrefManager.getInstance(context).getUserDetail().getResult().getName()));
        drawerList.addHeaderView(headerView);
    }


    private void logOutDialog(final Context context) {

        new iOSDialogBuilder(context)
                .setTitle(context.getString(R.string.app_name))
                .setSubtitle(getString(R.string.txt_want_to_logout))
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setPositiveListener(context.getResources().getString(R.string.ok_text), new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                        hitApi();
                    }
                }).setNegativeListener(context.getResources().getString(R.string.cancel_text), new iOSDialogClickListener() {
            @Override
            public void onClick(iOSDialog dialog) {
                dialog.dismiss();
            }
        }).build().show();
    }

    private void hitApi() {

        progressDialog = new ProgressDialog(context);
        if (!isFinishing())
            progressDialog.showDialog(ProgressDialog.DIALOG_CENTERED);
        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("sessiontoken", HRPrefManager.getInstance(context).getUserDetail().getResult().getToken());
            jsonObject.put("role", "supervisor");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Authentication.objectRequestAccount(context, HRUrlFactory.generateUrlWithVersion(HRAppConstants.URL_LOGOUT),
                this, jsonObject, HRUrlFactory.getDefaultHeaders());

    }

    @Override
    public void onTaskSuccess(Object responseObj) {
        progressDialog .dismiss();
        if (responseObj instanceof ModelSuccess) {
            HRPrefManager.getInstance(context).clearPrefs();
            HRPrefManager.getInstance(context).setKeyIsStart(true);
            HRPrefManager.getInstance(context).setKeyIsLoggedIn(false);
            startActivity(new Intent(context, ActivityBioMetricLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finishAffinity();

        }
    }

    @Override
    public void onTaskError(String errorMsg) {
        progressDialog .dismiss();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
