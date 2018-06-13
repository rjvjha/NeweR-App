package com.example.rajeev.newer.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * API Contract for the Newer app.
 */

public class NewerContract {

    // URI constants
    public static final String CONTENT_AUTHORITY = "com.example.rajeev.newer";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY );
    public static final String ARTICLES_PATH = "articles";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private NewerContract(){};

    public static final class ArticleEntry implements BaseColumns{

        // Specific Content URI to access all articles in db
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(ARTICLES_PATH).build();

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ARTICLES_PATH;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ARTICLES_PATH;

        /* Name of the database table */
        public static final String TABLE_NAME = "Article";

        /**
         * Unique ID number for the article (only for use in the database table).
         *
         * Type: INTEGER
         * Not Null
         */
        public static final String _ID = BaseColumns._ID;


        /**
         * Source Name of Article.
         * TYPE: TEXT
         * Not Null
         */
        public static final String SOURCE_NAME = "SourceName";


        /**
         * Author Name of Article.
         * TYPE: TEXT
         */
        public static final String AUTHOR_NAME = "AuthorName";


        /**
         * Available Image of Article.
         * TYPE: BLOB
         */
        public static final String IMAGE = "Image";


        /**
         * Title of Article.
         * TYPE: TEXT
         * Not Null
         */
        public static final String TITLE = "Title";


        /**
         * Descp of Article.
         * TYPE: TEXT
         */
        public static final String DESCRIPTION = "Description";


        /**
         * URL to Article.
         * TYPE: TEXT
         * Not Null
         */
        public static final String URL = "Url";

        /**
         * URL to ArticleImage.
         * TYPE: TEXT
         *
         */
        public static final String IMAGE_URL ="ImageUrl";

        /**
         * Published Time of Article.
         * TYPE: TEXT
         *
         */
        public static final String PUBLISHED_AT = "PublishedAt";




    }

}
