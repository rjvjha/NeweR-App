package com.example.rajeev.newer.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class NewerProvider extends ContentProvider {

    private NewerDbHelper mDbHelper;

    // Integer constants for URI Matcher
    public static final int ARTICLES = 100;
    public static final int ARTICLE_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // directory
        uriMatcher.addURI(NewerContract.CONTENT_AUTHORITY, NewerContract.ARTICLES_PATH, ARTICLES);
        // single item
        uriMatcher.addURI(NewerContract.CONTENT_AUTHORITY,NewerContract.ARTICLES_PATH + "/#",
                ARTICLE_WITH_ID);
        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new NewerDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        // get readable database
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor cursor;

        switch (match){

            // query all articles
            case ARTICLES :
                cursor = db.query(NewerContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case ARTICLE_WITH_ID:
                // using Selection and selection args
                // Uri : Content://Content_Authority/articles/#
                String id = uri.getPathSegments().get(1);
                selection = NewerContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[]{id};
                cursor = db.query(NewerContract.ArticleEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // default exception
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri resultUri;

        switch (match){

            case ARTICLES:
                // insert values into Articles table
                long id = db.insert(NewerContract.ArticleEntry.TABLE_NAME, null, values);
                if(id > 0){
                    resultUri = ContentUris.withAppendedId(NewerContract.ArticleEntry.CONTENT_URI, id);
                }else{
                    throw new SQLException("Failed to insert row into "+ uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri );

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case ARTICLES:
                rowsDeleted = db.delete(NewerContract.ArticleEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case ARTICLE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = NewerContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[]{id};
                rowsDeleted = db.delete(NewerContract.ArticleEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

         if (rowsDeleted!=0){
            getContext().getContentResolver().notifyChange(uri,null);
         }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated;
        int match = sUriMatcher.match(uri);
        switch(match){
            case ARTICLES:
                rowsUpdated = db.update(NewerContract.ArticleEntry.TABLE_NAME,
                        values,selection, selectionArgs);
                break;
            case ARTICLE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = NewerContract.ArticleEntry._ID + "=?";
                selectionArgs = new String[]{id};
                rowsUpdated = db.update(NewerContract.ArticleEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if(rowsUpdated!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }


    /**
     * Returns the MIME type of data for the content URI.
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch(match){
            case ARTICLES:
                return NewerContract.ArticleEntry.CONTENT_LIST_TYPE;
            case ARTICLE_WITH_ID:
                return NewerContract.ArticleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
    }


}
