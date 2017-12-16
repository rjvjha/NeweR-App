package com.example.rajeev.newer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjvjha on 16/12/17.
 */

class ArticleAdapter extends ArrayAdapter<Article> {


    public ArticleAdapter(Context context, List<Article> articles){
        super(context,0,articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // current article
        Article currentArticle = getItem(position);
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        TextView sourceName = (TextView) itemView.findViewById(R.id.source_name_textView);
        TextView publishDateTime = (TextView) itemView.findViewById(R.id.article_published_datetime);
        TextView title = (TextView) itemView.findViewById(R.id.article_title_textView);
        TextView authorName = (TextView) itemView.findViewById(R.id.article_author_textView);
        TextView description = (TextView) itemView.findViewById(R.id.article_description_textView);

        sourceName.setText(currentArticle.getSourceName());
        publishDateTime.setText(currentArticle.getPublishedAt());
        title.setText(currentArticle.getTitle());
        authorName.setText(currentArticle.getAuthor());
        description.setText(currentArticle.getDescription());


        return itemView;
    }
}
