package com.example.rajeev.newer.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.ArraySet;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.activities.EditNewsSourceActivity;
import com.example.rajeev.newer.adapters.ArticleAdapter;
import com.example.rajeev.newer.custom_classes.Article;
import com.example.rajeev.newer.loaders.ArticleLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomCategoryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = CustomCategoryFragment.class.getName();
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines?";
    private static List<Article> sData;
    private final int LOADER_ID = 1;
    private View emptyView;
    private ArticleAdapter adapter;
    private ImageView emptyListImageView;
    private TextView emptyListTextView1;
    private TextView emptyListTextViewSuggestionText;
    private ProgressBar progressIndicator;
    private TextView loadingFeedback;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Button emptyListButton;


    public CustomCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(getPrefNewsSourcesSetToList().size()==0){
            // empty the already available data
            sData = null;
            // return early
            return;
        }

        if(sData == null) {
            if (checkInternetConnectivity()) {
                getLoaderManager().initLoader(LOADER_ID, null, this);
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_articles, container, false);
        Context context = getContext();

        // adding references to views
        emptyView = rootView.findViewById(R.id.empty_list_view);
        progressIndicator = rootView.findViewById(R.id.progress_indicator);
        loadingFeedback = rootView.findViewById(R.id.loading_feedback_text);

        // handling empty list
        emptyListImageView = rootView.findViewById(R.id.empty_list_imageView);
        emptyListTextView1 = rootView.findViewById(R.id.empty_list_textView1);
        emptyListTextViewSuggestionText = rootView.findViewById((R.id.empty_list_suggestion));
        emptyListButton = rootView.findViewById(R.id.empty_list_button);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        ListView listView = rootView.findViewById(R.id.list_view);

        // Setting up adapter
        adapter = new ArticleAdapter(context,new ArrayList<Article>());
        listView.setEmptyView(emptyView);

        // if data is already available then add it to adapter
        if(sData!=null) {
            adapter.addAll(sData);
            loadingFeedback.setVisibility(View.GONE);
            progressIndicator.setVisibility(View.GONE);
        }
        listView.setAdapter(adapter);


        // Code for hiding the app bar when scrolling list view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
            mSwipeRefreshLayout.setNestedScrollingEnabled(true);
            emptyView.setNestedScrollingEnabled(true);
        }


        // Check for internet Connectivity
        if(!checkInternetConnectivity() && adapter.isEmpty()){
            progressIndicator.setVisibility(View.GONE);
            loadingFeedback.setVisibility(View.GONE);
            emptyListImageView.setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
            emptyListTextView1.setText(R.string.no_internet_connectivity);
            emptyListTextViewSuggestionText.setText(R.string.offline_mode_suggestion);

        }

        if(getPrefNewsSourcesSetToList().size()==0){
            handleEmptyNewsSources();
        }

        // Implementing Swipe-to-refresh behaviour
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articlesRefreshOperation();
            }
        });
        return rootView;
    }

    // private method to handle Empty View when no news Sources are selected
    private void handleEmptyNewsSources(){
        progressIndicator.setVisibility(View.GONE);
        loadingFeedback.setVisibility(View.GONE);
        emptyListButton.setVisibility(View.VISIBLE);
        emptyListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editNewsIntent = new Intent(getContext(), EditNewsSourceActivity.class);
                startActivity(editNewsIntent);
            }
        });
        emptyListImageView.setImageResource(R.drawable.ic_mood_black_96dp);
        emptyListTextView1.setText(R.string.custom_categoty_fragment_welcome_greet);
        emptyListTextViewSuggestionText.setText(R.string.custom_category_fragment_no_news_sources_suggestion);

    }

    private void articlesRefreshOperation(){

        if(getPrefNewsSourcesSetToList().size()==0){
            mSwipeRefreshLayout.setRefreshing(false);
            handleEmptyNewsSources();
            return;

        }
        if(checkInternetConnectivity()){
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }else{
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), R.string.no_internet_connectivity, Toast.LENGTH_SHORT).show();
        }
        // adapter.notifyDataSetChanged();
    }

    // private helper method to get preferred news sources list
    private List<String> getPrefNewsSourcesSetToList(){
        Set<String> newsSources = getActivity().getSharedPreferences("com.example.rajeev.newer",
                MODE_PRIVATE).getStringSet("sources", new ArraySet<String>());
        List<String> newsSourceList = new ArrayList<>(20);
        newsSourceList.addAll(newsSources);
        return newsSourceList;
    }
    // private helper method to format List values to string
    private String formatStringSetToString(List<String> newsSourceList){
        String sources = "" ;
        for(int i = 0;i< newsSourceList.size();i++ ){
            if(i == newsSourceList.size() -1){
             sources += newsSourceList.get(i);
            }else{
                sources += newsSourceList.get(i) +",";
            }
        }
        return sources;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.custom_category_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_refresh:
                mSwipeRefreshLayout.setRefreshing(true);
                articlesRefreshOperation();
                return true;
            case R.id.menu_edit_source:
                Intent editNewsIntent = new Intent(getContext(),EditNewsSourceActivity.class);
                startActivity(editNewsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // private helper method to check the internet connectivity
    private boolean checkInternetConnectivity(){
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

    // private method to get queryUrl
    private String getQueryUrl(){
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        Log.v(LOG_TAG,"sources="+formatStringSetToString(getPrefNewsSourcesSetToList()));
        uriBuilder.appendQueryParameter("sources",formatStringSetToString(getPrefNewsSourcesSetToList()));
        uriBuilder.appendQueryParameter("pageSize","100");
        uriBuilder.appendQueryParameter("apiKey", "e591d4b34f2e435ba3d8a1f4d4f0d185");
        return uriBuilder.toString();
    }



    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(getContext(),getQueryUrl());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        progressIndicator.setVisibility(View.GONE);
        loadingFeedback.setVisibility(View.GONE);
        adapter.clear();
        if(data != null && !data.isEmpty()){
            Toast.makeText(getContext(),"News updated",Toast.LENGTH_SHORT).show();
            sData = data;
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.addAll(data);
        }
        emptyListTextView1.setText(R.string.no_articles_found);
        emptyListTextViewSuggestionText.setText(R.string.no_internet_suggestion);

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();

    }



}
