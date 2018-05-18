package com.timeout72hours.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.timeout72hours.R;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CountryData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhumit on 2/12/17.
 */

public class SocialVerificationFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View view;
    private TextView tvTitle;

    private EditText etMobile,etEmail, etOtp,etCountry;
    private Button btn_ok;

    private APIInterface apiInterface;
    private CustomProgressDialog progressDialog;
    private SmsVerifyCatcher smsVerifyCatcher;
    private LinearLayout ll_detail;
    private String[] countryId, countryName;
    private String selectedCountryId = "";
    private String email="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_social_verification, null);

        initUi();

        return view;
    }

    private void initUi() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        ll_detail =  view.findViewById(R.id.ll_detail);
        etMobile =  view.findViewById(R.id.et_mobile);
        etEmail =  view.findViewById(R.id.et_email);
        etCountry = (EditText) view.findViewById(R.id.et_Country);
        etCountry.setOnClickListener(this);
        etOtp =  view.findViewById(R.id.et_otp);
        tvTitle = view.findViewById(R.id.tv_title);

        btn_ok = (Button) view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etEmail.getText().toString().length() == 0) {
                    Utility.showToast(getActivity(), "Enter Email ID");
                } else if (etMobile.getText().toString().length() == 0) {
                    Utility.showToast(getActivity(), "Enter Mobile Number");
                } else if (etCountry.getText().toString().length() == 0) {
                    Utility.showToast(getActivity(), "Select Your Country");
                } else if (etMobile.getText().toString().length() != 10) {
                    Utility.showToast(getActivity(), "Enter Valid Mobile Number");
                } else if (!Utility.isValidEmail(etEmail.getText().toString())) {
                    Utility.showToast(getActivity(), "Enter Valid Email ID");
                }  else {

                        mobileUpdate();

                }
            }
        });
        smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                Log.e("AutoSMS", ""+message);
//                String code[] = message.split("\\.");
//                String number = code[0];
//                number = number.substring((number.length() - 5), number.length());
                if (message.contains("OTP")) {
                    String code = message.replaceAll("[^0-9]", "");
                    ; //Parse verification code
                    Log.e("AutoSMS2", code);
                    etOtp.setText(code); //set code in edit text

                    if (etOtp.getText().toString().length()==6){
                        etOtp.setSelection(6);
                        VerifyOTP(etMobile.getText().toString());
                    }

                }
                //then you can send verification code to server
            }
        });
        getCountries();
        if(this.getArguments()!=null){
            email = getArguments().getString("email");
            if (email.trim().length()>0 && Utility.isValidEmail(email)){
                etEmail.setText(email);
                etEmail.setFocusable(false);
            }
        }
    }

    private void getCountries() {
        if (Utility.isNetworkAvaliable(mContext)) {
            Call<CountryData> call = apiInterface.getCountries();

            call.enqueue(new Callback<CountryData>() {
                @Override
                public void onResponse(Call<CountryData> call, Response<CountryData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CountryData countryData = response.body();

                        countryId = new String[countryData.getContries_list().size()];
                        countryName = new String[countryData.getContries_list().size()];

                        for (int i = 0; i < countryData.getContries_list().size(); i++) {
                            countryId[i] = countryData.getContries_list().get(i).getId();
                            countryName[i] = countryData.getContries_list().get(i).getName();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountryData> call, Throwable t) {
                 //   Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    private void mobileUpdate() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            apiInterface.mobileUpdate(etMobile.getText().toString().trim(), Utility.getAppPrefString(mContext, Constant.SOCIAL_ID),etEmail.getText().toString().trim(),selectedCountryId).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().get("response").getAsString().equalsIgnoreCase("true")){
                            etOtp.setAlpha(0f);

                            etOtp.animate().setDuration(200).alpha(1).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    etOtp.setVisibility(View.VISIBLE);
                                }
                            });

                            ll_detail.animate().setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    ll_detail.setVisibility(View.GONE);
                                    tvTitle.setVisibility(View.VISIBLE);
                                    if (selectedCountryId.equalsIgnoreCase("99")){
                                        tvTitle.setText("Please verify OTP sent on " + etMobile.getText().toString()+" / "+etEmail.getText().toString());
                                    } else {
                                        tvTitle.setText("Please verify OTP sent on " + etEmail.getText().toString());
                                    }

                                    btn_ok.setText("Verify");

                                    btn_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            VerifyOTP(etMobile.getText().toString());
                                        }
                                    });
                                }
                            });
                        } else {
                            Utility.showToast(getActivity(),response.body().get("message").getAsString());
                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {

        }
    }

    private void VerifyOTP(String user) {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            apiInterface.verifyOTP(user, etOtp.getText().toString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get("response").getAsString().equalsIgnoreCase("true")) {
                            Utility.showToastSuccess(getActivity(), response.body().get("message").getAsString());
                            Utility.writeSharedPreferences(getActivity(), Constant.USER_ID, response.body().get("users_id").getAsString());
                            Utility.writeSharedPreferences(getActivity(), Constant.NAME, response.body().get("user_name").getAsString());
                            Utility.writeSharedPreferences(getActivity(), Constant.EMAIL, response.body().get("email").getAsString());
                            Utility.writeSharedPreferences(getActivity(), Constant.MOBILE, response.body().get("mobile").getAsString());
                            Utility.writeSharedPreferences(getActivity(), Constant.IMAGE, response.body().get("image").getAsString());
                            Utility.writeSharedPreferences(getActivity(), Constant.SERIAL_NUMBER, response.body().get("serial_no").getAsString());
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            getActivity().finish();
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
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.et_Country:
                showCountryList();
                break;

        }

    }

    public void showCountryList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
        builder.setItems(countryName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etCountry.setText(countryName[which]);
                selectedCountryId = countryId[which];
            }
        });
        builder.setTitle("Select Country");
        builder.show();
    }

}
