package com.timeout72hours.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.activities.ChatListActivity;
import com.timeout72hours.adapter.FriendListAdapter;
import com.timeout72hours.adapter.SearchUserListAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CommonResponse;
import com.timeout72hours.model.FriendListResponse;
import com.timeout72hours.model.SearchUserListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.timeout72hours.attributes.Constant.USER_ID;

/**
 * Created by hardip on 8/12/17.
 */

public class FriendsFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private RecyclerView rv_user_list;
    private APIInterface apiInterface;
    public FloatingActionButton fab_message;
    private boolean is_created = false;
    private int page_no=1;

    LinearLayoutManager lLayout;

    private boolean loading = false;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private FriendListAdapter friendListAdapter;
    private CustomProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_friends, null);
        init();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && is_created){
            page_no = 1;
            friendListAdapter = null;
            getFriends();
        }
    }

    private void init() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);
        fab_message = view.findViewById(R.id.fab_message);

        fab_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ChatListActivity.class));
            }
        });
        rv_user_list = view.findViewById(R.id.rv_user_list);
        lLayout = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_user_list.setLayoutManager(lLayout);

        rv_user_list.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = lLayout.getChildCount();
                    totalItemCount = lLayout.getItemCount();
                    pastVisiblesItems = lLayout.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                            page_no = page_no+1;
                            if (!progressDialog.isShowing()) {
                                progressDialog.show();
                            }
                            getFriends();
                        }
                    }
                }
            }
        });

        page_no = 1;
        friendListAdapter = null;
        getFriends();
        is_created = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void getFriends() {
        if (Utility.isNetworkAvaliable(mContext)) {



            apiInterface.getFriendList(page_no+"",Utility.getAppPrefString(mContext, USER_ID)).enqueue(new Callback<FriendListResponse>() {
                @Override
                public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        if (friendListAdapter==null) {
                            friendListAdapter = new FriendListAdapter(mContext, response.body().getUser_data(), FriendsFragment.this);
                            rv_user_list.setAdapter(friendListAdapter);
                        } else {
                            friendListAdapter.setList(response.body().getUser_data());
                        }
                        if (page_no<Integer.parseInt(response.body().getPage_count())){
                            loading = true;
                        }
                    }
                }

                @Override
                public void onFailure(Call<FriendListResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
        }
    }
}
