package com.aht.business.kirti.pasitupusi.model.login;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.internet.InternetConnection;
import com.aht.business.kirti.pasitupusi.model.login.data.LoggedInUser;
import com.aht.business.kirti.pasitupusi.ui.login.LoginType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.aht.business.kirti.pasitupusi.ui.login.LoginType.EMAIL_ID;
import static com.aht.business.kirti.pasitupusi.ui.login.LoginType.PHONE;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private LoginWithEmail loginWithEmail = new LoginWithEmail();
    private LoginWithPhone loginWithPhone = new LoginWithPhone();

    public void logOut() {

        FirebaseAuth.getInstance().signOut();

    }

    public void getCurrentUser(MutableLiveData<LoggedInUser> result) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            String phone = currentUser.getPhoneNumber();
            result.setValue(new LoggedInUser(uid, email, phone));
        }

    }

    public void login(String email, String password, Activity activity, MutableLiveData<LoggedInUser> outResult, LoginType loginType) {

        if(!InternetConnection.isConnected(activity)) {
            outResult.setValue(new LoggedInUser(activity.getResources().getString(R.string.invalid_internetConnection)));
            return;
        }

        if(loginType == EMAIL_ID) {
            loginWithEmail.signInWithEmailAndPassword(email, password, activity, outResult);
        }
        if(loginType == PHONE) {
            loginWithPhone.login(email, activity, outResult);
        }

    }

    public void resetPassword(String email, Activity activity, MutableLiveData<LoggedInUser> outResult, LoginType loginType) {

        if(!InternetConnection.isConnected(activity)) {
            outResult.setValue(new LoggedInUser(activity.getResources().getString(R.string.invalid_internetConnection)));
            return;
        }

        if(loginType == EMAIL_ID) {
            loginWithEmail.resetPassword(email, activity, outResult);
        }
        if(loginType == PHONE) {
            loginWithPhone.login(email, activity, outResult);
        }

    }

}
