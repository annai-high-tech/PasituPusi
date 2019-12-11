package com.aht.business.kirti.pasitupusi.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.aht.business.kirti.pasitupusi.model.login.LoginRepository;
import com.aht.business.kirti.pasitupusi.model.login.data.LoggedInUser;
import com.aht.business.kirti.pasitupusi.R;

import static com.aht.business.kirti.pasitupusi.ui.login.LoginType.EMAIL_ID;
import static com.aht.business.kirti.pasitupusi.ui.login.LoginType.PHONE;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoggedInUser> loggedInUser = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, AppCompatActivity activity, LoginType loginType) {

        loggedInUser.observe(activity, mObserver);

        loginRepository.login(username, password, activity, loggedInUser, loginType);

    }

    public void resetPassword(String username, AppCompatActivity activity, LoginType loginType) {

        loggedInUser.observe(activity, mObserver);

        loginRepository.resetPassword(username, activity, loggedInUser, loginType);

    }

    public void checkCurrentUser(AppCompatActivity activity) {

        loggedInUser.observe(activity, mObserver);

        loginRepository.checkCurrentUser(loggedInUser);

    }

    public void loginDataChanged(String username, String password, LoginType loginType) {

        if (!isUserNameValid(username, loginType)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, false));
        } else if (!isPasswordValid(password, loginType)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, true));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }

    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username, LoginType loginType) {
        if (username == null) {
            return false;
        }

        if (loginType == EMAIL_ID && username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else if (loginType == PHONE && Patterns.PHONE.matcher(username).matches()) {
            return true;
        } else {
            return false; //!username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password, LoginType loginType) {
        if (loginType == PHONE) {
            return true;
        } else {
            return password != null && password.trim().length() > 5;
        }
    }

    private Observer<LoggedInUser> mObserver = new Observer<LoggedInUser>() {
        @Override
        public void onChanged(@Nullable LoggedInUser loggedInUser) {
            if (loggedInUser == null) {
                loginResult.setValue(new LoginResult(R.string.login_failed, "Null user"));
            } else if (!loggedInUser.isValidUser()) {
                    loginResult.setValue(new LoginResult(R.string.login_failed, loggedInUser.getErrorMsg()));
            } else {
                loginResult.setValue(
                        new LoginResult(
                                new LoggedInUserView(loggedInUser.getUserId(),
                                        loggedInUser.getEmail(),
                                        loggedInUser.getPhone())
                        )
                );
            }
        }
    };

}
