package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.timeout72hours.fragment.FriendsFragment;
import com.timeout72hours.fragment.SearchFragment;
import com.timeout72hours.model.UserListData;

import java.util.ArrayList;

/**
 * Created by hardip on 10/12/17.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<UserListData> userListData;
    private FriendsFragment friendsFragment;

    public FriendListAdapter(Context mContext, ArrayList<UserListData> userListData,FriendsFragment friendsFragment) {
        this.mContext = mContext;
        this.userListData = userListData;
        this.friendsFragment = friendsFragment;
    }

    public void setList(ArrayList<UserListData> userListData){
        this.userListData.addAll(userListData);
        notifyDataSetChanged();
    }

    @Override
    public FriendListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_friends_list, parent, false);

        return new FriendListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FriendListAdapter.MyViewHolder holder, final int position) {

//        if (userListData.get(position).getAge()!=null && !userListData.get(position).getAge().equals("0") && userListData.get(position).getAge().trim().length()>0){
//            holder.tv_name.setText(userListData.get(position).getUser_name()+", "+userListData.get(position).getAge());
//        } else {
//            holder.tv_name.setText(userListData.get(position).getUser_name());
//        }

        if (userListData.get(position).getGender()!=null && userListData.get(position).getGender().trim().length()>0){
            if (userListData.get(position).getGender().equals("Female")){
                if (userListData.get(position).getAge()!=null && !userListData.get(position).getAge().equals("0") && userListData.get(position).getAge().trim().length()>0){
                    holder.tv_name.setText(userListData.get(position).getUser_name()+", "+userListData.get(position).getAge()+" F");
                } else {
                    holder.tv_name.setText(userListData.get(position).getUser_name()+", F");
                }
            } else {
                if (userListData.get(position).getAge()!=null && !userListData.get(position).getAge().equals("0") && userListData.get(position).getAge().trim().length()>0){
                    holder.tv_name.setText(userListData.get(position).getUser_name()+", "+userListData.get(position).getAge()+" M");
                } else {
                    holder.tv_name.setText(userListData.get(position).getUser_name()+", M");
                }
            }
        } else {
            if (userListData.get(position).getAge()!=null && !userListData.get(position).getAge().equals("0") && userListData.get(position).getAge().trim().length()>0){
                holder.tv_name.setText(userListData.get(position).getUser_name()+", "+userListData.get(position).getAge());
            } else {
                holder.tv_name.setText(userListData.get(position).getUser_name());
            }
        }

        if (userListData.get(position).getCountry()!=null && userListData.get(position).getCountry().trim().length()>0){
            if (userListData.get(position).getCity()!=null && userListData.get(position).getCity().trim().length()>0){
                holder.tv_city_country.setText(userListData.get(position).getCountry() + ", " + userListData.get(position).getCity());
            } else {
                holder.tv_city_country.setText(userListData.get(position).getCountry());
            }
        }


        if (userListData.get(position).getUser_image()!=null && userListData.get(position).getUser_image().length()>0){
            Picasso.with(mContext)
                    .load(userListData.get(position).getUser_image())
                    .error(R.drawable.user_default)
                    .transform(new CircleTransform())
                    .resize(100, 100).centerCrop()
                    .into(holder.img);
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.user_default)
                    .transform(new CircleTransform())
                    .resize(100, 100).centerCrop()
                    .into(holder.img);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("type", "friends");
                intent.putExtra("token", userListData.get(position).getDevice_token());
                intent.putExtra("id", userListData.get(position).getUser_id());
                intent.putExtra("name", userListData.get(position).getUser_name());
                intent.putExtra("image", userListData.get(position).getUser_image());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img,img_chat;
        private TextView tv_name, tv_city_country;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_city_country = itemView.findViewById(R.id.tv_city_country);
            img_chat = itemView.findViewById(R.id.img_chat);

        }
    }
}

