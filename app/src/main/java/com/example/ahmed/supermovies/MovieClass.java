package com.example.ahmed.supermovies;

/**
 * Created by Ahmed on 9/11/2016.
 */
public class MovieClass
{
    public MovieClass(String id,String Name,String year,String rate,String description,String trailer,String reviews,String poster_Path)
    {
        this.Name=Name;
        this.year=year;
        this.rate=rate;
        this.Description=description;
        this.Poster_Path=poster_Path;
        this.trailer=trailer;
        this.reviews=reviews;
        this.id=id;


    }
    public MovieClass()
    {}
    String id ;
    String publisher ;
    String year;
    String Name;
    String Poster_Path;
    String Description;
    String rate;
    String trailer;
    String reviews;
}
