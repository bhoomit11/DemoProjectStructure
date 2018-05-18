package com.timeout72hours.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.adapter.ArticleListAdapter;
import com.timeout72hours.adapter.CommentUserListAdapter;
import com.timeout72hours.attributes.CustomProgressDialog;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.attributes.retrofit.APIClient;
import com.timeout72hours.attributes.retrofit.APIInterface;
import com.timeout72hours.model.AddLikeResponse;
import com.timeout72hours.model.ArticleDetailResponse;
import com.timeout72hours.model.ArticleListResponse;
import com.timeout72hours.model.CommentListResponse;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.timeout72hours.attributes.Constant.USER_ID;

/**
 * Created by hardip on 6/12/17.
 */

public class ArticleDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private APIInterface apiInterface;

    private ImageView img_article, img_likes, imag_comments, img_share;
    private TextView tv_title, tv_date, tv_time, tv_des, tv_likes, tv_comments;

    private LinearLayout btn_send;
    private EditText et_comment;
    private String article_id;

    private LinearLayout ll_likes, ll_share;
    private RelativeLayout rl_main;

    private CustomProgressDialog progressDialog;
    private ArticleDetailResponse articleDetailResponse;
    private String is_liked = "0";
    private int page_no = 1;
    private ImageView img_back;

    private RecyclerView rv_comments;
    private AppBarLayout app_bar_layout;
    private TextView tv_prev, tv_next, tv_article_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_article_detail);
        article_id = getIntent().getStringExtra("article_id");
        init();
        getArticleDetail();
    }

    private void init() {
        mContext = ArticleDetailActivity.this;

        apiInterface = APIClient.getClient().create(APIInterface.class);
        progressDialog = new CustomProgressDialog(mContext);

        ll_likes = findViewById(R.id.ll_likes);
        ll_share = findViewById(R.id.ll_share);
        rl_main = findViewById(R.id.rl_main);

        rv_comments = findViewById(R.id.rv_comments);

        tv_prev = findViewById(R.id.tv_prev);
        tv_next = findViewById(R.id.tv_next);

        img_back = findViewById(R.id.img_back);
        img_article = findViewById(R.id.img_article);
        img_likes = findViewById(R.id.img_likes);
        imag_comments = findViewById(R.id.imag_comments);
        img_share = findViewById(R.id.img_share);
        tv_title = findViewById(R.id.tv_title);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_des = findViewById(R.id.tv_des);
        tv_likes = findViewById(R.id.tv_likes);
        tv_comments = findViewById(R.id.tv_comments);
        btn_send = findViewById(R.id.btn_send);
        et_comment = findViewById(R.id.et_comment);
        tv_article_title = findViewById(R.id.tv_article_title);
        tv_article_title.setVisibility(View.GONE);

        ll_likes.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        img_back.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        tv_prev.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        app_bar_layout = findViewById(R.id.app_bar_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rv_comments.setLayoutManager(layoutManager);


        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < -350) {
                    // Collapsed
                    tv_article_title.setVisibility(View.VISIBLE);
                } else {
                    // Not collapsed
                    tv_article_title.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getArticleDetail() {

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            apiInterface.getArticleDetail(article_id, Utility.getAppPrefString(mContext, USER_ID)).enqueue(new Callback<ArticleDetailResponse>() {
                @Override
                public void onResponse(Call<ArticleDetailResponse> call, Response<ArticleDetailResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        articleDetailResponse = response.body();
                        fillData();
                    }
                }

                @Override
                public void onFailure(Call<ArticleDetailResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        } else {
            Utility.showToast(ArticleDetailActivity.this, getResources().getString(R.string.internetErrorMsg));
        }

    }

    private void fillData() {
        if (Integer.parseInt(articleDetailResponse.getPage_count()) > page_no) {
            tv_prev.setVisibility(View.VISIBLE);
        }
        rl_main.setVisibility(View.VISIBLE);
        tv_title.setText(articleDetailResponse.getArticle_name());
        tv_article_title.setText(articleDetailResponse.getArticle_name());

        if (!articleDetailResponse.getTotal_comment().equals("0")) {
            tv_comments.setText(articleDetailResponse.getTotal_comment() + " Comments");
        } else {
            tv_comments.setText("No Comments");
        }

        if (!articleDetailResponse.getTotal_likes().equals("0")) {
            tv_likes.setText(articleDetailResponse.getTotal_likes() + " Likes");
        } else {
            tv_likes.setText("No Likes");
        }
        is_liked = articleDetailResponse.getLiked();
        if (is_liked.equals("1")) {
            img_likes.setImageResource(R.drawable.icon_liked);
        }
        String datetime[] = articleDetailResponse.getPublish_date_time().split(" ");
        String date = datetime[0];
        String time = datetime[1];
        tv_date.setText(Utility.sendDateConvertreverse(date));
        tv_time.setText(time);

        Picasso.with(mContext).load(articleDetailResponse.getImage()).into(img_article);

        CommentUserListAdapter commentUserListAdapter = new CommentUserListAdapter(mContext, articleDetailResponse.getComment_data());
        rv_comments.setAdapter(commentUserListAdapter);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //   tv_notification_text.setText(Html.fromHtml(notificationDatas.get(i).getNotification_list().get(i1).getNotification_description(), Html.FROM_HTML_MODE_LEGACY));
            CharSequence sequence = Html.fromHtml(articleDetailResponse.getArticle_des(), Html.FROM_HTML_MODE_LEGACY);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            tv_des.setText(strBuilder);
            tv_des.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            CharSequence sequence = Html.fromHtml(articleDetailResponse.getArticle_des());
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
            for (URLSpan span : urls) {
                makeLinkClickable(strBuilder, span);
            }
            tv_des.setText(strBuilder);
            tv_des.setMovementMethod(LinkMovementMethod.getInstance());
            //  tv_notification_text.setText(Html.fromHtml(notificationDatas.get(i).getNotification_list().get(i1).getNotification_description()));
        }


    }

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setDataAndType(Uri.parse(span.getURL()), null);
                Intent chooser = Intent.createChooser(browserIntent, "Choose any one");
                chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // optional
                mContext.startActivity(chooser);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_prev:
                page_no = page_no + 1;
                tv_next.setVisibility(View.VISIBLE);
                getComments();
                break;

            case R.id.tv_next:
                page_no = page_no - 1;
                tv_prev.setVisibility(View.VISIBLE);
                getComments();
                break;

            case R.id.ll_likes:
                addLike();
                break;

            case R.id.btn_send:
                if (et_comment.getText().toString().trim().length() > 0) {
                    addComment();
                }
                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.ll_share:
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, articleDetailResponse.getArticle_name());
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, articleDetailResponse.getArticle_des());
//                startActivity(Intent.createChooser(sharingIntent, "Share"));
                if(!progressDialog.isShowing()) {
                    progressDialog.show();
                }

                SpannableStringBuilder strBuilder = new SpannableStringBuilder();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    CharSequence sequence = Html.fromHtml(articleDetailResponse.getArticle_name(), Html.FROM_HTML_MODE_LEGACY);
                    strBuilder = new SpannableStringBuilder(sequence);
                } else {
                    CharSequence sequence = Html.fromHtml(articleDetailResponse.getArticle_name());
                    strBuilder = new SpannableStringBuilder(sequence);
                }
                try {
                    URL url = new URL(articleDetailResponse.getImage());
                    Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media
                            .insertImage(getContentResolver(), image,
                                    strBuilder.toString(), "http://bit.ly/2nK4orY")));
                    intent.setType("image/jpeg");

                    intent.putExtra(Intent.EXTRA_SUBJECT, strBuilder.toString());
                    intent.putExtra(Intent.EXTRA_TEXT, strBuilder.toString()+"\n\n"+"Download Timeout72 App:\n"+"http://bit.ly/2nK4orY");

                    startActivity(Intent.createChooser(intent, "Share"));

                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                break;

        }

    }

    private void getComments() {

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            apiInterface.getComments(article_id, page_no + "").enqueue(new Callback<CommentListResponse>() {
                @Override
                public void onResponse(Call<CommentListResponse> call, Response<CommentListResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        CommentUserListAdapter commentUserListAdapter = new CommentUserListAdapter(mContext, response.body().getComment_data());
                        rv_comments.setAdapter(commentUserListAdapter);
                        if (Integer.parseInt(response.body().getPage_count()) > page_no) {
                            tv_prev.setVisibility(View.VISIBLE);
                        } else {
                            tv_prev.setVisibility(View.GONE);
                        }
                        if (page_no == 1) {
                            tv_next.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CommentListResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(ArticleDetailActivity.this, getResources().getString(R.string.internetErrorMsg));
        }
    }

    private void addComment() {
        if (Utility.isNetworkAvaliable(mContext)) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            String comment = "";

            try {
                comment = URLEncoder.encode(et_comment.getText().toString().trim(), "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }


            apiInterface.addComment(article_id, Utility.getAppPrefString(mContext, USER_ID), comment).enqueue(new Callback<AddLikeResponse>() {
                @Override
                public void onResponse(Call<AddLikeResponse> call, Response<AddLikeResponse> response) {

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {
                        et_comment.setText("");
                        if (!response.body().getTotal_comment().equals("0")) {
                            tv_comments.setText(response.body().getTotal_comment() + " Comments");
                        } else {
                            tv_comments.setText("No Comments");
                        }

                        if (!response.body().getTotal_likes().equals("0")) {
                            tv_likes.setText(response.body().getTotal_likes() + " Likes");
                        } else {
                            tv_likes.setText("No Likes");
                        }

                        page_no = 1;
                        getComments();
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddLikeResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showToast(ArticleDetailActivity.this, getResources().getString(R.string.internetErrorMsg));
        }

    }

    private void addLike() {

        if (Utility.isNetworkAvaliable(mContext)) {

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }

            apiInterface.addLike(article_id, Utility.getAppPrefString(mContext, USER_ID)).enqueue(new Callback<AddLikeResponse>() {
                @Override
                public void onResponse(Call<AddLikeResponse> call, Response<AddLikeResponse> response) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (response.isSuccessful() && response.body() != null && response.body().getResponse().equalsIgnoreCase("true")) {

                        if (!response.body().getTotal_comment().equals("0")) {
                            tv_comments.setText(response.body().getTotal_comment() + " Comments");
                        } else {
                            tv_comments.setText("No Comments");
                        }

                        if (!response.body().getTotal_likes().equals("0")) {
                            tv_likes.setText(response.body().getTotal_likes() + " Likes");
                        } else {
                            tv_likes.setText("No Likes");
                        }
                        if (is_liked.equals("1")) {
                            is_liked = "0";
                            img_likes.setImageResource(R.drawable.icon_unlike);
                        } else {
                            is_liked = "1";
                            img_likes.setImageResource(R.drawable.icon_liked);
                        }

                    }

                }

                @Override
                public void onFailure(Call<AddLikeResponse> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });

        } else {
            Utility.showToast(ArticleDetailActivity.this, getResources().getString(R.string.internetErrorMsg));
        }


    }

}
