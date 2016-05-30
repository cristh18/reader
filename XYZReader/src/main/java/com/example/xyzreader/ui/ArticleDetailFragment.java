package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private static final String TAG = "ArticleDetailFragment";
    public static final String ARG_ITEM_ID = "item_id";
    public static final String TEXT_PLAIN = "text/plain";
    private Cursor mCursor;
    private View mRootView;
    private View fabShareFeed;
    private ImageView imageViewPhotoFeed;
    private TextView textViewTitleArticle;
    private TextView textViewLineArticle;
    private TextView bodyView;
    private CollapsingToolbarLayout collapsingToolbarLayoutDetailFeed;
    private Toolbar toolbarDetailFeed;
    private AppBarLayout appBarLayoutDetailFeed;
    private long mItemId;
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId) {
        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItemId = getArguments().getLong(ARG_ITEM_ID);
        }
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.container_article_description);
            slide.setInterpolator(AnimationUtils.loadInterpolator(getActivity(), android.R.interpolator
                    .linear_out_slow_in));
            slide.setDuration(300);
            getActivity().getWindow().setEnterTransition(slide);
        }
    }

    public ArticleDetailActivity getActivityCast() {
        return (ArticleDetailActivity) getActivity();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        initViews();
        bindViews();
        setListeners();
        return mRootView;
    }

    private void initViews() {
        imageViewPhotoFeed = (ImageView) mRootView.findViewById(R.id.imageView_photo_feed);
        textViewTitleArticle = (TextView) mRootView.findViewById(R.id.textView_title_article);
        textViewLineArticle = (TextView) mRootView.findViewById(R.id.textView_line_article);
        bodyView = (TextView) mRootView.findViewById(R.id.article_body);
        collapsingToolbarLayoutDetailFeed = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsingToolbarLayout_detail_feed);
        toolbarDetailFeed = (Toolbar) mRootView.findViewById(R.id.toolbar_detail_feed);
        appBarLayoutDetailFeed = (AppBarLayout) mRootView.findViewById(R.id.appBarLayout_detail_feed);
        fabShareFeed = mRootView.findViewById(R.id.fab_share_feed);
    }

    private void bindViews() {
        if (mCursor != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);
            collapsingToolbarLayoutDetailFeed.setTitle(mCursor.getString(ArticleLoader.Query.TITLE));
            appBarLayoutDetailFeed.addOnOffsetChangedListener(this);
            textViewLineArticle.setMovementMethod(new LinkMovementMethod());
            bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));
            textViewTitleArticle.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            textViewLineArticle.setText(Html.fromHtml(
                    DateUtils.getRelativeTimeSpanString(
                            mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                            System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                            DateUtils.FORMAT_ABBREV_ALL).toString()
                            .concat(" by <font color='" + ContextCompat.getColor(getActivity(), R.color.blue01) + "'>")
                            .concat(mCursor.getString(ArticleLoader.Query.AUTHOR))
                            .concat("</font>")));
            bodyView.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY)));
            Picasso.with(getActivity()).load(mCursor.getString(ArticleLoader.Query.PHOTO_URL)).placeholder(R.color.placeholder).into(imageViewPhotoFeed);
        } else {
            mRootView.setVisibility(View.GONE);
            textViewTitleArticle.setText(getString(R.string.not_apply));
            textViewLineArticle.setText(getString(R.string.not_apply));
            collapsingToolbarLayoutDetailFeed.setTitle(getString(R.string.not_apply));
            bodyView.setText(getString(R.string.not_apply));
        }
    }

    private void setListeners() {

        fabShareFeed.setOnClickListener(this);
        toolbarDetailFeed.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHome();
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(i)) * 100
                / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true;

                ViewCompat.animate(fabShareFeed).scaleY(0).scaleX(0).start();
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(fabShareFeed).scaleY(1).scaleX(1).start();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (!isAdded()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(TAG, "Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        bindViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
        bindViews();
    }

    private void shareArticle() {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType(TEXT_PLAIN)
                .setText(getString(R.string.sample_text))
                .getIntent(), getString(R.string.action_share)));
    }

    private void backHome() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_share_feed:
                shareArticle();
                break;
        }
    }

}
