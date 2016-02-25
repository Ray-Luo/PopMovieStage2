package com.raystone.ray.popmoviesstage1;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.raystone.ray.popmoviesstage1.database.MovieContract;
import com.raystone.ray.popmoviesstage1.sync.MovieSyncAdapter;

/**
 * Created by Ray on 1/31/2016.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private View mRootView;
    private GridView mGridView;
    private MovieAdapter mMovieAdapter;
    private static final int MOVIE_LOADER = 0;
    private static String sortBy;


    public static MainFragment newInstance() {
         Bundle args = new Bundle();
         MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        sortBy = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("sort","popularity.desc");
        MovieSyncAdapter.syncImmediately(getActivity());
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        getLoaderManager().destroyLoader(MOVIE_LOADER);
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,savedInstanceState);
        mRootView = inflater.inflate(R.layout.main_fragment,container,false);
        mGridView = (GridView)mRootView.findViewById(R.id.grid_view);
        return mRootView;
    }

    public interface Callback
    {
        void onItemSelected(Uri movieUri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        if (sortBy.equals("vote_average.desc")){
            return new CursorLoader(getActivity(), MovieContract.ByRatingEntry.CONTENT_URI,null,null,null,null);}
        if (sortBy.equals("favorite")){
            return new CursorLoader(getActivity(), MovieContract.FavoriteEntry.CONTENT_URI,null,null,null,null);}
        else{
            Uri popularityUri = MovieContract.ByPopularityEntry.CONTENT_URI;
            Loader<Cursor> loader = new CursorLoader(getActivity(), popularityUri,null,null,null,null);
            return loader;}
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data)
    {
        mMovieAdapter = new MovieAdapter(getActivity(),null,0);
        mMovieAdapter.swapCursor(data);


        mGridView.setAdapter(mMovieAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);
                if(cursor != null)
                {
                    String movieId = cursor.getString(cursor.getColumnIndex("movie_id"));
                    String sortBy = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("sort","");
                    Uri movieUri = null;
                    if (sortBy.equals("vote_average.desc"))
                        movieUri = MovieContract.ByRatingEntry.buildMovieItemURI(movieId);
                    if(sortBy.equals("popularity.desc") || sortBy.equals(""))
                        movieUri = MovieContract.ByPopularityEntry.buildMovieItemURI(movieId);
                    if(sortBy.equals("favorite"))
                        movieUri = MovieContract.FavoriteEntry.buildMovieItemURI(movieId);
                    ((Callback)getActivity()).onItemSelected(movieUri);
                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mMovieAdapter.swapCursor(null);
    }

}
