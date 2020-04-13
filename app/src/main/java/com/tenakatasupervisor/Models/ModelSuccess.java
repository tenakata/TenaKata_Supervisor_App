
package com.tenakatasupervisor.Models;

public class ModelSuccess {

    private String status;
    private String mMessage;
    private String message;


    public String getCode() {
        return status;
    }

    public void setCode(String code) {
        status = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }






    public String gettMessage() {
        return message;
    }

    public void settMessage(String message) {
        this.message = message;
    }


}
