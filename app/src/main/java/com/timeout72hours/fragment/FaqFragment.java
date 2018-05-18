package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.timeout72hours.R;
import com.timeout72hours.adapter.FAQAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.FaqResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bhumit on 2/12/17.
 */

public class FaqFragment extends Fragment {
    private Context mContext;
    private View view;

    private RecyclerView rv_faq_list;
    private FAQAdapter faqAdapter;
    private APIInterface apiInterface;

    private CustomProgressDialog progressDialog;
    private boolean api_response=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getActivity().getLayoutInflater().inflate(R.layout.fragment_faq, null);

        initUi();

        return view;
    }

    private void initUi() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        rv_faq_list = (RecyclerView) view.findViewById(R.id.rv_faq_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_faq_list.setLayoutManager(layoutManager);

        getFaqList();
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

    private void getFaqList() {
        if (Utility.isNetworkAvaliable(mContext)) {

            Call<FaqResponse> call = apiInterface.getFaqs();

            call.enqueue(new Callback<FaqResponse>() {
                @Override
                public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    if (response.isSuccessful() && response.body() != null) {

                        faqAdapter = new FAQAdapter(mContext, response.body().getFaq(),FaqFragment.this);
                        rv_faq_list.setAdapter(faqAdapter);
                    }
                }

                @Override
                public void onFailure(Call<FaqResponse> call, Throwable t) {
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

    public void scrooltoposition(int pos){
        rv_faq_list.getLayoutManager().scrollToPosition(pos);
    }

}
