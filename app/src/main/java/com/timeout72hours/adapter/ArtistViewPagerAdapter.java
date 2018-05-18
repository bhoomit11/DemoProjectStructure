package com.timeout72hours.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
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
 * Created by bhumit on 29/4/17.
 */
public class ArtistViewPagerAdapter extends PagerAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ArrayList<ArtistListData> artistListData;

    public ArtistViewPagerAdapter(Context mContext, ArrayList<ArtistListData> artistListData) {
        this.mContext = mContext;
        this.artistListData = artistListData;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = mLayoutInflater.inflate(R.layout.custom_row_artist_detail, null);
        container.addView(view);

        ImageView img_artist = view.findViewById(R.id.img_artist);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_des = view.findViewById(R.id.tv_des);


        tv_name.setText(artistListData.get(position).getArtist_name());
//        if (artistListData.get(position).getPlay_date() != null) {
//            tv_date.setText("Will Play On " + Utility.sendDateConvertreverse(artistListData.get(position).getPlay_date()) + " AT VAGATOR, GOA");
//        } else {
//            tv_date.setVisibility(View.GONE);
//        }
//        tv_des.setText(Html.fromHtml(artistListData.get(position).getDescription()));

        String text = artistListData.get(position).getDescription();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            CharSequence sequence = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            tv_des.setText(strBuilder);
        } else {
            CharSequence sequence = Html.fromHtml(text);
            SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
            tv_des.setText(strBuilder);
        }
        if(artistListData.get(position).getPlay_date_1()!=null && artistListData.get(position).getPlay_date_1().trim().length()>0){
            //   holder.tv_date.setText(Utility.sendDateConvertreverse(artistListData.get(position).getPlay_date()));
            String textt[] = artistListData.get(position).getPlay_date_1().split(" ");
            String text2 = textt[0]+"<sup>th</sup> "+textt[1];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                CharSequence sequence2 = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
                SpannableStringBuilder strBuilder2 = new SpannableStringBuilder(sequence2);
                tv_des.setText(strBuilder2);
                CharSequence sequence = Html.fromHtml(text2, Html.FROM_HTML_MODE_LEGACY);
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                tv_date.setText(strBuilder);
            } else {
                CharSequence sequence2 = Html.fromHtml(text);
                SpannableStringBuilder strBuilder2 = new SpannableStringBuilder(sequence2);
                tv_des.setText(strBuilder2);
                CharSequence sequence = Html.fromHtml(text2);
                SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
                tv_date.setText(strBuilder);
            }
        }else {
            tv_date.setVisibility(View.GONE);
        }
        Picasso.with(mContext).load(artistListData.get(position).getImage_large()).into(img_artist);
        return view;
    }

    @Override
    public int getCount() {
        return artistListData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

}
