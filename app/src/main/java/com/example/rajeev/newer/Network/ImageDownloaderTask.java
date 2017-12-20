package com.example.rajeev.newer.Network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by rjvjha on 20/12/17.
 */

public final class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final static String LOG_TAG = ImageDownloaderTask.class.getSimpleName();
    // We use weakReference ImageView object so that it can be collected by GC
    private final WeakReference<ImageView> ImageViewReference;

    public ImageDownloaderTask(ImageView imageView){
        ImageViewReference = new WeakReference<ImageView>(imageView);
    }



    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            return QueryUtils.downloadBitmapFromInternet(params[0]);
        }catch (IOException e){
            Log.e(LOG_TAG, " error downloading Image for url :"+ params[0]);
            return null;
        }
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()){
            bitmap = null;
        }
        if(ImageViewReference!=null){
            ImageView imageView = ImageViewReference.get();
            if(imageView!=null){
                if(bitmap!=null){
                    imageView.setImageBitmap(bitmap);
                }else{
                    //@// TODO: 20/12/17  Handle null bitmap by setting placeholder drawable on ImageView
                }
            }
        }
    }
}
