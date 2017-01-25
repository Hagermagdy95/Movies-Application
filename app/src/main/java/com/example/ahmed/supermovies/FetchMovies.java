package com.example.ahmed.supermovies;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Ahmed on 9/11/2016.
 */
// Class that fetches the newest movies from TMDB API
public class FetchMovies extends AsyncTask<Void,Void, MovieClass[]>
{   // JSON objects names to be extracted
    public final String MOVIE_TITLE = "original_title";
    public final String POSTER_PATH = "poster_path";
    public final String OVERVIEW = "overview";
    public final String RELEASE_DATE = "release_date";
    public final String RATE = "vote_average";
    public final String ID = "id";
    URL url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=037dbdf0a0da88ea7b911b15e2023889");
    MovieClass Movies_list[];
    static ArrayList<MovieClass> UnsortedMovies;
    static ArrayList<MovieClass> SortedMovies;
    MainActivity mainObject;

    public FetchMovies(MainActivity mainObject) throws MalformedURLException {
        this.mainObject = mainObject;

    }
    public FetchMovies (URL top_rated,MainActivity mainObject) throws MalformedURLException {
        this.url=top_rated;
        this.mainObject=mainObject;

    }
    private MovieClass[] getMOVIEDataFromJson(String MOVIEtJsonStr)
            throws JSONException

    {
        JSONObject movieJSON = new JSONObject(MOVIEtJsonStr);
        JSONArray MoviesArray = movieJSON.getJSONArray("results");
        Movies_list = new MovieClass[MoviesArray.length()];

        // Saving the array of JSON objects into movies array
        for (int i = 0; i < MoviesArray.length(); i++)
        {
            MovieClass mov = new MovieClass();
            JSONObject Movie = MoviesArray.getJSONObject(i);
            mov.Name = Movie.getString(MOVIE_TITLE);
            mov.id = Movie.getString(ID);
            mov.year = Movie.getString(RELEASE_DATE);
            mov.Poster_Path = "http://image.tmdb.org/t/p/w500//" + Movie.getString(POSTER_PATH);
            mov.Description = Movie.getString(OVERVIEW);
            mov.rate = Movie.getString(RATE);
            Log.e("e", mov.Name);
            Log.e("f", mov.year);
            Log.e("x", mov.Poster_Path);
            Movies_list[i] = mov;
        }
        // Sort the movies by rate in advance
        UnsortedMovies = new ArrayList<MovieClass>(Arrays.asList(Movies_list));
        Log.e("Test4",UnsortedMovies.get(1).Name);
        SortMovies(UnsortedMovies);
        Log.e("Test2",UnsortedMovies.get(1).Name);
        return Movies_list;

    }

    @Override

    protected MovieClass[] doInBackground(Void... params)
    {
        String MOVIE_JsonStr = null;
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(c.getTime());
        Log.e("dateofmovie", s);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

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
            MOVIE_JsonStr = buffer.toString();
            getMOVIEDataFromJson(MOVIE_JsonStr);
        }
        catch (MalformedURLException e)
        {e.printStackTrace();}

        catch (ProtocolException e)
        {e.printStackTrace(); }

        catch (IOException e)
        {e.printStackTrace();}

        catch (JSONException e)
        { e.printStackTrace();}

        return Movies_list;
    }

    protected void onPostExecute(MovieClass[] movies)
    {
        super.onPostExecute(movies);


        // Checking if the movie list is ready to populate
        if (movies != null)

                mainObject.populateImageAdapter(movies);
        else {
               Toast.makeText(this.mainObject,"No Internet connection",Toast.LENGTH_SHORT).show();
             }
    }

    public ArrayList<MovieClass> getSortedMovies ()
    {
        return SortedMovies;
    }

    public void SortMovies(ArrayList<MovieClass> x)
    {
        Collections.sort(x, new Comparator<MovieClass>() {
            @Override
            public int compare(MovieClass o1, MovieClass o2)
            {
                return o1.rate.compareTo(o2.rate);
            }
        });
        SortedMovies=x;
        Collections.reverse(SortedMovies);
        Log.e("Test3",SortedMovies.get(1).Name);
    }
}





