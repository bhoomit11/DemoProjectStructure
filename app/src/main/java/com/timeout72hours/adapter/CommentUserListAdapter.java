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
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ArticleListData;
import com.timeout72hours.model.CommentListData;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hardip on 6/12/17.
 */

public class CommentUserListAdapter extends RecyclerView.Adapter<CommentUserListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<CommentListData> commentListData;

    public CommentUserListAdapter(Context mContext, ArrayList<CommentListData> commentListData) {
        this.mContext = mContext;
        this.commentListData = commentListData;
    }

    @Override
    public CommentUserListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_comments_user_list, parent, false);

        return new CommentUserListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentUserListAdapter.MyViewHolder holder, final int position) {

        if (commentListData.get(position).getUser_name()!=null){
            holder.tv_name.setText(commentListData.get(position).getUser_name());
            try {
                holder.tv_comment.setText(URLDecoder.decode(commentListData.get(position).getComment(), "UTF-8"));
            } catch (Exception e){
                e.printStackTrace();
            }
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                CharSequence sequence = Html.fromHtml(commentListData.get(position).getComment(), Html.FROM_HTML_MODE_LEGACY);
//                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
//                holder.tv_comment.setText(strBuilder);
//            } else {
//                CharSequence sequence = Html.fromHtml(commentListData.get(position).getComment());
//                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
//                holder.tv_comment.setText(strBuilder);
//            }
            String datetime[] = commentListData.get(position).getComment_date().split(" ");
            String date = datetime[0];
            String time = datetime[1];

//            SimpleDateFormat inputFormat = new SimpleDateFormat
//                    ("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
//            holder.tv_date.setText(inputFormat.format(commentListData.get(position).getComment_date()));

            holder.tv_date.setText(Utility.sendDateConvertreverse(date)+" "+time);
            Picasso.with(mContext).load(commentListData.get(position).getUser_image()).error(R.drawable.user_default).transform(new CircleTransform()).into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        return commentListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_name, tv_date, tv_comment;

        public MyViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_comment = itemView.findViewById(R.id.tv_comment);

        }
    }
}

