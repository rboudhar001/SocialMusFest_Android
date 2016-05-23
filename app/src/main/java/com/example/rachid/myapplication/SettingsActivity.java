package com.example.rachid.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Rachid on 25/03/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    public static Activity activity;

    private Button buttonChangePassword;
    private Button buttonChangeLanguage;

    private static MyNetwork myNetwork;

    private static ProgressDialog mProgressDialog;

    private static Handler handler;
    private static Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        activity = this;

        //AÑADIDO: BUTTON CHANGE PASSWORD
        // ----------------------------------------------------------------------------------------
        buttonChangePassword = (Button) findViewById(R.id.settings_button_changePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Cambiar la contraseña de usuario
                showAlertDialogChangePassword();
            }
        });
        //-----------------------------------------------------------------------------------------

        //AÑADIDO: BUTTON IDIOMA
        // ----------------------------------------------------------------------------------------
        buttonChangeLanguage = (Button) findViewById(R.id.settings_button_changeLanguage);
        buttonChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Cambiar el idioma de la aplicacion
                showAlertDialogChangeLanguage();
            }
        });
        //-----------------------------------------------------------------------------------------
    }

    //AÑADIDO:
    // ----------------------------------------------------------------------------------------
    //
    private void showAlertDialogChangePassword() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.settings_changePassowrd));

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_settings_change_password, null);
        dialog.setView(dialogView);

        final AutoCompleteTextView mOldPasswordView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_old_password);
        final AutoCompleteTextView mNewPasswordView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_new_password);
        final AutoCompleteTextView mRepeatNewPasswordView = (AutoCompleteTextView) dialogView.findViewById(R.id.dialog_repeat_new_password);

        dialog.setNegativeButton(getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.text_done), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Reset errors.
                mOldPasswordView.setError(null);
                mNewPasswordView.setError(null);
                mRepeatNewPasswordView.setError(null);

                // Store values at the time of the login attempt.
                final String mOldPassword = mOldPasswordView.getText().toString();
                final String mNewPassword = mNewPasswordView.getText().toString();
                final String mRepeatNewPassword = mRepeatNewPasswordView.getText().toString();

                boolean cancel = false;
                View focusView = null;

                // Check for a valid Old Password
                if ((!TextUtils.isEmpty(mOldPassword)) && (mOldPassword.length() > 4)) {
                    mOldPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mOldPasswordView;
                    cancel = true;
                }
                // Check for a valid New Password
                if ((!TextUtils.isEmpty(mNewPassword)) && (mNewPassword.length() > 4)) {
                    mNewPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mNewPasswordView;
                    cancel = true;
                }
                // Check for a valid Repeat New Password and is same New Password
                if ((!TextUtils.isEmpty(mRepeatNewPassword)) && (mRepeatNewPassword.length() > 4)) {
                    mRepeatNewPasswordView.setError(getString(R.string.error_invalid_password));
                    focusView = mRepeatNewPasswordView;
                    cancel = true;
                } else if (!mNewPassword.equals(mRepeatNewPassword)) {
                    mRepeatNewPasswordView.setError(getString(R.string.error_password_not_same));
                    focusView = mRepeatNewPasswordView;
                    cancel = true;
                }

                if (cancel) {
                    Log.i(TAG, "ENTRO A Settings:DialogChangePassword: ERROR_PARAMETERS");

                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    // Show a progress spinner and connect for change password
                    // ----------------------------------------------------------------------------------------
                    //showProgressDialog();
                    myNetwork = new MyNetwork(TAG, activity);
                    myNetwork.Connect();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (myNetwork.isConnected()) {

                                if (myNetwork.isLoggedIn()) {

                                    // TODO: Actualizar la contraseña en la base de datos del servidor
                                    myNetwork.changePassword(MyState.getUser().getID(), mOldPassword, mNewPassword);
                                    Log.i(TAG, "ENTRO A Settings:DialogChangePassword: PASSWORD CHANGED");

                                    myNetwork.Disconnect();
                                    Log.i(TAG, "ENTRO A Settings:DialogChangePassword: DISCONNECT");

                                    hideProgressDialog();
                                    // ----------------------------------------------------------------------------------------

                                } else {
                                    Log.i(TAG, "ENTRO A Settings:DialogChangePassword: NO_LOGGIN_IN");
                                    Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_password), Toast.LENGTH_SHORT).show();
                                    hideProgressDialog();
                                }

                            } else {
                                Log.i(TAG, "ENTRO A Settings:DialogChangePassword: NO_CONNECT");
                                Toast.makeText(getBaseContext(), getString(R.string.error_could_not_update_password), Toast.LENGTH_SHORT).show();
                                hideProgressDialog();
                            }
                        }
                    },5000);
                    // ----------------------------------------------------------------------------------------
                    //dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    //
    private void showAlertDialogChangeLanguage() {

    }
    //-----------------------------------------------------------------------------------------

    // **********
    // FUNTIONS
    // **********
    // ----------------------------------------------------------------------------------------
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
        }
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    // ----------------------------------------------------------------------------------------
}
