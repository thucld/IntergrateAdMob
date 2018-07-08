package com.thucld.integrateadmob;

import com.google.android.gms.ads.AdRequest;

public enum AdsError {

    ERROR_CODE_INTERNAL_ERROR(AdRequest.ERROR_CODE_INTERNAL_ERROR,
            "Something happened internally; for instance, an invalid response was received from the ad server."),
    ERROR_CODE_INVALID_REQUEST(AdRequest.ERROR_CODE_INVALID_REQUEST,
            "The ad request was invalid; for instance, the ad unit ID was incorrect."),
    ERROR_CODE_NETWORK_ERROR(AdRequest.ERROR_CODE_NETWORK_ERROR,
            "The ad request was unsuccessful due to network connectivity."),
    ERROR_CODE_NO_FILL(AdRequest.ERROR_CODE_NO_FILL,
            "The ad request was successful, but no ad was returned due to lack of ad inventory.");

    private final int code;
    private final String error;

    AdsError(int code, String error) {
        this.code = code;
        this.error = error;
    }

    private int getCode() {
        return code;
    }

    private String getError() {
        return error;
    }

    public static String getErrorMessage(int code) {
        if (code == ERROR_CODE_INTERNAL_ERROR.getCode()) {
            return ERROR_CODE_INTERNAL_ERROR.getError();
        } else if (code == ERROR_CODE_INVALID_REQUEST.getCode()) {
            return ERROR_CODE_INVALID_REQUEST.getError();
        } else if (code == ERROR_CODE_NETWORK_ERROR.getCode()) {
            return ERROR_CODE_NETWORK_ERROR.getError();
        } else {
            return ERROR_CODE_NO_FILL.getError();
        }
    }
}
