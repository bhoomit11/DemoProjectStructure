package com.timeout72hours.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.timeout72hours.R;
import com.timeout72hours.fragment.ForgotPasswordFragment;
import com.timeout72hours.fragment.LoginFragment;
import com.timeout72hours.fragment.NewPasswordFragment;
import com.timeout72hours.fragment.OTPFragment;
import com.timeout72hours.fragment.SignUpFragment;
import com.timeout72hours.fragment.SocialVerificationFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class LoginRegisterActivity extends AppCompatActivity {

    private Context mContext;
    FragmentTransaction fragmentTransaction;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndAddPermission();
        }
    }

    private void checkAndAddPermission() {
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WriteExternalStorage");
        if (!addPermission(permissionsList, Manifest.permission.READ_SMS))
            permissionsNeeded.add("ReadSMS");
        if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS))
            permissionsNeeded.add("ReceiveSMS");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Access Location");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Access Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                ActivityCompat.requestPermissions(LoginRegisterActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else {
                ActivityCompat.requestPermissions(LoginRegisterActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
        }

    }

    // Ask for permission
    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void initUI() {
        mContext = LoginRegisterActivity.this;
        changefragment(new LoginFragment(), "");

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.timeout72hours", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    public void changefragment(Fragment fragment, String email) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (!(fragment instanceof LoginFragment)) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if (fragment instanceof SignUpFragment || fragment instanceof ForgotPasswordFragment
                || fragment instanceof SocialVerificationFragment) {
            fragmentTransaction.addToBackStack("home");
            if (fragment instanceof ForgotPasswordFragment) {
                if ((getSupportFragmentManager().findFragmentById(R.id.fl_container)) instanceof SignUpFragment) {
                    Bundle bundle = new Bundle();
                    bundle.putString("Type", "SignUp");
                    fragment.setArguments(bundle);
                }
            }
            if (fragment instanceof SocialVerificationFragment) {
                Bundle bundle = new Bundle();
                bundle.putString("Type", "SignUp");
                bundle.putString("email", email);
                fragment.setArguments(bundle);
            }


        }

        fragmentTransaction.replace(R.id.fl_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getSupportFragmentManager();
        if ((getSupportFragmentManager().findFragmentById(R.id.fl_container) instanceof LoginFragment)) {
            super.onBackPressed();
        } else {
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public void toOtpFrag(String mobile, String type,String sent_on) {
        Fragment fragment = new OTPFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment);

        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        bundle.putString("type", type);
        bundle.putString("sent_on", sent_on);

        fragmentTransaction.addToBackStack("home");

        fragment.setArguments(bundle);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void toSetPassword(String mobile) {
        Fragment fragment = new NewPasswordFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, fragment);

        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        fragmentTransaction.addToBackStack("home");
        fragment.setArguments(bundle);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


}
