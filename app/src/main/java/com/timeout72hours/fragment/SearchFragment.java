package com.timeout72hours.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.activities.ArticleDetailActivity;
import com.timeout72hours.adapter.CommentUserListAdapter;
import com.timeout72hours.adapter.RequestsAdapter;
import com.timeout72hours.adapter.SearchUserListAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CommentListResponse;
import com.timeout72hours.model.CommonResponse;
import com.timeout72hours.model.SearchUserListResponse;
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

public class SearchFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private TextView tv_no_result;
    private EditText et_search, etCountry, etCity;
    //  private RecyclerView rv_user_list;
    private CustomProgressDialog progressDialog;
    private APIInterface apiInterface;

    private ProgressBar progressBar;
    private CardStackView cardStackView;
    private RequestsAdapter adapter;
    private ArrayList<UserListData> userListData;
    private int page_no = 1;
    private int page_count = 1;
    private String selectedCountryId = "", gender = "";
    private RadioGroup rgGender;
    private boolean is_created = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_search_users, null);
        init();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && is_created){
            search();
        }
    }

    private void init() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);

//        rv_user_list = view.findViewById(R.id.rv_user_list);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        rv_user_list.setLayoutManager(layoutManager);

        tv_no_result = view.findViewById(R.id.tv_no_result);
        et_search = view.findViewById(R.id.et_search);
        etCountry = view.findViewById(R.id.et_Country);
        etCountry.setOnClickListener(this);
        etCity = view.findViewById(R.id.et_city);

        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (et_search.getText().toString().trim().length() > 0) {
                        search();
                    }
                    return true;
                }
                return false;
            }
        });

        etCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (etCity.getText().toString().trim().length() > 0) {
                        search();
                    }
                    return true;
                }
                return false;
            }
        });


        rgGender = (RadioGroup) view.findViewById(R.id.rg_gender);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_male:
                        gender = "Male";
                        search();
                        break;
                    case R.id.rb_female:
                        gender = "Female";
                        search();
                        break;
                    case R.id.rb_both:
                        gender = "";
                        search();
                        break;
                    default:
                        break;
                }
            }
        });

        progressBar = view.findViewById(R.id.activity_main_progress_bar);

        cardStackView = view.findViewById(R.id.activity_main_card_stack_view);
        List<SwipeDirection> swipeDirectionList = new ArrayList<>();
        swipeDirectionList.add(SwipeDirection.Left);
        swipeDirectionList.add(SwipeDirection.Right);
        cardStackView.setSwipeDirection(swipeDirectionList);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
             //   Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
              //  Log.d("CardStackView", "onCardSwiped: " + direction.toString());
             //   Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
                if (direction.toString().equals("Right")) {
                    sendFriendREquest(false, true, userListData.get((cardStackView.getTopIndex() - 1)).getUser_id());
                } else if (direction.toString().equals("Left")) {
                    sendFriendREquest(false, false, userListData.get((cardStackView.getTopIndex() - 1)).getUser_id());
                }
                if (cardStackView.getTopIndex() == adapter.getCount()) {
                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
                    if (page_no < page_count) {
                        cardStackView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        page_no = page_no + 1;
                        searchUsers();
                    }
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
        search();
        is_created = true;
    }

    private void search() {
        cardStackView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Utility.hideSoftKeyboard(getActivity());
        page_no = 1;
        searchUsers();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.et_Country:
                showCountryList();
                break;

        }

    }

    public void showCountryList() {
        final FriendsMainFragment parentFrag = ((FriendsMainFragment) SearchFragment.this.getParentFragment());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        builder.setItems(parentFrag.countryName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etCountry.setText(parentFrag.countryName[which]);
                selectedCountryId = parentFrag.countryId[which];
                search();
            }
        });
        builder.setTitle("Select Country");
        builder.show();
    }

    private void paginate() {
        cardStackView.setPaginationReserved();
        adapter.addAll(userListData);
        adapter.notifyDataSetChanged();
    }

    private void searchUsers() {

        if (Utility.isNetworkAvaliable(mContext)) {

//            if (!progressDialog.isShowing()) {
//                progressDialog.show();
//            }

            apiInterface.searchUsers(page_no + "", Utility.getAppPrefString(mContext, USER_ID), et_search.getText().toString().trim(), gender, etCity.getText().toString().trim(), selectedCountryId).enqueue(new Callback<SearchUserListResponse>() {
                @Override
                public void onResponse(Call<SearchUserListResponse> call, Response<SearchUserListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        userListData = response.body().getUser_data();
                        if (userListData.size() > 0) {
                            page_count = Integer.parseInt(response.body().getPage_count());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter = new RequestsAdapter(mContext, null, SearchFragment.this, "search");
                                    adapter.addAll(userListData);
                                    cardStackView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    cardStackView.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }, 1000);
                            tv_no_result.setVisibility(View.GONE);
                        } else {
                            tv_no_result.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<SearchUserListResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
        }

    }

    public void sendFriendREquest(final boolean is_fragment, final boolean is_send, String friend_id) {
        String status = "";
        if (is_send) {
            status = "send";
            if (is_fragment) {
                swipeRight();
                return;
            }
        } else {
            status = "reject";
            if (is_fragment) {
                swipeLeft();
                return;
            }
        }

        if (Utility.isNetworkAvaliable(mContext)) {

            apiInterface.sendFriendRequest(Utility.getAppPrefString(mContext, USER_ID), friend_id, status).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        //    Utility.showToastSuccess(getActivity(),response.body().getMessage());
                    } else {
                        //   Utility.showToast(getActivity(),response.body().getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {

                }
            });
        } else {
            // Utility.showToast(getActivity(), getResources().getString(R.string.internetErrorMsg));
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
