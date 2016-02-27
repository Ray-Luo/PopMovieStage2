package com.raystone.ray.popmoviesstage1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.raystone.ray.popmoviesstage1.database.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Ray on 2/13/2016.
 */
public class MovieAdapter extends CursorAdapter {

    public MovieAdapter(Context context, Cursor cursor, int flags)
    {super(context,cursor,flags);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.main_fragment_single_item,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ImageView imageView = (ImageView)view;
        int posterId = cursor.getColumnIndex(MovieContract.ByRatingEntry.COLUMN_POSTER);
        String posterPath = cursor.getString(posterId);
        final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
        posterPath = BASE_POSTER_URL + posterPath;
        Picasso.with(context).load(posterPath).placeholder(R.drawable.white_placeholder).error(R.drawable.error_placeholder).resize(270,405).into(imageView);
    }
}
