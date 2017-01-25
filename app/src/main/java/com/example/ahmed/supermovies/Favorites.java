package com.example.ahmed.supermovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    ArrayList<MovieClass> Favorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        InputStream in= null;
        InputStreamReader isi=null;
        BufferedReader br=null;
        String fileread="";
        setTitle("Favorites");
        Favorites=new ArrayList<MovieClass>();

        try {
            in = openFileInput("favList");

            isi = new InputStreamReader(in);
            br = new BufferedReader(isi);
            StringBuilder x = new StringBuilder();

            while ((fileread = br.readLine()) != null) {
                Log.e("files", fileread);
               String buffer[]=fileread.split("~");
                MovieClass obj=new MovieClass(buffer[0],buffer[1],buffer[2],buffer[3],buffer[4],buffer[5],buffer[6],buffer[7]);
                Favorites.add(obj);
            }
            in.close();
            MovieClass[] arr = new MovieClass[Favorites.size()];
            for(int i=0;i<arr.length;i++)
            {
                arr[i]=Favorites.get(i);
            }
            populateImageAdapter(arr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void populateImageAdapter(final MovieClass[] movies)
    {
        GridView gridview = (GridView) findViewById(R.id.grid_movies);
        gridview.setAdapter(new ImageAdapter(this,movies));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent details= new Intent(Favorites.this,MovieDetails.class);
                details.putExtra("MOVIE_NAME",movies[position].Name);
                details.putExtra("MOVIE_DATE",movies[position].year);
                details.putExtra("MOVIE_RATE",movies[position].rate);
                details.putExtra("MOVIE_DESCRIPTION",movies[position].Description);
                details.putExtra("MOVIE_POSTER",movies[position].Poster_Path);
                details.putExtra("MOVIE_ID",movies[position].id);
                startActivity(details);
            }


        });
    }
}
