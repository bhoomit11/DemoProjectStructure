package com.timeout72hours.attributes.socialloginhelper.googleSignIn;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


/**
 * Created by multidots on 6/16/2016.
 */
public interface GoogleResponseListener {
    void onGSignInFail();

    void onGSignInSuccess(GoogleSignInAccount accountDetails);
}
