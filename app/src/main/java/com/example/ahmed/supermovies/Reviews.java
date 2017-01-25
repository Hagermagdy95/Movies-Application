package com.example.ahmed.supermovies;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Ahmed on 9/23/2016.
 */
public class Reviews extends AsyncTask<Void,Void,String> {
    public final String URL = "url";
    String MOVIE_IDEN;
    String URL_KEY= new String();

    public  Reviews(String iden)
    {
        MOVIE_IDEN=iden;
    }

    protected String doInBackground(Void... params)
    {
        String REVIEWS_JsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try
        {
            URL url = new URL("https://api.themoviedb.org/3/movie/"+MOVIE_IDEN+"/reviews?api_key=037dbdf0a0da88ea7b911b15e2023889");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            REVIEWS_JsonStr = buffer.toString();

            URL_KEY= getReviewsURL(REVIEWS_JsonStr);
            Log.e("KEYURL",URL_KEY);

        } catch (MalformedURLException e)
        {e.printStackTrace();}

        catch (ProtocolException e)
        {e.printStackTrace();}

        catch (IOException e)
        {e.printStackTrace();}

        catch (JSONException e)
        { e.printStackTrace();}

        return URL_KEY;

    }


    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        MovieDetails obj= new MovieDetails();
        DetailLand obj2=new DetailLand();
        obj2.setReviews_URL(s);
        obj.setReviews_URL(s);
    }
    // extract the key of the trailer
    private String getReviewsURL(String TrailerJsonStr)throws JSONException
    {
        String JSON_KEY="";
        JSONObject TrailerJSON = new JSONObject(TrailerJsonStr);
        JSONArray TrailerArray = TrailerJSON.getJSONArray("results");
        JSONObject Trailer= TrailerArray.getJSONObject(0);
        JSON_KEY=Trailer.getString(URL);
//        if(JSON_KEY=="")
//        {    Trailer= TrailerArray.getJSONObject(0);
//              JSON_KEY=Trailer.getString(TRAILER_KEY);
//        }
        Log.e("review",JSON_KEY);
        return JSON_KEY;

    }
}
