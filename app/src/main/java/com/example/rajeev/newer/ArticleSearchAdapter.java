package com.example.rajeev.newer;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rjvjha on 4/1/18.
 */

public class ArticleSearchAdapter extends ArrayAdapter<Article> {

    public ArticleSearchAdapter(Context context,List<Article> articles){
        super(context, 0, articles);
    }

    private static class ViewHolder{
        TextView title;
        TextView source;
        TextView description;
        ImageView articleImage;
        ImageButton shareButton;
        ImageButton downloadButton;
    }

    // private helper method for share action onClick

    private void shareAction(ImageButton button, final String TITLE, final String URL){
        button.setOnClickListener(new View.OnClickListener() {
            final String MSG = "Read this: \n" + TITLE +"\n" + URL + "\n" +
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

    // private helper method for downloadArticleAction onClick
    private void downloadArticle(ImageButton button, final Article CURRENT_ARTICLE){

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Article currentArticle = getItem(position);
        ViewHolder holder;
        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.article_item_compact,parent,false);
            holder = new ViewHolder();
            holder.title = itemView.findViewById(R.id.article_title);
            holder.description = itemView.findViewById(R.id.article_description);
            holder.source = itemView.findViewById(R.id.article_source);
            holder.articleImage = itemView.findViewById(R.id.article_image);
            holder.shareButton = itemView.findViewById(R.id.share_button_article);
            holder.downloadButton =itemView.findViewById(R.id.download_button_article);
            itemView.setTag(holder);
        } else{
            holder = (ViewHolder) itemView.getTag();
        }
        holder.description.setVisibility(View.VISIBLE);
        holder.articleImage.setVisibility(View.VISIBLE);

        // get the required values from article object
        String currentTitle = currentArticle.getTitle();
        String currentDescription = currentArticle.getDescription();
        String imageUrl = currentArticle.getUrlToImage();
        String currentSource = currentArticle.getSourceName();

        holder.source.setText(currentSource);


        if(!imageUrl.equals("null") && !TextUtils.isEmpty(imageUrl)){
            // RequestOptions options = new RequestOptions().set(MYC)

            GlideApp.with(getContext())
                    .load(imageUrl)
                    .thumbnail(GlideApp.with(getContext())
                        .load(imageUrl)
                        .override(80,80))
                    .placeholder(R.color.fallbackImageColor)
                    .error(R.drawable.ic_not_available_black_24dp)
                    .optionalCenterCrop()
                    .into(holder.articleImage);

        }else {
            holder.articleImage.setVisibility(View.GONE);
        }

        // Set Title
        holder.title.setText(currentTitle);

        // Check for null and empty description values
        if (currentDescription.equals("null" )|| TextUtils.isEmpty(currentDescription)) {
            holder.description.setVisibility(View.GONE);

        } else {
            holder.description.setText(currentDescription);
        }
        shareAction(holder.shareButton, currentTitle, currentArticle.getUrl());
        return itemView;
    }
}
