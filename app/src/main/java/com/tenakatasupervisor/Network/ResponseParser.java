package com.tenakatasupervisor.Network;


import com.tenakatasupervisor.Adapters.ModelBusinessDetails;
import com.tenakatasupervisor.Application.App;
import com.tenakatasupervisor.Models.LoginModel;
import com.tenakatasupervisor.Models.ModelBusinessVisit;
import com.tenakatasupervisor.Models.ModelHome;
import com.tenakatasupervisor.Models.ModelSuccess;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRUrlFactory;

public class ResponseParser {

    public static Object parse(String url, String response) {

        url = url.replaceAll(HRUrlFactory.getBaseUrlWithApiVersioning(), "");

        if (url.contains("?user_id")) {
            url = "events/";
        } else if (url.contains("?page"))
            url = "events?";

        try {
            switch (url) {

                /*========= parse model like bottom ================*/

                case HRAppConstants.URL_LOGIN:
                case HRAppConstants.URL_LOGIN_WITH_MPIN:
                    return App.getInstance().getGson().fromJson(response, LoginModel.class);

                case HRAppConstants.URL_CHECK_USER:
                case HRAppConstants.URL_FORGOT_PASSWORD:
                case HRAppConstants.URL_LOGOUT:
                case HRAppConstants.URL_SIGN_UP:
                case HRAppConstants.URL_CREATE_MPIN:
                    return App.getInstance().getGson().fromJson(response, ModelSuccess.class);

                case HRAppConstants.URL_HOME:
                    return  App.getInstance().getGson().fromJson(response, ModelHome.class);

                case HRAppConstants.URL_BUSINESSDETAIL :
                    return  App.getInstance().getGson().fromJson(response,ModelBusinessDetails.class);
                case HRAppConstants.URL_BUSINESSVISIT :
                    return  App.getInstance().getGson().fromJson(response, ModelBusinessVisit.class);

                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}