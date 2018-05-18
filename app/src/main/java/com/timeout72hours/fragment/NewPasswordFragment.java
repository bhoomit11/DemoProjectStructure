package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.timeout72hours.R;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.FaqResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhumit on 3/12/17.
 */

public class NewPasswordFragment extends Fragment {
    private Context mContext;
    private View view;
    private EditText etPassword, etConfirmPassword;

    private CustomProgressDialog progressDialog;
    private APIInterface apiInterface;

    private Button btn_ok;
    private String mobile = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password, null);
        if (this.getArguments() != null) {
            mobile = getArguments().getString("mobile");
        }
        initUI();

        return view;

    }

    private void initUI() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);

        etPassword = (EditText) view.findViewById(R.id.et_password);
        etConfirmPassword = (EditText) view.findViewById(R.id.et_confirm_password);

        btn_ok = (Button) view.findViewById(R.id.btn_ok);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().toString().length() == 0 ||
                        etConfirmPassword.getText().toString().length() == 0) {
                    Utility.showToast(getActivity(), "Please Fill Both Fields");
                } else {
                    if (!etPassword.getText().toString().equalsIgnoreCase(etConfirmPassword.getText().toString())) {
                        Utility.showToast(getActivity(), "Password & Confirm password must be same");
                    } else {
                        changePassword();
                    }
                }
            }
        });
    }

    private void changePassword() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            apiInterface.newPassOtpVerify(mobile, etPassword.getText().toString().trim()).enqueue(new Callback<FaqResponse>() {
                @Override
                public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null) {

                        if (response.body().getResponse().equalsIgnoreCase("true")) {
                            Utility.showToastSuccess(getActivity(), response.body().getMessage());
                            ((LoginRegisterActivity) mContext).changefragment(new LoginFragment(), "");
                        } else {
                            Utility.showToast(getActivity(), response.body().getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(Call<FaqResponse> call, Throwable t) {
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