package com.example.rajeev.newer;

import android.content.Context;
import android.graphics.Bitmap;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rajeev.newer.Network.ISO8601;
import com.example.rajeev.newer.Network.ImageDownloaderTask;

import java.lang.ref.WeakReference;
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
        }else{
            holder = (ViewHolder) itemView.getTag();
        }


        holder.publishDateTime.setVisibility(View.VISIBLE);
        holder.authorName.setVisibility(View.VISIBLE);
        holder.articleImage.setVisibility(View.VISIBLE);
        holder.description.setVisibility(View.VISIBLE);
        holder.byLabel.setVisibility(View.VISIBLE);

        // Get the values from Article object
        String currentAuthor = currentArticle.getAuthor();
        String publishAt = currentArticle.getPublishedAt();
        String description = currentArticle.getDescription();
        String imageUrl = currentArticle.getUrlToImage();

        // Set article source name
        holder.sourceName.setText(currentArticle.getSourceName());
        // Set publish DateTime
        holder.publishDateTime.setText(formatDateTime(publishAt,holder.publishDateTime));
        if(imageUrl!="null" && !TextUtils.isEmpty(imageUrl)){
           GlideApp.with(getContext())
                   .load(imageUrl)
                   .placeholder(R.color.backgroundColor)
                   .fallback(Color.GRAY)
                   .optionalCenterCrop()
                   .into(holder.articleImage);
        }else{
            holder.articleImage.setVisibility(View.GONE);
        }


        // Set Title
        holder.title.setText(currentArticle.getTitle());

        // Check for null and empty author values
        if (currentAuthor == "null" || TextUtils.isEmpty(currentAuthor)) {
            holder.byLabel.setVisibility(View.GONE);
            holder.authorName.setVisibility(View.GONE);

        } else {
            holder.authorName.setText(currentAuthor);
        }

        // Check for empty description and set description
        if (description != null && !TextUtils.isEmpty(description)) {
            holder.description.setText(currentArticle.getDescription());
        }else{
            holder.description.setVisibility(View.GONE);
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
