package com.example.rajeev.newer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajeev.newer.Network.ArticleLoader;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = SearchActivity.class.getName();
    private int pageNo ;
    private String BASE_URL = "https://newsapi.org/v2/everything?";
    private final int LOADER_ID = 42;
    private EditText searchEditText;
    private TextView emptyTextView;
    private ProgressBar progressBar;
    private TextView searchingFeedback;
    private ArticleSearchAdapter adapter;
    private ImageView emptyImage;
    private Spinner sortBySpinner;
    private String sortBySelectedOption;
    private static boolean isFirstSearch ;

    // SortBy Possible Values
    private static final String SORT_BY_RELEVANCY = "relevancy";
    private static final String SORT_BY_POPULARITY = "popularity";
    private static final String SORT_BY_PUBLISHED_AT = "publishedAt";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_search);
        setContentView(R.layout.activity_search_actvity);
        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
        ImageView searchButton = findViewById(R.id.search_button);
        emptyImage = findViewById(R.id.empty_list_imageView);
        progressBar = findViewById(R.id.progress_indicator_search);
        searchingFeedback = findViewById(R.id.searching_feedback_text_search);

        FrameLayout navFooterView = (FrameLayout) getLayoutInflater().
                inflate(R.layout.footer_buttons_view,null);
        FrameLayout sortByHeaderView = (FrameLayout) getLayoutInflater().
                inflate(R.layout.header_sortby_setting,null);
        Button sortByApplyButton = sortByHeaderView.findViewById(R.id.apply_button);

        final TextView displayPageNo = navFooterView.findViewById(R.id.page_no_textView);
        sortBySpinner = sortByHeaderView.findViewById(R.id.spinner_sortBy);

        ListView listView = findViewById(R.id.articles_search_list_view);
        emptyTextView = findViewById(R.id.empty_list_textView);
        View emptyView = findViewById(R.id.empty_state_list);
        listView.setEmptyView(emptyView);
        listView.addFooterView(navFooterView);
        listView.addHeaderView(sortByHeaderView);
        List<Article> articlesList = new ArrayList<>();

        adapter = new ArticleSearchAdapter(this, articlesList);
        listView.setAdapter(adapter);

        //sortBySelectedOption = getString(R.string.sortBy_relevancy);

        final Boolean isConnected = checkInternetConnectivity();

        if (!isConnected) {
            // Handle no internet connectivity
            emptyTextView.setText(R.string.no_internet_connectivity);
        }
        setupSpinner();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the behaviour of search button click
                String searchQuery = searchEditText.getText().toString().trim();
                boolean isConnected = checkInternetConnectivity();
                if (searchQuery != null && !searchQuery.isEmpty() && isConnected) {
                    hideSoftKeyboard(SearchActivity.this);
                    adapter.clear();
                    emptyTextView.setVisibility(View.GONE);
                    emptyImage.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    searchingFeedback.setVisibility(View.VISIBLE);
                    pageNo = 1;
                    displayPageNo.setText("page "+ formatPageNo());
                    isFirstSearch = true;
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, SearchActivity.this);
                } else if (!isConnected) {
                    Toast.makeText(getBaseContext(), "Please check your internet connection.",
                            Toast.LENGTH_SHORT).show();
                } else if (searchQuery.isEmpty()) {
                    {
                        // Handle empty search query
                        Toast.makeText(getBaseContext(), "I can't find anonymous articles.",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        // Implementing Navigation next Page Button onClick
        navFooterView.findViewById(R.id.next_page_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo++;
                triggerLoader();
                displayPageNo.setText("page "+ formatPageNo());

            }
        });

        // Implementing Navigation previous Page Button
        navFooterView.findViewById(R.id.prev_page_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNo==1){
                    Toast.makeText(getBaseContext(), "I can't go back further!", Toast.LENGTH_SHORT).show();
                }else{
                    pageNo--;
                    triggerLoader();
                    displayPageNo.setText("page " + formatPageNo());
                }

            }
        });

        // Implementing sortBy Apply button
        sortByApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternetConnectivity()){
                    triggerLoader();
                }

            }
        });

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the available sortBy options.
     */
    private void setupSpinner(){
        final ArrayAdapter sortBySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_sortBy_options,android.R.layout.simple_spinner_item);
        sortBySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        sortBySpinner.setAdapter(sortBySpinnerAdapter);

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)){
                    if(selection.equals(getString(R.string.sortBy_relevancy))){
                        sortBySelectedOption = SORT_BY_RELEVANCY;

                    }else if(selection.equals(getString(R.string.sortBy_popularity))){
                        sortBySelectedOption = SORT_BY_POPULARITY;
                    } else {
                        sortBySelectedOption = SORT_BY_PUBLISHED_AT;

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               sortBySelectedOption = SORT_BY_RELEVANCY;
            }
        });
    }
    // private helper method to trigger loader
    private void triggerLoader(){
        getSupportLoaderManager().restartLoader(LOADER_ID, null, SearchActivity.this);
        adapter.notifyDataSetChanged();
        adapter.clear();
        progressBar.setVisibility(View.VISIBLE);
    }

    // private method to clear image cache from memory
    private void clearImageCache(){
        GlideApp.get(this).clearMemory();
    }

    /**
     * Method to generate dynamic urls based on search text
     *
     * @return a url string
     */
    private String getSearchQueryUrl() {
        String query = searchEditText.getText().toString().trim();
        //query = query.replace(" ", "+");
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", query);
        uriBuilder.appendQueryParameter("language", "en");
        uriBuilder.appendQueryParameter("page", formatPageNo());
        uriBuilder.appendQueryParameter("sortBy",sortBySelectedOption);
        uriBuilder.appendQueryParameter("apiKey", "e591d4b34f2e435ba3d8a1f4d4f0d185");
        return uriBuilder.toString();
    }

    // returns true if connectivity is available.
    private Boolean checkInternetConnectivity() {
        // Code to check the network connectivity status
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Helper method to hide soft keyboard when search button is clicked.
     *
     * @param activity takes current activity context
     */
    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    // Helper method to convert pageNo variable to String
    private String formatPageNo(){
       return Integer.toString(pageNo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        String queryUrl = getSearchQueryUrl();
        Log.v(LOG_TAG, "url: " + queryUrl);
        return new ArticleLoader(SearchActivity.this, queryUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        progressBar.setVisibility(View.GONE);
        searchingFeedback.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
        emptyTextView.setText(R.string.no_articles_found);

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }
}
