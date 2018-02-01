package com.example.rajeev.newer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String SAMPLE_NEWS_URL ="https://newsapi.org/v2/top-headlines?sources=google-news-in,the-times-of-india,the-hindu&apiKey=e591d4b34f2e435ba3d8a1f4d4f0d185";
    private static final String LOG_TAG = MainActivity.class.getName();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        fab = findViewById(R.id.fab_saved_articles);
        // code to handle fab button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SavedArticlesCatalog.class);
                startActivity(intent);

            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_content_container, new TopStoriesFragment());
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                return true;

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Handle the nav items  clicks here
        switch (id){

            case R.id.nav_top_stories:
                setTitle(R.string.title_activity_main);
                openFragment(new TopStoriesFragment());
                break;

            case R.id.nav_country:
                setTitle(getCountryLabel());
                openFragment(new IndiaCategoryFragment());
                break;

            case R.id.nav_world:
                setTitle(R.string.menu_world);
                openFragment(new WorldFragment());
                break;

            case R.id.nav_business:
                setTitle(R.string.menu_Business);
                openFragment(new BusinessFragment());
                break;

            case R.id.nav_technology:
                setTitle(R.string.menu_technology);
                openFragment(new TechnologyFragment());
                break;

            case R.id.nav_entertainment:
                setTitle(R.string.menu_entertainment);
                openFragment(new EntertainmentFragment());
                break;

            case R.id.nav_sport:
                setTitle(R.string.menu_sport);
                openFragment(new SportFragment());
                break;

            case R.id.nav_science:
                setTitle(R.string.menu_science);
                openFragment(new ScienceFragment());
                break;

            case R.id.nav_health:
                setTitle(R.string.menu_health);
                openFragment(new HealthFragment());
                break;

            case R.id.nav_change_source:
                Intent editNewsIntent = new Intent(this,EditNewsSource.class);
                drawer.closeDrawer(GravityCompat.START);
                startActivity(editNewsIntent);
                return true;

        }

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
        NavigationView navigationView =  findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem navCountry = menu.findItem(R.id.nav_country);
        navCountry.setTitle(getCountryLabel());
    }
    // Helper method to open new Fragment
    private void openFragment(final Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_content_container,fragment);
        ft.commit();
    }

    FloatingActionButton getFloatingActionButton(){
        return fab;
    }

    // Private helper method to return the country selected by the user
    private String getCountryLabel(){
        String [] countryLabels = getResources().getStringArray(R.array.settings_countries_labels);
        String [] countryValues = getResources().getStringArray(R.array.settings_countries_values);
        List<String> countryValuesList = Arrays.asList(countryValues);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedCountry = sharedPreferences.getString(
                getString(R.string.pref_country_key),
                getString(R.string.pref_country_default));
        int index = countryValuesList.indexOf(selectedCountry);
        return countryLabels[index];
    }


}
