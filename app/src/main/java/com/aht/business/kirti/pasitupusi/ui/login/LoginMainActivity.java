package com.aht.business.kirti.pasitupusi.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.ui.main.MainActivity;

import static com.aht.business.kirti.pasitupusi.ui.login.LoginType.EMAIL_ID;
import static com.aht.business.kirti.pasitupusi.ui.login.LoginType.PHONE;

public class LoginMainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView resetButton;
    private ProgressBar loadingProgressBar;
    private RadioGroup radioGroupLoginType;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        resetButton = findViewById(R.id.reset);
        loadingProgressBar = findViewById(R.id.loading);
        radioGroupLoginType = findViewById(R.id.radioGroupLogin);

        usernameEditText.addTextChangedListener(mTextWatcher);
        passwordEditText.addTextChangedListener(mTextWatcher);
        radioGroupLoginType.setOnCheckedChangeListener(mOnCheckedChangeListener);
        loginButton.setOnClickListener(mOnClickListener);
        resetButton.setOnClickListener(mOnClickListener);
        loginViewModel.getLoginFormState().observe(this, mObserver);
        loginViewModel.getLoginResult().observe(this, mObserverResult);

        loginViewModel.checkCurrentUser(this);

        radioGroupLoginType.check(R.id.radioButtonPhone);
        //((RadioButton)findViewById(R.id.radioButtonPhone)).setChecked(true);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

            LoginType loginType = EMAIL_ID;
            if(radioGroupLoginType.getCheckedRadioButtonId() == findViewById(R.id.radioButtonPhone).getId()) {
                loginType = PHONE;
            }

            loginViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString(), loginType);

        }
    };

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            loadingProgressBar.setVisibility(View.VISIBLE);

            LoginType loginType = EMAIL_ID;
            if(radioGroupLoginType.getCheckedRadioButtonId() == findViewById(R.id.radioButtonPhone).getId()) {
                loginType = PHONE;
            }

            if(view.getId() == loginButton.getId()) {
                String buttonText = loginButton.getText().toString();
                if (buttonText.equals(getResources().getString(R.string.action_sign_in))) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), LoginMainActivity.this, loginType);
                }
            }

            if(view.getId() == resetButton.getId()) {
                loginViewModel.resetPassword(usernameEditText.getText().toString(), LoginMainActivity.this, loginType);
            }

        }
    };

    RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int selectedButtonId) {

            if(selectedButtonId == findViewById(R.id.radioButtonEmail).getId()) {

                usernameEditText.setHint(getResources().getString(R.string.prompt_email));
                passwordEditText.setHint(getResources().getString(R.string.prompt_email_password));
                passwordEditText.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);

            }

            if(selectedButtonId == findViewById(R.id.radioButtonPhone).getId()) {

                usernameEditText.setHint(getResources().getString(R.string.prompt_phone));
                //passwordEditText.setHint(getResources().getString(R.string.prompt_phone_password));
                //passwordEditText.setEnabled(false);
                passwordEditText.setText("");
                passwordEditText.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.INVISIBLE);

            }
        }

    };

    Observer<LoginFormState> mObserver = new Observer<LoginFormState>() {
        @Override
        public void onChanged(@Nullable LoginFormState loginFormState) {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            resetButton.setEnabled(loginFormState.isUserNameValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        }
    };

    Observer<LoginResult> mObserverResult = new Observer<LoginResult>() {
        @Override
        public void onChanged(@Nullable LoginResult loginResult) {

            loadingProgressBar.setVisibility(View.GONE);

            if (loginResult == null) {
                return;
            }

            if(loginResult.isNeedInfomation()) {
                passwordEditText.setHint(getResources().getString(R.string.prompt_phone_password));
                passwordEditText.setVisibility(View.VISIBLE);
            }

            if (loginResult.getSuccess() != null) {
                Toast.makeText(LoginMainActivity.this, "Login Success: " + loginResult.getSuccess().getUserId(), Toast.LENGTH_LONG).show();

                Intent mainPage = new Intent(LoginMainActivity.this, MainActivity.class);
                mainPage.putExtra("uid", loginResult.getSuccess().getUserId());
                LoginMainActivity.this.startActivity(mainPage);

            } else {
                Toast.makeText(LoginMainActivity.this, "Login failed: " + loginResult.getErrorMsg(), Toast.LENGTH_LONG).show();
            }
        }
    };


}