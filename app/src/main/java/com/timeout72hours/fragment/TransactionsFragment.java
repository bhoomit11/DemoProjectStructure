package com.timeout72hours.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.adapter.TransactionAdapter;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CommonResponse;
import com.timeout72hours.model.TransactionsListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.timeout72hours.attributes.Constant.SERIAL_NUMBER;
import static com.timeout72hours.attributes.Constant.USER_ID;

/**
 * Created by hardip on 16/12/17.
 */

public class TransactionsFragment extends android.support.v4.app.Fragment {

    private Context mContext;
    private View view;

    private Button btn_submit;
    private TextView tv_msg,tv_no_data;
    private EditText et_serial_number;

    private boolean is_added = false;

    private APIInterface apiInterface;
    private CustomProgressDialog mProgressDialog;
    private String mobile_number = "";
    private String serial_number="";
    private LinearLayout ll_serial_number,ll_transactions;

    private RecyclerView rv_transaction_list;
    private EditText et_dates;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_transactions,null);
        init();
        return view;
    }

    private void init(){
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        btn_submit = view.findViewById(R.id.btn_submit);
        tv_msg = view.findViewById(R.id.tv_msg);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        et_serial_number = view.findViewById(R.id.et_serial_number);
        ll_serial_number = view.findViewById(R.id.ll_serial_number);
        ll_transactions = view.findViewById(R.id.ll_transactions);
        et_dates = view.findViewById(R.id.et_dates);

        rv_transaction_list = view.findViewById(R.id.rv_transaction_list);

        rv_transaction_list = (RecyclerView) view.findViewById(R.id.rv_transaction_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_transaction_list.setLayoutManager(layoutManager);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if (et_serial_number.getText().toString().trim().length()>0){
                       if (!is_added){
                           submitSerialNumber();
                       } else {
                           if (et_serial_number.getText().toString().trim().equals(mobile_number.trim())){
                               getTransactions(serial_number);
                           } else {
                               Utility.showToast(getActivity(),"Please enter valid mobile number");
                           }

                       }
                   }

            }
        });

        if (Utility.getAppPrefString(mContext,SERIAL_NUMBER).trim().length()>0){

            getTransaction();

        } else {
            ll_serial_number.setVisibility(View.VISIBLE);
        }

        et_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatesDialog();
            }
        });

    }
    private String[] dates={"2017-12-27","2017-12-28","2017-12-29"}, datename={"27 DEC 2017","28 DEC 2017","29 DEC 2017"};
    private String selectedDate="2017-12-27";
    private void openDatesDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
        builder.setItems(datename, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_dates.setText(datename[which]);
                selectedDate = dates[which];
                getTransactions(Utility.getAppPrefString(mContext,SERIAL_NUMBER));
            }
        });
        builder.setTitle("Select Date");
        builder.show();
    }

    private void submitSerialNumber(){

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            apiInterface.addSerialNumber(et_serial_number.getText().toString().trim()).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body()!=null && response.body().getResponse().equals("true")) {
                        serial_number = et_serial_number.getText().toString().trim();
                        et_serial_number.setInputType(InputType.TYPE_CLASS_NUMBER);
                        et_serial_number.setHint("Enter Mobile Number");
                        et_serial_number.setText("");

                        if (response.body().getMobile().length()>3){
                            mobile_number = response.body().getMobile();
                            String mobile = mobile_number.substring(mobile_number.length()-3);
                            tv_msg.setText("Please Verify Your Mobile Number XXXXXXX"+mobile);
                            is_added = true;

                        }

                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
        }


    }

    private void getTransactions(final String serial_number){

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            apiInterface.getTransactions(selectedDate,serial_number,Utility.getAppPrefString(mContext,USER_ID)).enqueue(new Callback<TransactionsListResponse>() {
                @Override
                public void onResponse(Call<TransactionsListResponse> call, Response<TransactionsListResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body()!=null && response.body().getResponse().equals("true")) {
                        ll_serial_number.setVisibility(View.GONE);
                         Utility.writeSharedPreferences(mContext,SERIAL_NUMBER,serial_number);
                        ll_transactions.setVisibility(View.VISIBLE);
                         if (response.body().getTransaction_data()==null || response.body().getTransaction_data().size()==0){
                             tv_no_data.setVisibility(View.VISIBLE);
                             rv_transaction_list.setVisibility(View.GONE);
                         } else {
                             tv_no_data.setVisibility(View.GONE);
                             rv_transaction_list.setVisibility(View.VISIBLE);
                             TransactionAdapter transactionAdapter = new TransactionAdapter(mContext,response.body().getTransaction_data());
                             rv_transaction_list.setAdapter(transactionAdapter);
                         }

                    }
                }

                @Override
                public void onFailure(Call<TransactionsListResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
        }
    }

    private void getTransaction(){

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            apiInterface.getTransaction(selectedDate,Utility.getAppPrefString(mContext,USER_ID)).enqueue(new Callback<TransactionsListResponse>() {
                @Override
                public void onResponse(Call<TransactionsListResponse> call, Response<TransactionsListResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body()!=null && response.body().getResponse().equals("true")) {
                        ll_serial_number.setVisibility(View.GONE);
                        ll_transactions.setVisibility(View.VISIBLE);
                        if (response.body().getTransaction_data()==null || response.body().getTransaction_data().size()==0){
                            tv_no_data.setVisibility(View.VISIBLE);
                            rv_transaction_list.setVisibility(View.GONE);
                        } else {
                            tv_no_data.setVisibility(View.GONE);
                            rv_transaction_list.setVisibility(View.VISIBLE);
                            TransactionAdapter transactionAdapter = new TransactionAdapter(mContext,response.body().getTransaction_data());
                            rv_transaction_list.setAdapter(transactionAdapter);
                        }

                    }
                }

                @Override
                public void onFailure(Call<TransactionsListResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
        }
    }

}
