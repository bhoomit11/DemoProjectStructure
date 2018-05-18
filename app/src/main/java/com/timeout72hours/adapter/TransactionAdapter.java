package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.activities.TransactionDetailActivity;
import com.timeout72hours.fragment.FaqFragment;
import com.timeout72hours.model.FaqData;
import com.timeout72hours.model.TransactionData;

import java.util.ArrayList;

/**
 * Created by hardip on 16/12/17.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<TransactionData> transactionData;

    public TransactionAdapter(Context mContext, ArrayList<TransactionData> transactionData) {
        this.mContext = mContext;
        this.transactionData = transactionData;
    }

    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_transaction_list, parent, false);

        return new TransactionAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TransactionAdapter.MyViewHolder holder, final int position) {

        holder.tv_time.setText(transactionData.get(position).getDate_time ());
        holder.tv_name.setText(transactionData.get(position).getTransaction_type());
        holder.tv_amount.setText(transactionData.get(position).getAmount()+"/-");

        if (transactionData.get(position).getTransaction_type().equalsIgnoreCase("PURCHASE")){
            holder.img_arrow.setVisibility(View.GONE);
            try {
                holder.tv_name.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
                holder.tv_name.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            } catch (Exception e){}
        } else {
            holder.tv_name.setTextColor(ContextCompat.getColor(mContext,R.color.black));
            holder.img_arrow.setVisibility(View.GONE);
        }

        if (transactionData.get(position).getTransaction_type().equalsIgnoreCase("RECHARGE")){
            holder.tv_amount.setText("+"+transactionData.get(position).getAmount()+"/-");
        } else {
            holder.tv_amount.setText("-"+transactionData.get(position).getAmount()+"/-");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (transactionData.get(position).getHas_detail().equals("1")){
                    Intent in = new Intent(mContext,TransactionDetailActivity.class);
                    in.putExtra("transaction_id",transactionData.get(position).getTransaction_id());
                    mContext.startActivity(in);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return transactionData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_time, tv_name,tv_amount;
        private ImageView img_arrow;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_time =  itemView.findViewById(R.id.tv_time);
            tv_name =  itemView.findViewById(R.id.tv_name);
            tv_amount =  itemView.findViewById(R.id.tv_amount);
            img_arrow =  itemView.findViewById(R.id.img_arrow);

        }
    }
}
