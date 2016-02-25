package com.raystone.ray.popmoviesstage1;

/**
 * Created by Ray on 2/12/2016.
 */
public class Movie {

    String title;
    String releaseDate;
    String moviePoster;
    String voteAverage;
    String plotSynopsis;
    String id;
    String trailerPath;
    public static String sortBy = "Sort by rate";

    public Movie(String title,String releaseDate,String moviePoster,String voteAverage,String plotSynopsis,String id,String trailerPath)
    {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
        this.trailerPath = trailerPath;
        this.id = id;
    }
}
