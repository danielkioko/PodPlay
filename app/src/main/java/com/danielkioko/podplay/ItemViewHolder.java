package com.danielkioko.podplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public ItemViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setLabel(String title){
        TextView post_title = mView.findViewById(R.id.label);
        post_title.setText(title);
    }

    public void setCover(Context ctx, String imageUrl){
        ImageView post_image = mView.findViewById(R.id.cover);
        Picasso.with(ctx).load(imageUrl).into(post_image);
    }

}
