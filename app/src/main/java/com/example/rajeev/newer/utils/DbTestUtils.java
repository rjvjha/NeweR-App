package com.example.rajeev.newer.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.rajeev.newer.data.NewerContract;

import static com.example.rajeev.newer.activities.SavedArticlesCatalog.getBytes;

/**
 * Created by rjvjha on 5/6/18.
 */

public class DbTestUtils {

    private static final String LOG_TAG = DbTestUtils.class.getSimpleName();

    private DbTestUtils() {
    }

    ;

    public static void insertFakeData(SQLiteDatabase db, Bitmap bitmap) {
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();

        cv.put(NewerContract.ArticleEntry.SOURCE_NAME, "Google News (India)");
        cv.put(NewerContract.ArticleEntry.AUTHOR_NAME, "Rajeev");
        cv.put(NewerContract.ArticleEntry.TITLE, "When Chhetri ran towards Pakistan fans to celebrate a goal - Times of India,");
        cv.put(NewerContract.ArticleEntry.IMAGE, getBytes(bitmap));
        cv.put(NewerContract.ArticleEntry.DESCRIPTION, "Football News: India skipper Sunil Chhetri on" +
                " Sunday recalled his first international match when excitement got the better of him and he ended up running in the dire");
        cv.put(NewerContract.ArticleEntry.URL, "https://timesofindia.indiatimes.com/sports/football/top-stories/" +
                "when-chhetri-ran-towards-pakistan-fans-to-celebrate-a-goal/articleshow/64438876.cms");
        cv.put(NewerContract.ArticleEntry.IMAGE_URL, "https://static.toiimg.com/thumb/msid-64438853,width-1070,height-580" +
                ",imgsize-1358689,resizemode-6,overlay-toi_sw,pt-32,y_pad-40/photo.jpg");
        cv.put(NewerContract.ArticleEntry.PUBLISHED_AT, "2018-06-03T14:13:43+00:00");


        try {
            db.beginTransaction();
            //clear the table first
            db.delete(NewerContract.ArticleEntry.TABLE_NAME, null, null);
            db.insert(NewerContract.ArticleEntry.TABLE_NAME, null, cv);

            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            Log.d(LOG_TAG, "Exception occured" + e);

        } finally {
            db.endTransaction();
        }
    }
}
