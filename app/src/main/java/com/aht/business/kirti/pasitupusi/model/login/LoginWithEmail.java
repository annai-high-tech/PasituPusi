package com.aht.business.kirti.pasitupusi.model.login;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;

import com.aht.business.kirti.pasitupusi.R;
import com.aht.business.kirti.pasitupusi.model.login.data.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginWithEmail {

    private static final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private MutableLiveData<LoggedInUser> result;
    private Activity activity;

    public void logOut() {
        firebaseAuth.signOut();
    }

    public void login(final String email, final String password, final Activity activity, final MutableLiveData<LoggedInUser> outResult) {

        this.result = outResult;
        this.activity = activity;

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (!task.isSuccessful()) {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                signInWithEmailAndPassword(email, password, activity, outResult);
                            } else {
                                result.setValue(new LoggedInUser(e.getMessage()));
                            }

                        } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            final String uid = task.getResult().getUser().getUid();
                            final String displayName = task.getResult().getUser().getEmail();

                            sendEmailVerification(user, password);

                        }

                    }
                });

    }

    public void resetPassword(final String email, final Activity activity, final MutableLiveData<LoggedInUser> outResult) {

        this.result = outResult;
        this.activity = activity;

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(activity, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        //checking if success

                        displayResetMsgBox(email, task);

                    }
                });

    }

    public void signInWithEmailAndPassword(final String email, final String password, final Activity activity, final MutableLiveData<LoggedInUser> outResult) {

        this.result = outResult;
        this.activity = activity;

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the task is successfull

                        LoggedInUser user = null;

                        if(task.isSuccessful()){

                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                            if (currentUser.isEmailVerified()) {
                                user = new LoggedInUser(
                                        currentUser.getUid(),
                                        currentUser.getEmail(),
                                        currentUser.getPhoneNumber()
                                );

                            } else {
                                displayActivationMsgBox(currentUser, password);
                                return;
                                //user = new LoggedInUser(getMessage(R.string.invalid_userNotVerified));
                            }
                        } else {
                            user = new LoggedInUser(getMessage(R.string.invalid_userDetails));
                        }

                        result.setValue(user);
                    }
                });

    }


    private String getMessage(int id) {
        return activity.getResources().getString(id);
    }

    private void sendEmailVerification(final FirebaseUser user, final String password) {

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            displayActivationMsgBox(user, password);

                        } else {
                            Exception e = task.getException();

                            if(e instanceof FirebaseTooManyRequestsException) {
                                result.setValue(new LoggedInUser(getMessage(R.string.invalid_tooManyResend)));
                            } else {
                                result.setValue(new LoggedInUser(task.getException().getMessage()));
                            }
                        }
                    }
                });
    }

    private void displayActivationMsgBox(final FirebaseUser user, final String password) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder = builder.setMessage(
                getMessage(R.string.login_activation))
                .setCancelable(false)
                .setPositiveButton(getMessage(R.string.login_verify_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                login(user.getEmail(), password, activity, result);
                            }
                        })
                .setNegativeButton(getMessage(R.string.login_resend_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                sendEmailVerification(user, password);
                            }
                        })
                .setNeutralButton(getMessage(R.string.login_cancel_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                LoggedInUser user = new LoggedInUser(getMessage(R.string.invalid_userNotVerified));
                                result.setValue(user);

                        /*firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    System.out.println("User account not deleted.");
                                }
                            }
                        });*/
                            }
                        });

        builder.create().show();

    }

    private void displayResetMsgBox(final String email, Task task) {

        String message = "Some Error";

        if(task.isSuccessful()) {
            message = getMessage(R.string.login_reset_password);
        } else {
            message = task.getException().getMessage();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder = builder.setMessage(message)
                .setCancelable(false)
                .setNeutralButton(getMessage(R.string.login_ok_button),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                LoggedInUser user = new LoggedInUser(getMessage(R.string.invalid_userNotVerified));
                                result.setValue(user);

                            }
                        });

        builder.create().show();

    }
}
