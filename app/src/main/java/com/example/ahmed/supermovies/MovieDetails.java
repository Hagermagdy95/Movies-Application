package com.example.ahmed.supermovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity
{   String Name;
    String Year;
    String desc;
    String pos;
    String ra;
    String title;
    static StringBuilder Trailer_URL=new StringBuilder("https://www.youtube.com/watch?v=");
    static StringBuilder Reviews_URL= new StringBuilder("");
    static ArrayList<MovieClass> fav_movies=new ArrayList<>();
    String ID;
    String fileread="";
    boolean inFile=false;
    String x;
    FileOutputStream favorties=null;


    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        try {
            favorties= openFileOutput("favList",MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        TextView name=(TextView)findViewById(R.id.name);
        ImageView poster=(ImageView) findViewById(R.id.poster);
        TextView date=(TextView)findViewById(R.id.date);
        TextView Desc=(TextView)findViewById(R.id.desc);
        RatingBar rate=(RatingBar)findViewById(R.id.rate);
        Button trailer=(Button)findViewById(R.id.trailer);
        Button reviews=(Button)findViewById(R.id.reviews);
        Intent intent= getIntent();
        title=intent.getStringExtra("MOVIE_NAME");
        setTitle(title);
        Name=intent.getStringExtra("MOVIE_NAME");
        name.setText(intent.getStringExtra("MOVIE_NAME"));
        ID=intent.getStringExtra("MOVIE_ID");

        Trailer obj= new Trailer(ID);
        obj.execute();
        Reviews obj2=new Reviews(ID);
        obj2.execute();
        Year=intent.getStringExtra("MOVIE_DATE");
        date.setText(intent.getStringExtra("MOVIE_DATE"));
        desc=intent.getStringExtra("MOVIE_DESCRIPTION");
        Desc.setText(intent.getStringExtra("MOVIE_DESCRIPTION"));
        ra=intent.getStringExtra("MOVIE_RATE");
        rate.setRating(Float.parseFloat(ra));
        pos=intent.getStringExtra("MOVIE_POSTER");
        String pos1=pos.replace("w500","w342");
        Log.e("posters",pos1);
        Picasso.with(this).load(pos1).into(poster);
        poster.setMinimumWidth(100);
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Button addToFav=(Button)findViewById(R.id.fav);
        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Trailer_URL.toString()));
                startActivity(intent);
                Log.e("errr",Trailer_URL.toString());
            }
        });
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Reviews_URL.toString()));
                    startActivity(intent);
                    Log.e("errr6", Reviews_URL.toString());

                } catch (ActivityNotFoundException e) {

                    Toast.makeText(MovieDetails.this, "No reviews available now", Toast.LENGTH_SHORT).show();
                }
            }
            });
           addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream in = null;
                InputStreamReader isi = null;
                BufferedReader br = null;

                try {
                    in = openFileInput("favList");
                    isi = new InputStreamReader(in);
                    br = new BufferedReader(isi);
                    fileread = "";
                    StringBuilder x = new StringBuilder();
                    while ((fileread = br.readLine()) != null) {
                        if (fileread.contains(ID)) {
                            inFile=true;
                            break;
                        }
                    }
                    in.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!inFile)
                {
                    x = String.format("%s~%s~%s~%s~%s~%s~%s~%s\n", ID, Name, Year, ra, desc, Trailer_URL, Reviews_URL, pos);
                    try {
                        favorties.write(x.getBytes());
                        favorties.flush();
                        favorties.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(MovieDetails.this, "Added to favorites", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(MovieDetails.this,"Already added",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void setTrailer(String k)
    {
        Log.e("lol",k);
       Trailer_URL.setLength(0);
        Trailer_URL.append("https://www.youtube.com/watch?v="+k);
        Log.e("errr3",Trailer_URL.toString());
    }

    void setReviews_URL(String k)
    {
        Log.e("rev",k);
        Reviews_URL.setLength(0);
        Reviews_URL.append(k);
        Log.e("revxy",Reviews_URL.toString());
    }
}
