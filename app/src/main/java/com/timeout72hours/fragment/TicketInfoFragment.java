package com.timeout72hours.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.timeout72hours.R;
import com.timeout72hours.adapter.TicketInfoAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.PassesListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 3/12/17.
 */

public class TicketInfoFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private RecyclerView rv_passes;
    private APIInterface apiInterface;
    private Button buy_ticket,buy_packages;
    private CustomProgressDialog progressDialog;
    private boolean api_response=false;
    private LinearLayout ll_buy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_ticketinfo,null);
        init();
        getPasses();
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
        return view;
    }

    private void init(){
      mContext = getActivity();
        progressDialog = new CustomProgressDialog(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        buy_ticket = view.findViewById(R.id.buy_ticket);
        buy_packages = view.findViewById(R.id.buy_packages);
        ll_buy = view.findViewById(R.id.ll_buy);

        buy_ticket.setOnClickListener(this);
        buy_packages.setOnClickListener(this);

        rv_passes = view.findViewById(R.id.rv_passes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_passes.setLayoutManager(layoutManager);
    }

    private void getPasses(){
        if (Utility.isNetworkAvaliable(mContext)) {
        apiInterface.getPasses().enqueue(new Callback<PassesListResponse>() {
            @Override
            public void onResponse(Call<PassesListResponse> call, Response<PassesListResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                api_response = true;
                if (response.isSuccessful()){
                    ll_buy.setVisibility(View.VISIBLE);
                    TicketInfoAdapter ticketInfoAdapter = new TicketInfoAdapter(mContext,response.body().getPass_detail());
                    rv_passes.setAdapter(ticketInfoAdapter);
                }

            }

            @Override
            public void onFailure(Call<PassesListResponse> call, Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                api_response = true;
            }
        });
    } else {
        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
    }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buy_ticket:
                Intent buy_ticket = new Intent(Intent.ACTION_VIEW, Uri.parse("https://insider.in/timeout-72-dec27-dec28-dec29-2017/event"));
                startActivity(buy_ticket);
                break;

            case R.id.buy_packages:
                Intent buy_packages = new Intent(Intent.ACTION_VIEW, Uri.parse("http://timeout72.roomnhouse.com/packages/"));
                startActivity(buy_packages);
                break;

        }

    }

}
