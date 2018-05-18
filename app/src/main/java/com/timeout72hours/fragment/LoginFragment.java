package com.timeout72hours.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.JsonObject;
import com.timeout72hours.R;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.attributes.socialloginhelper.facebookSignIn.FacebookHelper;
import com.timeout72hours.attributes.socialloginhelper.facebookSignIn.FacebookResponse;
import com.timeout72hours.attributes.socialloginhelper.facebookSignIn.FacebookUser;
import com.timeout72hours.attributes.socialloginhelper.googleSignIn.GooglePlusSignInHelper;
import com.timeout72hours.attributes.socialloginhelper.googleSignIn.GoogleResponseListener;
import com.timeout72hours.model.UserDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhumit on 29/11/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener, FacebookResponse, GoogleResponseListener {
    private Context mContext;
    private View view;
    private Button btnSignUp, btnLogin;
    private TextView tvForgetPass;
    private EditText etUsername, etPassword;

    private ImageView mBtnGoogleSignIn;
    private ImageView mBtnFacebookSignIn;
    private ImageView mBtnTwitterSignIn;

    private APIInterface apiInterface;

    private GooglePlusSignInHelper mGHelper = null;
    private FacebookHelper mFbHelper = null;

    private final int GOOGLE_SIGN_IN_REQUEST_CODE = 101;
    private final int TWITTER_SIGN_IN_REQUEST_CODE = 140;

    private UserDetails userDetails;
    private CustomProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, null);

        initUi();

        return view;
    }

    private void initUi() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);

        btnSignUp = (Button) view.findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);

        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        tvForgetPass = (TextView) view.findViewById(R.id.tv_forget_pass);
        tvForgetPass.setOnClickListener(this);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (mGHelper == null) {
            //Google api initialization
            mGHelper = new GooglePlusSignInHelper(getActivity(), (GoogleResponseListener) LoginFragment.this);
        }

        if (mFbHelper == null) {
            //fb api initialization
            mFbHelper = new FacebookHelper((FacebookResponse) LoginFragment.this,
                    "id,name,email,gender,birthday,picture.type(large),cover",
                    getActivity());
        }
        mBtnGoogleSignIn = (ImageView) view.findViewById(R.id.btn_google_sign_in_button);
        mBtnFacebookSignIn = (ImageView) view.findViewById(R.id.bt_act_login_fb);
        mBtnTwitterSignIn = (ImageView) view.findViewById(R.id.bt_act_login_twitter);
        mBtnGoogleSignIn.setOnClickListener(this);
        mBtnFacebookSignIn.setOnClickListener(this);
        mBtnTwitterSignIn.setOnClickListener(this);

        etUsername = (EditText) view.findViewById(R.id.et_username);
        etPassword = (EditText) view.findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                ((LoginRegisterActivity) getActivity()).changefragment(new SignUpFragment(), "");
                break;
            case R.id.btn_login:
                if (etUsername.getText().toString().length() != 0 &&
                        etPassword.getText().toString().length() != 0) {
                    if (TextUtils.isDigitsOnly(etUsername.getText().toString())) {
                        if (etUsername.getText().toString().length() == 10) {
                            performLogin();
                        } else {
                            Utility.showToast(getActivity(), "Enter Valid Mobile Number");
                        }
                    } else {
                        if (Utility.isValidEmail(etUsername.getText().toString())) {
                            performLogin();
                        } else {
                            Utility.showToast(getActivity(), "Enter Valid Email ID");
                        }
                    }
                } else {
                    if (etUsername.getText().toString().length() == 0 && etPassword.getText().toString().length() == 0) {
                        Utility.showToast(getActivity(), "Enter Username & Password");
                    } else {
                        if (etUsername.getText().toString().length() == 0) {
                            Utility.showToast(getActivity(), "Enter Username");
                        } else if (etPassword.getText().toString().length() == 0) {
                            Utility.showToast(getActivity(), "Enter Password");
                        }
                    }
                }
                break;
            case R.id.tv_forget_pass:
                ((LoginRegisterActivity) getActivity()).changefragment(new ForgotPasswordFragment(), "");
                break;
            case R.id.btn_google_sign_in_button:
                mGHelper.performSignIn();
                break;
            case R.id.bt_act_login_fb:
                mFbHelper.performSignIn(getActivity());
                break;
            case R.id.bt_act_login_twitter:
