package com.raystone.ray.popmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ray on 2/13/2016.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    private Context mContext;
    private String[] trailersUri;


    public TrailersAdapter(Context context,String[] strings)
    {
        mContext = context;
        trailersUri = strings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.trailerName.setText("Trailer " + Integer.toString(position + 1));
    }

    @Override
    public int getItemCount() {
        return trailersUri.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener
    {
        public ImageView playButton;
        public TextView trailerName;

        public ViewHolder(View itemView)
        {
            super(itemView);
            playButton = (ImageView) itemView.findViewById(R.id.play_button);
            trailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view)
        {
            int itemPosition = getAdapterPosition();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(trailersUri[itemPosition]));
            mContext.startActivity(intent);
        }
    }
}
