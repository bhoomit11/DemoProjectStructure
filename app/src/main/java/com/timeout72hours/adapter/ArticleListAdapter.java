package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.ArticleDetailActivity;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ArticleListData;
import com.timeout72hours.model.ArtistListData;

import java.util.ArrayList;

/**
 * Created by hardip on 6/12/17.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ArticleListData> articleListData;

    public ArticleListAdapter(Context mContext, ArrayList<ArticleListData> articleListData) {
        this.mContext = mContext;
        this.articleListData = articleListData;
    }

    @Override
    public ArticleListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_article_list, parent, false);

        return new ArticleListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleListAdapter.MyViewHolder holder, final int position) {
        holder.tv_title.setText(articleListData.get(position).getArticle_name());
        if (!articleListData.get(position).getTotal_comment().equals("0")){
            holder.img_comment.setVisibility(View.VISIBLE);
            holder.tv_comment.setText(articleListData.get(position).getTotal_comment()+" People comment on this post");
        } else {
            holder.img_comment.setVisibility(View.GONE);
        }

        String datetime[] = articleListData.get(position).getPublish_date_time().split(" ");
        String date = datetime[0];
        String time = datetime[1];
        holder.tv_date.setText(Utility.sendDateConvertreverse(date));
        holder.tv_time.setText(time);
        Picasso.with(mContext).load(articleListData.get(position).getImage()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext,ArticleDetailActivity.class);
                in.putExtra("article_id",articleListData.get(position).getArticle_id());
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img,img_comment;
        private TextView tv_title, tv_date, tv_time, tv_comment;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            img_comment = itemView.findViewById(R.id.img_comment);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_comment = itemView.findViewById(R.id.tv_comment);

        }
    }
}

