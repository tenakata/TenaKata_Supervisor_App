package com.tenakatasupervisor.CallBacks;

public interface AuthenticationCallBacks {

    void onSuccessCallback(Object response);

    void onSuccessCallback(boolean isSuccess);

    void onErrorCallBack(String error);

    void onInternetNotFound();

    void onSessionExpire(String message);

}
