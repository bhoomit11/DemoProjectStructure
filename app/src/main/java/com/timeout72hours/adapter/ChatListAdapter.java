package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.ChatActivity;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by bhumit on 14/12/17.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private Context mContext;
    ArrayList<ChatMessage> chatListDataArrayList;

    public ChatListAdapter(Context mContext, ArrayList<ChatMessage> chatListDataArrayList) {
        this.mContext = mContext;
        this.chatListDataArrayList = chatListDataArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_name_list, parent, false);

        return new ChatListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tv_username.setText(chatListDataArrayList.get(position).getDisplay_name());
        if (chatListDataArrayList.get(position).getSender_id().equalsIgnoreCase(
                Utility.getAppPrefString(mContext, Constant.USER_ID))) {
            holder.tv_text.setText("You: " + chatListDataArrayList.get(position).getText());

            holder.tv_username.setTypeface(null, Typeface.NORMAL);
            holder.tv_text.setTypeface(null, Typeface.NORMAL);

            holder.tv_username.setTextSize(16f);
            holder.tv_text.setTextSize(14f);
        } else {
            holder.tv_text.setText(chatListDataArrayList.get(position).getDisplay_name() + ": " +
                    chatListDataArrayList.get(position).getText());

            if (!chatListDataArrayList.get(position).getIsRead().equalsIgnoreCase("true")) {
                holder.tv_username.setTypeface(null, Typeface.BOLD);
                holder.tv_text.setTypeface(null, Typeface.BOLD);

                holder.tv_username.setTextSize(17f);
                holder.tv_text.setTextSize(15f);
            } else {
                holder.tv_username.setTypeface(null, Typeface.NORMAL);
                holder.tv_text.setTypeface(null, Typeface.NORMAL);

                holder.tv_username.setTextSize(16f);
                holder.tv_text.setTextSize(14f);
            }
        }

        Picasso.with(mContext)
                .load(chatListDataArrayList.get(position).getUserImage())
                .transform(new CircleTransform())
                .resize(100, 100).centerCrop()
                .into(holder.iv_user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("type", "friends");
                intent.putExtra("token", chatListDataArrayList.get(position).getDevide_token());
                intent.putExtra("id", chatListDataArrayList.get(position).getSender_id().equalsIgnoreCase(Utility.getAppPrefString(mContext, Constant.USER_ID)) ?
                        chatListDataArrayList.get(position).getRecever_id() : chatListDataArrayList.get(position).getSender_id());
                intent.putExtra("name", chatListDataArrayList.get(position).getSender_id().equalsIgnoreCase(Utility.getAppPrefString(mContext, Constant.USER_ID)) ?
                        chatListDataArrayList.get(position).getRecevier_name() : chatListDataArrayList.get(position).getSender_name());
                intent.putExtra("image", chatListDataArrayList.get(position).getUserImage());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tv_count, tv_text;
        private ImageView iv_user_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);

            iv_user_image = (ImageView) itemView.findViewById(R.id.iv_user_image);
        }
    }
}
