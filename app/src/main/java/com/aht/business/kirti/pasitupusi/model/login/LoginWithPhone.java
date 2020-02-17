package com.aht.business.kirti.pasitupusi.model.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.login.data.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginWithPhone {

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private static final PhoneAuthProvider firebasePhoneAuth = PhoneAuthProvider.getInstance();

    private MutableLiveData<LoggedInUser> result;
    private Activity activity;
    private String phoneNumber;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    public void login(String phoneNumber, Activity activity, MutableLiveData<LoggedInUser> outResult) {

        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        this.result = outResult;
        this.activity = activity;
        this.phoneNumber = phoneNumber;

        startPhoneNumberVerification(phoneNumber);

    }

    public void logOut() {
        firebaseAuth.signOut();
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        firebasePhoneAuth.verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        firebasePhoneAuth.verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, MutableLiveData<LoggedInUser> outResult) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        LoggedInUser user = null;
                        if (task.isSuccessful()) {

                            user = new LoggedInUser(
                                    task.getResult().getUser().getUid(),
                                    task.getResult().getUser().getEmail(),
                                    task.getResult().getUser().getPhoneNumber()
                            );

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                user = new LoggedInUser(getMessage(R.string.invalid_otp));
                            } else {
                                user = new LoggedInUser(task.getException().getMessage());
                            }
                        }
                        result.setValue(user);

                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {

            signInWithPhoneAuthCredential(credential, result);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            LoggedInUser user = null;

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                user = new LoggedInUser(getMessage(R.string.invalid_phoneDetails));

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                 user = new LoggedInUser(getMessage(R.string.invalid_tooManyResend));

            } else {
                user = new LoggedInUser(e.getMessage());
            }

            result.setValue(user);
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            System.out.println("onCodeSent : " + mVerificationId);

            displayOtpActivationMsgBox();
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(String verificationId) {

            System.out.println("onCodeAutoRetrievalTimeOut : " + verificationId);

            LoggedInUser user = new LoggedInUser(getMessage(R.string.invalid_otpTimeOut));
            result.setValue(user);
        }

    };

    private String getMessage(int id) {
        return activity.getResources().getString(id);
    }

    private void displayOtpActivationMsgBox() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // Set up the input
        final EditText input = new EditText(activity);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder = builder.setMessage(
                getMessage(R.string.login_activation_otp))
                .setCancelable(false)
                .setPositiveButton(getMessage(R.string.login_verify_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                String code = "";
                                if(input.getText() != null) {
                                    code = input.getText().toString();
                                }
                                verifyPhoneNumberWithCode(mVerificationId, code);
                            }
                        })
                .setNegativeButton(getMessage(R.string.login_resend_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                resendVerificationCode(phoneNumber, mResendToken);
                            }
                        })
                .setNeutralButton(getMessage(R.string.login_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                LoggedInUser user = new LoggedInUser(getMessage(R.string.invalid_userNotVerified));
                                result.setValue(user);

                            }
                        });

        builder.create().show();

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = null;

        try {
            credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential, result);
        } catch(IllegalArgumentException e) {
            LoggedInUser user = new LoggedInUser(getMessage(R.string.invalid_otp));
            result.setValue(user);
        } catch(Exception e) {
            LoggedInUser user = new LoggedInUser(getMessage(R.string.invalid_otp));
            result.setValue(user);
        }
    }

}
