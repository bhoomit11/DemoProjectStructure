package com.timeout72hours.attributes.FCM;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;

/**
 * Created by bhumit.belani on 11/24/2016.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(Constant.TAG, "Refreshed token: " + refreshedToken);
        // TODO: Implement this method to send any registration to your app's servers.
        Utility.writeSharedPreferences(getApplicationContext(),Constant.ARG_FIREBASE_TOKEN, refreshedToken);
    }
}