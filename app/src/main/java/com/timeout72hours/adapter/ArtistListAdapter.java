package com.timeout72hours.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.attributes.Constant;
import com.timeout72hours.attributes.Utility;
import com.timeout72hours.model.ArtistListData;
import com.timeout72hours.model.FaqData;

import java.util.ArrayList;

/**
 * Created by hardip on 2/12/17.
 */

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ArtistListData> artistListData;

    public ArtistListAdapter(Context mContext, ArrayList<ArtistListData> artistListData) {
        this.mContext = mContext;
        this.artistListData = artistListData;
    }

    @Override
    public ArtistListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_artist_list, parent, false);

        return new ArtistListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArtistListAdapter.MyViewHolder holder, final int position) {
        holder.tv_name.setText(artistListData.get(position).getArtist_name());
        Picasso.with(mContext).load(artistListData.get(position).getImage_small()).placeholder(R.drawable.logo).into(holder.img_artist);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)mContext).selected_artist=position;
                ((MainActivity)mContext).addArtistDetailPage();
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistListData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_artist;
        private TextView tv_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            img_artist =  itemView.findViewById(R.id.img_artist);
            tv_name =  itemView.findViewById(R.id.tv_name);
        }
    }
}
