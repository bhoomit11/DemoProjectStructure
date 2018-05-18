package com.timeout72hours.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.ChatHistoryActivity;
import com.timeout72hours.activities.ChatListActivity;
import com.timeout72hours.activities.LoginRegisterActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;

/**
 * Created by hardip on 2/12/17.
 */

public class MenuFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ImageView user_profile;
    private Context mContext;
    private TextView tv_name;
    private LinearLayout ll_home, ll_friends, ll_artist, ll_ticket_info, ll_faq, ll_about_us,
            ll_logout, ll_profile, ll_articles, ll_events, ll_directions, ll_transactions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_menu, null);
        init();
        return view;
    }

    private void init() {
        mContext = getActivity();
        user_profile = view.findViewById(R.id.user_profile);
        tv_name = view.findViewById(R.id.tv_name);

        ll_home = view.findViewById(R.id.ll_home);
        ll_friends = view.findViewById(R.id.ll_friends);
        ll_artist = view.findViewById(R.id.ll_artist);
        ll_ticket_info = view.findViewById(R.id.ll_ticket_info);
        ll_faq = view.findViewById(R.id.ll_faq);
        ll_about_us = view.findViewById(R.id.ll_about_us);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_profile = view.findViewById(R.id.ll_profile);
        ll_articles = view.findViewById(R.id.ll_articles);
        ll_events = view.findViewById(R.id.ll_events);
        ll_directions = view.findViewById(R.id.ll_directions);
        ll_transactions = view.findViewById(R.id.ll_transactions);

        ll_home.setOnClickListener(this);
        ll_friends.setOnClickListener(this);
        ll_artist.setOnClickListener(this);
        ll_ticket_info.setOnClickListener(this);
        ll_faq.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        ll_profile.setOnClickListener(this);
        ll_articles.setOnClickListener(this);
        ll_events.setOnClickListener(this);
        ll_directions.setOnClickListener(this);
        ll_transactions.setOnClickListener(this);

        //  Picasso.with(mContext).load(Utility.getAppPrefString(mContext, Constant.IMAGE)).transform(new CircleTransform()).into(user_profile);
        tv_name.setText(Utility.getAppPrefString(mContext, Constant.NAME));
        if (Utility.getAppPrefString(mContext, Constant.IMAGE).trim().length() > 0) {
            Picasso.with(getActivity()).invalidate(Utility.getAppPrefString(mContext, Constant.IMAGE));
            Picasso.with(mContext).load(Utility.getAppPrefString(mContext, Constant.IMAGE)).error(R.drawable.user).transform(new CircleTransform()).into(user_profile);
        } else {
            Picasso.with(mContext).load(R.drawable.user).transform(new CircleTransform()).into(user_profile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void loadImage() {
        if (Utility.getAppPrefString(mContext, Constant.IMAGE).trim().length() > 0) {
            Picasso.with(getActivity()).invalidate(Utility.getAppPrefString(mContext, Constant.IMAGE));
            Picasso.with(mContext).load(Utility.getAppPrefString(mContext, Constant.IMAGE)).error(R.drawable.user).transform(new CircleTransform()).into(user_profile);
        } else {
            Picasso.with(mContext).load(R.drawable.user).transform(new CircleTransform()).into(user_profile);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_home:
                ((MainActivity) mContext).changefragment(0);
                break;
            case R.id.ll_friends:
                ((MainActivity) mContext).changefragment(10);
              //  startActivity(new Intent(mContext, FriendsActivity.class));
                break;
            case R.id.ll_artist:
                ((MainActivity) mContext).changefragment(1);
                break;

            case R.id.ll_profile:
                ((MainActivity) mContext).changefragment(2);
                break;

            case R.id.ll_faq:
                ((MainActivity) mContext).changefragment(3);
                break;
            case R.id.ll_about_us:
                ((MainActivity) mContext).changefragment(4);
                break;

            case R.id.ll_ticket_info:
                ((MainActivity) mContext).changefragment(5);
                break;

            case R.id.ll_articles:
                ((MainActivity) mContext).changefragment(6);
                break;

            case R.id.ll_events:
                ((MainActivity) mContext).changefragment(7);
                break;

            case R.id.ll_directions:
                ((MainActivity) mContext).changefragment(8);
                break;

            case R.id.ll_transactions:
                ((MainActivity) mContext).changefragment(9);
                break;

            case R.id.ll_logout:
                new AlertDialog.Builder(mContext)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((MainActivity) mContext).callLogout();
                                        // TODO Auto-generated method stub

                                    }
                                })
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss();
                                    }
                                }).show();

                break;
            default:
                break;
        }


    }
}
