package com.example.xyzreader.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.ui.ForegroundImageView;
import com.example.xyzreader.ui.ImageLoaderHelper;
import com.example.xyzreader.ui.viewHolders.ReaderViewHolder;
import com.squareup.picasso.Picasso;

/**
 * Created by Cristhian on 5/30/2016.
 */
public class ReaderAdapter extends RecyclerView.Adapter<ReaderViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public ReaderAdapter(Cursor cursor, Context context) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @Override
    public ReaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(ReaderViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        String subtitle = DateUtils.getRelativeTimeSpanString(
                mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL).toString()
                + " by "
                + mCursor.getString(ArticleLoader.Query.AUTHOR);
        holder.subtitleView.setText(subtitle);
        String imageUrl = mCursor.getString(ArticleLoader.Query.THUMB_URL);
//        holder.thumbnailView.setImageUrl(
//                mCursor.getString(ArticleLoader.Query.THUMB_URL),
//                ImageLoaderHelper.getInstance(mContext).getImageLoader());
//        holder.thumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
        Picasso.with(mContext).load(imageUrl).placeholder(R.color.placeholder).into(holder.thumbnailView);

//        ItemsContract.Items.buildItemUri(getItemId(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


//    public Photo getItem(int position) {
//        return photos.get(position);
//    }
}
