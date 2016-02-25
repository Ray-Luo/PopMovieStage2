package com.raystone.ray.popmoviesstage1;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.raystone.ray.popmoviesstage1.database.MovieContract;
import com.raystone.ray.popmoviesstage1.database.MovieDBHelper;
import com.raystone.ray.popmoviesstage1.database.MovieProvider;
import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ray on 2/1/2016.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private View mRootView;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mRateAverage;
    private ImageView mHeart;
    private TextView mDescription;
    private TextView mTitle;
    private RecyclerView mTrailers;
    private static final int DETAIL_LOADER = 0;
    public static final String MOVIE_DETAIL_URI = "URI";
    private Uri mUri;
    private final ContentValues mValues = new ContentValues();
    private ShareActionProvider mShareActionProvider;
    private String movieTrailerURL;


    private static final String[] MOVIE_COLUMNS =
            {
                    MovieContract.ByRatingEntry._ID,
                    MovieContract.ByRatingEntry.COLUMN_TITLE,
                    MovieContract.ByRatingEntry.COLUMN_DATE,
                    MovieContract.ByRatingEntry.COLUMN_RATE,
                    MovieContract.ByRatingEntry.COLUMN_DESCRIPTION,
                    MovieContract.ByRatingEntry.COLUMN_POSTER,
                    MovieContract.ByRatingEntry.COLUMN_ID,
                    MovieContract.ByRatingEntry.COLUMN_TRAILER,
            };

    private static final int ID = 0;
    private static final int TITLE = 1;
    private static final int DATE = 2;
    private static final int RATE = 3;
    private static final int DESCRIPTION = 4;
    private static final int POSTER = 5;
    private static final int UUID = 6;
    private static final int TRAILER = 7;

    public static MovieDetailFragment newInstance() {
         Bundle args = new Bundle();
         MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment()
    {setHasOptionsMenu(true);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater,container,savedInstanceState);

        Bundle args = getArguments();
        if (args != null)
            mUri = args.getParcelable(MOVIE_DETAIL_URI);
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);

        mRootView = inflater.inflate(R.layout.movie_detail,container,false);
        return mRootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.share_movie, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (movieTrailerURL != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        Uri uri = mUri;
        if(null != uri)
        {
            return new CursorLoader(getActivity(),uri,MOVIE_COLUMNS,null,null,null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data)
    {
        if(!data.moveToFirst())
            return;



        mValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER, data.getString(POSTER));
        mValues.put(MovieContract.FavoriteEntry.COLUMN_DATE, data.getString(DATE));
        mValues.put(MovieContract.FavoriteEntry.COLUMN_RATE, data.getString(RATE));
        mValues.put(MovieContract.FavoriteEntry.COLUMN_ID, data.getString(UUID));
        mValues.put(MovieContract.FavoriteEntry.COLUMN_DESCRIPTION, data.getString(DESCRIPTION));
        mValues.put(MovieContract.FavoriteEntry.COLUMN_TITLE, data.getString(TITLE));
        mValues.put(MovieContract.FavoriteEntry.COLUMN_TRAILER, data.getString(TRAILER));

        final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";
        String posterURL = BASE_POSTER_URL + data.getString(POSTER);
        mPoster = (ImageView)mRootView.findViewById(R.id.detail_poster);
        Picasso.with(getActivity().getApplicationContext()).load(posterURL).into(mPoster);

        mReleaseDate = (TextView)mRootView.findViewById(R.id.release_date);
        mReleaseDate.setText(data.getString(DATE));

        mRateAverage = (TextView)mRootView.findViewById(R.id.detail_rate);
        mRateAverage.setText(data.getString(RATE)+"/10");


        Cursor cursor = getActivity().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,null,"movie_id=?",new String[] {data.getString(UUID)},null,null);
        mHeart = (ImageView)mRootView.findViewById(R.id.heart);
        if (cursor.moveToFirst())
        {mHeart.setImageResource(R.drawable.red_heart);}
        mHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieId = mValues.get(MovieContract.FavoriteEntry.COLUMN_ID).toString();
                Cursor cursor = getActivity().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,null,"movie_id=?",new String[] {movieId},null,null);
                if(cursor.moveToFirst()){
                    mHeart.setImageResource(R.drawable.empty_heart);
                    getActivity().getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI,"movie_id=?",new String[] {movieId});
                    return;
                }
                else {
                    mHeart.setImageResource(R.drawable.red_heart);
                    getActivity().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI,mValues);
                    return;
                }
            }
        });


        mDescription = (TextView)mRootView.findViewById(R.id.detail_description);
        mDescription.setText(data.getString(DESCRIPTION));

        mTitle = (TextView)mRootView.findViewById(R.id.detail_title);
        mTitle.setText(data.getString(TITLE));

        mTrailers = (RecyclerView)mRootView.findViewById(R.id.trailer_list);
        mTrailers.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTrailers.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).drawable(R.drawable.line_divider).build());
        String[] trailers  = trailerUris(data.getString(TRAILER));
        if(trailers.length > 0){
            movieTrailerURL = mTitle.getText().toString() + "\n" + trailers[0];}
        else
            movieTrailerURL = mTitle.getText().toString();

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        }

        TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(),trailers);
        mTrailers.setAdapter(trailersAdapter);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
    }


    public String[] trailerUris(String rawString)
    {
        try {
            JSONObject trailerJson = new JSONObject(rawString);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            String[] returnPath = new String[trailerArray.length()];

            for (int j = 0; j < trailerArray.length(); j++) {
                JSONObject trailerItem = trailerArray.getJSONObject(j);
                String trailerKey = trailerItem.getString("key");
                returnPath[j] = "https://www.youtube.com/watch?v=" + trailerKey;
            }
            return returnPath;
        } catch (JSONException e) {
            Log.e("getTrailerUri", e.getMessage(), e);
            e.printStackTrace();
            return null;
        }

    }

    private Intent createShareTrailerIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movieTrailerURL);
        return shareIntent;
    }

}
