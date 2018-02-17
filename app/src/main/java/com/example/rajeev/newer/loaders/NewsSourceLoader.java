package com.example.rajeev.newer.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rajeev.newer.network.QueryUtils;
import com.example.rajeev.newer.custom_classes.NewsSource;

import java.util.List;

/**
 * Created by rjvjha on 14/2/18.
 */

public class NewsSourceLoader extends AsyncTaskLoader<List<NewsSource>>{
    private static final String LOG_TAG = NewsSourceLoader.class.getName();
    private String [] urls;
    private List<NewsSource> mData;

    public NewsSourceLoader(Context context,String ...urls){
        super(context);
        this.urls = urls;
    }
    @Override
    protected void onStartLoading() {
        if(mData!=null){
            // use cached data
            deliverResult(mData);
        }else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public List<NewsSource> loadInBackground() {
        if(urls.length < 1 || urls[0]==null){
            return null;
        }
        return QueryUtils.fetchNewsSourcesFromNetwork(urls[0]);
    }

    @Override
    public void deliverResult(List<NewsSource> data) {
        // We’ll save the data for later retrieval
        mData = data;
        super.deliverResult(data);
    }
}
