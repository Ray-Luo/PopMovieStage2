package com.raystone.ray.popmoviesstage1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Ray on 2/1/2016.
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null)
        {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieDetailFragment.MOVIE_DETAIL_URI, getIntent().getData());

            FragmentManager manager = getFragmentManager();
            MovieDetailFragment fragment = MovieDetailFragment.newInstance();
            fragment.setArguments(arguments);
            manager.beginTransaction().add(R.id.movie_detail, fragment).commit();
        }

    }
}
