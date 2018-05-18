package com.timeout72hours.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.timeout72hours.R;
import com.timeout72hours.activities.MainActivity;
import com.timeout72hours.model.ArtistListData;
import com.timeout72hours.model.PassData;

import java.util.ArrayList;

/**
 * Created by hardip on 3/12/17.
 */

public class TicketInfoAdapter extends RecyclerView.Adapter<TicketInfoAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<PassData> passData;

    public TicketInfoAdapter(Context mContext, ArrayList<PassData> passData) {
        this.mContext = mContext;
        this.passData = passData;
    }

    @Override
    public TicketInfoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_passes_list, parent, false);

        return new TicketInfoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TicketInfoAdapter.MyViewHolder holder, final int position) {

        holder.tv_title.setText(passData.get(position).getTitle());
        holder.tv_price.setText(passData.get(position).getPrice()+"/-");

    }

    @Override
    public int getItemCount() {
        return passData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_artist;
        private TextView tv_title,tv_price;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_title =  itemView.findViewById(R.id.tv_title);
            tv_price =  itemView.findViewById(R.id.tv_price);

        }
    }
}
