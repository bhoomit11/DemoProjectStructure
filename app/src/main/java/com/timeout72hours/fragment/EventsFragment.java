package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.adapter.EventListAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.EventListResponse;
import com.timeout72hours.model.EventListStageData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 7/12/17.
 */

public class EventsFragment extends android.support.v4.app.Fragment {

    private View view;
    private Context mContext;

    //    private RecyclerView rv_events;
    private TextView tv_msg, tv_stage_name;
    private APIInterface apiInterface;

    private CustomProgressDialog progressDialog;
    private boolean api_response = false;

    private EventListResponse eventListResponse;
    private int date_pos = 0;
    private TabLayout tab_dates;

    private ViewPager viewpager;
    private TabLayout tab_dots;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_event, null);
        init();
        return view;
    }

    private void init() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);

//        rv_events = view.findViewById(R.id.rv_events);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
//        rv_events.setLayoutManager(layoutManager);

        tv_msg = view.findViewById(R.id.tv_msg);
        tv_stage_name = view.findViewById(R.id.tv_stage_name);

        viewpager = view.findViewById(R.id.viewpager);
        tab_dots = view.findViewById(R.id.tab_dots);

        tab_dates = view.findViewById(R.id.tab_dates);

        tab_dates.addTab(tab_dates.newTab().setText("27 DECEMBER"));
        tab_dates.addTab(tab_dates.newTab().setText("28 DECEMBER"));
        tab_dates.addTab(tab_dates.newTab().setText("29 DECEMBER"));

//        for (int i = 0; i < tab_dates.getTabCount(); i++) {
//            TextView tv=(TextView)LayoutInflater.from(getActivity()).inflate(R.layout.custom_text_layout,null);
//            tab_dates.getTabAt(i).setCustomView(tv);
//        }

        tab_dates.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("27 DECEMBER")) {
                    date_pos = 0;
                } else if (tab.getText().toString().equals("28 DECEMBER")) {
                    date_pos = 1;
                } else if (tab.getText().toString().equals("29 DECEMBER")) {
                    date_pos = 2;
                }
                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), eventListResponse.getMain_data().get(date_pos).getStage_data());
                viewpager.setAdapter(mCustomPagerAdapter);
                viewpager.setCurrentItem(0);
//                viewpager.setCurrentItem(0);
//                fillData(date_pos, 0);
                tv_stage_name.setText(eventListResponse.getMain_data().get(date_pos).getStage_data().get(0).getStage_name());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Intent in = new Intent(FriendsActivity.this,ChatActivity.class);
//                startActivity(in);
            }
        });

        getEvents();
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
    }

    private void getEvents() {
        if (Utility.isNetworkAvaliable(mContext)) {

            apiInterface.getEvents().enqueue(new Callback<EventListResponse>() {
                @Override
                public void onResponse(Call<EventListResponse> call, Response<EventListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        eventListResponse = response.body();
//                        ArrayList<String> list_images = new ArrayList<>();
//                        list_images.add(eventListResponse.getMain_data().get(0).getStage_data().get(0).getStage_image());
//                        list_images.add(eventListResponse.getMain_data().get(0).getStage_data().get(1).getStage_image());
//                        list_images.add(eventListResponse.getMain_data().get(0).getStage_data().get(2).getStage_image());
                        if (eventListResponse.getMain_data() != null && eventListResponse.getMain_data().size() > 0) {
                            if (eventListResponse.getMain_data().get(date_pos).getStage_data() != null &&
                                    eventListResponse.getMain_data().get(date_pos).getStage_data().size() > 0) {
                                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity(), eventListResponse.getMain_data().get(date_pos).getStage_data());
                                viewpager.setAdapter(mCustomPagerAdapter);
                                tab_dots.setupWithViewPager(viewpager, true);

                                tv_stage_name.setText(eventListResponse.getMain_data().get(date_pos).getStage_data().get(0).getStage_name());
                            }
                        }

                        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
//                                fillData(date_pos, position);
                                tv_stage_name.setText(eventListResponse.getMain_data().get(date_pos).getStage_data().get(position).getStage_name());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
//                        fillData(0, 0);
                    }
                }

                @Override
                public void onFailure(Call<EventListResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                }
            });

        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }

    }

