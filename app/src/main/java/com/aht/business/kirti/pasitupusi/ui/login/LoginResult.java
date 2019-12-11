package com.aht.business.kirti.pasitupusi.ui.login;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;
    @Nullable
    private String errorMsg;
    @Nullable
    private boolean needInfomation;

    LoginResult(@Nullable Integer error, String errorMsg) {
        this.error = error;
        this.errorMsg = errorMsg;
        this.success = null;
        this.needInfomation = false;
    }

    LoginResult(boolean needInfomation, String errorMsg) {
        this(null, errorMsg);
        this.needInfomation = needInfomation;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this(null, "");
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Nullable
    String getErrorMsg() {
        return errorMsg;
    }

    public boolean isNeedInfomation() {
        return needInfomation;
    }
}
