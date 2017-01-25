package com.example.ahmed.supermovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;




public class MainActivity extends AppCompatActivity
{
    boolean two_pane;
    static MovieClass mov[]=null;
    ArrayList<MovieClass> moviesBuffer= new ArrayList<>();
    URL url =new URL("http://api.themoviedb.org/3/movie/top_rated?api_key=037dbdf0a0da88ea7b911b15e2023889");

    public MainActivity() throws MalformedURLException {
    }

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout lay=(LinearLayout)findViewById(R.id.details);
        if(null==lay)
        {
            two_pane=false;
        }
        else
        {
            two_pane=true;
        }
        FetchMovies getMovies= null;
        try {
            getMovies = new FetchMovies(this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getMovies.execute();
        setTitle("Popular Movies");

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();

        if (id == R.id.sort)
        {
            FetchMovies obj= null;
            try {
                obj = new FetchMovies(this);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            moviesBuffer=obj.getSortedMovies();
            MovieClass [] ratedMovies= new  MovieClass[moviesBuffer.size()];
            Log.e("test5",moviesBuffer.get(1).Name);
            for (int i=0;i<moviesBuffer.size();i++)
            {
                ratedMovies[i]=moviesBuffer.get(i);
                Log.e("test6",ratedMovies[i].Name);
            }

             populateImageAdapter(ratedMovies);
            
            return true;
        }
        if (id == R.id.rated)
        {
            setTitle("Top Rated");
            FetchMovies obj= null;
            try {
                obj = new FetchMovies(url,this);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            obj.execute();

            return true;
        }
        if(id==R.id.favorites)
        {
            Intent i= new Intent(MainActivity.this,Favorites.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }


    void populateImageAdapter(final MovieClass[] movies)
  {
      getMovies(movies);
      final GridView gridview = (GridView) findViewById(R.id.grid_movies);


      gridview.setAdapter(new ImageAdapter(this,movies));

      gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              DetailLand detailsFragment=null;
              if(!two_pane){
//                  getSupportFragmentManager().beginTransaction().remove(detailsFragment).addToBackStack(null).commit();
              Intent details= new Intent(MainActivity.this,MovieDetails.class);
              details.putExtra("MOVIE_NAME",movies[position].Name);
              details.putExtra("MOVIE_DATE",movies[position].year);
              details.putExtra("MOVIE_RATE",movies[position].rate);
              details.putExtra("MOVIE_DESCRIPTION",movies[position].Description);
              details.putExtra("MOVIE_POSTER",movies[position].Poster_Path);
              details.putExtra("MOVIE_ID",movies[position].id);
              Log.e("IDxxx",movies[position].id);
              startActivity(details);}
              else {
                  LinearLayout l=(LinearLayout)findViewById(R.id.details);
                  l.setBackgroundColor(0);
                  detailsFragment= new DetailLand();
                  Bundle extras= new Bundle();
                  Log.e("ooo","here");
                  extras.putString("name", movies[position].Name);
                  extras.putString("MOVIE_NAME",movies[position].Name);
                  extras.putString("MOVIE_DATE",movies[position].year);
                  extras.putString("MOVIE_RATE",movies[position].rate);
                  extras.putString("MOVIE_DESCRIPTION",movies[position].Description);
                  extras.putString("MOVIE_POSTER",movies[position].Poster_Path);
                  extras.putString("MOVIE_ID",movies[position].id);
                  detailsFragment.setArguments(extras);

                  getSupportFragmentManager().beginTransaction().replace(R.id.details,detailsFragment).commit();


              }
          }


      });
  }
    void getMovies(MovieClass movies[])
    {
        mov=new MovieClass[movies.length];
        for(int i=0;i<movies.length;i++)
        {
            mov[i]=movies[i];
            Log.e("OPP",mov[i].Name);
        }

    }

}
