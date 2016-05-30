package com.example.xyzreader.ui.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.ForegroundImageView;

/**
 * Created by Cristhian on 5/30/2016.
 */
public class ReaderViewHolder extends RecyclerView.ViewHolder {
    public ForegroundImageView thumbnailView;
    public TextView titleView;
    public TextView subtitleView;

    public ReaderViewHolder(View view) {
        super(view);
        thumbnailView = (ForegroundImageView) view.findViewById(R.id.thumbnail);
        titleView = (TextView) view.findViewById(R.id.article_title);
        subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
    }
}
