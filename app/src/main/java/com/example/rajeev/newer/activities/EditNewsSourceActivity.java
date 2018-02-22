package com.example.rajeev.newer.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.adapters.NewsSourceAdapter;
import com.example.rajeev.newer.custom_classes.NewsSource;
import com.example.rajeev.newer.loaders.NewsSourceLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EditNewsSourceActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<NewsSource>>{

    private static final String LOG_TAG = EditNewsSourceActivity.class.getName();
    private final String BASE_URL = "https://newsapi.org/v2/sources?";
    private final int LOADER_ID = 12;
    private NewsSourceAdapter adapter;
    private View emptyView;
    private ProgressBar progressBar;
    private TextView emptyTextView1;
    private Spinner spinnerCountry;
    private Spinner spinnerLanguage;
    private Spinner spinnerCategory;
    private String sSelectedCategory;
    private String sSelectedLanguage;
    private String sSelectedCountry;
    private String sharedPrefFile = "com.example.rajeev.newer";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news_source);
        // creating preference file
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        ListView listView = findViewById(R.id.edit_news_sources_list_view);
        emptyView = findViewById(R.id.empty_list_view_editNewsSource);
        emptyTextView1 = findViewById(R.id.empty_list_textView1_editNewsSource);
        progressBar = findViewById(R.id.progress_indicator_editNewsSource);
        Button applyButton = findViewById(R.id.news_source_apply_operation);
        listView.setEmptyView(emptyView);
        spinnerLanguage = findViewById(R.id.language_selector);
        spinnerCountry = findViewById(R.id.country_selector);
        spinnerCategory = findViewById(R.id.category_selector);
        adapter = new NewsSourceAdapter(this, new ArrayList<NewsSource>());
        listView.setAdapter(adapter);
        setupSpinners();
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerNetworkOperation();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf("EditNewsSourceActivity.class","SelectedNewsSources :"+ getPrefNewsSourcesSetToList());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(NewsSource.isNewsSourcesIdChanged){
            Log.wtf(LOG_TAG, "Edit preferences");
            SharedPreferences.Editor preferenceEditor = mPreferences.edit();
            preferenceEditor.putStringSet("sources", getSelectedNewsSourcesListToSet());
            preferenceEditor.apply();
            NewsSource.isNewsSourcesIdChanged = false;
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void triggerNetworkOperation(){
        if(checkInternetConnectivity()){
            Log.v(LOG_TAG, "Making Query to url : " + getQueryUrl());
            progressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    // private method to setup spinners
    private void setupSpinners(){

        // Setting spinner adapters
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.settings_countries_labels,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.news_sources_category_labels,
                android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.news_sources_languages_labels,
                android.R.layout.simple_spinner_item);

        spinnerCountry.setAdapter(countryAdapter);
        spinnerLanguage.setAdapter(languageAdapter);
        spinnerCategory.setAdapter(categoryAdapter);

        // Setting up dropDownViewResource
        countryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Setting up onItemSelectedListeners

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCountry = parent.getItemAtPosition(position).toString();
                if(!TextUtils.isEmpty(selectedCountry)){
                    String [] countryValues = getResources().getStringArray(R.array.settings_countries_values);
                    String [] countryLabels = getResources().getStringArray(R.array.settings_countries_labels);
                    List<String> countryList = Arrays.asList(countryLabels);
                    int index = countryList.indexOf(selectedCountry);
                    sSelectedCountry = countryValues[index];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sSelectedCountry = getString(R.string.country_india_value);
            }
        });

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                if(!TextUtils.isEmpty(selectedLanguage)){
                    String [] languageValues = getResources().getStringArray(R.array.news_sources_languages_values);
                    String [] languageLabels = getResources().getStringArray(R.array.news_sources_languages_labels);
                    List<String> languagesList = Arrays.asList(languageLabels);
                    int index = languagesList.indexOf(selectedLanguage);
                    sSelectedLanguage = languageValues[index];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sSelectedLanguage = getString(R.string.language_english_value);

            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                if(!TextUtils.isEmpty(selectedCategory)){
                    String [] categoryValues = getResources().getStringArray(R.array.news_sources_category_values);
                    String [] categoryLabels = getResources().getStringArray(R.array.news_sources_category_labels);
                    List<String> categoriesList = Arrays.asList(categoryLabels);
                    int index = categoriesList.indexOf(selectedCategory);
                    sSelectedCategory = categoryValues[index];
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sSelectedCategory = getString(R.string.category_general_value);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_news_source, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.reset_news_sources:
                showResetDialogConfirmation();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Private method to create  and show reset dialog

    private void showResetDialogConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_reset);
        builder.setMessage(R.string.edit_news_source_reset_dialog_confiramation);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset
                resetNewsSourcesPreferences();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cancel
                if(dialog!=null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // private method to get queryUrl for news Sources
    private String getQueryUrl(){
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("language", sSelectedLanguage);
        uriBuilder.appendQueryParameter("country", sSelectedCountry);
        uriBuilder.appendQueryParameter("category", sSelectedCategory);
        uriBuilder.appendQueryParameter("apiKey", "e591d4b34f2e435ba3d8a1f4d4f0d185");
        return uriBuilder.toString();
    }


    // private method to get selectednewsSourceIds
    private Set<String> getSelectedNewsSourcesListToSet(){
        List<String> list = NewsSource.getSelectedSourceIds();
        Set<String> newsSourceSet = new ArraySet<>();
        newsSourceSet.addAll(list);
        return newsSourceSet;
    }

    private List<String> getPrefNewsSourcesSetToList(){
        Set<String> newsSources = mPreferences.getStringSet("sources", new ArraySet<String>());
        List<String> newsSourceList = new ArrayList<>(20);
        newsSourceList.addAll(newsSources);
        return newsSourceList;
    }

    private void resetNewsSourcesPreferences(){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }
    // private helper method to check the internet connectivity
    private boolean checkInternetConnectivity(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

    @Override
    public Loader<List<NewsSource>> onCreateLoader(int id, Bundle args) {
        return new NewsSourceLoader(this, getQueryUrl());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsSource>> loader, List<NewsSource> data) {
        progressBar.setVisibility(View.GONE);
        adapter.clear();
        if(data != null && !data.isEmpty()){
            adapter.addAll(data);
        }
        emptyTextView1.setText(R.string.edit_news_source_no_news_sources_found);

    }

    @Override
    public void onLoaderReset(Loader<List<NewsSource>> loader) {
        adapter.clear();
    }
}
