package com.example.rajeev.newer.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.rajeev.newer.R;
import com.example.rajeev.newer.adapters.SavedArticleAdapter;
import com.example.rajeev.newer.data.NewerContract;
import com.example.rajeev.newer.data.NewerDbHelper;

import java.io.ByteArrayOutputStream;

public class SavedArticlesCatalog extends AppCompatActivity {
    private static final String LOG_TAG = SavedArticlesCatalog.class.getSimpleName();
    private RecyclerView mRecylerView;
    private Cursor mData;

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_articles_catalog);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        NewerDbHelper dbHelper = new NewerDbHelper(this);
        Bitmap dummyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
        //DbTestUtils.insertFakeData(dbHelper.getWritableDatabase(),dummyBitmap);
        mData = getDataFromDb(dbHelper.getReadableDatabase());

        // Setup RecyclerView
        mRecylerView = findViewById(R.id.saved_articles_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecylerView.setLayoutManager(layoutManager);
        mRecylerView.setHasFixedSize(true);

        SavedArticleAdapter adapter = new SavedArticleAdapter(this, mData);
        mRecylerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private Cursor getDataFromDb(SQLiteDatabase db) {
        String[] projection = {NewerContract.ArticleEntry.TITLE,
                NewerContract.ArticleEntry.DESCRIPTION,
                NewerContract.ArticleEntry.IMAGE,
                NewerContract.ArticleEntry.SOURCE_NAME};

        return db.query(NewerContract.ArticleEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mData.close();
    }
}
