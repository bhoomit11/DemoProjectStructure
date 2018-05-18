package com.timeout72hours.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bhumit on 8/12/17.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatMessage> chatMessageList;
    private Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ChatAdapter(List<ChatMessage> chatMessageList, Context mContext) {
        this.chatMessageList = chatMessageList;
        this.mContext = mContext;
    }

    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent, false);

        return new ChatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.MyViewHolder holder, int position) {
        if (chatMessageList.get(position).getSender_id().equalsIgnoreCase(
                Utility.getAppPrefString(mContext, Constant.USER_ID))) {
            holder.llOther.setVisibility(View.INVISIBLE);
            holder.llMe.setVisibility(View.VISIBLE);
            holder.tvMe.setText(chatMessageList.get(position).getText());
        } else {
            holder.llOther.setVisibility(View.VISIBLE);
            holder.llMe.setVisibility(View.INVISIBLE);
            holder.tvOther.setText(chatMessageList.get(position).getText());
        }

        if (sdf.format(Utility.getDateFromMillis(chatMessageList.get(position).getDate(), "dd-MMM hh:mm").getTime())
                .compareTo(sdf.format(new Date())) == 0) {

            if (chatMessageList.get(position).getSender_id().equalsIgnoreCase(
                    Utility.getAppPrefString(mContext, Constant.USER_ID))) {
                holder.tv_me_date.setText(Utility.getDateFromMillisString(chatMessageList.get(position).getDate(), "hh:mm a"));
            } else {
                holder.tv_other_date.setText(Utility.getDateFromMillisString(chatMessageList.get(position).getDate(), "hh:mm a"));
            }
        } else {
            if (chatMessageList.get(position).getSender_id().equalsIgnoreCase(
                    Utility.getAppPrefString(mContext, Constant.USER_ID))) {
                holder.tv_me_date.setText(Utility.getDateFromMillisString(chatMessageList.get(position).getDate(), "dd-MMM hh:mm a"));
            } else {
                holder.tv_other_date.setText(Utility.getDateFromMillisString(chatMessageList.get(position).getDate(), "dd-MMM hh:mm a"));
            }
        }
    }

    public void update(ChatMessage chatMessage) {
        chatMessageList.add(chatMessage);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llOther, llMe;
        private TextView tvOther, tvMe, tv_other_date, tv_me_date;

        public MyViewHolder(View itemView) {
            super(itemView);

            llOther = itemView.findViewById(R.id.ll_other);
            llMe = itemView.findViewById(R.id.ll_me);

            tvOther = itemView.findViewById(R.id.tv_other);
            tvMe = itemView.findViewById(R.id.tv_me);
            tv_other_date = itemView.findViewById(R.id.tv_other_date);
            tv_me_date = itemView.findViewById(R.id.tv_me_date);
        }
    }
}
