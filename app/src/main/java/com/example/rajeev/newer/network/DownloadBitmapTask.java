package com.example.rajeev.newer.network;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.rajeev.newer.activities.SavedArticlesCatalog;
import com.example.rajeev.newer.data.NewerContract;

import java.io.IOException;

public class DownloadBitmapTask extends AsyncTask<String,Void, byte []> {
    private static final String LOG_TAG = DownloadBitmapTask.class.getSimpleName();
    private ContentValues mContentValues;
    private Context mContext;

    public DownloadBitmapTask(ContentValues cv, Context context){
        this.mContentValues = cv;
        this.mContext = context;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        mContentValues.put(NewerContract.ArticleEntry.IMAGE, bytes);
        Uri insertUri = mContext.getContentResolver().insert(NewerContract.ArticleEntry.CONTENT_URI, mContentValues);
        Toast.makeText(mContext,"Article Saved : "+
                insertUri, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected byte[] doInBackground(String... strings) {
        Bitmap bitmap = null;
        if(strings.length > 0 && strings[0]!= null){
            try{
                bitmap = QueryUtils.downloadBitmapFromInternet(strings[0]);
            }catch (IOException e){
                Log.e(LOG_TAG, "Error while downloading bitmap", e);
            }
        }
        if(bitmap!=null){
            return SavedArticlesCatalog.getBytes(bitmap);
        }
        return null;
    }


}
