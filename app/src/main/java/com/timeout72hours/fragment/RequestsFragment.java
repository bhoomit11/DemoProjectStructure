package com.timeout72hours.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.adapter.FriendListAdapter;
import com.timeout72hours.adapter.RequestsAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CommonResponse;
import com.timeout72hours.model.FriendListResponse;
import com.timeout72hours.model.UserListData;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.timeout72hours.attributes.Constant.USER_ID;

/**
 * Created by hardip on 8/12/17.
 */

public class RequestsFragment extends Fragment {

    private View view;
    private Context mContext;
    private CustomProgressDialog progressDialog;
    private APIInterface apiInterface;
    private TextView tv_no_req;

    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private RequestsAdapter adapter;
    private ArrayList<UserListData> userListData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_friend_requests, null);
        init();
        return view;
    }

    private void init() {
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);

        progressBar = view.findViewById(R.id.activity_main_progress_bar);

        tv_no_req = view.findViewById(R.id.tv_no_req);
        cardStackView = view.findViewById(R.id.activity_main_card_stack_view);
        List<SwipeDirection> swipeDirectionList = new ArrayList<>();
        swipeDirectionList.add(SwipeDirection.Left);
        swipeDirectionList.add(SwipeDirection.Right);
        cardStackView.setSwipeDirection(swipeDirectionList);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
                Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
                if (direction.toString().equals("Right")) {
                    statusRequests(userListData.get((cardStackView.getTopIndex() - 1)).getRequest_id(), "1");
                } else if (direction.toString().equals("Left")) {
                    statusRequests(userListData.get((cardStackView.getTopIndex() - 1)).getRequest_id(), "0");
                }
                if (cardStackView.getTopIndex() == adapter.getCount()) {
                    tv_no_req.setVisibility(View.VISIBLE);
                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
                    //   paginate();
                }
            }

            @Override
            public void onCardReversed() {
                Log.d("CardStackView", "onCardReversed");
            }

            @Override
            public void onCardMovedToOrigin() {
                Log.d("CardStackView", "onCardMovedToOrigin");
            }

            @Override
            public void onCardClicked(int index) {
                Log.d("CardStackView", "onCardClicked: " + index);
            }
        });

        getRequests();
    }

    public void updateStatus(final boolean is_accept, final String request_id) {

        if (is_accept) {
            swipeRight();
         //   statusRequests(request_id, "1");
        } else {
            swipeLeft();
          //  statusRequests(request_id, "0");
        }

    }

    private void paginate() {
        cardStackView.setPaginationReserved();
        adapter.addAll(userListData);
        adapter.notifyDataSetChanged();
    }


    private void getRequests() {

        if (Utility.isNetworkAvaliable(mContext)) {

//            if (!progressDialog.isShowing()) {
//                progressDialog.show();
//            }

            apiInterface.getFriendRequestList(Utility.getAppPrefString(mContext, USER_ID)).enqueue(new Callback<FriendListResponse>() {
                @Override
                public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        if (response.body().getUser_data() != null && response.body().getUser_data().size() > 0) {
                            userListData = response.body().getUser_data();
                            cardStackView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new RequestsAdapter(mContext, RequestsFragment.this, null, "request");
                                    adapter.addAll(userListData);
                                    cardStackView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    cardStackView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 1000);

                            tv_no_req.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            tv_no_req.setVisibility(View.VISIBLE);
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
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

    public void statusRequests(String request_id, String status) {

        if (Utility.isNetworkAvaliable(mContext)) {

//            if (!progressDialog.isShowing()) {
//                progressDialog.show();
//            }

            apiInterface.statusRequests(request_id, status).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {


                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
        }


    }

    public void swipeLeft() {
//        List<TouristSpot> spots = extractRemainingTouristSpots();
//        if (spots.isEmpty()) {
//            return;
//        }

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Left, set);
    }

    public void swipeRight() {
//        List<TouristSpot> spots = extractRemainingTouristSpots();
//        if (spots.isEmpty()) {
//            return;
//        }

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Right, set);
    }

}
