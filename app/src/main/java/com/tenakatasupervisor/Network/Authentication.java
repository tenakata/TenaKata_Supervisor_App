package com.tenakatasupervisor.Network;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tenakatasupervisor.Application.App;
import com.tenakatasupervisor.BuildConfig;
import com.tenakatasupervisor.CallBacks.AuthenticationCallBacks;
import com.tenakatasupervisor.CallBacks.BaseCallBacks;
import com.tenakatasupervisor.R;
import com.tenakatasupervisor.Utilities.HRAppConstants;
import com.tenakatasupervisor.Utilities.HRNetworkUtils;
import com.tenakatasupervisor.Utilities.HRUrlFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Authentication {

    public static void post(@NonNull final String url, @NonNull final HashMap<String, String> params,
                            @NonNull final BaseCallBacks callBacks, final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getAppHeaders();
            } else {
                headers = HRUrlFactory.getDefaultHeaders();
            }
            hit(full_url, params, headers, Request.Method.POST, callBacks);

        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void get(@NonNull final String url, @NonNull final BaseCallBacks callBacks,
                           final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            print(url, "hitting", "get type", headers.toString());
            hit(full_url, null, headers, Request.Method.GET, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void getWithoutLoader(@NonNull final String url, @NonNull final BaseCallBacks callBacks,
                                        final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            //callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            print(url, "hitting", "get type", headers.toString());
            hit(full_url, null, headers, Request.Method.GET, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void getWithAppVersion(@NonNull final String url, @NonNull final BaseCallBacks callBacks,
                                         final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            print(url, "hitting", "get type", headers.toString());
            hit(full_url, null, headers, Request.Method.GET, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void postWithAppVersion(@NonNull final String url, @NonNull final HashMap<String, String> params, @NonNull final BaseCallBacks callBacks,
                                          final boolean useDefaultHeaders) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            String full_url = HRUrlFactory.generateUrlWithVersion(url);
            HashMap<String, String> headers;
            if (useDefaultHeaders) {
                headers = HRUrlFactory.getDefaultHeaders();
            } else {
                headers = HRUrlFactory.getAppHeaders();
            }
            hit(full_url, params, headers, Request.Method.POST, callBacks);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void print(String url, String response, String params, String headers) {
        if (BuildConfig.DEBUG) {
            try {
                Log.i(url, response+"\n\n"+"params: "+params+ "\n\n"+"headers: "+headers+"\n\n");
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    public static void hit(@NonNull final String url, final HashMap<String, String> params,
                           @NonNull final HashMap<String, String> headers, int method,
                           @NonNull final BaseCallBacks callBacks) {

        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, response, params != null ? params.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                        }
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                return headers;
            }
        };


        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void homePageListingAPi(@NonNull final String url, final HashMap<String, String> params,
                           @NonNull final HashMap<String, String> headers,
                           @NonNull final BaseCallBacks callBacks) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }
        String full_url = HRUrlFactory.generateUrlWithVersion(url);

        StringRequest request = new StringRequest(Request.Method.POST,full_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, response, params != null ? params.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                        }
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {

                return headers;
            }
        };


        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void updateUserDetails(String driver_id, String f_name, String l_Name, String contact, String imagePath,
                                         @NonNull final BaseCallBacks callBacks) {


        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion("");

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }

            public String getBodyContentType() {
                return "multipart/form-data; boundary=MINE_BOUNDARY";
            }
        };

        request.addStringParam("driver_id", driver_id);
        request.addStringParam("first_name", f_name);
        request.addStringParam("last_name", l_Name);
        request.addStringParam("econtact", contact);
        if (imagePath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam("image", imagePath);
            } else {
                request.addFile("image", imagePath);
            }
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void addOdoMeterApi(String odometerReading,
                                      String imagePath,
                                      @NonNull final BaseCallBacks callBacks, final String Url,String tripId) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                Url, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(Url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };

        request.addStringParam("preOdometerReading", odometerReading);
        request.addStringParam("tripId", tripId);
        if (imagePath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam("preOdometerImage", imagePath);
            } else {
                request.addFile("preOdometerImage", imagePath);
            }
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void updateDateOFBirth(Context context, final AuthenticationCallBacks callBacks,
                                          final String Url,
                                          final JSONObject jsonObject,final HashMap<String, String> headers) {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url,jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(Url, response.toString(), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(Url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onSuccessCallback((boolean) r);
                            } else {
                                callBacks.onSuccessCallback(r);
                            }
                        } else {
                            callBacks.onSuccessCallback("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onErrorCallBack(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onErrorCallBack(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(Url, String.valueOf(""), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        App.getInstance().getRequestQueue().getCache().clear();
        Volley.newRequestQueue(context).add(request);

    }

    public static void checkUsers(final Context context, final String url,
                                 final AuthenticationCallBacks callBacks,
                                 final HashMap<String, String> paramsession,final HashMap<String,String> headers) {

        final JSONObject loginParams = new JSONObject(paramsession);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, loginParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(url, String.valueOf(response), paramsession.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onSuccessCallback((boolean) r);
                            } else {
                                callBacks.onSuccessCallback(r);
                            }
                        } else {
                            callBacks.onSuccessCallback("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactNotExist) {
                        callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onErrorCallBack(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onErrorCallBack(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, "", paramsession.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            public Map<String, String> getHeaders() {
                return headers;
            }
        };


        Volley.newRequestQueue(context).getCache().clear();
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public static void apiOfLogin(Context context, final String url,
                                  final AuthenticationCallBacks callBacks, final JSONObject jsonObject,
                                  final HashMap<String, String> headers) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(url, String.valueOf(response), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());

                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onSuccessCallback((boolean) r);
                            } else {
                                callBacks.onSuccessCallback(r);
                            }
                        } else {
                            callBacks.onSuccessCallback("");
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onErrorCallBack(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onErrorCallBack(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onErrorCallBack(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, "", jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onErrorCallBack(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        Volley.newRequestQueue(context).add(request);
    }

    public static void objectRequestAccount(Context context, final String url,
                                            final BaseCallBacks callBacks, final JSONObject jsonObject,
                                            final HashMap<String, String> headers) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseDLNotExit
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice
                                || resObj.getInt(HRAppConstants.kResponseCode) == 425) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void objectRequestLandingPageApi(Context context, final String url,
                                            final BaseCallBacks callBacks, final JSONObject jsonObject,
                                            final HashMap<String, String> headers,boolean isShowLoader) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            if (isShowLoader) callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseDLNotExit
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice
                                || resObj.getInt(HRAppConstants.kResponseCode) == 425) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void sendMessage(Context context, final String url,
                                   final BaseCallBacks callBacks, final JSONObject jsonObject,
                                   final HashMap<String, String> headers, boolean isLoaderEnable) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            if (isLoaderEnable) callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void objectRequestWithoutLoader(Context context, final String url,
                                                  final BaseCallBacks callBacks, final JSONObject jsonObject,
                                                  final HashMap<String, String> headers) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void objectRequest(Context context, final String url,
                                     final BaseCallBacks callBacks, final JSONObject jsonObject,
                                     final HashMap<String, String> headers) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                if (r instanceof Boolean) {
                                    callBacks.onTaskSuccess((boolean) r);
                                } else {
                                    callBacks.onTaskSuccess(r);
                                }
                            } else {
                                callBacks.onTaskError(null);
                            }
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }


    public static void searchFilterApi(final Context context, final String url,
                                       final BaseCallBacks callBacks, final JSONObject jsonObject,
                                       final HashMap<String, String> headers, boolean isLaderEnable) {
        if (HRUrlFactory.isModeDevelopment()) {
            print(url, "Params:" + jsonObject, "", "");
        }
        if (HRNetworkUtils.isNetworkAvailable()) {
            if (isLaderEnable && !((Activity) context).isFinishing()) callBacks.showLoader();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject resObj = new JSONObject(String.valueOf(response));
                        if (HRUrlFactory.isModeDevelopment()) {
                            print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                        }
                        if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                                || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                            Object r = ResponseParser.parse(url, String.valueOf(response));
                            if (r != null) {
                                String page = "";
                                page = jsonObject.getString("page");
                                if (page != null) {
                                    if (page.equals("1")) {
                                        if (r instanceof Boolean) {
                                            callBacks.onTaskSuccess((boolean) r);
                                        } else {
                                            callBacks.onTaskSuccess(r);

                                        }
                                    } else {
                                        if (r instanceof Boolean) {
                                            callBacks.onLoadMore((boolean) r);
                                        } else {
                                            callBacks.onLoadMore(r);
                                        }
                                    }
                                }

                            } else {
                                callBacks.onTaskError(null);
                            }

                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                            callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                            callBacks.onAppNeedLogin();
                        } else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                        }else if (resObj.has(HRAppConstants.kResponseCode) &&
                                resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                            callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                        } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                            if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                            }
                        } else {
                            if (resObj.has(HRAppConstants.kResponseMsg)) {
                                callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                            } else {
                                callBacks.onTaskError(null);
                            }
                        }
                    } catch (JSONException e) {
                        callBacks.onTaskError(null);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            Volley.newRequestQueue(context).add(request);
        } else {
            callBacks.onInternetNotFound();
        }
    }

    public static void object(final Context context, final String url,
                              final BaseCallBacks callBacks,
                              final JSONObject jsonObject) {
       // callBacks.showLoader();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                print(url, String.valueOf(response), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskSuccess("");
                        }
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               print(url, error.getMessage(), jsonObject.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };


        Volley.newRequestQueue(context).getCache().clear();
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public static void stringRequest(final Context context, final String url,
                              final BaseCallBacks callBacks,
                              final HashMap<String,String> parms) {
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                print(url, String.valueOf(response), parms.toString(), HRUrlFactory.getDefaultHeaders().toString());
                try {
                    JSONObject resObj = new JSONObject(String.valueOf(response));
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskSuccess("");
                        }
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                print(url, error.getMessage(), parms.toString(), HRUrlFactory.getDefaultHeaders().toString());
                if (callBacks != null)
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof AuthFailureError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                    } else if (error instanceof ServerError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                    } else if (error instanceof NetworkError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                    } else if (error instanceof ParseError) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                    }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }

            public Map<String, String> getParams() {
                return parms;
            }
        };


        Volley.newRequestQueue(context).getCache().clear();
        Volley.newRequestQueue(context).add(jsonRequest);
    }

    public static void multiPartRequest(String kID, String kImage, String id, String imagePath, String Url, @NonNull final BaseCallBacks callBacks) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion(Url);

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                if (HRUrlFactory.isModeDevelopment()) {
                    print(url, String.valueOf(response), "", "");
                }
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };
        request.addStringParam(kID, id);
        if (imagePath != null) {
            Log.i("Path", imagePath);
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam(kImage, imagePath);
            } else {
                request.addFile(kImage, imagePath);
            }
            Log.i("Path", imagePath);
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }






    public static void signUpApi( String Url, @NonNull final BaseCallBacks callBacks,String id,
    String business_name,
    String name,
    String phone,
    String location,
    String country_code,
    String name_of_owner,
    String is_registered,
    String registration_no,
    String gender,
    String core_business,
    String activities,
    String business_start_date,
    String branches,
    String no_of_employees,
    String anyloan,
    String financial_institution,
    String receive_payments,
    String make_payments,
    String business_funding,
    String loan_purpose,
    String loan_amount,
    String path,String latitude,String longitude) {
        if (HRNetworkUtils.isNetworkAvailable()) {
           callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = Url;

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {

                    print(url, String.valueOf(response), "", "");

                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactNotExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };
        request.addStringParam(HRAppConstants.key_role,"user");
        request.addStringParam("supervisor_id",id);
        request.addStringParam(HRAppConstants.key_businessname,business_name);
        request.addStringParam(HRAppConstants.key_name,name);
        request.addStringParam(HRAppConstants.key_mobilenumber,phone);
        request.addStringParam(HRAppConstants.key_spinnershoplocation,location);
        request.addStringParam(HRAppConstants.key_country_code,country_code);
        request.addStringParam(HRAppConstants.key_nameofowner,name_of_owner);
        request.addStringParam(HRAppConstants.key_isbusinessregistered,is_registered);
        request.addStringParam(HRAppConstants.key_registrationno,registration_no);
        request.addStringParam(HRAppConstants.key_gender,gender);
        request.addStringParam(HRAppConstants.key_corebusiness,core_business);
        request.addStringParam(HRAppConstants.key_activities,activities);
        request.addStringParam(HRAppConstants.key_businessstartdate,business_start_date);
        request.addStringParam(HRAppConstants.key_radiobranches,branches);
        request.addStringParam(HRAppConstants.key_noofemployees,no_of_employees);
        request.addStringParam(HRAppConstants.key_takenanyloan,anyloan);
        request.addStringParam(HRAppConstants.key_financial_institution,financial_institution);
        request.addStringParam(HRAppConstants.key_receivepayments,receive_payments);
        request.addStringParam(HRAppConstants.key_makepayments,make_payments);
        request.addStringParam(HRAppConstants.key_busniness_funding,business_funding);
        request.addStringParam(HRAppConstants.key_loanpurpose,loan_purpose);
        request.addStringParam(HRAppConstants.key_loanamount,loan_amount);
        request.addStringParam(HRAppConstants.key_latitude,latitude);
        request.addStringParam(HRAppConstants.key_longitude,longitude);
        if (path != null) {
            if (android.util.Patterns.WEB_URL.matcher(path).matches()) {
                request.addStringParam(HRAppConstants.key_imagepath, path);
            } else {
                request.addFile(HRAppConstants.key_imagepath, path);
            }
            Log.i(HRAppConstants.key_imagepath, path);
        }
        App.getInstance().getRequestQueue().getCache().clear();
       App.getInstance().getRequestQueue().add(request);
    }


    public static void signUpDetailValidation( String Url, @NonNull final BaseCallBacks callBacks,String business_name,
                                  String registration_no,
                                  String phone,String business_registered) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = Url;

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                if (HRUrlFactory.isModeDevelopment()) {
                    print(url, String.valueOf(response), "", "");
                }
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactNotExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };
        request.addStringParam("registation_no",registration_no);
        request.addStringParam("phone",phone);
        request.addStringParam("business_name",business_name);
        request.addStringParam("business_registered",business_registered);
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }























    public static void objectApi(Context context, final String url,
                                 final BaseCallBacks callBacks, final JSONObject jsonObject,
                                 final HashMap<String, String> headers, boolean isLaderEnable) {
        if (isLaderEnable) callBacks.showLoader();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, String.valueOf(response), jsonObject != null ? jsonObject.toString() : "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kUpdateDevice) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            if (r instanceof Boolean) {
                                callBacks.onTaskSuccess((boolean) r);
                            } else {
                                callBacks.onTaskSuccess(r);
                            }
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));

                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }/*else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }*/
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void carRegistrationPhotoReq(String kID, String kImage, String id,
                                               String imagePath, String kImageType, String
                                                       imageType, String Url,
                                               @NonNull final BaseCallBacks callBacks) {

        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion(Url);

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                if (HRUrlFactory.isModeDevelopment()) {
                    print(url, String.valueOf(response), "", "");
                }
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };
        request.addStringParam(kID, id);
        request.addStringParam(kImageType, imageType);
        if (imagePath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagePath).matches()) {
                request.addStringParam(kImage, imagePath);
            } else {
                request.addFile(kImage, imagePath);
            }
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void postTypeWithLoader(@NonNull final String url,
                                          @NonNull final HashMap<String, String> headers, final HashMap<String, String> params, int method,
                                          @NonNull final BaseCallBacks callBacks) {
        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject resObj = new JSONObject(response.toString());
                    if (HRUrlFactory.isModeDevelopment()) {
                        print(url, response.toString(), "get type", headers.toString());
                    }
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response.toString());
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseContactExist) {
                        callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    }else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseNotAccess) {
                        callBacks.onAppNeedLogin();
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        if (resObj.getString(HRAppConstants.kResponseMsg).equalsIgnoreCase("Please activate your car first does not exists !!")) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                        }
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_server_not_respond));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                }
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

    public static void ProfilemultiPartRequest(String imagepath,String Url,
                                               @NonNull final BaseCallBacks callBacks,String phone,String id_no,String name,String email,String role) {


        if (HRNetworkUtils.isNetworkAvailable()) {
            callBacks.showLoader();
        } else {
            callBacks.onInternetNotFound();
            return;
        }

        final String url = HRUrlFactory.generateUrlWithVersion(Url);

        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            public void onResponse(String response) {
                if (HRUrlFactory.isModeDevelopment()) {
                    print(url, String.valueOf(response), "", "");
                }
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseOK
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSignUp
                            || resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUserUpdated) {
                        Object r = ResponseParser.parse(url, response);
                        if (r != null) {
                            callBacks.onTaskSuccess(r);
                        } else {
                            callBacks.onTaskError(null);
                        }
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseUpdate) {
                        callBacks.onAppNeedUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseForceUpdate) {
                        callBacks.onAppNeedForceUpdate(resObj.getString(HRAppConstants.kResponseMsg));
                    } else if (resObj.has(HRAppConstants.kResponseCode) &&
                            resObj.getInt(HRAppConstants.kResponseCode) == HRAppConstants.kResponseSessionExpire) {
                        callBacks.onSessionExpire(resObj.getString(HRAppConstants.kResponseMsg));
                    } else {
                        if (resObj.has(HRAppConstants.kResponseMsg)) {
                            callBacks.onTaskError(resObj.getString(HRAppConstants.kResponseMsg));
                        } else {
                            callBacks.onTaskError(null);
                        }
                    }
                } catch (JSONException e) {
                    callBacks.onTaskError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof AuthFailureError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                } else if (error instanceof ServerError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.text_server_error));
                } else if (error instanceof NetworkError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_connection_error));
                } else if (error instanceof ParseError) {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_parsing_error));
                } else {
                    callBacks.onTaskError(App.getInstance().getString(R.string.txt_went_wrong));
                }
            }
        }) {
            public Map<String, String> getHeaders() {
                return HRUrlFactory.getAppHeaders();
            }
        };





        request.addStringParam("phone", phone);
        Log.e("phone",phone);
        request.addStringParam("user_id", id_no);
        request.addStringParam("email", email);
        Log.e("user_id",id_no);
        request.addStringParam("role", role);

        request.addStringParam("name", name);
        Log.e("name",name);


        if (imagepath != null) {
            if (android.util.Patterns.WEB_URL.matcher(imagepath).matches()) {
                request.addStringParam("image", imagepath);
                request.addStringParam("image", imagepath);
                Log.e("image",imagepath);
            } else {
                request.addFile("image", imagepath);
                Log.e("image",imagepath);
            }
            Log.i("Path", imagepath);
        }
        App.getInstance().getRequestQueue().getCache().clear();
        App.getInstance().getRequestQueue().add(request);
    }

}
