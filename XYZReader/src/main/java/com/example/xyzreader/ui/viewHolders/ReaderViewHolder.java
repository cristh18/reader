package com.example.xyzreader.ui.viewHolders;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;

/**
 * Created by Cristhian on 5/30/2016.
 */
public class ReaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView thumbnailView;
    public TextView titleView;
    public TextView subtitleView;

    private OnClickArticle onClickArticle;

    public ReaderViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        initViews(view);
    }

    private void initViews(View view) {
        Typeface sourceSansPro = Typeface.createFromAsset(view.getContext().getAssets(), "SourceSansPro-Regular.ttf");
        thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        titleView = (TextView) view.findViewById(R.id.article_title);
        titleView.setTypeface(sourceSansPro);
        subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        subtitleView.setTypeface(sourceSansPro);
    }

    @Override
    public void onClick(View view) {
        onClickArticle.onSelected(view, getAdapterPosition());
    }

    public void setOnClickArticle(OnClickArticle onClickArticle) {
        this.onClickArticle = onClickArticle;
    }

    public interface OnClickArticle {
        void onSelected(View v, int position);
    }
}
