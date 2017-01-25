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
 * Created by Ahmed on 9/11/2016.
 */

// Fetching the trailer of the fetched movies from the API
public class Trailer extends AsyncTask<Void,Void,String>
{
    public final String TRAILER_KEY = "key";
    String MOVIE_IDEN;
    String VIDEO_KEY= new String();

    public  Trailer(String iden)
        {
            MOVIE_IDEN=iden;
        }

    protected String doInBackground(Void... params)
    {
        String TRAILER_JsonStr = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try
        {
            URL url = new URL("https://api.themoviedb.org/3/movie/"+MOVIE_IDEN+"/videos?api_key=037dbdf0a0da88ea7b911b15e2023889");
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
            TRAILER_JsonStr = buffer.toString();

            VIDEO_KEY= getTrailerKey(TRAILER_JsonStr);
            Log.e("KEY",VIDEO_KEY);

        } catch (MalformedURLException e)
        {e.printStackTrace();}

        catch (ProtocolException e)
        {e.printStackTrace();}

        catch (IOException e)
        {e.printStackTrace();}

        catch (JSONException e)
        { e.printStackTrace();}

        return VIDEO_KEY;

    }


    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        MovieDetails obj= new MovieDetails();
        DetailLand obj2= new DetailLand();
        obj.setTrailer(s);
        obj2.setTrailer(s);
    }
    // extract the key of the trailer
    private String getTrailerKey(String TrailerJsonStr)throws JSONException
    {
        String JSON_KEY="";
        JSONObject TrailerJSON = new JSONObject(TrailerJsonStr);
        JSONArray TrailerArray = TrailerJSON.getJSONArray("results");
        JSONObject Trailer= TrailerArray.getJSONObject(0);
        JSON_KEY=Trailer.getString(TRAILER_KEY);
//        if(JSON_KEY=="")
//        {    Trailer= TrailerArray.getJSONObject(0);
//              JSON_KEY=Trailer.getString(TRAILER_KEY);
//        }
        Log.e("trailer",JSON_KEY);
        return JSON_KEY;

    }
}
