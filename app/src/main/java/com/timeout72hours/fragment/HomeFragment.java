package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.adapter.ArticleListAdapter;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.ArticleListResponse;
import com.timeout72hours.model.EventListEventData;
import com.timeout72hours.model.OngoingEventResponse;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 2/12/17.
 */

public class HomeFragment extends Fragment {

    private View view;
    private Context mContext;


    long milliseconds = 0;
    long diff = 0;
    long startTime = 0;

    TextView tv_days,tv_hours,tv_minutes,tv_second;
    CountDownTimer mCountDownTimer;

    private TabLayout tab_dots;
    private ViewPager viewpager;

    private APIInterface apiInterface;
    private CustomProgressDialog mProgressDialog;
    private boolean api_response=false;

    private LinearLayout ll_events,ll_timer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_home, null);


        init();


        return view;
    }

    private void init(){
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        tv_days =  view.findViewById(R.id.tv_days);
        tv_hours =  view.findViewById(R.id.tv_hours);
        tv_minutes =  view.findViewById(R.id.tv_minutes);
        tv_second = view.findViewById(R.id.tv_second);
        ll_events = view.findViewById(R.id.ll_events);
        ll_timer = view.findViewById(R.id.ll_timer);

        tab_dots =  view.findViewById(R.id.tab_dots);
        viewpager =  view.findViewById(R.id.viewpager);

//        tab_events.addTab(tab_events.newTab().setText("MAIN STAGE"));
//        tab_events.addTab(tab_events.newTab().setText("PSY STAGE"));
//        tab_events.addTab(tab_events.newTab().setText("LIVE STAGE"));



    }

    @Override
    public void onResume() {
        super.onResume();
        getOngoingEvent();
        Handler hnd = new Handler();
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!api_response){
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                } else {
                    api_response = false;
                }
            }
        },1000);
    }

    private void getOngoingEvent(){
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
            apiInterface.getOngoingEvent().enqueue(new Callback<OngoingEventResponse>() {
                @Override
                public void onResponse(Call<OngoingEventResponse> call, Response<OngoingEventResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    api_response = true;
                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
//                        response.body().setIs_timer("false");
                        if (response.body().getIs_timer().equals("true")){
                            ll_events.setVisibility(View.GONE);
                            ll_timer.setVisibility(View.VISIBLE);
                            timeRemaining();
                        } else {
                            ll_events.setVisibility(View.VISIBLE);
                            ll_timer.setVisibility(View.GONE);
                            if (response.body().getEvent_data().size() > 0) {
                                CustomPageAdapter mCustomPagerAdapter = new CustomPageAdapter(getActivity(), response.body().getEvent_data());
                                viewpager.setAdapter(mCustomPagerAdapter);
                                tab_dots.setupWithViewPager(viewpager,true);
                            } else {
                                ll_events.setVisibility(View.GONE);
                            }
                        }



                    } else {
                        ll_events.setVisibility(View.GONE);
                        ll_timer.setVisibility(View.GONE);
                    }
                }


                @Override
                public void onFailure(Call<OngoingEventResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    api_response = true;
                }
            });

        } else {
            Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.internetErrorMsg));
        }

    }

    private void timeRemaining() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        formatter.setLenient(false);

        String endTime = "27.12.2017, 12:00:00";

        Date endDate;
        try {
            endDate = formatter.parse(endTime);
            milliseconds = endDate.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        diff = milliseconds - startTime;

        try {
            mCountDownTimer.cancel();
        } catch (Exception e){}

        mCountDownTimer  = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                startTime = startTime - 1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                if (daysLeft.trim().length()==1){
                    tv_days.setText("0"+daysLeft);
                } else {
                    tv_days.setText(daysLeft);
                }


                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                if (hoursLeft.trim().length()==1){
                    tv_hours.setText("0"+hoursLeft);
                } else {
                    tv_hours.setText(hoursLeft);
                }


                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                if (minutesLeft.trim().length()==1){
                    tv_minutes.setText("0"+minutesLeft);
                } else {
                    tv_minutes.setText(minutesLeft);
                }


                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);
                if (secondsLeft.trim().length()==1){
                    tv_second.setText("0"+secondsLeft);
                } else {
                    tv_second.setText(secondsLeft);
                }


            }

            @Override
            public void onFinish() {

            }
        };

        mCountDownTimer.start();
    }

    private class CustomPageAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<EventListEventData> eventListEventData;
        private LayoutInflater layoutInflater;
        public CustomPageAdapter(Context mContext, ArrayList<EventListEventData> eventListEventData){
            this.mContext = mContext;
            this.eventListEventData = eventListEventData;
            this.layoutInflater = (LayoutInflater)this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return eventListEventData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = this.layoutInflater.inflate(R.layout.custom_row_event_ongoing, container, false);

            ImageView img_artist =  itemView.findViewById(R.id.img_artist);
            TextView tv_start_time =  itemView.findViewById(R.id.tv_start_time);
            TextView tv_end_time =  itemView.findViewById(R.id.tv_end_time);
            TextView tv_event_name =  itemView.findViewById(R.id.tv_event_name);
            TextView tv_artist_name =  itemView.findViewById(R.id.tv_artist_name);
            TextView tv_stage =  itemView.findViewById(R.id.tv_stage);

            tv_start_time.setText(eventListEventData.get(position).getStart_time());
            tv_end_time.setText(eventListEventData.get(position).getEnd_time());
            tv_event_name.setText(eventListEventData.get(position).getEvent_name());
            tv_artist_name.setText("Artist:  "+eventListEventData.get(position).getArtist_name().trim());
            tv_stage.setText(eventListEventData.get(position).getStage_name());

            if (eventListEventData.get(position).getArtist_image()!=null && eventListEventData.get(position).getArtist_image().trim().length()>0){
                Picasso.with(mContext).load(eventListEventData.get(position).getArtist_image()).error(R.drawable.user_default).transform(new CircleTransform()).into(img_artist);
            } else {
                Picasso.with(mContext).load(R.drawable.user_default).transform(new CircleTransform()).into(img_artist);
            }

            container.addView(itemView);
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return eventListEventData.get(position).getStage_name().trim();
//        }
    }

}
