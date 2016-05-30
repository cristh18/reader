package com.example.xyzreader.provider;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Cristhian on 5/30/2016.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();
    private static final Handler mainThread = new Handler(Looper.getMainLooper());

    private BusProvider() {
    }

    public static Bus getInstance() {
        return BUS;
    }

    public static void postOnMain(final Object event) {
        mainThread.post(new Runnable() {
            @Override
            public void run() {
                getInstance().post(event);
            }
        });
    }
}