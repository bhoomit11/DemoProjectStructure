package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.timeout72hours.R;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.adapter.ArtistDetailAdapter;
import com.timeout72hours.adapter.ArtistViewPagerAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.retrofit.APIInterface;

/**
 * Created by hardip on 2/12/17.
 */

public class ArtistDetailFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
//    private RecyclerView rv_artist_detail;

    //    private ArtistDetailAdapter artistDetailFragment;
    private ImageView btn_prev, btn_next;
    private int scroll_pos = 0;

    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_artist_detail, null);
        init();
        return view;
    }

    private void init() {
        mContext = getActivity();

        btn_prev = view.findViewById(R.id.btn_prev);
        btn_next = view.findViewById(R.id.btn_next);

        btn_prev.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        viewPager = view.findViewById(R.id.view_pager);
        ArtistViewPagerAdapter artistViewPagerAdapter = new ArtistViewPagerAdapter(mContext, ((MainActivity) mContext).artistListData);
        viewPager.setAdapter(artistViewPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                scroll_pos = position;
                Log.e("Position",position+"");
                if (scroll_pos == 0) {
                    btn_prev.setVisibility(View.GONE);
                } else if (!(scroll_pos < ((MainActivity) mContext).artistListData.size() - 1)) {
                    btn_next.setVisibility(View.GONE);
                }else {
                    btn_next.setVisibility(View.VISIBLE);
                    btn_prev.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewPager.setCurrentItem(((MainActivity) mContext).selected_artist);
//        rv_artist_detail = view.findViewById(R.id.rv_artist_detail);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false) {
//            @Override
//            public boolean canScrollHorizontally() {
//                return false;
//            }
//        };
//        rv_artist_detail.setLayoutManager(layoutManager);
//        artistDetailFragment = new ArtistDetailAdapter(mContext, ((MainActivity) mContext).artistListData);
//        rv_artist_detail.setAdapter(artistDetailFragment);
//        rv_artist_detail.getLayoutManager().scrollToPosition(((MainActivity) mContext).selected_artist);
        scroll_pos = ((MainActivity) mContext).selected_artist;
        if (scroll_pos == 0) {
            btn_prev.setVisibility(View.GONE);
        }
        if (!(scroll_pos < ((MainActivity) mContext).artistListData.size() - 1)) {
            btn_next.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_prev:
                if (scroll_pos != 0) {
                    scroll_pos = scroll_pos - 1;
//                    rv_artist_detail.getLayoutManager().scrollToPosition(scroll_pos);
                    viewPager.setCurrentItem(scroll_pos);
                    btn_next.setVisibility(View.VISIBLE);
                    if (scroll_pos == 0) {
                        btn_prev.setVisibility(View.GONE);
                    }
                }
                break;

            case R.id.btn_next:
                if (scroll_pos < ((MainActivity) mContext).artistListData.size() - 1) {
                    scroll_pos = scroll_pos + 1;
                    viewPager.setCurrentItem(scroll_pos);
                    btn_prev.setVisibility(View.VISIBLE);
                    if (!(scroll_pos < ((MainActivity) mContext).artistListData.size() - 1)) {
                        btn_next.setVisibility(View.GONE);
                    }
                }
                break;
        }

    }
}
