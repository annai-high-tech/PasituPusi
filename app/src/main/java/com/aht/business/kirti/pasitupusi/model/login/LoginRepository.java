package com.aht.business.kirti.pasitupusi.model.login;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.model.login.data.LoggedInUser;
import com.aht.business.kirti.pasitupusi.ui.login.LoginType;

/**
 * Class that requests authentication and user information from the remote model source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logOut(){
        user = null;
        dataSource.logOut();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String username, String password, Activity activity, MutableLiveData<LoggedInUser> result, LoginType loginType) {
        // handle login
        dataSource.login(username, password, activity, result, loginType);
    }

    public void resetPassword(String username, Activity activity, MutableLiveData<LoggedInUser> result, LoginType loginType) {
        // handle login
        dataSource.resetPassword(username, activity, result, loginType);
    }

    public void checkCurrentUser(MutableLiveData<LoggedInUser> result) {
        // handle login
        dataSource.getCurrentUser(result);
    }
}
