package com.timeout72hours.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;

import com.timeout72hours.R;
import com.timeout72hours.adapter.TransactionDetailAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CommonResponse;
import com.timeout72hours.model.TransactionDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 20/12/17.
 */

public class TransactionDetailActivity extends AppCompatActivity {

    private Context mContext;
    private ImageView img_back;
    private RecyclerView rv_transaction_list;
    private String transaction_id;
    private APIInterface apiInterface;
    private CustomProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_transaction_detail);
        transaction_id = getIntent().getStringExtra("transaction_id");
        init();
    }

    private void init(){
        mContext = TransactionDetailActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        img_back = findViewById(R.id.img_back);
        rv_transaction_list = findViewById(R.id.rv_transaction_list);

        rv_transaction_list = (RecyclerView) findViewById(R.id.rv_transaction_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_transaction_list.setLayoutManager(layoutManager);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getTransactinDetail();

    }

    private void getTransactinDetail(){

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            apiInterface.getTransactionDetail(transaction_id).enqueue(new Callback<TransactionDetailResponse>() {
                @Override
                public void onResponse(Call<TransactionDetailResponse> call, Response<TransactionDetailResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body()!=null && response.body().getResponse().equals("true")) {

                        TransactionDetailAdapter transactionDetailAdapter = new TransactionDetailAdapter(mContext,response.body().getTransaction_detail());
                        rv_transaction_list.setAdapter(transactionDetailAdapter);

                    }

                }

                @Override
                public void onFailure(Call<TransactionDetailResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(TransactionDetailActivity.this, getResources().getString(R.string.internetErrorMsg));
        }


    }

}
