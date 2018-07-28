package com.example.rajeev.newer.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.ArraySet;

import com.glidebitmappool.GlideBitmapPool;

import java.util.Set;

public class GlideBitmapPoolLibrary extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 10 MB Size
        Set<Bitmap.Config> configs = new ArraySet<>();
        configs.add(Bitmap.Config.ARGB_8888);
        configs.add(Bitmap.Config.RGB_565);
        GlideBitmapPool.initialize(10*1024*1024);
    }

}