//    private void fillData(final int date_pos, final int stage_pos) {
//        if (eventListResponse.getMain_data().get(date_pos).getStage_data().get(stage_pos).getEvent_data_data().size() > 0) {
//            tv_msg.setVisibility(View.GONE);
//        } else {
//            tv_msg.setVisibility(View.VISIBLE);
//        }
//        EventListAdapter eventListAdapter = new EventListAdapter(mContext, eventListResponse.getMain_data().get(date_pos).getStage_data().get(stage_pos).getEvent_data_data());
//        rv_events.setAdapter(eventListAdapter);
//        if (stage_pos == 0) {
//            tv_stage_name.setText("MAIN STAGE");
//        } else if (stage_pos == 1) {
//            tv_stage_name.setText("PSY STAGE");
//        } else if (stage_pos == 2) {
//            tv_stage_name.setText("LIVE STAGE");
//        }
//        //   Picasso.with(mContext).load(eventListResponse.getMain_data().get(date_pos).getStage_data().get(stage_pos).getStage_image()).into(img_stage);
//
//    }

//    private class CustomPageAdapter extends PagerAdapter {
//        private Context mContext;
//        private ArrayList<String> list_images;
//        private LayoutInflater layoutInflater;
//
//        public CustomPageAdapter(Context mContext, ArrayList<String> list_images) {
//            this.mContext = mContext;
//            this.list_images = list_images;
//            this.layoutInflater = (LayoutInflater) this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
//        }
//
//        @Override
//        public int getCount() {
//            return list_images.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == ((LinearLayout) object);
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            View itemView = this.layoutInflater.inflate(R.layout.custom_row_stage, container, false);
//
//            ImageView img_stage = itemView.findViewById(R.id.img_stage);
//
//            if (list_images.get(position) != null && list_images.get(position).trim().length() > 0) {
//                Picasso.with(mContext).load(list_images.get(position)).error(R.drawable.user_default).into(img_stage);
//            } else {
//                Picasso.with(mContext).load(R.drawable.user_default).into(img_stage);
//            }
//
//            container.addView(itemView);
//            return itemView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View) object);
//        }
////        @Override
////        public CharSequence getPageTitle(int position) {
////            return eventListEventData.get(position).getStage_name().trim();
////        }
//    }


    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;
        private ArrayList<EventListStageData> stage_data;
        private LayoutInflater layoutInflater;

        public CustomPagerAdapter(Context mContext, ArrayList<EventListStageData> stage_data) {
            this.mContext = mContext;
            this.stage_data = stage_data;
            this.layoutInflater = (LayoutInflater) this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = (View) inflater.inflate(customPagerEnum.getLayoutResId(), null);

            TextView tv_msg = view.findViewById(R.id.tv_msg);
            ImageView img_stage = view.findViewById(R.id.img_stage);

            if (stage_data.get(position).getStage_image() != null && stage_data.get(position).getStage_image().trim().length() > 0) {
                Picasso.with(mContext).load(stage_data.get(position).getStage_image()).error(R.drawable.user_default).into(img_stage);
            } else {
                Picasso.with(mContext).load(R.drawable.user_default).into(img_stage);
            }

            RecyclerView rv_events = view.findViewById(R.id.rv_events);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            rv_events.setLayoutManager(layoutManager);

            if (stage_data.get(position).getEvent_data_data() != null && stage_data.get(position).getEvent_data_data().size() > 0) {
                EventListAdapter eventListAdapter = new EventListAdapter(mContext, stage_data.get(position).getEvent_data_data());
                rv_events.setAdapter(eventListAdapter);
                rv_events.setVisibility(View.VISIBLE);
            } else {
                tv_msg.setVisibility(View.VISIBLE);
                rv_events.setVisibility(View.GONE);
            }

            collection.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return CustomPagerEnum.values().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }


    public enum CustomPagerEnum {

        STAGE1("stage1", R.layout.custom_row_stage_temp),
        STAGE2("stage2", R.layout.custom_row_stage_temp),
        STAGE3("stage3", R.layout.custom_row_stage_temp);

        private String mTitleResId;
        private int mLayoutResId;

        CustomPagerEnum(String titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public String getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }
}