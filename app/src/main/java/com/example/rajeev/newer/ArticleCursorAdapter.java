package com.example.rajeev.newer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by rjvjha on 3/1/18.
 */

public class ArticleCursorAdapter extends ArrayAdapter<Article> {

    ArticleCursorAdapter(Context context, List<Article> list){
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        Article currentArticle = getItem(position);
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.article_item_compact, parent, false);
        }
        //ImageView articleImage = itemView.findViewById(R.id.article_image);
        TextView title = itemView.findViewById(R.id.article_title);
        TextView description = itemView.findViewById(R.id.article_description);
        TextView source = itemView.findViewById(R.id.article_source);

        title.setText(currentArticle.getTitle());
        description.setText(currentArticle.getDescription());
        source.setText(currentArticle.getSourceName());

        return  itemView;
    }
}
