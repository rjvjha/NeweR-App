package com.example.rajeev.newer;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rajeev.newer.Network.ArticleLoader;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScienceFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Article>>{
    private static final String LOG_TAG = EntertainmentFragment.class.getName();
    private static final String SAMPLE_URL = "https://newsapi.org/v2/top-headlines?category=science-and-nature&language=en&country=&apiKey=e591d4b34f2e435ba3d8a1f4d4f0d185";
    private final int LOADER_ID = 7;
    private View emptyView;
    private ArticleAdapter adapter;
    private ImageView emptyListImageView;
    private TextView emptyListTextView1;
    private TextView emptyListTextViewSuggestionText;
    private ProgressBar progressIndicator;
    private static List<Article> sData;
    private TextView loadingFeedback;


    public ScienceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
        emptyView = rootView.findViewById(R.id.empty_list_view);
        progressIndicator = rootView.findViewById(R.id.progress_indicator);
        loadingFeedback = rootView.findViewById(R.id.loading_feedback_text);
        emptyListImageView = rootView.findViewById(R.id.empty_list_imageView);
        emptyListTextView1 = rootView.findViewById(R.id.empty_list_textView1);
        emptyListTextViewSuggestionText = rootView.findViewById((R.id.empty_list_suggestion));
        ListView listView = rootView.findViewById(R.id.list_view);
        adapter = new ArticleAdapter(context,new ArrayList<Article>());
        listView.setEmptyView(emptyView);
        // if data is already availabe then add it to adapter
        if(sData!=null) {
            adapter.addAll(sData);
            loadingFeedback.setVisibility(View.GONE);
            progressIndicator.setVisibility(View.GONE);
        }
        // Code for hiding the app bar when scrolling list view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }
        listView.setAdapter(adapter);

        // Check for internet Connectivity

        if(!checkInternetConnectivity() && adapter.isEmpty()){
            progressIndicator.setVisibility(View.GONE);
            loadingFeedback.setVisibility(View.GONE);
            emptyListImageView.setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
            emptyListTextView1.setText(R.string.no_internet_connectivity);
            emptyListTextViewSuggestionText.setText(R.string.offline_mode_suggestion);

        }
        return rootView;
    }

    // private helper method to check the internet connectivity
    private boolean checkInternetConnectivity(){
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }


    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return new ArticleLoader(getContext(),SAMPLE_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
        progressIndicator.setVisibility(View.GONE);
        loadingFeedback.setVisibility(View.GONE);
        adapter.clear();
        if(data != null && !data.isEmpty()){
            Toast.makeText(getContext(),"News updated",Toast.LENGTH_SHORT).show();
            sData = data;
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
