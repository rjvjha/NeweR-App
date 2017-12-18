package com.example.rajeev.newer;

import android.content.Context;
import android.graphics.Bitmap;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajeev.newer.Network.ISO8601;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rjvjha on 16/12/17.
 */

class ArticleAdapter extends ArrayAdapter<Article> {
    private TextView mPublishDateTime;


    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // current article
        Article currentArticle = getItem(position);
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView sourceName = (TextView) itemView.findViewById(R.id.source_name_textView);
        mPublishDateTime = (TextView) itemView.findViewById(R.id.article_published_datetime);
        TextView title = (TextView) itemView.findViewById(R.id.article_title_textView);
        TextView authorName = (TextView) itemView.findViewById(R.id.article_author_textView);
        TextView byLabel = (TextView) itemView.findViewById(R.id.by_label_text);
        TextView description = (TextView) itemView.findViewById(R.id.article_description_textView);
        ImageView articleImage = (ImageView) itemView.findViewById(R.id.article_image_imageView);

        mPublishDateTime.setVisibility(View.VISIBLE);
        authorName.setVisibility(View.VISIBLE);
        articleImage.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        byLabel.setVisibility(View.VISIBLE);


        String currentAuthor = currentArticle.getAuthor();
        Bitmap articleThumnail = currentArticle.getArticleImage();
        String publishAt = currentArticle.getPublishedAt();

        // Check for null and empty author values
        if (currentAuthor == "null" || TextUtils.isEmpty(currentAuthor)) {
            byLabel.setVisibility(View.GONE);
            authorName.setVisibility(View.GONE);

        } else {
            authorName.setText(currentAuthor);
        }
        // Check for null Bitmap values
        if (articleThumnail == null) {
            articleImage.setVisibility(View.GONE);
        } else {
            articleImage.setImageBitmap(articleThumnail);
        }

        sourceName.setText(currentArticle.getSourceName());

        mPublishDateTime.setText(formatDateTime(publishAt));

        title.setText(currentArticle.getTitle());

        // Check for empty description
        if (currentArticle.getDescription() != null) {
            description.setText(currentArticle.getDescription());
        }

        return itemView;
    }

    // Helper method to format Date and time
    private String formatDateTime(String publishDateTime) {
        String displayDateTime = new String();
        if (publishDateTime != "null" && !TextUtils.isEmpty(publishDateTime)) {
            try {
                Calendar calendar = ISO8601.toCalendar(publishDateTime);
                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL, yyyy h:mm a");
                publishDateTime = simpleDateFormat.format(date);
                return publishDateTime;

            }catch (ParseException e){
                Log.wtf("ArticleAdapter.java", "Error in parsing the given dateTime", e);
                return displayDateTime;

            }

        }else{
            mPublishDateTime.setVisibility(View.GONE);
            return displayDateTime;
        }

    }
}
