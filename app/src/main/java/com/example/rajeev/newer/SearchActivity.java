package com.example.rajeev.newer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_search);
        setContentView(R.layout.activity_search_actvity);
        ListView listView = findViewById(R.id.articles_search_list_view);
        View emptyView = findViewById(R.id.empty_state_list);
        listView.setEmptyView(emptyView);
    }
}