//                mTwitterHelper.performSignIn();
                break;
            default:
                break;
        }
    }

    private void performLogin() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            Call<JsonObject> call = apiInterface.login(etUsername.getText().toString(),
                    etPassword.getText().toString(),
                    Utility.getAppPrefString(getActivity(), Constant.ARG_FIREBASE_TOKEN), "android");

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get("response").getAsString().equalsIgnoreCase("true")) {
//
                            if (response.body().get("mobile_verify").getAsString().equalsIgnoreCase("0")) {
                                sendOTP(response.body().get("mobile").getAsString());
                            } else {
                                Utility.writeSharedPreferences(mContext, Constant.USER_ID, response.body().get("users_id").getAsString());
                                Utility.writeSharedPreferences(mContext, Constant.NAME, response.body().get("user_name").getAsString());
                                Utility.writeSharedPreferences(mContext, Constant.EMAIL, response.body().get("email").getAsString());
                                Utility.writeSharedPreferences(mContext, Constant.MOBILE, response.body().get("mobile").getAsString());
                                Utility.writeSharedPreferences(mContext, Constant.IMAGE, response.body().get("image").getAsString());
                                Utility.writeSharedPreferences(mContext, Constant.SERIAL_NUMBER, response.body().get("serial_no").getAsString());

                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                getActivity().finish();
                            }
                        } else {
                            Utility.showToast(getActivity(), response.body().get("message").getAsString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

    private void sendOTP(final String mobile) {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            Call<JsonObject> call = apiInterface.forgotPassword(mobile);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        final JsonObject jsonObject = response.body();

                        if (jsonObject.get("response").getAsString().equalsIgnoreCase("true")) {
                            ((LoginRegisterActivity) getActivity()).toOtpFrag(mobile, "login", jsonObject.get("sent_on").getAsString());
                        } else {
                            Utility.showToast(getActivity(), jsonObject.get("message").getAsString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        Log.e("ReqCode", requestCode + "");

        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            mGHelper.onActivityResult(requestCode, responseCode, intent);
        } else {
            mFbHelper.onActivityResult(requestCode, responseCode, intent);
        }
    }

    @Override
    public void onFbSignInFail() {

    }

    @Override
    public void onFbSignInSuccess() {

    }

    @Override
    public void onFbProfileReceived(FacebookUser facebookUser) {

        userDetails = new UserDetails();
        if (facebookUser.facebookID != null) {
            userDetails.setID(facebookUser.facebookID);
            if (facebookUser.name != null) {
                userDetails.setName(facebookUser.name);
            }
            if (facebookUser.gender != null) {
                if (facebookUser.gender.equalsIgnoreCase("male")) {
                    userDetails.setGender("Male");
                } else {
                    userDetails.setGender("Female");
                }
            }
            if (facebookUser.email != null) {
                userDetails.setEmail(facebookUser.email);
            }
            if (facebookUser.profilePic != null) {
                userDetails.setProfilePic(facebookUser.profilePic);
            }
            if (facebookUser.coverPicUrl != null) {
                userDetails.setCoverPicUrl(facebookUser.coverPicUrl);
            }

            performSocialLogin("facebook");
        }

    }

    @Override
    public void onGSignInFail() {

    }

    @Override
    public void onGSignInSuccess(GoogleSignInAccount accountDetails) {

//        accountDetails.get();

        userDetails = new UserDetails();
        if (accountDetails.getId() != null) {
            userDetails.setID(accountDetails.getId());
        }
        if (accountDetails.getDisplayName() != null) {
            userDetails.setName(accountDetails.getDisplayName());
        }
        if (accountDetails.getEmail() != null) {
            userDetails.setEmail(accountDetails.getEmail());
        }
        if (accountDetails.getPhotoUrl() != null) {
            userDetails.setProfilePic(accountDetails.getPhotoUrl() + "");
        }

        performSocialLogin("google");
    }

    private void performSocialLogin(String social_type) {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            Call<JsonObject> call = apiInterface.socialLogin(userDetails.getID(), social_type,
                    userDetails.getName(), userDetails.getEmail(), "", "",
                    userDetails.getGender(), "", userDetails.getProfilePic(), "", Utility.getAppPrefString(getActivity(), Constant.ARG_FIREBASE_TOKEN), "android");

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get("response").getAsString().equalsIgnoreCase("true")) {
                            Utility.writeSharedPreferences(mContext, Constant.SOCIAL_ID, response.body().get("users_id").getAsString());

                            if (response.body().get("mobile") != null && response.body().get("mobile").getAsString().equalsIgnoreCase("")) {
                                if (response.body().get("email") != null) {
                                    ((LoginRegisterActivity) mContext).changefragment(new SocialVerificationFragment(), response.body().get("email").getAsString());
                                } else {
                                    ((LoginRegisterActivity) mContext).changefragment(new SocialVerificationFragment(), "");
                                }
                            } else {
                                if (response.body().get("mobile_verify").getAsString().equalsIgnoreCase("0")) {
                                    sendOTP(response.body().get("mobile").getAsString());
                                } else {
                                    Utility.showToastSuccess(getActivity(), response.body().get("message").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.USER_ID, response.body().get("users_id").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.NAME, response.body().get("name").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.EMAIL, response.body().get("email").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.MOBILE, response.body().get("mobile").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.IMAGE, response.body().get("image").getAsString());
                                    Utility.writeSharedPreferences(mContext, Constant.SERIAL_NUMBER, response.body().get("serial_no").getAsString());

                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    getActivity().finish();
                                }
                            }
                        } else {
                            Utility.showToast(getActivity(), response.body().get("message").getAsString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }
}
