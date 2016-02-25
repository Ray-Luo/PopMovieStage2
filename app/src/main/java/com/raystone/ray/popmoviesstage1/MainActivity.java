package com.raystone.ray.popmoviesstage1;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.raystone.ray.popmoviesstage1.sync.MovieSyncAdapter;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail) != null)
        {
            mTwoPane = true;
            if (savedInstanceState == null)
                getFragmentManager().beginTransaction().replace(R.id.movie_detail,MovieDetailFragment.newInstance(),"MOVIEDETAIL").commit();
        }
        else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
            getFragmentManager().beginTransaction().add(R.id.fragment_container,MainFragment.newInstance()).commit();
        }
        MovieSyncAdapter.initializeSyncAdapter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.settings)
        {
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri movieUri)
    {
        if (mTwoPane)
        {
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.MOVIE_DETAIL_URI,movieUri);

            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance();
            detailFragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.movie_detail,detailFragment,"MOVIEDETAIL").commit();
        }
        else
        {
            Intent intent = new Intent(this, MovieDetailActivity.class).setData(movieUri);
            startActivity(intent);
        }
    }

}
