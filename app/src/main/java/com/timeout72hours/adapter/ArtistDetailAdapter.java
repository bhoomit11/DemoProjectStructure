package com.timeout72hours.adapter;

import android.content.Context;
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
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ArtistListData;

import java.util.ArrayList;

/**
 * Created by hardip on 2/12/17.
 */

public class ArtistDetailAdapter extends RecyclerView.Adapter<ArtistDetailAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ArtistListData> artistListData;

    public ArtistDetailAdapter(Context mContext, ArrayList<ArtistListData> artistListData) {
        this.mContext = mContext;
        this.artistListData = artistListData;
    }

    @Override
    public ArtistDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_artist_detail, parent, false);

        return new ArtistDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistDetailAdapter.MyViewHolder holder, int position) {
        holder.tv_name.setText(artistListData.get(position).getArtist_name());
//        if(artistListData.get(position).getPlay_date_1()!=null){
//         //   holder.tv_date.setText(Utility.sendDateConvertreverse(artistListData.get(position).getPlay_date()));
//            holder.tv_date.setText(artistListData.get(position).getPlay_date_1());
//        }else {
//            holder.tv_date.setVisibility(View.GONE);
//        }
//        holder.tv_des.setText(Html.fromHtml(artistListData.get(position).getDescription()));

        String text = artistListData.get(position).getDescription();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            CharSequence sequence2 = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
            SpannableStringBuilder strBuilder2 = new SpannableStringBuilder(sequence2);
            holder.tv_des.setText(strBuilder2);
        } else {
            CharSequence sequence2 = Html.fromHtml(text);
            SpannableStringBuilder strBuilder2 = new SpannableStringBuilder(sequence2);
            holder.tv_des.setText(strBuilder2);
        }
        if(artistListData.get(position).getPlay_date_1()!=null && artistListData.get(position).getPlay_date_1().trim().length()>0){
            //   holder.tv_date.setText(Utility.sendDateConvertreverse(artistListData.get(position).getPlay_date()));
            String textt[] = artistListData.get(position).getPlay_date_1().split(" ");
            String text2 = textt[0]+"<sup>th</sup> "+textt[1];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                CharSequence sequence2 = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
                SpannableStringBuilder strBuilder2 = new SpannableStringBuilder(sequence2);
                holder.tv_des.setText(strBuilder2);
                CharSequence sequence = Html.fromHtml(text2, Html.FROM_HTML_MODE_LEGACY);
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                holder.tv_date.setText(strBuilder);
            } else {
                CharSequence sequence2 = Html.fromHtml(text);
                SpannableStringBuilder strBuilder2 = new SpannableStringBuilder(sequence2);
                holder.tv_des.setText(strBuilder2);
                CharSequence sequence = Html.fromHtml(text2);
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                holder.tv_date.setText(strBuilder);
            }
        }else {
            holder.tv_date.setVisibility(View.GONE);
        }
        Picasso.with(mContext).load(artistListData.get(position).getImage_large()).into(holder.img_artist);
    }

    @Override
    public int getItemCount() {
        return artistListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_artist;
        private TextView tv_name,tv_date,tv_des;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_artist =  itemView.findViewById(R.id.img_artist);
            tv_name =  itemView.findViewById(R.id.tv_name);
            tv_date =  itemView.findViewById(R.id.tv_date);
            tv_des =  itemView.findViewById(R.id.tv_des);

        }
    }
}

