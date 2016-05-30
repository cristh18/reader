package com.example.xyzreader.events;

import android.view.View;

/**
 * Created by Cristhian on 5/30/2016.
 */
public class ArticleSelectEvent {

    private static final String TAG = ArticleSelectEvent.class.getSimpleName();

    private View view;
    private int position;

    public ArticleSelectEvent(View view, int position) {
        this.view = view;
        this.position = position;
    }

    public View getView() {
        return view;
    }

    public int getPosition() {
        return position;
    }
}
