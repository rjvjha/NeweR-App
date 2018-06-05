package com.example.rajeev.newer.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.activities.SavedArticlesCatalog;
import com.example.rajeev.newer.data.NewerContract;


/**
 * Created by rjvjha on 3/1/18.
 */

public class SavedArticleAdapter extends RecyclerView.Adapter<SavedArticleAdapter.SavedArticlesViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    public SavedArticleAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;

    }

    @Override
    public SavedArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.article_item_compact, parent, false);
        return new SavedArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedArticlesViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        String title = mCursor.getString(mCursor.getColumnIndex(NewerContract.ArticleEntry.TITLE));
        String descp = mCursor.getString(mCursor.getColumnIndex(NewerContract.ArticleEntry.DESCRIPTION));
        String source = mCursor.getString(mCursor.getColumnIndex(NewerContract.ArticleEntry.SOURCE_NAME));
        Bitmap image = SavedArticlesCatalog.getImage(mCursor.getBlob(mCursor.getColumnIndex(NewerContract.ArticleEntry.IMAGE)));

        // bind data to views
        holder.articleTitle.setText(title);
        holder.articleDesc.setText(descp);
        holder.articleSource.setText(source);
        holder.articleImage.setImageBitmap(image);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class SavedArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;
        TextView articleDesc;
        TextView articleSource;
        ImageView articleImage;


        SavedArticlesViewHolder(View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleDesc = itemView.findViewById(R.id.article_description);
            articleSource = itemView.findViewById(R.id.article_source);
        }


    }
}