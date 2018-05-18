package com.timeout72hours.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timeout72hours.R;
import com.timeout72hours.adapter.ArticleListAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.ArticleListResponse;
import com.timeout72hours.model.EventListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hardip on 5/12/17.
 */

public class ArticlesFragment extends android.support.v4.app.Fragment {

    private View view;
    private Context mContext;
    private RecyclerView rv_article_list;
    private APIInterface apiInterface;

    private CustomProgressDialog progressDialog;
    private boolean api_response=false;
    private TextView tv_msg;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_articles, null);
        init();

        return view;
    }

    private void init() {
        mContext = getActivity();

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);

        tv_msg = view.findViewById(R.id.tv_msg);
        rv_article_list = view.findViewById(R.id.rv_article_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_article_list.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        getArticles();
        Handler hnd = new Handler();
        hnd.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!api_response){
                    if (!progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                } else {
                    api_response = false;
                }
            }
        },1000);
    }

    private void getArticles() {

        if (Utility.isNetworkAvaliable(mContext)) {

            apiInterface.getArticles().enqueue(new Callback<ArticleListResponse>() {
                @Override
                public void onResponse(Call<ArticleListResponse> call, Response<ArticleListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    api_response = true;
                    if (response.isSuccessful() && response.body()!=null && response.body().getResponse().equalsIgnoreCase("true")){
                        if (response.body().getArticle_data().size()>0){
                            tv_msg.setVisibility(View.GONE);
                            ArticleListAdapter articleListAdapter = new ArticleListAdapter(mContext,response.body().getArticle_data());
                            rv_article_list.setAdapter(articleListAdapter);
                        } else {
                            tv_msg.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tv_msg.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ArticleListResponse> call, Throwable t) {
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

}
