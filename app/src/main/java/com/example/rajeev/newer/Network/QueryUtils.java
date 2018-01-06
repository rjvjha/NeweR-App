package com.example.rajeev.newer.Network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.example.rajeev.newer.Article;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjvjha on 17/12/17.
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    /** Private Constructor as we don't want to create instances of {@link QueryUtils} */
    private QueryUtils(){}


    public static List<Article> fetchArticlesFromNetwork(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "error while closing input stream.",e);
            e.printStackTrace();
        }
        return fetchArticleFromJson(jsonResponse);
    }

    // private helper methd to create url from string
    private static URL createUrl(String stringUrl){
        URL newUrl = null;
        try{
            newUrl = new URL(stringUrl);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG,"Error creating Url", e);
            return null;
        }
        return newUrl;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private  static String makeHttpRequest(URL url) throws IOException{
        String jsonRespnse = " ";

        // if url is null, return early
        if(url == null){
            return jsonRespnse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.v(LOG_TAG,"Connection Opened");
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            Log.v(LOG_TAG,"Connecting to the given url" + url);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode== HttpURLConnection.HTTP_OK){
                inputStream = urlConnection.getInputStream();
                jsonRespnse = readFromStream(inputStream);
            } else {
                Log.wtf(LOG_TAG," Http Error code :"+ responseCode);
                return null;
            }

        } catch (IOException e){
            Log.e(LOG_TAG,"Error reading the input stream",e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonRespnse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Make an HTTP request to the given imageURL and return a Bitmap as the response.
     */
    public static Bitmap downloadBitmapFromInternet(String imageUrl) throws IOException{
        //@ToDo: write code to fetch bitmap from imageUrl
        Bitmap thumbnail = null;
        URL url = createUrl(imageUrl);
        Log.v(LOG_TAG,"Image url:"+url);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.v(LOG_TAG,"Connection Opened");
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(500);
            urlConnection.setUseCaches(true);
            Log.v(LOG_TAG,"Connecting to the given url");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.wtf(LOG_TAG,"Connection successful, fetching input sream now");
                inputStream = urlConnection.getInputStream();
                Log.wtf(LOG_TAG,"received the stream, decoding stream now");
                thumbnail = BitmapFactory.decodeStream(inputStream);
                return thumbnail;
            }else{
                Log.wtf(LOG_TAG, "error while connecting, http error code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){

            Log.e(LOG_TAG,"error while reading the input stream", e);

        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }

            if(inputStream != null){
                inputStream.close();
            }
        }
        return null;
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Article> fetchArticleFromJson(final String JSON_RESPONSE){
        List<Article> articlesList = new ArrayList<>();

        // Check for empty Json response
        if(JSON_RESPONSE == null || TextUtils.isEmpty(JSON_RESPONSE)){
            return null;
        }
        try{
            JSONObject root = new JSONObject(JSON_RESPONSE);
            String status = root.getString("status");
            // if status is ok start reading from JSON
            if(TextUtils.equals(status,"ok")){
                JSONArray articlesArray = root.getJSONArray("articles");
                for(int i=0; i< articlesArray.length();i++) {
                    // fetch only 49 articles at a time.
                    if(i >= 49){
                        break;
                    }
                    String sourceId;
                    String sourceName;
                    String author;
                    String title;
                    String description;
                    String url;
                    String urlToImage = null;
                    String publishedAt;
                    JSONObject article = articlesArray.getJSONObject(i);
                    JSONObject source = article.getJSONObject("source");
                    sourceId = source.optString("id");
                    sourceName = source.optString("name");
                    author = article.optString("author");
                    title = article.optString("title");
                    description = article.optString("description");
                    url = article.optString("url");
                    urlToImage = article.optString("urlToImage");
                    publishedAt = article.getString("publishedAt");
                    articlesList.add(new Article(sourceId,
                            sourceName,
                            author,
                            title,
                            description,
                            url,
                            urlToImage,
                            publishedAt
                            )) ;
                }
                return articlesList;
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Json Results", e);
            e.printStackTrace();
        }

        return null;
    }


}
