package com.timeout72hours.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.fragment.SearchFragment;
import com.timeout72hours.model.CommentListData;
import com.timeout72hours.model.UserListData;

import java.util.ArrayList;

/**
 * Created by hardip on 8/12/17.
 */

public class SearchUserListAdapter extends RecyclerView.Adapter<SearchUserListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<UserListData> userListData;
    private SearchFragment searchFragment;

    public SearchUserListAdapter(Context mContext, ArrayList<UserListData> userListData,SearchFragment searchFragment) {
        this.mContext = mContext;
        this.userListData = userListData;
        this.searchFragment = searchFragment;
    }

    @Override
    public SearchUserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_search_user_list, parent, false);

        return new SearchUserListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchUserListAdapter.MyViewHolder holder, final int position) {

        holder.tv_name.setText(userListData.get(position).getUser_name());

        if (userListData.get(position).getCountry()!=null && userListData.get(position).getCountry().trim().length()>0){
            holder.tv_city_country.setText(userListData.get(position).getCountry() + ", " + userListData.get(position).getCity());
        }

        if (userListData.get(position).getIs_friend().equals("1")){
            holder.img_add_friend.setVisibility(View.GONE);
            holder.tv_status.setVisibility(View.VISIBLE);
        } else {
            holder.img_add_friend.setVisibility(View.VISIBLE);
            holder.tv_status.setVisibility(View.GONE);
        }

        if (userListData.get(position).getUser_image()!=null && userListData.get(position).getUser_image().length()>0){
            Picasso.with(mContext).load(userListData.get(position).getUser_image()).error(R.drawable.user_default).transform(new CircleTransform()).into(holder.img);
        } else {
            Picasso.with(mContext).load(R.drawable.user_default).transform(new CircleTransform()).into(holder.img);
        }

        holder.img_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //  searchFragment.sendFriendREquest(userListData.get(position).getUser_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img,img_add_friend;
        private TextView tv_name, tv_city_country, tv_status;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_city_country = itemView.findViewById(R.id.tv_city_country);
            img_add_friend = itemView.findViewById(R.id.img_add_friend);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }
}

