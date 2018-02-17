package com.example.rajeev.newer.activities;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.adapters.NewsSourceAdapter;
import com.example.rajeev.newer.custom_classes.NewsSource;
import com.example.rajeev.newer.loaders.NewsSourceLoader;

import java.util.ArrayList;
import java.util.List;

public class EditNewsSourceActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<NewsSource>>{

    private static final String LOG_TAG = EditNewsSourceActivity.class.getName();
    private final String SAMPLE_URL = "https://newsapi.org/v2/sources?country=us&apiKey=e591d4b34f2e435ba3d8a1f4d4f0d185";
    private final int LOADER_ID = 12;
    private NewsSourceAdapter adapter;
    private View emptyView;
    private ProgressBar progressBar;
    private TextView emptyTextView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news_source);
        ListView listView = findViewById(R.id.edit_news_sources_list_view);
        emptyView = findViewById(R.id.empty_list_view_editNewsSource);
        emptyTextView1 = findViewById(R.id.empty_list_textView1_editNewsSource);
        progressBar = findViewById(R.id.progress_indicator_editNewsSource);
        listView.setEmptyView(emptyView);
        adapter = new NewsSourceAdapter(this, new ArrayList<NewsSource>());
        listView.setAdapter(adapter);
        if(checkInternetConnectivity()){
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf("EditNewsSourceActivity.class","SelectedNewsSources :"+ getSelectedNewsSouceId());
    }

    // private method to setup spinners
    private void setupSpinners(){

    }

    // private method to get selectednewsSourceIds
    private String getSelectedNewsSouceId(){
        return NewsSource.getSelectedSourceIds().toString();
    }

    // private helper method to check the internet connectivity
    private boolean checkInternetConnectivity(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<NewsSource>> onCreateLoader(int id, Bundle args) {
        return new NewsSourceLoader(this, SAMPLE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsSource>> loader, List<NewsSource> data) {
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
        emptyTextView1.setText(R.string.no_internet_suggestion);

    }

    @Override
    public void onLoaderReset(Loader<List<NewsSource>> loader) {
        adapter.clear();
    }
}
