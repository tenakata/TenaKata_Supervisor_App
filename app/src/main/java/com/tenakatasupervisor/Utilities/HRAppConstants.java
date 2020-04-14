package com.tenakatasupervisor.Utilities;

public interface HRAppConstants {

    String FILTER_NOTIFICATIONS = "example.filter.notification";
    String LOCATION_NOTIFICATION_BROADCAST = "com.example.location";
    String CHANNEL_ID = "com.example";
    String CHANNEL_NAME = "Notification";
    String CHANNEL_DESCRIPTION = "Example Partner Notifications";

    String kResponseCode = "status";
    String kResponseMsg = "message";
    String kRESPONSE_ERROR = "errors";




    String key_businessname="business_name";
    String key_nameofowner="owner_name";
    String key_mobilenumber="phone";
    String key_registrationno="registation_no";
    String key_isbusinessregistered="business_registered";
    String key_gender="gender";
    String key_spinnershoplocation="location";
    String key_corebusiness="core_business";
    String key_capturedocuments ="image" ;
    String key_businessstartdate = "business_date";
    String key_noofemployees = "no_of_employees";
    String key_radiobranches = "branches";
    String key_name = "name";
    String key_takenanyloan ="any_loan" ;
    String key_loanamount = "loan_amount";
    String key_loanpurpose = "loan_purpose";
    String key_country_code="country_code";
    String key_receivepayments="receive_payments";
    String key_makepayments="make_payments";
    String key_busniness_funding="busniness_funding";
    String key_activities="activities";
    String key_financial_institution="financial_institution";
    String key_role="role";

    int kResponseOK = 200;
    int kResponseSignUp = 201;
    int kResponseUserUpdated = 205;
    int kResponseSessionExpire = 345;
    int kResponseDLNotExit = 425;
    int kResponseContactExist = 409;
    int kResponseContactNotExist = 400;
    int kResponsePendingRide = 303;
    int kResponseUpdate = 101;
    int kResponseForceUpdate = 102;
    int kResponseNotAccess = 401;
    int kUpdateDevice = 205;
    int kRESPONSE_NO_TOKEN = 403;
  //  http://ec2-34-221-232-228.us-west-2.compute.amazonaws.com/index.php/set_mp_pin
    String URL_LOGIN ="index.php/login";
    String URL_CREATE_MPIN ="index.php/set_mp_pin";
    String URL_CHECK_USER ="index.php/check_user";
    String URL_FORGOT_PASSWORD ="index.php/forget_password";
    String URL_LOGIN_WITH_MPIN ="index.php/mp_pin";
    String URL_LOGOUT ="index.php/logout";
    String URL_SIGN_UP="index.php/sign_up";
    String URL_HOME="index.php/supervisor_home";
    String URL_BUSINESSVISIT="index.php/bussiness_visit";
    String URL_BUSINESSDETAIL="index.php/business_details";
    String URL_PROFILE="index.php/profile";
    String URL_TRAINING="index.php/training";
    String URL_TRAINING_VIEW="index.php/training_viewDetails";
    String URL_TRAINING_RATING="index.php/training_rating";
    String URL_CHECKVALIDATION="index.php/check_validation";


    String key_imagepath ="image" ;
    String key_latitude = "latitude";
    String key_longitude = "longitude";
}
