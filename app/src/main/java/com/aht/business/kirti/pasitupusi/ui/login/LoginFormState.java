package com.aht.business.kirti.pasitupusi.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;
    private boolean isUserNameValid;

    LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, boolean isUserNameValid) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
        this.isUserNameValid = isUserNameValid;
    }

    LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
        this.isUserNameValid = true;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
    boolean isUserNameValid() {
        return isUserNameValid;
    }
}
