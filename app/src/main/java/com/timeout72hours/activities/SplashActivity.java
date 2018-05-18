package com.timeout72hours.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.timeout72hours.R;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.FaqResponse;
import com.timeout72hours.model.GetVersionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Log.e(Constant.TAG, "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());

        Utility.writeSharedPreferences(getApplicationContext(),Constant.ARG_FIREBASE_TOKEN, FirebaseInstanceId.getInstance().getToken());

        checkVersion();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = null;
//                if (Utility.getAppPrefString(SplashActivity.this, Constant.USER_ID).equalsIgnoreCase("")) {
//                    intent = new Intent(SplashActivity.this, LoginRegisterActivity.class);
//                } else {
//                    intent = new Intent(SplashActivity.this, MainActivity.class);
//                }
//                startActivity(intent);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                finish();
//            }
//        }, 3000);

    }
    private String versionName="";
    private void checkVersion() {
        if (Utility.isNetworkAvaliable(mContext)) {
        PackageInfo pInfo = null;


        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        apiInterface.getVersion("android",Utility.getAppPrefString(mContext, Constant.USER_ID),Utility.getAppPrefString(SplashActivity.this, Constant.ARG_FIREBASE_TOKEN)).enqueue(new Callback<GetVersionResponse>() {
            @Override
            public void onResponse(Call<GetVersionResponse> call, Response<GetVersionResponse> response) {

                if (response.isSuccessful() && response.body()!=null && response.body().getResponse().equals("true")) {

                    if (Double.parseDouble(versionName)<Double.parseDouble(response.body().getVersion())) {

                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("New Version Available")
                                .setMessage(response.body().getMessage())
                                .setCancelable(false)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }
                                    }
                                }).show();

                    } else {
                        Intent intent = null;
                        if (Utility.getAppPrefString(SplashActivity.this, Constant.USER_ID).equalsIgnoreCase("")) {
                            intent = new Intent(SplashActivity.this, LoginRegisterActivity.class);
                        } else {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<GetVersionResponse> call, Throwable t) {

            }
        });
        } else {
            Utility.showToast(SplashActivity.this, getResources().getString(R.string.internetErrorMsg));
        }
    }


}
