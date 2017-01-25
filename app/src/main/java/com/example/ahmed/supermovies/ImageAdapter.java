package com.example.ahmed.supermovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Ahmed on 9/11/2016.
 */
public class ImageAdapter extends BaseAdapter
{
    private Context mContext;
    private MovieClass[] Movies;
    public ImageAdapter(Context c,MovieClass movies[])
    {
        mContext = c;
        Movies=movies;
    }

    public int getCount()
    {
        return Movies.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;

        if (convertView == null)
        {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setPadding(0,0,0,0);
            imageView.setMaxHeight(700);
            imageView.setMinimumHeight(480);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).load(Movies[position].Poster_Path).into(imageView);
        return imageView;
    }
}

