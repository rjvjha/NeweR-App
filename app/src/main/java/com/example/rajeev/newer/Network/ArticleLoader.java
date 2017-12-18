package com.example.rajeev.newer.Network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.rajeev.newer.Article;

import java.util.List;

/**
 * /**
 * ArticleLoader does background work of network call and is created by LoaderManager Callback method.
 */


public class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private static final String LOG_TAG = ArticleLoader.class.getName();
    String [] urls;

    public ArticleLoader(Context context,String... urls){
        super(context);
        this.urls = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {
        if(urls.length < 1 || urls[0]==null){
            return null;
        }
        return QueryUtils.fetchArticlesFromNetwork(urls[0]);
    }
}
