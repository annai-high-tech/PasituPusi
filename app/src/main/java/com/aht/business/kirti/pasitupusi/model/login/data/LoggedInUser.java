package com.aht.business.kirti.pasitupusi.model.login.data;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String email;
    private String phone;

    private boolean validUser;
    private String errorMsg;

    public LoggedInUser(String userId, String email, String phone) {
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        this.validUser = true;
        this.errorMsg = "";
    }

    public LoggedInUser(String errorMsg) {
        this.userId = "";
        this.email = "";
        this.phone = "";
        this.validUser = false;
        this.errorMsg = errorMsg;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isValidUser() {
        return validUser;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
