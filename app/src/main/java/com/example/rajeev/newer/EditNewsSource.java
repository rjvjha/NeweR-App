package com.example.rajeev.newer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EditNewsSource extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_news_source);
        ListView listView = findViewById(R.id.edit_news_sources_list_view);
        List<NewsSource> newsSourceList = new ArrayList<>();
        for(int i = 0;i<10;i++){
            newsSourceList.add(new NewsSource("abc-news",
                    "ABC News",
                    "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
                    "http://abcnews.go.com"));
        }
        NewsSourceAdapter newsSourceAdapter = new NewsSourceAdapter(this, newsSourceList);
        listView.setAdapter(newsSourceAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.wtf("EditNewsSource.class","SelectedNewsSources :"+ getSelectedNewsSouceId());
    }

    // private method to get selectednewsSourceIds
    private String getSelectedNewsSouceId(){
        return NewsSource.getSelectedSourceIds().toString();
    }

}
