package com.timeout72hours.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
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
 * Created by bhumit on 29/11/17.
 */

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View view;
    private TextView tvTitle;

    private EditText etEmail;
    private Button btnOkay, btnCancel;

    private APIInterface apiInterface;
    private CustomProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_frgot_pass, null);

        initUi();

        return view;
    }

    private void initUi() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        etEmail = view.findViewById(R.id.et_email);

        tvTitle = view.findViewById(R.id.tv_title);

        btnOkay = view.findViewById(R.id.btn_ok);
        btnCancel = view.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(this);
        btnOkay.setOnClickListener(this);

        if (this.getArguments() != null) {
            if (this.getArguments().getString("Type").equalsIgnoreCase("SignUp")) {
                etEmail.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                getActivity().onBackPressed();
                break;
            case R.id.btn_ok:
                if (etEmail.getText().toString().length() == 0) {
                    Utility.showToast(getActivity(), "Enter Your Registerd Mobile Number");
                } else {
                    if (etEmail.getText().toString().length() == 10) {
                        performForgetPassword();
                        Utility.hideSoftKeyboard(getActivity());
                    } else {
                        Utility.showToast(getActivity(), "Enter Valid Mobile Number");
                    }
                }
                break;
        }
    }

    private void performForgetPassword() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            Call<JsonObject> call = apiInterface.forgotPassword(etEmail.getText().toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {
                        final JsonObject jsonObject = response.body();

                        if (jsonObject.get("response").getAsString().equalsIgnoreCase("true")) {
                            ((LoginRegisterActivity) getActivity()).toOtpFrag(etEmail.getText().toString().trim(),"forgot",jsonObject.get("sent_on").getAsString());
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
}
