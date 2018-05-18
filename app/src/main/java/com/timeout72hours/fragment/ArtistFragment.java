package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.timeout72hours.R;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.adapter.ArtistListAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.ArtistListData;
import com.timeout72hours.model.ArtistListResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by hardip on 2/12/17.
 */

public class ArtistFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private View view;
    private RecyclerView rv_artist_list;
    private ImageView img_previous, img_next;
    private APIInterface apiInterface;
    private GridLayoutManager gridLayoutManager;
    private int pos = 0;

    private CustomProgressDialog progressDialog;
    private LinearLayout ll_prev_next;
    private boolean api_response = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_artistlist, null);
        init();
        getArtistList();
        Handler hnd = new Handler();
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!api_response) {
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                }
            }
        }, 1000);
        return view;
    }

    private void init() {
        mContext = getActivity();

        progressDialog = new CustomProgressDialog(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        rv_artist_list = view.findViewById(R.id.rv_artist_list);
        img_previous = view.findViewById(R.id.img_previous);
        img_next = view.findViewById(R.id.img_next);

        ll_prev_next = view.findViewById(R.id.ll_prev_next);

        img_previous.setOnClickListener(this);
        img_next.setOnClickListener(this);

        gridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.HORIZONTAL, false);
        rv_artist_list.setLayoutManager(gridLayoutManager);

//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.findSnapView(gridLayoutManager);

        rv_artist_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {
                    pos = gridLayoutManager.findFirstVisibleItemPosition();
                    pos+=2;

                    if (pos-2 == 0) {
                        img_previous.setImageResource(R.drawable.previous_inactive);
                    }
                    if (!((pos + 4) < ((MainActivity) mContext).artistListData.size())) {
                        img_next.setImageResource(R.drawable.next_inactive);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                img_next.setImageResource(R.drawable.next_active);
                if (pos != 0) {
                    img_previous.setImageResource(R.drawable.previous_active);
                }
            }
        });
    }

    private void getArtistList() {
        if (Utility.isNetworkAvaliable(mContext)) {

            apiInterface.getArtistList().enqueue(new Callback<ArtistListResponse>() {
                @Override
                public void onResponse(Call<ArtistListResponse> call, Response<ArtistListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    if (response.isSuccessful() && response.body() != null) {
                        ll_prev_next.setVisibility(View.VISIBLE);
                        ((MainActivity) mContext).artistListData = response.body().getArtist_list();
                        ArtistListAdapter artistListAdapter = new ArtistListAdapter(mContext, response.body().getArtist_list());
                        rv_artist_list.setAdapter(artistListAdapter);
                    }
                }

                @Override
                public void onFailure(Call<ArtistListResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.apiErrorMsg));
                }
            });
        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_previous:
                Log.e("tet", "tet");
                if (pos != 0) {
                    pos = pos - 4;
                    rv_artist_list.smoothScrollToPosition(pos);
                    img_next.setImageResource(R.drawable.next_active);
                    if (pos == 0) {
                        img_previous.setImageResource(R.drawable.previous_inactive);
                    }
                }
                break;

            case R.id.img_next:
                if ((pos + 4) < ((MainActivity) mContext).artistListData.size()) {
                    pos = pos + 4;
                    rv_artist_list.smoothScrollToPosition(pos);
                    img_previous.setImageResource(R.drawable.previous_active);
                    if (!((pos + 4) < ((MainActivity) mContext).artistListData.size())) {
                        img_next.setImageResource(R.drawable.next_inactive);
                    }
                }
                break;
        }

    }
}
