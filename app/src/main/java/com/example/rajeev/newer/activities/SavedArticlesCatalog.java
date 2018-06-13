package com.example.rajeev.newer.activities;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.adapters.SavedArticleAdapter;
import com.example.rajeev.newer.data.NewerContract;

import java.io.ByteArrayOutputStream;

public class SavedArticlesCatalog extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = SavedArticlesCatalog.class.getSimpleName();
    private static final int sLoaderID = 100;
    private RecyclerView mRecylerView;
    private SavedArticleAdapter mAdapter;
    private Cursor mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles_catalog);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Setup RecyclerView
        mRecylerView = findViewById(R.id.saved_articles_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecylerView.setLayoutManager(layoutManager);
        mRecylerView.setHasFixedSize(true);

        mAdapter = new SavedArticleAdapter(this);
        mRecylerView.setAdapter(mAdapter);


        // Swipe-to-delete function
        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                Uri uri = ContentUris.withAppendedId(NewerContract.ArticleEntry.CONTENT_URI, id);
                getContentResolver().delete(uri, null,null);
                getSupportLoaderManager().restartLoader(sLoaderID, null, SavedArticlesCatalog.this);
                Toast.makeText(SavedArticlesCatalog.this, "Article deleted!", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(mRecylerView);

        getSupportLoaderManager().initLoader(sLoaderID, null, this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mData.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(sLoaderID, null, this);
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }




    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { NewerContract.ArticleEntry._ID,
                NewerContract.ArticleEntry.TITLE,
                NewerContract.ArticleEntry.DESCRIPTION,
                NewerContract.ArticleEntry.IMAGE,
                NewerContract.ArticleEntry.SOURCE_NAME};

        try{
            return new CursorLoader(this,
                    NewerContract.ArticleEntry.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null);

        }catch (Exception e){
            Log.e(LOG_TAG,"Failed to asynchronously load data.", e);
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if(data!=null){
           mData = mAdapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
            mData = mAdapter.swapCursor(null);
        }


}


