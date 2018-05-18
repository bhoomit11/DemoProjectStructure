package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.activities.TransactionDetailActivity;
import com.timeout72hours.model.TransactionData;
import com.timeout72hours.model.TransactionDetailData;

import java.util.ArrayList;

/**
 * Created by hardip on 20/12/17.
 */

public class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<TransactionDetailData> transactionData;

    public TransactionDetailAdapter(Context mContext, ArrayList<TransactionDetailData> transactionData) {
        this.mContext = mContext;
        this.transactionData = transactionData;
    }

    @Override
    public TransactionDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_invoice_detail, parent, false);

        return new TransactionDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TransactionDetailAdapter.MyViewHolder holder, final int position) {

        holder.tv_item.setText(transactionData.get(position).getItem());
        holder.tv_qty.setText(transactionData.get(position).getQty());
        holder.tv_amount.setText(transactionData.get(position).getAmount()+"/-");


    }

    @Override
    public int getItemCount() {
        return transactionData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_item, tv_qty,tv_amount;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_item =  itemView.findViewById(R.id.tv_item);
            tv_qty =  itemView.findViewById(R.id.tv_qty);
            tv_amount =  itemView.findViewById(R.id.tv_amount);

        }
    }
}
