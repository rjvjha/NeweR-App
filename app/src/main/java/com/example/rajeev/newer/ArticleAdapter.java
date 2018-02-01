package com.example.rajeev.newer;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


    private class ViewHolder{
        TextView sourceName;
        TextView publishDateTime;
        TextView title;
        TextView authorName;
        TextView byLabel;
        TextView description;
        ImageView articleImage;
        Button shareButton;
        Button saveButton;
        Button readMoreButton;
        int position;
    }

    // private helper methods to add listener to ShareAction
    private void shareAction(Button button, final String articleTitle, final String url){
        button.setOnClickListener(new View.OnClickListener() {
            final String MSG = "Read this: \n" + articleTitle +"\n" + url + "\n" +
                    "shared from Newer news app.";
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, MSG);
                shareIntent.setType("text/plain");
                String chooserTitle = getContext().getResources().getString(R.string.chooser_title);
                Intent chooser = Intent.createChooser(shareIntent, chooserTitle);

                // Verify the intent will resolve to at least one activity
                if(shareIntent.resolveActivity(getContext().getPackageManager()) != null){
                    getContext().startActivity(chooser);
                }

            }
        });
    }
    // private helper methods to add listener to readMoreAction
    private void readMoreAction(Button button, final String url){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int toolbarColorId = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
                intentBuilder.setToolbarColor(toolbarColorId);
                intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(getContext().
                        getResources(), R.drawable.ic_arrow_back_white_24dp));
                intentBuilder.setStartAnimations(getContext(),R.anim.slide_in_right,
                        R.anim.slide_out_left);
                intentBuilder.setExitAnimations(getContext(),android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);

                CustomTabsIntent customTabsIntent = intentBuilder.build();
                customTabsIntent.launchUrl(getContext(), Uri.parse(url));
            }
        });

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
            holder.sourceName = itemView.findViewById(R.id.source_name_textView);
            holder.publishDateTime = itemView.findViewById(R.id.article_published_datetime);
            holder.title = itemView.findViewById(R.id.article_title_textView);
            holder.authorName = itemView.findViewById(R.id.article_author_textView);
            holder.byLabel = itemView.findViewById(R.id.by_label_text);
            holder.description = itemView.findViewById(R.id.article_description_textView);
            holder.articleImage = itemView.findViewById(R.id.article_image_imageView);
            holder.shareButton = itemView.findViewById(R.id.article_button_share);
            holder.saveButton =  itemView.findViewById(R.id.article_save_button);
            holder.readMoreButton = itemView.findViewById(R.id.article_read_more_button);
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
        String currentTitle = currentArticle.getTitle();
        String currentAuthor = currentArticle.getAuthor();
        String publishAt = currentArticle.getPublishedAt();
        String description = currentArticle.getDescription();
        String imageUrl = currentArticle.getUrlToImage();
        String articleUrl = currentArticle.getUrl();


        // Set article source name
        holder.sourceName.setText(currentArticle.getSourceName());
        // Set publish DateTime
        holder.publishDateTime.setText(formatDateTime(publishAt,holder.publishDateTime));
        if(imageUrl!="null" && !TextUtils.isEmpty(imageUrl)){
           GlideApp.with(getContext())
                   .load(imageUrl)
                   .placeholder(R.color.fallbackImageColor)
                   .fallback(Color.GRAY)
                   .optionalCenterCrop()
                   .into(holder.articleImage);
        }else{
            holder.articleImage.setVisibility(View.GONE);
        }


        // Set Title
        holder.title.setText(currentTitle);

        // Check for null and empty author values
        if (currentAuthor.equals("null") || TextUtils.isEmpty(currentAuthor)) {
            holder.byLabel.setVisibility(View.GONE);
            holder.authorName.setVisibility(View.GONE);

        } else {
            holder.authorName.setText(currentAuthor);
        }

        // Check for empty description and set description
        if (!description.equals("null") && !TextUtils.isEmpty(description)) {
            holder.description.setText(currentArticle.getDescription());
        }else{
            holder.description.setVisibility(View.GONE);
        }

        // attach onClick Listeners to buttons here;
        shareAction(holder.shareButton, currentTitle, articleUrl);
        readMoreAction(holder.readMoreButton, articleUrl);

        return itemView;
    }

    // Helper method to format Date and time
    private String formatDateTime(String publishDateTime, TextView dateTime) {
        String displayDateTime = new String();
        if (!publishDateTime.equals("null") && !TextUtils.isEmpty(publishDateTime)) {
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
