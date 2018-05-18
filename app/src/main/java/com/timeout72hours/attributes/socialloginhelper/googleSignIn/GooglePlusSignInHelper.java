package com.timeout72hours.attributes.socialloginhelper.googleSignIn;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.timeout72hours.attributes.Constant;

import java.util.Random;

/**
 * Created by bhumit on 19/4/17.
 */
public class GooglePlusSignInHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int SIGN_IN_REQUEST_CODE = 101;
    private static final int ERROR_DIALOG_REQUEST_CODE = 102;
    private static final int PERMISSIONS_REQUEST_GET_ACCOUNTS = 103;

    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private FragmentActivity mContext;
    private GoogleResponseListener mListener;

    private boolean isResolutionInProgress;

    public GooglePlusSignInHelper(FragmentActivity context, GoogleResponseListener listener) {
        mContext = context;
        mListener = listener;

        buildGoogleApiClient();
    }


    private void buildGoogleApiClient() {
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();


//        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//            signOut();
//            mGoogleApiClient.stopAutoManage(mContext);
//            mGoogleApiClient.disconnect();
//        }
        Random r = new Random();
        int Low = 10;
        int High = 100;
        int Result = r.nextInt(High-Low) + Low;
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .enableAutoManage(mContext /* FragmentActivity */,Result, this/* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


    }

    public void performSignIn() {
        mGoogleApiClient.disconnect();
        if (!mGoogleApiClient.isConnecting()) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            mContext.startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GooglePlusSignInHelper.SIGN_IN_REQUEST_CODE) {
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(Constant.TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mListener.onGSignInSuccess(acct);
        } else {
            // Signed out, show unauthenticated UI.
            mListener.onGSignInFail();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMISSIONS_REQUEST_GET_ACCOUNTS);
        } else {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            mListener.onGSignInFail();

            //check if the error has it's own resolution??
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), mContext, ERROR_DIALOG_REQUEST_CODE).show();
        } else if (!isResolutionInProgress) {
            processSignInError(connectionResult);
        }
    }

    private void processSignInError(ConnectionResult connectionResult) {
        if (connectionResult != null && connectionResult.hasResolution()) {
            try {
                isResolutionInProgress = true;
                connectionResult.startResolutionForResult(mContext, SIGN_IN_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                isResolutionInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
}
