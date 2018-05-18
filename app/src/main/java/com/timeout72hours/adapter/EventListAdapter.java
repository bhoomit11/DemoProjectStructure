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
import com.timeout72hours.attributes.CircleTransform;
import com.timeout72hours.model.EventListEventData;
import com.timeout72hours.model.PassData;

import java.util.ArrayList;

/**
 * Created by hardip on 7/12/17.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<EventListEventData> eventListEventData;

    public EventListAdapter(Context mContext, ArrayList<EventListEventData> eventListEventData) {
        this.mContext = mContext;
        this.eventListEventData = eventListEventData;
    }

    @Override
    public EventListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_event_list, parent, false);

        return new EventListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventListAdapter.MyViewHolder holder, final int position) {

        holder.tv_start_time.setText(eventListEventData.get(position).getStart_time());
        holder.tv_end_time.setText(eventListEventData.get(position).getEnd_time());
        holder.tv_event_name.setText(eventListEventData.get(position).getEvent_name());
        holder.tv_artist_name.setText("Artist:  "+eventListEventData.get(position).getArtist_name());

        if (eventListEventData.get(position).getArtist_image()!=null && eventListEventData.get(position).getArtist_image().trim().length()>0){
            Picasso.with(mContext).load(eventListEventData.get(position).getArtist_image()).error(R.drawable.user_default).transform(new CircleTransform()).into(holder.img_artist);
        } else {
            Picasso.with(mContext).load(R.drawable.user_default).transform(new CircleTransform()).into(holder.img_artist);
        }

    }

    @Override
    public int getItemCount() {
        return eventListEventData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_start_time,tv_end_time,tv_event_name,tv_artist_name;
        private ImageView img_artist;

        public MyViewHolder(View itemView) {
            super(itemView);

            img_artist =  itemView.findViewById(R.id.img_artist);
            tv_start_time =  itemView.findViewById(R.id.tv_start_time);
            tv_end_time =  itemView.findViewById(R.id.tv_end_time);
            tv_event_name =  itemView.findViewById(R.id.tv_event_name);
            tv_artist_name =  itemView.findViewById(R.id.tv_artist_name);

        }
    }
}
