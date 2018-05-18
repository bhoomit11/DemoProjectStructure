package com.timeout72hours.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.fragment.FaqFragment;
import com.timeout72hours.model.FaqData;

import java.util.ArrayList;

/**
 * Created by bhumit on 2/12/17.
 */

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<FaqData> faqData;
    private int selected_pos = -1;
    private FaqFragment faqFragment;

    public FAQAdapter(Context mContext, ArrayList<FaqData> faqData, FaqFragment faqFragment) {
        this.mContext = mContext;
        this.faqData = faqData;
        this.faqFragment = faqFragment;
    }

    @Override
    public FAQAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FAQAdapter.MyViewHolder holder, final int position) {
        holder.tvQue.setText(faqData.get(position).getQuestion());
        holder.tvAns.setText(faqData.get(position).getAnswer());

        if (faqData.get(position).isOpen()) {
            holder.llAnswer.setVisibility(View.VISIBLE);
        } else {
            holder.llAnswer.setVisibility(View.GONE);
        }

        holder.llQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (holder.llAnswer.getVisibility() == View.GONE) {
//                    faqData.get(position).setOpen(true);
//                } else {
//                    faqData.get(position).setOpen(false);
//                }
                if (selected_pos != -1) {
                    faqData.get(selected_pos).setOpen(false);
                    notifyItemChanged(selected_pos);
                }
                if (selected_pos != position) {
                    faqData.get(position).setOpen(true);
                    selected_pos = position;
                } else {
                    selected_pos = -1;
                }

                notifyItemChanged(position);
                //if (position==(faqData.size()-1)){
                   faqFragment.scrooltoposition(position);
              //  }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llQuestion, llAnswer;
        private TextView tvQue, tvAns;

        public MyViewHolder(View itemView) {
            super(itemView);

            llQuestion = (LinearLayout) itemView.findViewById(R.id.ll_question);
            llAnswer = (LinearLayout) itemView.findViewById(R.id.ll_answer);

            tvQue = (TextView) itemView.findViewById(R.id.tv_question);
            tvAns = (TextView) itemView.findViewById(R.id.tv_answer);
        }
    }
}
