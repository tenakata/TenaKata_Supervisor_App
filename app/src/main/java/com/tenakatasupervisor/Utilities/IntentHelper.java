package com.tenakatasupervisor.Utilities;

import android.content.Context;
import android.content.Intent;

import com.tenakatasupervisor.Activity.ActivityBioMetricLogin;
import com.tenakatasupervisor.Activity.ActivityForgotPassword;
import com.tenakatasupervisor.Activity.ActivityLogin;
import com.tenakatasupervisor.Activity.ActivityLoginWithMpin;
import com.tenakatasupervisor.Activity.ActivityOTPVerification;
import com.tenakatasupervisor.Activity.ActivityShopDetails;
import com.tenakatasupervisor.Activity.ActivityTutorials;
import com.tenakatasupervisor.Activity.ActivityDashboard;
import com.tenakatasupervisor.Activity.ActivityVerifyMobileNumber;
import com.tenakatasupervisor.BuildConfig;
import com.tenakatasupervisor.R;

public class IntentHelper {

    public static Intent getTutorials(Context context) {
        return new Intent(context, ActivityTutorials.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getShopDetails(Context context) {
        return new Intent(context, ActivityShopDetails.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }
    public static Intent getDashboard(Context context) {
        return new Intent(context, ActivityDashboard.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getLogin(Context context) {
        return new Intent(context, ActivityLogin.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getBioMetric(Context context) {
        return new Intent(context, ActivityBioMetricLogin.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getVerifyMobileNumber(Context context) {
        return new Intent(context, ActivityVerifyMobileNumber.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getOtpVerification(Context context) {
        return new Intent(context, ActivityOTPVerification.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getForgotPassword(Context context) {
        return new Intent(context, ActivityForgotPassword.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public static Intent getActivityLoginWithMpin(Context context) {
        return new Intent(context, ActivityLoginWithMpin.class)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }





    public static void shareAPP(Context context, String description) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
            String shareMessage = description+"\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "Share Via"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
