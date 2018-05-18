package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhumit on 3/12/17.
 */

public class AboutUsFragment extends Fragment {
    private Context mContext;
    private View view;

    private TextView tvTitle, tvDesc;
    private APIInterface apiInterface;

    private CustomProgressDialog progressDialog;
    private boolean api_response=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, null);

        initUi();

        return view;
    }

    private void initUi() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvDesc = (TextView) view.findViewById(R.id.tv_desc);

        getAboutData();
        Handler hnd = new Handler();
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!api_response){
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
            }
        },1000);
    }

    private void getAboutData() {
        if (Utility.isNetworkAvaliable(mContext)) {

            Call<String> call = apiInterface.getAboutEvent();

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());

                            if (jsonObject.getString("response").equalsIgnoreCase("True")) {
                                String text = jsonObject.getString("description");
                                tvTitle.setText(jsonObject.getString("title"));

                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    CharSequence sequence = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
                                    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                                    tvDesc.setText(strBuilder);
                                } else {
                                    CharSequence sequence = Html.fromHtml(text);
                                    SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                                    tvDesc.setText(strBuilder);

                                }
                            } else {
                                Utility.showToast(getActivity(), jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }
}
