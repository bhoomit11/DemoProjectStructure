package com.timeout72hours.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.ProfileResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 13/12/17.
 */

public class UserProfileActivity extends AppCompatActivity {

    private Context mContext;
    private String user_id = "";
    private CustomProgressDialog mProgressDialog;
    private APIInterface apiInterface;
    private TextView tv_name, tv_country, tv_age, tv_gender;
    private ImageView user_profile;
    private ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        user_id = getIntent().getStringExtra("user_id");
        init();
    }

    private void init() {
        mContext = UserProfileActivity.this;

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        tv_name = findViewById(R.id.tv_name);
        tv_country = findViewById(R.id.tv_country);
        tv_age = findViewById(R.id.tv_age);
        tv_gender = findViewById(R.id.tv_gender);
        user_profile = findViewById(R.id.user_profile);

        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUserProfile();
    }

    private void getUserProfile() {
        if (Utility.isNetworkAvaliable(mContext)) {

            apiInterface.getUserProfile(user_id).enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        try {
                            tv_name.setText(response.body().getName());

                            if (response.body().getCountries_name() != null && response.body().getCountries_name().trim().length() > 0) {
                                if (response.body().getCity() != null && response.body().getCity().trim().length() > 0) {
                                    tv_country.setText(response.body().getCountries_name() + ", " + response.body().getCity());
                                } else {
                                    tv_country.setText(response.body().getCountries_name() );
                                }
                            }
                            if (response.body().getAge() != null && response.body().getAge().trim().length() > 0) {
                                tv_age.setText(response.body().getAge());
                            }
                            if (response.body().getGender() != null && response.body().getGender().trim().length() > 0) {
                                tv_gender.setText(response.body().getGender());
                            }
//
                            Picasso.with(mContext).load(response.body().getImage()).error(R.drawable.user_default).into(user_profile);
//
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });

        }


    }


}
