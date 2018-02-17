package com.example.rajeev.newer.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rajeev.newer.custom_classes.Article;
import com.example.rajeev.newer.network.QueryUtils;

import java.util.List;

/**
 * /**
 * ArticleLoader does background work of network call and is created by LoaderManager Callback method.
 */


public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private static final String LOG_TAG = ArticleLoader.class.getName();
    private String [] urls;
    private List<Article> mData;


    public ArticleLoader(Context context,String... urls){
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
    public List<Article> loadInBackground() {
        if(urls.length < 1 || urls[0]==null){
            return null;
        }
        return QueryUtils.fetchArticlesFromNetwork(urls[0]);
    }

    @Override
    public void deliverResult(List<Article> data) {
        // We’ll save the data for later retrieval
        mData = data;
        super.deliverResult(data);
    }
}
