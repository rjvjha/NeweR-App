package com.example.rajeev.newer.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Database helper for Newer app. Manages database creation and version management.
 */

public class NewerDbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "SavedArticles.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;


    public NewerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                NewerContract.ArticleEntry.TABLE_NAME + "( "
                + NewerContract.ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewerContract.ArticleEntry.SOURCE_NAME + " TEXT NOT NULL, "
                + NewerContract.ArticleEntry.AUTHOR_NAME + " TEXT, "
                + NewerContract.ArticleEntry.IMAGE + " BLOB, "
                + NewerContract.ArticleEntry.TITLE + " TEXT NOT NULL, "
                + NewerContract.ArticleEntry.DESCRIPTION + " TEXT, "
                + NewerContract.ArticleEntry.URL + " TEXT NOT NULL, "
                + NewerContract.ArticleEntry.IMAGE_URL + " TEXT, "
                + NewerContract.ArticleEntry.PUBLISHED_AT + " TEXT ) ;";

        db.execSQL(SQL_CREATE_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SQL statement to drop table
        final String dropTableCommand = "DROP TABLE IF EXISTS " +
                NewerContract.ArticleEntry.TABLE_NAME + ";";
        db.execSQL(dropTableCommand);
        onCreate(db);

    }
}
