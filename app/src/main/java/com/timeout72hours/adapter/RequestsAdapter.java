package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.UserProfileActivity;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.fragment.RequestsFragment;
import com.timeout72hours.fragment.SearchFragment;
import com.timeout72hours.model.UserListData;

import java.util.ArrayList;

/**
 * Created by hardip on 11/12/17.
 */

public class RequestsAdapter extends ArrayAdapter<UserListData> {

   // private ArrayList<UserListData> userListData;
    private Context mContext;
    private RequestsFragment requestsFragment;
    private SearchFragment searchFragment;
    private String type = "";

    public RequestsAdapter(Context context, RequestsFragment requestsFragment, SearchFragment searchFragment,String type) {
        super(context, 0);
   //     this.userListData = userListData;
        this.mContext = context;
        this.requestsFragment = requestsFragment;
        this.searchFragment = searchFragment;
        this.type = type;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.custom_row_requests_list, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

          final UserListData userListData = getItem(position);

    //    Log.e("name",userListData.get(position).getUser_name());
        if (userListData.getCountry()!=null && userListData.getCountry().trim().length()>0){
            if (userListData.getCity()!=null && userListData.getCity().trim().length()>0){
                holder.tv_city_country.setText(userListData.getCountry() + ", " + userListData.getCity());
            } else {
                holder.tv_city_country.setText(userListData.getCountry());
            }
        }

        if (userListData.getGender()!=null && userListData.getGender().trim().length()>0){
            if (userListData.getGender().equals("Female")){
                if (userListData.getAge()!=null && !userListData.getAge().equals("0") && userListData.getAge().trim().length()>0){
                    holder.tv_name.setText(userListData.getUser_name()+", "+userListData.getAge()+" F");
                } else {
                    holder.tv_name.setText(userListData.getUser_name()+", F");
                }
            } else {
                if (userListData.getAge()!=null && !userListData.getAge().equals("0") && userListData.getAge().trim().length()>0){
                    holder.tv_name.setText(userListData.getUser_name()+", "+userListData.getAge()+" M");
                } else {
                    holder.tv_name.setText(userListData.getUser_name()+", M");
                }
            }
        } else {
            if (userListData.getAge()!=null && !userListData.getAge().equals("0") && userListData.getAge().trim().length()>0){
                holder.tv_name.setText(userListData.getUser_name()+", "+userListData.getAge());
            } else {
                holder.tv_name.setText(userListData.getUser_name());
            }
        }


//        if (type.equals("search")){
//            holder.img_accept.setImageResource(R.drawable.add_friend);
//        } else {
//            holder.img_accept.setImageResource(R.drawable.icon_accept_request);
//        }

        if (userListData.getUser_image()!=null && userListData.getUser_image().length()>0){
            Picasso.with(mContext).load(userListData.getUser_image()).error(R.drawable.user_default).into(holder.img_user);
        } else {
            Picasso.with(mContext).load(R.drawable.user_default).into(holder.img_user);
        }

        holder.img_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("search")){
                    searchFragment.sendFriendREquest(true,true,userListData.getUser_id());
                } else {
                    requestsFragment.updateStatus(true,userListData.getRequest_id());
                }

            }
        });

        holder.img_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("search")){
                    searchFragment.sendFriendREquest(true,false,userListData.getUser_id());
                } else {
                    requestsFragment.updateStatus(false,userListData.getRequest_id());
                }

            }
        });

        holder.img_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, UserProfileActivity.class);
                in.putExtra("user_id",userListData.getUser_id());
                mContext.startActivity(in);
            }
        });

        return contentView;
    }

    private static class ViewHolder {
        public TextView tv_name,tv_city_country,tv_age;
        public ImageView img_user,img_accept,img_reject,img_view_profile;

        public ViewHolder(View view) {
            this.img_user =  view.findViewById(R.id.img_user);
            this.img_accept =  view.findViewById(R.id.img_accept);
            this.img_reject =  view.findViewById(R.id.img_reject);
            this.img_view_profile =  view.findViewById(R.id.img_view_profile);
            this.tv_name =  view.findViewById(R.id.tv_name);
            this.tv_city_country =  view.findViewById(R.id.tv_city_country);
            this.tv_age =  view.findViewById(R.id.tv_age);
        }
    }

}
