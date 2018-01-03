package com.example.rajeev.newer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SavedArticlesCatalog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles_catalog);
        ListView listView = findViewById(R.id.saved_articles_list_view);
        List<Article> articles = new ArrayList<>();
        // Dummy Data
        for(int i=0; i<10;i++){
            articles.add(new Article("Google-news", "The Times of India","Soofi Akhtar",
                    "Sonia confident that 'fearless' Rahul will reinvigorate Congress",
                    "skjfksajfkaskjassfas","http://", "alksjfjlajs","27/09/2017"));
        }
        ArticleCursorAdapter adapter = new ArticleCursorAdapter(this,articles);
        listView.setAdapter(adapter);
    }
}
