package com.timeout72hours.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.activities.ChatListActivity;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.CountryData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 18/12/17.
 */

public class FriendsMainFragment extends Fragment {

    private View view;
    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private APIInterface apiInterface;
    public String[] countryId, countryName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_friends_main, null);
        initUI();
        getCountries();
        return view;
    }

    private void initUI() {
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);

        tabLayout = view.findViewById(R.id.tabs);
        viewpager = view.findViewById(R.id.viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Requests"));
        tabLayout.addTab(tabLayout.newTab().setText("Search"));

        tabLayout.setupWithViewPager(viewpager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_text_layout, null);
            tabLayout.getTabAt(i).setCustomView(tv);
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

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
        setupViewPager();
    }

    private void getCountries() {
        if (Utility.isNetworkAvaliable(mContext)) {

            Call<CountryData> call = apiInterface.getCountries();

            call.enqueue(new Callback<CountryData>() {
                @Override
                public void onResponse(Call<CountryData> call, Response<CountryData> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CountryData countryData = response.body();

                        countryId = new String[(countryData.getContries_list().size() + 1)];
                        countryName = new String[(countryData.getContries_list().size() + 1)];
                        countryId[0] = "";
                        countryName[0] = "All";
                        for (int i = 1; i <= countryData.getContries_list().size(); i++) {
                            countryId[i] = countryData.getContries_list().get(i - 1).getId();
                            countryName[i] = countryData.getContries_list().get(i - 1).getName();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CountryData> call, Throwable t) {

                }
            });
        }else {
            Utility.showToast(getActivity(),getActivity().getResources().getString(R.string.internetErrorMsg));
        }
    }

    private void setupViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FriendsFragment(), "Friends");
        adapter.addFragment(new RequestsFragment(), "Requests");
        adapter.addFragment(new SearchFragment(), "Search");
        viewpager.setAdapter(adapter);

        if (getArguments()!=null && getArguments().getString("sub_type") != null) {
            if (getArguments().getString("sub_type").equalsIgnoreCase("request")) {
                viewpager.setCurrentItem(1);
            }
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
