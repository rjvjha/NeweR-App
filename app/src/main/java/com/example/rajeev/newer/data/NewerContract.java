package com.example.rajeev.newer.data;

import android.provider.BaseColumns;


/**
 * API Contract for the Newer app.
 */

public class NewerContract {
    // URI constants
    public static final String CONTENT_AUTHORITY = "com.example.rajeev.newer";

    // TODO: define following URI a/c to our app
    //    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY );
    //    public static final String PETS_PATH = "pets";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private NewerContract(){};

    public static final class ArticleEntry implements BaseColumns{

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
