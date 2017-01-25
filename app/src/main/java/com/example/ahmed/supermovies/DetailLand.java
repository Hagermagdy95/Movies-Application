package com.example.ahmed.supermovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import static android.content.Context.MODE_APPEND;
import static com.example.ahmed.supermovies.R.id.fav;


public class DetailLand extends Fragment {
    static StringBuilder Trailer_URL=new StringBuilder("https://www.youtube.com/watch?v=");
    static StringBuilder Reviews_URL= new StringBuilder("");
    String ID;
    String Name;
    String Year;
    String desc;
    String pos;
    final String ra=null;
    String title;
    String fileread="";
    boolean inFile=false;
    String x;
    FileOutputStream favorties=null;

    public DetailLand() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        


        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == fav)
        {
            if (!inFile)
            {
                x = String.format("%s~%s~%s~%s~%s~%s~%s~%s\n", ID, Name, Year, ra, desc, Trailer_URL, Reviews_URL,pos);
                try {
                    favorties.write(x.getBytes());
                    favorties.flush();
                    favorties.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }


                Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

            }
            else
            {
                InputStream in= null;
                InputStreamReader isi=null;
                BufferedReader br=null;

                try
                {
                    in = getContext().openFileInput("favList");
                    isi = new InputStreamReader(in);
                    br = new BufferedReader(isi);
                    fileread = "";
                    StringBuilder x = new StringBuilder();
                    while ((fileread = br.readLine()) != null) {
                        if (fileread.contains(ID)) {
                            break;
                        }
                    }
                    in.close();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detailed, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_detail_land, container, false);
        final String ra;
        final String pos;

        try {
            favorties= getContext().openFileOutput("favList",MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        TextView name=(TextView)fragment.findViewById(R.id.name);
        TextView date=(TextView)fragment.findViewById(R.id.date);
        TextView Desc=(TextView)fragment.findViewById(R.id.desc);
        ImageView poster=(ImageView)fragment.findViewById(R.id.poster);
        RatingBar rate=(RatingBar)fragment.findViewById(R.id.rate);
        Button trailer=(Button)fragment.findViewById(R.id.trailer);
        Button favo=(Button)fragment.findViewById(R.id.fav);
        Button reviews=(Button)fragment.findViewById(R.id.reviews);

        name.setText(getArguments().getString("MOVIE_NAME"));
        Name=getArguments().getString("MOVIE_NAME");
        date.setText(getArguments().getString("MOVIE_DATE"));
        Year=getArguments().getString("MOVIE_DATE");
        Desc.setText(getArguments().getString("MOVIE_DESCRIPTION"));
        desc=getArguments().getString("MOVIE_DESCRIPTION");
        ra=getArguments().getString("MOVIE_RATE");
        rate.setRating(Float.parseFloat(ra));
        pos=getArguments().getString("MOVIE_POSTER");
        String pos1=pos.replace("w500","w92");
        Log.e("posters",pos1);
        Picasso.with(getContext()).load(pos1).into(poster);
        ID=getArguments().getString("MOVIE_ID");
        Trailer obj= new Trailer(ID);
        obj.execute();
        Reviews obj2=new Reviews(ID);
        obj2.execute();
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

                    Toast.makeText(getContext(), "No reviews available now", Toast.LENGTH_SHORT).show();
                }
            }
        });
        favo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream in = null;
                InputStreamReader isi = null;
                BufferedReader br = null;

                try {
                    in = getContext().openFileInput("favList");

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


                    Toast.makeText(getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(),"Already added",Toast.LENGTH_SHORT).show();
                }
            }
        });



        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

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
    void setInFile(boolean inFile)
    {
        this.inFile=inFile;
    }
}
