package com.aht.business.kirti.pasitupusi.ui.login;

import java.io.Serializable;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView implements Serializable {

    private String userId;

    private String emailId;

    private String phoneNumber;

    //... other model fields that may be accessible to the UI

    private LoggedInUserView() {

    }

    LoggedInUserView(String userId, String emailId, String phoneNumber) {
        this.userId = userId;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
    }

    String getUserId() {
        return userId;
    }

    String getEmailId() {
        return emailId;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }
}
