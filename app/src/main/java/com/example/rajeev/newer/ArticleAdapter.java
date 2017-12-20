package com.example.rajeev.newer;

import android.content.Context;
import android.graphics.Bitmap;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }


    static class ViewHolder{
        TextView sourceName;
        TextView publishDateTime;
        TextView title;
        TextView authorName;
        TextView byLabel;
        TextView description;
        ImageView articleImage;
        int position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // current article
        Article currentArticle = getItem(position);
        ViewHolder holder;
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.sourceName = (TextView) itemView.findViewById(R.id.source_name_textView);
            holder.publishDateTime = (TextView) itemView.findViewById(R.id.article_published_datetime);
            holder.title = (TextView) itemView.findViewById(R.id.article_title_textView);
            holder.authorName = (TextView) itemView.findViewById(R.id.article_author_textView);
            holder.byLabel = (TextView) itemView.findViewById(R.id.by_label_text);
            holder.description = (TextView) itemView.findViewById(R.id.article_description_textView);
            holder.articleImage = (ImageView) itemView.findViewById(R.id.article_image_imageView);
            holder.position = position;
            itemView.setTag(holder);
        }
        holder = (ViewHolder) itemView.getTag();


        holder.publishDateTime.setVisibility(View.VISIBLE);
        holder.authorName.setVisibility(View.VISIBLE);
        holder.articleImage.setVisibility(View.VISIBLE);
        holder.description.setVisibility(View.VISIBLE);
        holder.byLabel.setVisibility(View.VISIBLE);

        // Get the values from Article object
        String currentAuthor = currentArticle.getAuthor();
        Bitmap articleThumnail = currentArticle.getArticleImage();
        String publishAt = currentArticle.getPublishedAt();
        String description = currentArticle.getDescription();

        // Check for null and empty author values
        if (currentAuthor == "null" || TextUtils.isEmpty(currentAuthor)) {
            holder.byLabel.setVisibility(View.GONE);
            holder.authorName.setVisibility(View.GONE);

        } else {
            holder.authorName.setText(currentAuthor);
        }

//        // Check for null Bitmap values
//        if (articleThumnail == null) {
//            articleImage.setVisibility(View.GONE);
//        } else {
//            articleImage.setImage
//        }

        holder.sourceName.setText(currentArticle.getSourceName());

        holder.publishDateTime.setText(formatDateTime(publishAt,holder.publishDateTime));

        holder.title.setText(currentArticle.getTitle());

        // Check for empty description
        if (description != null && !TextUtils.isEmpty(description)) {
            holder.description.setText(currentArticle.getDescription());
        }

        return itemView;
    }

    // Helper method to format Date and time
    private String formatDateTime(String publishDateTime, TextView dateTime) {
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
            dateTime.setVisibility(View.GONE);
            return displayDateTime;
        }

    }
}
