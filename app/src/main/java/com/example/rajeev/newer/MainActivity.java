package com.example.rajeev.newer;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajeev.newer.Network.ArticleLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String SAMPLE_NEWS_URL ="https://newsapi.org/v2/top-headlines?sources=google-news-in,the-times-of-india,the-hindu&apiKey=e591d4b34f2e435ba3d8a1f4d4f0d185";
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int  LOADER_ID = 0;
    private ArticleAdapter adapter;
    private ProgressBar progressIndicator;
    private Chronometer chronometer;
    private View emptyView;
    private ImageView emptyListImageView;
    private TextView emptyListTextView1;
    private TextView emptyListTextViewSuggestionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        emptyView = findViewById(R.id.empty_list_view);
        progressIndicator = (ProgressBar)findViewById(R.id.progress_indicator);
        chronometer = (Chronometer) findViewById(R.id.timer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        emptyListImageView = findViewById(R.id.empty_list_imageView);
        emptyListTextView1 = findViewById(R.id.empty_list_textView1);
        emptyListTextViewSuggestionText = findViewById(R.id.empty_list_suggestion);

        //  Main Content code starts from here

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(emptyView);

        // Code for hiding the app bar when scrolling list view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }

        adapter = new ArticleAdapter(this, new ArrayList<Article>());
        listView.setAdapter(adapter);

        // Check for internet Connectivity
        if(checkInternetConnectivity()){
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            chronometer.start();
        } else if(!checkInternetConnectivity() && adapter.isEmpty()){
            progressIndicator.setVisibility(View.GONE);
            emptyListImageView.setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
            emptyListTextView1.setText(R.string.no_internet_connectivity);
            emptyListTextViewSuggestionText.setText(R.string.offline_mode_suggestion);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){

            case R.id.action_search:
                Intent searchIntent = new Intent(this, SearchActvity.class);
                startActivity(searchIntent);
                return true;

            case R.id.action_refresh:
                // DO nothing for now;
                if(checkInternetConnectivity()) {
                    emptyView.setVisibility(View.GONE);
                    progressIndicator.setVisibility(View.VISIBLE);
                    getSupportLoaderManager().restartLoader(LOADER_ID, null ,this);
                }
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_about:
                // Do nothing for now;
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // Handle the nav items here
        switch (id){

            case R.id.nav_top_stories:
                setTitle(R.string.title_activity_main);
                break;

            case R.id.nav_india:
                setTitle(R.string.menu_india);
                break;

            case R.id.nav_world:
                setTitle(R.string.menu_world);
                break;

            case R.id.nav_business:
                setTitle(R.string.menu_Business);
                break;

            case R.id.nav_technology:
                setTitle(R.string.menu_technology);
                break;

            case R.id.nav_entertainment:
                setTitle(R.string.menu_entertainment);
                break;

            case R.id.nav_sport:
                setTitle(R.string.menu_sport);
                break;

            case R.id.nav_science:
                setTitle(R.string.menu_science);
                break;

            case R.id.nav_health:
                setTitle(R.string.menu_health);
                break;

            case R.id.nav_change_source:
                // Do nothing for now
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // private helper method to check the internet connectivity
    private boolean checkInternetConnectivity(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    // Loader Callback methods are implemented here


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(this,SAMPLE_NEWS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        progressIndicator.setVisibility(View.GONE);
        adapter.clear();
        if(data != null && !data.isEmpty()){
            chronometer.stop();
            Toast.makeText(this,"News updated",Toast.LENGTH_SHORT).show();
            Log.v(LOG_TAG, "Total time taken: "+ chronometer.getText());
            adapter.addAll(data);
        }
        emptyListTextView1.setText(R.string.no_aricles_found);
        emptyListTextViewSuggestionText.setText(R.string.no_articles_suggestion);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
       adapter.clear();

    }
}
