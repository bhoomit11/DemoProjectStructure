package com.timeout72hours.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.timeout72hours.R;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.fragment.AboutUsFragment;
import com.timeout72hours.fragment.ArticlesFragment;
import com.timeout72hours.fragment.ArtistDetailFragment;
import com.timeout72hours.fragment.ArtistFragment;
import com.timeout72hours.fragment.DirectionFragment;
import com.timeout72hours.fragment.EventsFragment;
import com.timeout72hours.fragment.FaqFragment;
import com.timeout72hours.fragment.FriendsMainFragment;
import com.timeout72hours.fragment.HomeFragment;
import com.timeout72hours.fragment.MenuFragment;
import com.timeout72hours.fragment.ProfileFragment;
import com.timeout72hours.fragment.TicketInfoFragment;
import com.timeout72hours.fragment.TransactionsFragment;
import com.timeout72hours.model.ArtistListData;
import com.timeout72hours.model.CommonResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 2/12/17.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    private int selected_page = 0;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private ImageView img_menu;
    public ArrayList<ArtistListData> artistListData;
    public int selected_artist = 0;
    private boolean is_detail_open = false;

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;
    private Fragment menu_content_frame;

    private APIInterface apiInterface;
    private CustomProgressDialog mProgressDialog;
    private String type = "", sub_type = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
            sub_type = getIntent().getStringExtra("sub_type");
        }

        init();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int densityDpi = (int) (metrics.density * 160f);

        Log.e("tet", "" + densityDpi);
    }

    private void init() {
        mContext = MainActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new CustomProgressDialog(mContext);

        mDrawer = (DrawerLayout) findViewById(R.id.dl_home);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        img_menu = (ImageView) findViewById(R.id.img_menu);
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
                    ((MenuFragment) menu_content_frame).loadImage();
                    mDrawer.openDrawer(GravityCompat.START);
                } else {
                    mDrawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        menu_content_frame = getSupportFragmentManager().findFragmentById(R.id.menu_content_frame);

        Fragment fragment = new HomeFragment();


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, "home");
        fragmentTransaction.commitAllowingStateLoss();

        if (type.equals("notification")) {
            changefragment(10, sub_type);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    public void closeDrawer() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (selected_page != 0) {
                //  changefragment(0);
                if (is_detail_open) {
                    is_detail_open = false;
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack("artist", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        //   tv_toolbar_title.setText("Home");
                    }
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    if (fm.getBackStackEntryCount() > 0) {
                        fm.popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        selected_page = 0;
                        //   tv_toolbar_title.setText("Home");
                    }
                }

            } else {
                new AlertDialog.Builder(mContext)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to Exit?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        finishAffinity();
                                        System.exit(0);
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

            }
        }
    }

    public void changefragment(int pos, String sub_type) {
        closeDrawer();

        Fragment fragment = null;
        selected_page = pos;

        //  tv_toolbar_title.setText("Feedback");
        fragment = new FriendsMainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sub_type", sub_type);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out, android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack("home");
        fragmentTransaction.commitAllowingStateLoss();


    }

    public void changefragment(int pos) {
        closeDrawer();

        if (selected_page != pos) {
            Fragment fragment = null;
            selected_page = pos;
            if (pos == 0) {
                // tv_toolbar_title.setText("Home");
                fragment = new HomeFragment();
            } else if (pos == 1) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new ArtistFragment();
            } else if (pos == 2) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new ProfileFragment();
            } else if (pos == 3) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new FaqFragment();
            } else if (pos == 4) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new AboutUsFragment();
            } else if (pos == 5) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new TicketInfoFragment();
            } else if (pos == 6) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new ArticlesFragment();
            } else if (pos == 7) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new EventsFragment();
            } else if (pos == 8) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new DirectionFragment();
            } else if (pos == 9) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new TransactionsFragment();
            } else if (pos == 10) {
                //  tv_toolbar_title.setText("Feedback");
                fragment = new FriendsMainFragment();
            }
            final Fragment finalFragment = fragment;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out, android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, finalFragment);
            fragmentTransaction.addToBackStack("home");
            fragmentTransaction.commitAllowingStateLoss();
//                }
//            }, 300);

        }

    }

    public void addArtistDetailPage() {

        is_detail_open = true;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out, android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, new ArtistDetailFragment());
        fragmentTransaction.addToBackStack("artist");
        fragmentTransaction.commitAllowingStateLoss();
    }

    public void callLogout() {

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

            apiInterface.logout(Utility.getAppPrefString(mContext, Constant.USER_ID)).enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equals("true")) {
                        Utility.showToastSuccess(MainActivity.this, response.body().getMessage());
                        try {
                            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (nMgr != null) {
                                nMgr.cancelAll();
                            }
                        } catch (Exception e) {
                        }
                        Intent intent = new Intent(mContext, LoginRegisterActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Utility.writeSharedPreferences(mContext, Constant.USER_ID, "");

                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(MainActivity.this, getResources().getString(R.string.internetErrorMsg));
        }


    }


}
