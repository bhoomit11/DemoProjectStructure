package com.timeout72hours.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.timeout72hours.R;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhumit on 3/12/17.
 */

public class OTPFragment extends Fragment {
    private Context mContext;
    private View view;
    private EditText etOtp;
    private TextView tv_title;

    private CustomProgressDialog progressDialog;
    private APIInterface apiInterface;

    private Button btn_ok;
    private String mobile="",type="",sent_on="";
    private SmsVerifyCatcher smsVerifyCatcher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_otp, null);

        initUI();

        return view;
    }

    private void initUI() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);
        etOtp = (EditText) view.findViewById(R.id.et_otp);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        tv_title = view.findViewById(R.id.tv_title);

        if(this.getArguments()!=null){
            mobile = getArguments().getString("mobile");
            type = getArguments().getString("type");
            sent_on = getArguments().getString("sent_on");
            tv_title.setText("Please verify OTP sent on "+sent_on);
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOtp.getText().toString().length()==0){
                    Utility.showToast(getActivity(),"Enter OTP to verify");
                }else {
                    if(etOtp.getText().toString().length()!=6){
                        Utility.showToast(getActivity(),"Enter Valid OTP");
                    }else {
                        VerifyOTP();
                    }
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
                        VerifyOTP();
                    }

                }
                //then you can send verification code to server
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void VerifyOTP() {
        if (Utility.isNetworkAvaliable(getActivity())) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            apiInterface.verifyOTP(mobile, etOtp.getText().toString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().get("response").getAsString().equalsIgnoreCase("true")) {
                            if (!type.equalsIgnoreCase("forgot")){
                                Utility.showToastSuccess(getActivity(), response.body().get("message").getAsString());
                                Log.e("tet",response.body().toString());
                                Utility.writeSharedPreferences(getActivity(), Constant.USER_ID, response.body().get("users_id").getAsString());
                                Utility.writeSharedPreferences(getActivity(), Constant.NAME, response.body().get("user_name").getAsString());
                                Utility.writeSharedPreferences(getActivity(), Constant.EMAIL, response.body().get("email").getAsString());
                                Utility.writeSharedPreferences(getActivity(), Constant.MOBILE, response.body().get("mobile").getAsString());
                                Utility.writeSharedPreferences(getActivity(), Constant.IMAGE, response.body().get("image").getAsString());
                                Utility.writeSharedPreferences(getActivity(), Constant.SERIAL_NUMBER, response.body().get("serial_no").getAsString());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                getActivity().finish();
                            } else {
                                ((LoginRegisterActivity)getActivity()).toSetPassword(mobile);
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

}
